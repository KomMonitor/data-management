package de.hsbo.kommonitor.datamanagement.config;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesRepository;
import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialAccessControlSetup implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    Logger logger = LoggerFactory.getLogger(InitialAccessControlSetup.class);

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private OrganizationalUnitRepository organizationalUnitRepository;

    @Value("${keycloak.enabled:false}")
    private boolean isKeycloakEnabled;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String anonymousOUname;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String authenticatedOUname;

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
		
	}

	private boolean checkFirstTimeCreation() {
    	OrganizationalUnitEntity anonymousUnit;
    	OrganizationalUnitEntity authenticatedUnit;
		
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
        OrganizationalUnitEntity anonymousUnit;
        if (!organizationalUnitRepository.existsByName(anonymousOUname)) {
            anonymousUnit = createAndFlushOrganization(anonymousOUname, "groups all unauthenticated users");
        } else {
            anonymousUnit = organizationalUnitRepository.findByName(anonymousOUname);
        }

        OrganizationalUnitEntity authenticatedUnit;
        if (!organizationalUnitRepository.existsByName(authenticatedOUname)) {
            authenticatedUnit = createAndFlushOrganization(authenticatedOUname, "groups all authenticated users");
        } else {
            authenticatedUnit = organizationalUnitRepository.findByName(authenticatedOUname);
        }

        createAndFlushRole(anonymousUnit, PermissionLevelType.VIEWER);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.CREATOR);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.PUBLISHER);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.EDITOR);
        createAndFlushRole(authenticatedUnit, PermissionLevelType.VIEWER);
        logger.info("Finished creating default roles.");
    }

    @Transactional
    protected void createAndFlushRole(OrganizationalUnitEntity ou, PermissionLevelType level) {
        if (!roleRepository.existsByOrganizationalUnitAndPermissionLevel(ou, level)) {
            logger.info("Creating role '{}-{}'", ou.getName(), level.toString());
            RolesEntity role = new RolesEntity();
            role.setOrganizationalUnit(ou);
            role.setPermissionLevel(level);
            roleRepository.saveAndFlush(role);
        } else {
            logger.info("Skipping creating role '{}-{}' - Role already exists", ou.getName(), level.toString());
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
