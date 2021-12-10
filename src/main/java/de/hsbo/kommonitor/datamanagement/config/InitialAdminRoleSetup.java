package de.hsbo.kommonitor.datamanagement.config;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesRepository;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Component
public class InitialAdminRoleSetup implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(InitialAdminRoleSetup.class);

    boolean alreadySetup = false;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private OrganizationalUnitRepository organizationalUnitRepository;

    @Value("${keycloak.enabled:false}")
    private boolean isKeycloakEnabled;

    @Value("${kommonitor.roles.admin:administrator}")
    private String adminRoleName;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        logger.info("Begin initial setup of configured administrator role if keyloak is enabled.");

        if (!isKeycloakEnabled) {
            logger.info("Keyloak connection is disabled. Hence, no default admin user is registered.");
        } else {
            logger.info(
                "Keyloak connection is enabled." +
                    " A default admin user with name {} will be registered if not already setup.",
                adminRoleName);

            createRolesEntityIfNotFound(adminRoleName);
        }

        logger.info("Initial setup of default admin role finished.");
    }

    @Transactional
    protected void createRolesEntityIfNotFound(String name) {

        organizationalUnitRepository.deleteAll();
        roleRepository.deleteAll();

        if (!organizationalUnitRepository.existsByName(name)) {
            OrganizationalUnitEntity admin_organization = new OrganizationalUnitEntity();
            admin_organization.setName(name);
            admin_organization.setContact(name);
            admin_organization.setDescription("global administration group");
            organizationalUnitRepository.saveAndFlush(admin_organization);

            RolesEntity role = new RolesEntity();
            role.setOrganizationalUnit(admin_organization);
            role.setPermissionLevel(PermissionLevelType.NONE);
            roleRepository.saveAndFlush(role);
        }
    }

}
