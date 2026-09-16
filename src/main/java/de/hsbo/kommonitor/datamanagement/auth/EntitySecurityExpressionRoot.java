/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitHierarchyEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitHierarchyRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.WebServicesRepository;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParserFactory;
import de.hsbo.kommonitor.datamanagement.api.impl.users.UserInfoEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.users.UserInfoRepository;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPATCHDisplayOrderInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMembershipInputType;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
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

    private TokenParser tokenParser;

    private final AuthHelperService authHelperService;
    private final GeoresourcesMetadataRepository georesourceRepository;
    private final IndicatorsMetadataRepository indicatorRepository;
    private final SpatialUnitsMetadataRepository spatialunitsRepository;
    private final SpatialUnitHierarchyRepository spatialUnitHierarchyRepository;
    private final IndicatorSpatialUnitsRepository indicatorspatialunitsRepository;
    private final WebServicesRepository webServicesRepository;
    private final UserInfoRepository userInfoRepository;

    private final OrganizationalUnitRepository organizationalUnitRepository;
    private final AuthInfoProviderFactory authInfoProviderFactory;

    private final TokenParserFactory tokenParserFactory;

    public EntitySecurityExpressionRoot(Authentication authentication) {
        super(authentication);

        this.authHelperService = AuthHelperService.GetInstance();
        this.georesourceRepository = this.authHelperService.getGeoresourceRepository();
        this.indicatorRepository = this.authHelperService.getIndicatorRepository();
        this.spatialunitsRepository = this.authHelperService.getSpatialunitsRepository();
        this.spatialUnitHierarchyRepository = this.authHelperService.getSpatialUnitHierarchyRepository();
        this.indicatorspatialunitsRepository = this.authHelperService.getIndicatorSpatialunitsRepository();
        this.webServicesRepository = this.authHelperService.getWebServicesRepository();
        this.userInfoRepository = this.authHelperService.getUserInfoRepository();

        this.organizationalUnitRepository = this.authHelperService.getOrganizationalUnitRepository();
        this.authInfoProviderFactory = this.authHelperService.getAuthInfoProviderFactory();
        this.tokenParserFactory = this.authHelperService.getTokenParserFactory();

        if (Principal.class.isAssignableFrom(this.getAuthentication().getPrincipal().getClass())) {
            Principal principal = ((Principal) this.getAuthentication().getPrincipal());
            this.tokenParser = (this.tokenParserFactory.createTokenParser(principal));
            this.authInfoProvider = this.authInfoProviderFactory.createAuthInfoProvider(principal, tokenParser);
        }
        else {
            this.tokenParser = (this.tokenParserFactory.createTokenParser(this.getAuthentication()));
            this.authInfoProvider = this.authInfoProviderFactory.createAuthInfoProvider(this.getAuthentication(), tokenParser);
        }

    }

    /**
     * Checks whether the user has admin permissions to perform admin operations or not.
     *
     * @return true if the user has global admin permissions
     */
    public boolean isAuthorizedForAdminOperations() {
        return this.authInfoProvider.hasGlobalAdminPermissions();
    }

    /**
     * custom security method to check if a user has permissions to access an entity,
     * to be used with @PreAuthorize and @PostAuthorize annotations
     * @param entityID
     * @param entityType
     * @param permissionLevel
     * @return 
     */
    public boolean isAuthorizedForEntity(String entityID, String entityType, String permissionLevel) {
        logger.debug("called isAuthorizedForEntity with entity id " + entityID);
        // Fail fast if user has not the required role for managing resources, with no need to request an entity
        if (!hasRequiredPermissionLevel(permissionLevel, PermissionResourceType.RESOURCES.getValue())){
            return false;
        }

        try {
            RestrictedEntity entity = this.retrieveEntity(entityID, EntityType.fromValue(entityType));
            if (entity == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find entity " + entityID + " of type " + entityType);
            }
            boolean isAuthorized = this.authInfoProvider.checkPermissions(entity, PermissionLevelType.fromValue(permissionLevel.toLowerCase()));
            logger.info("access for " + this.getAuthentication().getName() + " to entity " + entityID + " of type " + entityType + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate permissions for entity with id " + entityID + " of type " + entityType + "; return not authorized", ex);
            return false;
        }
    }

    /**
     * custom security method to check if a user has permissions to manage an OrganizationalUnit,
     * to be used with @PreAuthorize and @PostAuthorize annotations
     * @param organizationalUnitId

     * @return
     */
    public boolean isAuthorizedForOrganization(String organizationalUnitId) {
        logger.debug("called isAuthorizedForOrganization with OrganizationalUnit id " + organizationalUnitId);
        // Fail fast if user has not the required role for managing users, with no need to request an entity
        if (!this.authInfoProvider.hasRequiredPermissionLevel(PermissionLevelType.CREATOR, PermissionResourceType.USERS)) {
            return false;
        }

        try {
            OrganizationalUnitEntity ouEntity = this.organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
            if (ouEntity == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find OrganizationalUnit " + organizationalUnitId);
            }
            boolean isAuthorized = this.authInfoProvider.checkOrganizationalUnitPermissions(ouEntity);
            logger.info("access for " + this.getAuthentication().getName() + " to OrganizationalUnit " + organizationalUnitId + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate permissions for OrganizationalUnit with id " + organizationalUnitId, ex);
            return false;
        }
    }

    /**
     * custom security method to check if a user is allowed to manage the given spatial unit
     * hierarchy, i.e. has the required resource permission level and belongs to the mandant
     * that owns the hierarchy. To be used with @PreAuthorize / @PostAuthorize annotations.
     * to be used with @PreAuthorize and @PostAuthorize annotations
     *
     * @param hierarchyId the id of the spatial unit hierarchy
     * @param permissionLevel the required resource permission level (e.g. 'editor', 'creator')
     * @return true if the user is authorized to manage the hierarchy
     */
    public boolean isAuthorizedForSpatialUnitHierarchy(String hierarchyId, String permissionLevel) {
        logger.debug("called isAuthorizedForSpatialUnitHierarchy with hierarchy id " + hierarchyId);
        if (!hasRequiredPermissionLevel(permissionLevel, PermissionResourceType.RESOURCES.getValue())) {
            return false;
        }
        if (isAuthorizedForAdminOperations()) {
            return true;
        }
        try {
            SpatialUnitHierarchyEntity hierarchy = this.spatialUnitHierarchyRepository.findById(hierarchyId).orElse(null);
            if (hierarchy == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find spatial unit hierarchy " + hierarchyId);
            }
            boolean isAuthorized = this.authInfoProvider.checkMandantOperationPermissions(hierarchy.getMandant());
            logger.info("access for " + this.getAuthentication().getName() + " to spatial unit hierarchy " + hierarchyId + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate permissions for spatial unit hierarchy with id " + hierarchyId + "; return not authorized", ex);
            return false;
        }
    }

    /**
     * Check if a user is allowed to create resources for the given mandant, i.e. has the required resource permission
     * level for that mandant.
     * to be used with @PreAuthorize and @PostAuthorize annotations
     *
     * @param mandantId the id of the mandant (organizational unit)
     * @param permissionLevel the required resource permission level (e.g. 'creator')
     * @return true if the user is authorized for the mandant
     */
    public boolean isAuthorizedForMandant(String mandantId, String permissionLevel) {
        logger.debug("called isAuthorizedForMandant with mandant id " + mandantId);
        if (!hasRequiredPermissionLevel(permissionLevel, PermissionResourceType.RESOURCES.getValue())) {
            return false;
        }
        if (isAuthorizedForAdminOperations()) {
            return true;
        }
        try {
            OrganizationalUnitEntity mandant = this.organizationalUnitRepository.findByOrganizationalUnitId(mandantId);
            if (mandant == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find mandant " + mandantId);
            }
            boolean isAuthorized = this.authInfoProvider.checkMandantOperationPermissions(mandant);
            logger.info("access for " + this.getAuthentication().getName() + " to mandant " + mandantId + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate mandant membership for mandant with id " + mandantId + "; return not authorized", ex);
            return false;
        }
    }

    /**
     * Check if a user is allowed to change the hierarchy memberships of a spatial unit: the user must have the
     * required permission level on the spatial unit itself AND be authorized (via the owning mandant) for each
     * referenced hierarchy.
     * to be used with @PreAuthorize and @PostAuthorize annotations
     *
     * @param spatialUnitId the id of the spatial unit whose memberships are changed
     * @param memberships the requested hierarchy memberships
     * @param permissionLevel the required resource permission level (e.g. 'editor')
     * @return true if the user is authorized for the spatial unit and every referenced hierarchy
     */
    public boolean isAuthorizedForSpatialUnitHierarchyMemberships(String spatialUnitId,
            List<SpatialUnitHierarchyMembershipInputType> memberships, String permissionLevel) {
        logger.debug("called isAuthorizedForSpatialUnitHierarchyMemberships for spatial unit id " + spatialUnitId);
        if (!isAuthorizedForEntity(spatialUnitId, EntityType.SPATIALUNIT.toString(), permissionLevel)) {
            return false;
        }
        if (memberships != null) {
            for (SpatialUnitHierarchyMembershipInputType membership : memberships) {
                if (!isAuthorizedForSpatialUnitHierarchy(membership.getHierarchyId(), permissionLevel)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * custom security method to check if a user has permissions to access a list of IndicatorPATCHDisplayOrderInputType entities,
     * to be used with @PreAuthorize and @PostAuthorize annotations
     * @param indicatorOrderArray
     * @param entityType
     * @param permissionLevel
     * @return 
     */
    public boolean isAuthorizedForEntity(List<IndicatorPATCHDisplayOrderInputType> indicatorOrderArray, String entityType, String permissionLevel) {
        logger.debug("called isAuthorizedForEntity with a list of IndicatorPATCHDisplayOrderInputType entities.");
        
        boolean allAuthorized = true;
        
        for (IndicatorPATCHDisplayOrderInputType indicatorPATCHDisplayOrderInputType : indicatorOrderArray) {
			String indicatorId = indicatorPATCHDisplayOrderInputType.getIndicatorId();
			if(! isAuthorizedForEntity(indicatorId, entityType, permissionLevel)) {
				allAuthorized = false;
				break;
			}			
		}
        
        return allAuthorized;
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
            RestrictedEntity entity = this.retrieveJoinedEntity(entityID1, entityID2, JoinedEntityType.fromValue(joinedEntityType));
            if (entity == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find entity for id " + entityID1 + " and " + entityID2 + " of type " + joinedEntityType);
            }
            boolean isAuthorized = this.authInfoProvider.checkPermissions(entity, PermissionLevelType.fromValue(permissionLevel.toLowerCase()));
            logger.info("access for " + this.getAuthentication().getName() + " to joined  entity " + entityID1 + " and " + entityID2 + " of type " + joinedEntityType + " authorized? " + isAuthorized);
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

    public boolean hasRequiredPermissionLevel(String requiredPermissionLevel, String permissionResourceType){
        logger.debug("called haRequiredPermissionLevel with required permission level " + requiredPermissionLevel + " and permission resource type " + permissionResourceType);
        try{
            return this.authInfoProvider.hasRequiredPermissionLevel(PermissionLevelType.fromValue(requiredPermissionLevel), PermissionResourceType.fromValue(permissionResourceType));
        }catch (Exception ex){
            logger.error("unable to evaluate if required permission level " + requiredPermissionLevel + " is met; return not authorized", ex);
            return false;
        }
    }

    /**
     * custom security method to check if a user has permissions to view and manage user info, to be used
     * with @PreAuthorize and @PostAuthorize annotations
     *
     * @param userInfoId ID for the user info

     * @return true for global admins and if a user owns the requested information
     */
    public boolean isAuthorizedForUserInfo(String userInfoId) {
        logger.debug("called isAuthorizedForUserInfo with user info id " + userInfoId);
        // Fast success for global admins
        if (this.authInfoProvider.hasGlobalAdminPermissions()) {
            return true;
        }

        try {
            UserInfoEntity userInfoEntity = this.userInfoRepository.findByUserInfoId(userInfoId);
            if(userInfoEntity == null) {
                throw new ResourceNotFoundException(NOTFOUNDCODE, "could not find user info for user id " + userInfoId);
            }

            boolean isAuthorized = userInfoEntity.getKeycloakId().equals(this.authInfoProvider.getUserId());
            logger.info("access for " + this.getAuthentication().getName() + " to user info " + userInfoId + " authorized? " + isAuthorized);
            return isAuthorized;
        } catch (Exception ex) {
            logger.error("unable to evaluate permissions for user info with id " + userInfoId, ex);
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
    private RestrictedEntity retrieveEntity(String entityID, EntityType entityType) throws Exception {
        switch (entityType) {
            case GEORESOURCE:
                return this.georesourceRepository.findByDatasetId(entityID);
            case INDICATOR:
                return this.indicatorRepository.findByDatasetId(entityID);
            case SPATIALUNIT:
                return this.spatialunitsRepository.findByDatasetId(entityID);
            case WEBSERVICE:
                return this.webServicesRepository.findById(entityID);
            default:
                throw new IllegalArgumentException("cannot retrieve entity, unknown entity type " + entityType.toString());
        }
    }
    
    private RestrictedEntity retrieveJoinedEntity(String entityID1, String entityID2, JoinedEntityType joinedEntityType){
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
        SPATIALUNIT("spatialunit"),
        WEBSERVICE("web-service");

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
