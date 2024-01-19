package de.hsbo.kommonitor.datamanagement.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesEntity;

@Component
public class InitialAccessControlSetup implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    Logger logger = LoggerFactory.getLogger(InitialAccessControlSetup.class);
    
    private final String oldAdministratorRoleName = "administrator";

    @Autowired
    private RolesRepository roleRepository;
    
    @Autowired
    private SpatialUnitsMetadataRepository spatialUnitsRepository;
    
    @Autowired
    private GeoresourcesMetadataRepository georesourceRepository;
    
    @Autowired
    private IndicatorsMetadataRepository indicatorRepository;
    
    @Autowired
    private IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepository;

    @Autowired
    private OrganizationalUnitRepository organizationalUnitRepository;

    @Value("${keycloak.enabled:false}")
    private boolean isKeycloakEnabled;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String anonymousOUname;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String authenticatedOUname;
    
    
    private OrganizationalUnitEntity anonymousUnit;
	private OrganizationalUnitEntity authenticatedUnit;
	private RolesEntity anonymousViewerRole;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	
    	boolean firstTimeCreation = checkFirstTimeCreation();

        if (!isKeycloakEnabled) {
            logger.info("Keyloak connection is disabled. Hence, no default roles will be registered.");
        } else {
            logger.info("Keyloak connection is enabled. Registering default roles and upgrade old data model if necessary.");            
            
            if (firstTimeCreation) {
            	createDefaultRoles();
            	
            	logger.info("Trying to upgrade existing role mapping to new data model");
            	upgradeExistingRoleMapping();
            }
        }
    }

    private void upgradeExistingRoleMapping() {
		/*
		 * Upgrade data model 
		 * 
		 * - gather list of other existing roles of old data model
		 * - create organization and roles/rights for each of those old roles by overtaking the respective name as organization name
		 * - iterate over all resources (spatial units, georesources, indicator metadata and spatial unit mapping) and set at least viewer rights 
		 *  for the respective new organisation + remove old role
		 * - remove old roles from repo completely
		 * 
		 * - sync changes with Keycloak?
		 */
    	
    	List<RolesEntity> existingRoles_oldDataModel = gatherOldRoles();
    	logger.info("Found {} old roles", existingRoles_oldDataModel.size());
    	
    	logger.info("Create Organisations and Roles for reach old role");
    	List<OrganizationalUnitEntity> newOrgs = createNewOrganisations(existingRoles_oldDataModel);
    	List<RolesEntity> newRoles = createNewRoles(newOrgs);
    	
    	logger.info("Created {} new organisations and {} respective roles/rights", newOrgs.size(), newRoles.size());
    	
    	logger.info("Begin automatic upgrade of old role mapping to new access control role mapping preserving read and admin access to spatialUnit, georesource and indicator datasets. KomMonitor-Administrators must then check and set editor, publisher, and creator rights themselves.");
    	upgradeRoleMapping(newOrgs, newRoles, existingRoles_oldDataModel);
    	
    	logger.info("Removing old relic role entries completely");
    	removeOldRoles(existingRoles_oldDataModel);
		
	}

	private List<RolesEntity> createNewRoles(List<OrganizationalUnitEntity> newOrgs) {
		List<RolesEntity> newRoles = new ArrayList<RolesEntity>();
		
		for (OrganizationalUnitEntity orgEntity : newOrgs) {
			newRoles.add(createAndFlushRole(orgEntity, PermissionLevelType.CREATOR));
			newRoles.add(createAndFlushRole(orgEntity, PermissionLevelType.PUBLISHER));
			newRoles.add(createAndFlushRole(orgEntity, PermissionLevelType.EDITOR));
			newRoles.add(createAndFlushRole(orgEntity, PermissionLevelType.VIEWER));
		}
		
		return newRoles;
	}

	private List<OrganizationalUnitEntity> createNewOrganisations(List<RolesEntity> existingRoles_oldDataModel) {
		
		List<OrganizationalUnitEntity> newOrgs = new ArrayList<OrganizationalUnitEntity>();		
		
		for (RolesEntity rolesEntity : existingRoles_oldDataModel) {
			String roleName = rolesEntity.getRoleName();
			
			if (!organizationalUnitRepository.existsByName(roleName)) {
	            newOrgs.add(createAndFlushOrganization(roleName, roleName));
	        } else {
	            logger.info("For old role with name '{}' an organization entry already exists. Skipping this role.", roleName);
	        }
		}
		
		return newOrgs;
	}

	private void removeOldRoles(List<RolesEntity> existingRoles_oldDataModel) {
		
		// remove list of old roles
		roleRepository.deleteAll(existingRoles_oldDataModel);
		
		// remove old administrator role explicitly
		RolesEntity oldAdminRole = roleRepository.findByRoleName(oldAdministratorRoleName);
		if(oldAdminRole != null) {
			roleRepository.delete(oldAdminRole);
		}
		
	}

	private void upgradeRoleMapping(List<OrganizationalUnitEntity> newOrgs, List<RolesEntity> newRoles, List<RolesEntity> oldRoles) {
		// map
		
		// key = old role
		// value = new role
		// old administrator role is new kommonitor-creator role!
		RolesEntity newAdminRole = roleRepository.findByOrganizationalUnitAndPermissionLevel(authenticatedUnit, PermissionLevelType.CREATOR);
		RolesEntity oldAdminRole = roleRepository.findByRoleName(oldAdministratorRoleName);
		
		Map<String, RolesEntity> roleMapping = new HashMap<String, RolesEntity>();
		
		for (int i = 0; i < oldRoles.size(); i++) {
			/*
			 * per old role one new organization and four roles have been created
			 * hence we simply replace the old role association with the VIEWER role of the associated newly created organization!
			 */
			roleMapping.put(oldRoles.get(i).getRoleId(), roleRepository.findByOrganizationalUnitAndPermissionLevel(newOrgs.get(i), PermissionLevelType.VIEWER));
		}
		
		// admin role mapping
		if(oldAdminRole != null) {
			roleMapping.put(oldAdminRole.getRoleId(), newAdminRole);
		}
		
		logger.info("Upgrading spatial units ...");
		upgradeRoleMapping_spatialUnits(roleMapping);
		logger.info("Upgrading georesources ...");
		upgradeRoleMapping_georesources(roleMapping);
		logger.info("Upgrading indicators metadata and their assoication with spatial units ...");
		upgradeRoleMapping_indicators(roleMapping);
	}

	private void upgradeRoleMapping_indicators(Map<String, RolesEntity> roleMapping) {
		// metadata
		List<MetadataIndicatorsEntity> indicatorsMetadata = indicatorRepository.findAll();
		
		for (MetadataIndicatorsEntity metadataIndicatorsEntity : indicatorsMetadata) {
			HashSet<RolesEntity> oldRoles = metadataIndicatorsEntity.getRoles();
			HashSet<RolesEntity> newRoles = new HashSet<RolesEntity>();
			
			if(oldRoles.size() == 0) {
				// only add new explicit anonymous role
				newRoles.add(anonymousViewerRole);
			}
			else {
				for (RolesEntity oldRole : oldRoles) {
					newRoles.add(roleMapping.get(oldRole.getRoleId()));
				}
			}
			
			metadataIndicatorsEntity.setRoles(newRoles);
			indicatorRepository.saveAndFlush(metadataIndicatorsEntity);
		}
		
		// spatial unit mapping
		List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnitsMetadata = indicatorSpatialUnitsRepository.findAll();
		
		for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitEntity : indicatorSpatialUnitsMetadata) {
			HashSet<RolesEntity> oldRoles = indicatorSpatialUnitEntity.getRoles();
			HashSet<RolesEntity> newRoles = new HashSet<RolesEntity>();
			
			if(oldRoles.size() == 0) {
				// only add new explicit anonymous role
				newRoles.add(anonymousViewerRole);
			}
			else {
				for (RolesEntity oldRole : oldRoles) {
					newRoles.add(roleMapping.get(oldRole.getRoleId()));
				}
			}
			
			indicatorSpatialUnitEntity.setRoles(newRoles);
			indicatorSpatialUnitsRepository.saveAndFlush(indicatorSpatialUnitEntity);
		}
		
	}

	private void upgradeRoleMapping_georesources(Map<String, RolesEntity> roleMapping) {
		List<MetadataGeoresourcesEntity> georesourcesMetadata = georesourceRepository.findAll();
		
		for (MetadataGeoresourcesEntity metadataGeoresourcesEntity : georesourcesMetadata) {
			HashSet<RolesEntity> oldRoles = metadataGeoresourcesEntity.getRoles();
			HashSet<RolesEntity> newRoles = new HashSet<RolesEntity>();
			
			if(oldRoles.size() == 0) {
				// only add new explicit anonymous role
				newRoles.add(anonymousViewerRole);
			}
			else {
				for (RolesEntity oldRole : oldRoles) {
					newRoles.add(roleMapping.get(oldRole.getRoleId()));
				}
			}
			
			metadataGeoresourcesEntity.setRoles(newRoles);
			georesourceRepository.saveAndFlush(metadataGeoresourcesEntity);
		}
		
	}

	private void upgradeRoleMapping_spatialUnits(Map<String, RolesEntity> roleMapping) {
		List<MetadataSpatialUnitsEntity> spatialUnitsMetadata = spatialUnitsRepository.findAll();
		
		for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : spatialUnitsMetadata) {
			HashSet<RolesEntity> oldRoles = metadataSpatialUnitsEntity.getRoles();
			HashSet<RolesEntity> newRoles = new HashSet<RolesEntity>();
			
			if(oldRoles.size() == 0) {
				// only add new explicit anonymous role
				newRoles.add(anonymousViewerRole);
			}
			else {
				for (RolesEntity oldRole : oldRoles) {
					newRoles.add(roleMapping.get(oldRole.getRoleId()));
				}
			}
			
			metadataSpatialUnitsEntity.setRoles(newRoles);
			spatialUnitsRepository.saveAndFlush(metadataSpatialUnitsEntity);
		}		
		
	}

	private List<RolesEntity> gatherOldRoles() {
		
		adjustOldRolesToNewDataModel();
		
		List<RolesEntity> allRoles = roleRepository.findAll();
		
		List<RolesEntity> oldRoles = new ArrayList<RolesEntity>();
		
		List<String> adminAndPublicOrgIds = new ArrayList<String>();
		adminAndPublicOrgIds.add(anonymousUnit.getOrganizationalUnitId());
		adminAndPublicOrgIds.add(authenticatedUnit.getOrganizationalUnitId());
		
		for (RolesEntity rolesEntity : allRoles) {	
			if (( rolesEntity.getRoleName() != null && !rolesEntity.getRoleName().equalsIgnoreCase(oldAdministratorRoleName))) {				
				oldRoles.add(rolesEntity);
			}
		}
		
		return oldRoles;
	}

	private void adjustOldRolesToNewDataModel() {
		
		/*
		 * due to a not-null constraint on orgnizationalUnit property of rolesEntity
		 * we must first set an organizational unit and then delete the old role...
		 */
		List<RolesEntity> allRoles = roleRepository.findAll();
		
		for (RolesEntity rolesEntity : allRoles) {
			if(rolesEntity.getOrganizationalUnit() == null) {
				rolesEntity.setOrganizationalUnit(anonymousUnit);
				rolesEntity.setPermissionLevel(PermissionLevelType.VIEWER);
				roleRepository.saveAndFlush(rolesEntity);
			}			
		}
		
	}

	private boolean checkFirstTimeCreation() {    	
		
		if (!organizationalUnitRepository.existsByName(anonymousOUname)) {
            return true;
        } else {
            anonymousUnit = organizationalUnitRepository.findByName(anonymousOUname);
        }
		if (!organizationalUnitRepository.existsByName(authenticatedOUname)) {
            return true;
        } else {
        	authenticatedUnit = organizationalUnitRepository.findByName(authenticatedOUname);
        }
		
		// check public viewer of existing organisation
		if(!roleExists(anonymousUnit, PermissionLevelType.VIEWER)) {
			return true;
		}
		// check kommonitor roles of existing organisation
		if(!roleExists(authenticatedUnit, PermissionLevelType.VIEWER)) {
			return true;
		}
		if(!roleExists(authenticatedUnit, PermissionLevelType.EDITOR)) {
			return true;
		}
		if(!roleExists(authenticatedUnit, PermissionLevelType.PUBLISHER)) {
			return true;
		}
		if(!roleExists(authenticatedUnit, PermissionLevelType.CREATOR)) {
			return true;
		}
		
		return false;
	}

	private boolean roleExists(OrganizationalUnitEntity unit, PermissionLevelType level) {
		if (roleRepository.existsByOrganizationalUnitAndPermissionLevel(unit, level)) {
			return true;
		}
		return false;
	}

	@Transactional
    protected void createDefaultRoles() {
        
        if (!organizationalUnitRepository.existsByName(anonymousOUname)) {
            anonymousUnit = createAndFlushOrganization(anonymousOUname, "groups all unauthenticated users");
        } else {
            anonymousUnit = organizationalUnitRepository.findByName(anonymousOUname);
        }

        if (!organizationalUnitRepository.existsByName(authenticatedOUname)) {
            authenticatedUnit = createAndFlushOrganization(authenticatedOUname, "groups all authenticated users");
        } else {
            authenticatedUnit = organizationalUnitRepository.findByName(authenticatedOUname);
        }

        anonymousViewerRole = createAndFlushRole(anonymousUnit, PermissionLevelType.VIEWER);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.CREATOR);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.PUBLISHER);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.EDITOR);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.VIEWER);
        logger.info("Finished creating default roles.");
    }

    @Transactional
    protected RolesEntity createAndFlushRole(OrganizationalUnitEntity ou, PermissionLevelType level) {
        if (!roleRepository.existsByOrganizationalUnitAndPermissionLevel(ou, level)) {
            logger.info("Creating role '{}-{}'", ou.getName(), level.toString());
            RolesEntity role = new RolesEntity();
            role.setOrganizationalUnit(ou);
            role.setPermissionLevel(level);
            return roleRepository.saveAndFlush(role);
        } else {
            logger.info("Skipping creating role '{}-{}' - Role already exists", ou.getName(), level.toString());
            return roleRepository.findByOrganizationalUnitAndPermissionLevel(ou, level);
        }
    }

    @Transactional
    protected OrganizationalUnitEntity createAndFlushOrganization(String name, String description) {
        logger.info("Creating organizationalUnit '{}' with description '{}'", name, description);
        OrganizationalUnitEntity orga = new OrganizationalUnitEntity();
        orga.setName(name);
        orga.setContact(name);
        orga.setDescription(description);
        return organizationalUnitRepository.saveAndFlush(orga);
    }

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 2;
	}

}
