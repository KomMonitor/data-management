package de.hsbo.kommonitor.datamanagement.config.db;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.*;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Repository
@Component
public class DbInitLoader_5_0_0 implements DbInitLoader {

    private static final Logger LOG = LoggerFactory.getLogger(DbInitLoader_5_0_0.class);

    private static final String DB_VERSION = "5.0.0";

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String defaultAnonymousOUname;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String defaultAuthenticatedOUname;

    @Value("${kommonitor.migration.delete-legacy-admin-organizationalUnit:true}")
    private boolean deleteAdminOu;

    @Value("${kommonitor.migration.delete-legacy-public-organizationalUnit:true}")
    private boolean deletePublicOu;

    @Autowired
    PermissionManager permissionManager;
    @Autowired
    OrganizationalUnitManager organizationalUnitManager;
    @Autowired
    OrganizationalUnitRepository organizationalUnitRepository;
    @Autowired
    GeoresourcesMetadataRepository georesourceRepository;
    @Autowired
    SpatialUnitsMetadataRepository spatialUnitsRepository;
    @Autowired
    IndicatorsMetadataRepository indicatorsRepository;
    @Autowired
    IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepository;


    public String getDbVersion() {
        return DB_VERSION;
    }

    @Override
    public void load() {
        LOG.info("Run initial data migration tasks for KomMonitor DB schema version {}.", DB_VERSION);
        // Delete special 'public' and 'kommonitor' OrganizationalUnit
        if (deletePublicOu) {
            deleteOrganizationalUnitAndRolesByName(defaultAnonymousOUname);
        }
        if (deleteAdminOu) {
            deleteOrganizationalUnitAndRolesByName(defaultAuthenticatedOUname);
        }
        // Initialize Keycloak groups for all other OrganizationalUnits
        setupOrganizationalUnits();
        LOG.info("Successfully migrated KomMonitor DB.");
    }

    private void setupOrganizationalUnits() {
        LOG.info("Load additional permissions if missing and create Keycloak groups for OrganizationalUnits.");
        List<OrganizationalUnitEntity> unitList = organizationalUnitRepository.findAll();
        // We must ensure that parent Keycloak groups will be created first. Otherwise, creating a group will fail
        // if its parent group does not exist
        Iterator<OrganizationalUnitEntity> iterator = unitList.iterator();
        while (iterator.hasNext()) {
            OrganizationalUnitEntity orga = iterator.next();
            if (orga.getParent() == null || orga.getParent().getKeycloakId() != null) {
                setupGroup(orga);
                iterator.remove();
            }
            if (!iterator.hasNext()) {
                iterator = unitList.iterator();
            }
        }
        LOG.info("Successfully created permissions and Keycloak groups.");
    }

    private void setupGroup(OrganizationalUnitEntity ou) {

        permissionManager.addPermission(ou, PermissionLevelType.CREATOR, PermissionResourceType.USERS);
        permissionManager.addPermission(ou, PermissionLevelType.CREATOR, PermissionResourceType.THEMES);

        try {
            organizationalUnitManager.initializeKeycloakGroup(ou);
        } catch (Exception ex) {
            LOG.error("Initializing Keycloak group failed for OrganizationalUnit '{}'.", ou.getName(), ex);
        }

    }

    public void deleteOrganizationalUnitAndRolesByName(String organizationalUnitName) {
        LOG.info("Deleting default OrganizationalUnit with name '{}'", organizationalUnitName);
        OrganizationalUnitEntity unit = organizationalUnitRepository.findByName(organizationalUnitName);
        if (unit != null) {
            // First, remove all associations of single permissions from datasets
            unit.getPermissions().forEach(p -> {
                LOG.info("Remove {} permissions for OrganizationalUnit '{}'.", p.getPermissionLevel().getValue(), organizationalUnitName);
                removeGeoresourcePermissions(p);
                removeSpatialUnitsPermissions(p);
                removeIndicatorsPermission(p);
                removeIndicatorsSpatialUnitsPermission(p);
            });
            // This should automatically propagate to associated roles via @CascadeType.REMOVE
            LOG.info("Remove OrganizationalUnit '{}'.", organizationalUnitName);
            try {
                organizationalUnitRepository.deleteByOrganizationalUnitId(unit.getOrganizationalUnitId());
            } catch (Exception ex) {
                LOG.error("Error while trying to delete OrganizationalUnit '{}'", organizationalUnitName);
            }
        } else {
            LOG.warn("No OrganizationalUnit with name '{}' was found in database. Delete request has no effect.",
                    organizationalUnitName);
        }
    }

    private void removeIndicatorsSpatialUnitsPermission(PermissionEntity p) {
        indicatorSpatialUnitsRepository.findAll()
                .stream()
                .filter(e -> e.getPermissions().contains(p))
                .forEach(e -> {
                    LOG.debug("Remove permission '{}-{}' from Indicator timeseries '{}'", p.getOrganizationalUnit().getName(), p.getPermissionLevel().getValue(), e.getEntryId());
                    HashSet<PermissionEntity> currentPermissions = e.getPermissions();
                    currentPermissions.remove(p);
                    e.setPermissions(currentPermissions);
                    indicatorSpatialUnitsRepository.saveAndFlush(e);
                });
    }

    private void removeIndicatorsPermission(PermissionEntity p) {
        indicatorsRepository.findAll()
                .stream()
                .filter(e -> e.getPermissions().contains(p))
                .forEach(e -> {
                    LOG.debug("Remove permission '{}-{}' from Indicator metadata '{}'", p.getOrganizationalUnit().getName(), p.getPermissionLevel().getValue(), e.getDatasetId());
                    HashSet<PermissionEntity> currentPermissions = e.getPermissions();
                    currentPermissions.remove(p);
                    e.setPermissions(currentPermissions);
                    indicatorsRepository.saveAndFlush(e);
                });
    }

    private void removeSpatialUnitsPermissions(PermissionEntity p) {
        spatialUnitsRepository.findAll()
                .stream()
                .filter(e -> e.getPermissions().contains(p))
                .forEach(e -> {
                    LOG.debug("Remove permission '{}-{}' from SpatialUnit '{}'", p.getOrganizationalUnit().getName(), p.getPermissionLevel().getValue(), e.getDatasetId());
                    HashSet<PermissionEntity> currentPermissions = e.getPermissions();
                    currentPermissions.remove(p);
                    e.setPermissions(currentPermissions);
                    spatialUnitsRepository.saveAndFlush(e);
                });
    }

    private void removeGeoresourcePermissions(PermissionEntity p) {
        georesourceRepository.findAll()
                .stream()
                .filter(e -> e.getPermissions().contains(p))
                .forEach(e -> {
                    LOG.debug("Remove permission '{}-{}' from Georesource '{}'", p.getOrganizationalUnit().getName(), p.getPermissionLevel().getValue(), e.getDatasetId());
                    HashSet<PermissionEntity> currentPermissions = e.getPermissions();
                    currentPermissions.remove(p);
                    e.setPermissions(currentPermissions);
                    georesourceRepository.saveAndFlush(e);
                });
    }

}
