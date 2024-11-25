package de.hsbo.kommonitor.datamanagement.config.db;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitManager;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ApiException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class DbInitLoader_5_0_0 implements DbInitLoader{

    private static final Logger LOG = LoggerFactory.getLogger(DbInitLoader_5_0_0.class);

    private static final String DB_VERSION = "5.0.0";

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String defaultAnonymousOUname;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String defaultAuthenticatedOUname;

    @Autowired
    PermissionManager permissionManager;
    @Autowired
    OrganizationalUnitManager organizationalUnitManager;
    @Autowired
    OrganizationalUnitRepository organizationalUnitRepository;

    public String getDbVersion() {
        return DB_VERSION;
    }

    @Override
    public void load() {
        // Delete special 'public' and 'kommonitor' OrganizationalUnit
        deleteOrganizationalUnitAndRolesByName(defaultAnonymousOUname);
        deleteOrganizationalUnitAndRolesByName(defaultAuthenticatedOUname);
        // Initialize Keycloak groups for all other OrganizationalUnits
        setupOrganizationalUnits();
    }

    private void setupOrganizationalUnits() {
        LOG.info("Load additional permissions if missing and create Keycloak groups for OrganizationalUnits.");
        Stream<OrganizationalUnitEntity> units = organizationalUnitRepository.findAll().stream();
        units.forEach(ou -> {
            permissionManager.addPermission(ou, PermissionLevelType.CREATOR, PermissionResourceType.USERS);
            permissionManager.addPermission(ou, PermissionLevelType.CREATOR, PermissionResourceType.THEMES);

            try {
                organizationalUnitManager.initializeKeycloakGroup(ou);
            } catch (KeycloakException ex) {
                LOG.error("Initializing Keycloak group failed for OrganizationalUnit '{}'.", ou.getName(), ex);
            }
        });
    }

    public void deleteOrganizationalUnitAndRolesByName(String organizationalUnitName) {
        LOG.info("Trying to delete default OrganizationalUnit with name '{}'", organizationalUnitName);
        OrganizationalUnitEntity unit = organizationalUnitRepository.findByName(organizationalUnitName);
        if (unit != null) {
            // This should automatically propagate to associated roles via @CascadeType.REMOVE
            organizationalUnitRepository.deleteByOrganizationalUnitId(unit.getOrganizationalUnitId());
        } else {
            LOG.warn("No OrganizationalUnit with name '{}' was found in database. Delete request has no effect.",
                    organizationalUnitName);
        }
    }
}
