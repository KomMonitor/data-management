/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * provides custom security method to check if a user has permissions to access a entity,
 * @author Arne
 */
public class EntitySecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private static final Logger logger = LoggerFactory.getLogger(EntitySecurityExpressionRoot.class);

    private static final int NOTFOUNDCODE = 404;

    private Object filterObject;
    private Object returnObject;
    private AuthInfoProvider authInfoProvider;

    private final AuthHelperService authHelperService;
    private final GeoresourcesMetadataRepository georesourceRepository;
    private final IndicatorsMetadataRepository indicatorRepository;
    private final SpatialUnitsMetadataRepository spatialunitsRepository;
    private final IndicatorSpatialUnitsRepository indicatorspatialunitsRepository;
    private final AuthInfoProviderFactory authInfoProviderFactory;

    public EntitySecurityExpressionRoot(Authentication authentication) {
        super(authentication);

        this.authHelperService = AuthHelperService.GetInstance();
        this.georesourceRepository = this.authHelperService.getGeoresourceRepository();
        this.indicatorRepository = this.authHelperService.getIndicatorRepository();
        this.spatialunitsRepository = this.authHelperService.getSpatialunitsRepository();
        this.indicatorspatialunitsRepository = this.authHelperService.getIndicatorSpatialunitsRepository();
        this.authInfoProviderFactory = this.authHelperService.getAuthInfoProviderFactory();

        if (Principal.class.isAssignableFrom(this.authentication.getPrincipal().getClass())) {
            this.authInfoProvider = (this.authInfoProviderFactory.createAuthInfoProvider(((Principal) this.authentication.getPrincipal())));
        } else {
            throw new IllegalArgumentException("cannot cast princicpal of type " + this.authentication.getPrincipal().getClass().getSimpleName() + " to type " + Principal.class.getSimpleName());
        }
    }

    /**
     * custom security method to check if a user has permissions to access a entity,
     * to be used with @PreAuthorize and @PostAuthorize annotations
     * @param entityID
     * @param entityType
     * @param permissionLevel
     * @return 
     */
    public boolean isAuthorizedForEntity(String entityID, String entityType, String permissionLevel) {
        logger.debug("called isAuthorizedForEntity with entity id " + entityID);
        // Fail fast if user has not the required permission, with no need to request an entity
        if (!hasRequiredPermissionLevel(permissionLevel)){
            return false;
        }

        try {
            RestrictedByRole entity = this.retrieveEntity(entityID, EntityType.fromValue(entityType));
            if (entity == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find entity " + entityID + " of type " + entityType);
            }
            boolean isAuthorized = this.authInfoProvider.checkPermissions(entity, PermissionLevelType.fromValue(permissionLevel.toLowerCase()));
            logger.info("access for " + this.authentication.getName() + " to entity " + entityID + " of type " + entityType + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate permissions for entity with id " + entityID + " of type " + entityType + "; return not authorized", ex);
            return false;
        }
    }
    
        /**
     * custom security method to check if a user has permissions to access a joined entity (e.g.IndicatorSpatialUnits),
 to be used with @PreAuthorize and @PostAuthorize annotations
     * @param entityID1
     * @param entityID2
     * @param joinedEntityType
     * @param permissionLevel
     * @return 
     */
    public boolean isAuthorizedForJoinedEntity(String entityID1, String entityID2, String joinedEntityType, String permissionLevel) {
        logger.debug("called isAuthorizedForJoinedEntity with entity id " + entityID1 + " and " + entityID2);

        try {
            RestrictedByRole entity = this.retrieveJoinedEntity(entityID1, entityID2, JoinedEntityType.fromValue(joinedEntityType));
            if (entity == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find entity for id " + entityID1 + " and " + entityID2 + " of type " + joinedEntityType);
            }
            boolean isAuthorized = this.authInfoProvider.checkPermissions(entity, PermissionLevelType.fromValue(permissionLevel.toLowerCase()));
            logger.info("access for " + this.authentication.getName() + " to joined  entity " + entityID1 + " and " + entityID2 + " of type " + joinedEntityType + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate permissions for joined entity with ids " + entityID1 + " and " + entityID2 + " of type " + joinedEntityType + "; return not authorized", ex);
            return false;
        }
    }
    
    /**
     *
     * @param requiredPermissionLevel
     * @return
     */
    public boolean hasRequiredPermissionLevel(String requiredPermissionLevel){
        logger.debug("called haRequiredPermissionLevel with required permission level " + requiredPermissionLevel);
        try{
            return this.authInfoProvider.hasRequiredPermissionLevel(PermissionLevelType.fromValue(requiredPermissionLevel));
        }catch (Exception ex){
            logger.error("unable to evaluate if required permission level " + requiredPermissionLevel + " is met; return not authorized", ex);
            return false;
        }
    }

    /**
     * retrieve the entity from the corresponding repository
     * @param entityID
     * @param entityType
     * @return
     * @throws Exception 
     */
    private RestrictedByRole retrieveEntity(String entityID, EntityType entityType) throws Exception {
        switch (entityType) {
            case GEORESOURCE:
                return this.georesourceRepository.findByDatasetId(entityID);
            case INDICATOR:
                return this.indicatorRepository.findByDatasetId(entityID);
            case SPATIALUNIT:
                return this.spatialunitsRepository.findByDatasetId(entityID);
            default:
                throw new IllegalArgumentException("cannot retrieve entity, unknown entity type " + entityType.toString());
        }
    }
    
    private RestrictedByRole retrieveJoinedEntity(String entityID1, String entityID2, JoinedEntityType joinedEntityType){
        switch(joinedEntityType){
            case INDICATOR_SPATIALUNIT:
                return this.indicatorspatialunitsRepository.findByIndicatorMetadataIdAndSpatialUnitId(entityID1, entityID2);
            default:
                throw new IllegalArgumentException("cannot retrieve joined entity, unknown joined entity type " + joinedEntityType.toString());
        }
    }

    @Override
    public void setFilterObject(Object o) {
        this.filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object o) {
        this.returnObject = o;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    public enum EntityType {
        GEORESOURCE("georesource"),
        INDICATOR("indicator"),
        SPATIALUNIT("spatialunit");

        private final String value;

        EntityType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static EntityType fromValue(String text) {
            for (EntityType et : EntityType.values()) {
                if (String.valueOf(et.value).equalsIgnoreCase(text)) {
                    return et;
                }
            }
            throw new IllegalArgumentException("unknown entity type " + text);
        }
    }
    
    public enum JoinedEntityType {
        INDICATOR_SPATIALUNIT("indicator_spatialunit");

        private final String value;

        JoinedEntityType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static JoinedEntityType fromValue(String text) {
            for (JoinedEntityType jet : JoinedEntityType.values()) {
                if (String.valueOf(jet.value).equalsIgnoreCase(text)) {
                    return jet;
                }
            }
            throw new IllegalArgumentException("unknown entity type " + text);
        }
    }
}
