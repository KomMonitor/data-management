package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ApiException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.auth.KeycloakAdminService;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Repository
@Component
public class OrganizationalUnitManager {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationalUnitManager.class);

    @Autowired
    OrganizationalUnitRepository organizationalUnitRepository;

    @Autowired
    PermissionManager permissionManager;

    @Autowired
    KeycloakAdminService keycloakAdminService;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String defaultAnonymousOUname;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String defaultAuthenticatedOUname;

    public OrganizationalUnitOverviewType addOrganizationalUnit(
            OrganizationalUnitInputType inputOrganizationalUnit,
            AuthInfoProvider provider
    ) throws Exception {
        String name = inputOrganizationalUnit.getName();
        LOG.info("Trying to persist OrganizationalUnit with name '{}'", name);

        if (organizationalUnitRepository.existsByName(name)) {
            LOG.error(
                    "The OrganizationalUnit with name '{}' already exists. Thus aborting add OrganizationalUnit request.",
                    name);
            throw new ApiException(400, "OrganizationalUnit already exists. Aborting addOrganizationalUnit request.");
        }

        // We do not allow Mandants with parents
        if (inputOrganizationalUnit.getParentId() != null && inputOrganizationalUnit.getMandant()) {
            LOG.warn("trying to add organizationalUnit as mandant in hierarchy!");
            throw new ApiException(400, "OrganizationalUnit cannot be Mandant and have a parent OrganizationalUnit. Aborting addOrganizationalUnit " +
                    "request.");
        }

        // We require global admin rights to create a mandant
        if (inputOrganizationalUnit.getMandant() && !provider.hasGlobalAdminPermissions()) {
            LOG.warn("trying to create mandant without global admin rola!");
            throw new ApiException(403, "Cannot create OrganizationalUnit as Mandant without global admin role!");
        }

        /*
         * ID will be autogenerated by JPA / Hibernate
         */
        OrganizationalUnitEntity jpaUnit = new OrganizationalUnitEntity();
        jpaUnit.setName(name);
        jpaUnit.setContact(inputOrganizationalUnit.getContact());
        jpaUnit.setDescription(inputOrganizationalUnit.getDescription());
        jpaUnit.setIsMandant(inputOrganizationalUnit.getMandant());

        String keycloakId;
        OrganizationalUnitEntity parent = null;

        if (inputOrganizationalUnit.getParentId() != null && !inputOrganizationalUnit.getParentId().isEmpty()) {
            parent = organizationalUnitRepository.findByOrganizationalUnitId(inputOrganizationalUnit.getParentId());
            if (parent == null) {
                LOG.error("parent with given id {} does not exist.", inputOrganizationalUnit.getParentId());
                throw new Exception("parent with given id does not exist");
            }
            if (!provider.checkOrganizationalUnitCreationPermissions(parent)) {
                throw new ApiException(405, "The OrganizationalUnit can not be created due to insufficient permissions.");
            }
            jpaUnit.setParent(parent);
            keycloakId = keycloakAdminService.addSubGroup(inputOrganizationalUnit, parent);

        } else {
            if (!provider.checkOrganizationalUnitCreationPermissions(null)) {
                throw new ApiException(405, "The OrganizationalUnit can not be created due to insufficient permissions.");
            }
            keycloakId = keycloakAdminService.addGroup(inputOrganizationalUnit);
        }

        try {
            // Find mandant, possibly traversing upwards the hierarchy
            OrganizationalUnitEntity current = jpaUnit;
            do {
                if (current.isMandant()) {
                    // We have found a mandant
                    jpaUnit.setMandant(current);
                    break;
                } else {
                    // Check if there are more upstream units to search
                    if (current.getParent() != null) {
                        current = current.getParent();
                    } else {
                        // there is no mandant in this hierarchy
                        String msg = String.format("Creating OrganizationalUnit %s failed - Unit is not a mandant and no mandant " +
                                "is found in the hierarchy! Group creation aborted.", inputOrganizationalUnit.getName());
                        LOG.error(msg);
                        throw new ApiException(400, msg);
                    }
                }
            } while (true);
        } catch (ApiException ex) {
            keycloakAdminService.deleteGroup(keycloakId);
            throw ex;
        }

        inputOrganizationalUnit.setKeycloakId(keycloakId);
        OrganizationalUnitEntity saved = null;
        try {
            keycloakAdminService.createRolesForGroup(inputOrganizationalUnit);
            keycloakAdminService.createRolePolicies(inputOrganizationalUnit, parent);

            jpaUnit.setKeycloakId(UUID.fromString(keycloakId));
            saved = organizationalUnitRepository.saveAndFlush(jpaUnit);

            // Generate appropriate roles
            List<PermissionEntity> roles = new ArrayList<>();
            for (PermissionLevelType level : PermissionLevelType.values()) {
                roles.add(permissionManager.addPermission(saved, level, PermissionResourceType.RESOURCES));
            }
            roles.add(permissionManager.addPermission(saved, PermissionLevelType.CREATOR, PermissionResourceType.USERS));
            roles.add(permissionManager.addPermission(saved, PermissionLevelType.CREATOR, PermissionResourceType.THEMES));
            saved.setPermissions(roles);
            // Reference roles
            keycloakAdminService.referenceOrganizationalUnitWithGroup(saved);
            // Reference groups
            keycloakAdminService.referenceOrganizationalUnitWithRoles(saved);

            return OrganizationalUnitMapper.mapToSwaggerOrganizationalUnit(saved);
        } catch (ClientErrorException | KeycloakException ex) {
            LOG.error(String.format("Creating roles and policies for OrganizationalUnit %s and Keycloak ID %s failed." +
                    "Group creation aborted.", inputOrganizationalUnit.getName(), keycloakId));
            keycloakAdminService.deleteGroup(keycloakId);
            keycloakAdminService.deleteRolesForGroupName(inputOrganizationalUnit.getName());
            if (saved != null) {
                organizationalUnitRepository.delete(saved);
            }
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Creating group and roles in Keycloak failed " +
                    "due to internal Keycloak conflicts.");
        }
    }

    public boolean deleteOrganizationalUnitAndRolesById(String organizationalUnitId) throws ApiException {
        OrganizationalUnitEntity unit = organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
        if (unit != null) {
            // Prevent deletion of default units.
            if (unit.getName().equals(defaultAnonymousOUname) || unit.getName().equals(defaultAuthenticatedOUname)) {
                LOG.error("Trying to delete default OrganizationalUnits.");
                throw new ApiException(HttpStatus.FORBIDDEN.value(),
                        "Tried to delete default OrganizationalUnits");
            }
            keycloakAdminService.deleteGroupAndRoles(unit);
            // This should automatically propagate to associated roles via @CascadeType.REMOVE
            organizationalUnitRepository.deleteByOrganizationalUnitId(organizationalUnitId);
            return true;
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database. Delete request has no effect.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete OrganizationalUnit, but no OrganizationalUnit " +
                            "existes with id " +
                            organizationalUnitId);
        }
    }

    public List<OrganizationalUnitOverviewType> getOrganizationalUnits(AuthInfoProvider provider) {
        Stream<OrganizationalUnitEntity> units;
        if (provider.hasGlobalAdminPermissions()) {
            LOG.debug("user is global admin - retrieving all organizationalUnits from db");
            units = organizationalUnitRepository.findAll().stream();
        } else {
            LOG.debug("user is not admin - retrieving organizationalUnits in same mandants from db");
            units = provider.getGroupNames().stream()
                    .map(name -> {
                        try {
                            LOG.debug("User has access to mandant: {}", name);
                            // Get Mandant of the user by checking group->getMandant()
                            String keycloakIdentifier = keycloakAdminService.getGroupByName(name).getId();
                            OrganizationalUnitEntity unit = organizationalUnitRepository.findByKeycloakId(UUID.fromString(keycloakIdentifier));
                            if (unit == null) {
                                throw new RuntimeException("cannot find group by keycloak-id - possible desync!");
                            } else {
                                return unit.getMandant();
                            }
                        } catch (KeycloakException e) {
                            throw new RuntimeException("cannot find group with name! Token may be corrupted");
                        }
                    })
                    .distinct()
                    // User is allowed to see all descendants of the given mandant
                    .map(OrganizationalUnitEntity::getDescendants)
                    .flatMap(Collection::stream);
        }

        return units.map(o -> {
            OrganizationalUnitOverviewType orgaOverview = OrganizationalUnitMapper.mapToSwaggerOrganizationalUnit(o);
            orgaOverview.setUserAdminRoles(provider.getOrganizationalUnitCreationPermissions(o));
            return orgaOverview;
        }).toList();
    }

    public OrganizationalUnitOverviewType getOrganizationalUnitById(String organizationalUnitId, AuthInfoProvider provider) throws ResourceNotFoundException {
        LOG.info("Retrieving OrganizationalUnit for organizationalUnitId '{}'", organizationalUnitId);
        if (provider.hasGlobalAdminPermissions()) {
            OrganizationalUnitEntity unit = organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
            OrganizationalUnitOverviewType orgaOverview = OrganizationalUnitMapper.mapToSwaggerOrganizationalUnit(unit);
            orgaOverview.setUserAdminRoles(provider.getOrganizationalUnitCreationPermissions(unit));
            return orgaOverview;
        } else {
            Set<OrganizationalUnitEntity> mandants = provider.getGroupNames().stream()
                    .map(name -> {
                        // Get Mandant of the user by checking group->getMandant()
                        try {
                            String keycloakId = keycloakAdminService.getGroupByName(name).getId();
                            return organizationalUnitRepository.findByKeycloakId(UUID.fromString(keycloakId)).getMandant();
                        } catch (KeycloakException e) {
                            throw new RuntimeException("Cannot find group with given name! Token may be corrupted");
                        }
                    }).collect(Collectors.toSet());
            // Retrieve OrganizationalUnit
            for (OrganizationalUnitEntity mandant : mandants) {
                // Only search within allowed mandants
                OrganizationalUnitEntity organizationalUnitEntity =
                        organizationalUnitRepository.findByKeycloakIdAndMandantOrganizationalUnitId(
                                UUID.fromString(organizationalUnitId),
                                mandant.getOrganizationalUnitId());
                if (organizationalUnitEntity != null) {
                    OrganizationalUnitOverviewType orgaOverview = OrganizationalUnitMapper.mapToSwaggerOrganizationalUnit(organizationalUnitEntity);

                    orgaOverview.setUserAdminRoles(provider.getOrganizationalUnitCreationPermissions(organizationalUnitEntity));
                    return orgaOverview;
                }
            }
        }

        LOG.error("No OrganizationalUnit with id '{}' was found in database. GET request has no effect.",
                organizationalUnitId);
        throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                "No OrganizationalUnit exists with id " + organizationalUnitId);
    }

    /**
     * Fetches all Keycloak roles that are associated with a Keycloak group that represents the provided
     * {@link OrganizationalUnitEntity} and returns them as a list of  {@link GroupAdminRolesType}
     *
     * @param organizationalUnitId The Id of the organizational unit to request the associated roles for. This unit must
     *                             have a corresponding Keycloak group.
     * @return administrative roles for the organizational unit
     */
    public List<GroupAdminRolesType> getGroupAdminRoles(String organizationalUnitId) throws ResourceNotFoundException {
        LOG.info("Retrieving authorized admin roles for OrganizationalUnitId '{}'", organizationalUnitId);

        OrganizationalUnitEntity organizationalUnitEntity =
                organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
        if (organizationalUnitEntity != null) {
            try {
                LOG.info("Fetch admin roles from Keycloak for organizational unit {}", organizationalUnitEntity.getOrganizationalUnitId());
                List<RoleRepresentation> simpleRolesRepList = keycloakAdminService.getRolesForGroup(organizationalUnitEntity.getKeycloakId().toString());

                // Requested roles for group are a simple representation and do not contain attributes. Hence, we have
                // to request every single role firs, to fetch all details.
                List<RoleRepresentation> rolesRepList = simpleRolesRepList.stream()
                        .map(r -> {
                            try {
                                return keycloakAdminService.getRoleById(r.getId());
                            } catch (KeycloakException ex) {
                                LOG.error("Error while fetching Keycloak role with ID '{}'.", r.getId());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();
                LOG.debug("Found {} assigned admin roles for organizational unit {}", rolesRepList.size(), organizationalUnitEntity.getOrganizationalUnitId());
                Map<String, List<RoleRepresentation>> groupsMap =
                        rolesRepList.stream()
                                .filter(r -> r.getAttributes().get(KeycloakAdminService.KOMMONITOR_ID_ATTRIBUTE) != null
                                        && r.getAttributes().get(KeycloakAdminService.KOMMONITOR_ROLE_TYPE_ATTRIBUTE) != null
                                )
                                .collect(Collectors.groupingBy(r -> r.getAttributes().get(KeycloakAdminService.KOMMONITOR_ID_ATTRIBUTE).get(0)));
                LOG.debug("Found admin roles for {} groups that are valid.", groupsMap.size());

                return groupsMap.entrySet().stream()
                        .map(e -> {
                            List<AdminRoleType> roleNames = e.getValue().stream()
                                    .map(v -> AdminRoleType.fromValue(v.getAttributes().get(KeycloakAdminService.KOMMONITOR_ROLE_TYPE_ATTRIBUTE).get(0)))
                                    .collect(Collectors.toList());
                            return new GroupAdminRolesType(e.getKey(), roleNames);
                        }).toList();
            } catch (KeycloakException ex) {
                LOG.error(String.format("Error while trying to fetch admin roles from Keycloak for OrganizationalUnit" +
                        " '%s'.", organizationalUnitEntity.getOrganizationalUnitId()), ex);
                return Collections.emptyList();
            }
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to fetch Keycloak roles, but no OrganizationalUnit existes with id " +
                            organizationalUnitId);
        }
    }

    public List<GroupAdminRolesType> getDelegatedGroupAdminRoles(String organizationalUnitId) throws ResourceNotFoundException {
        LOG.info("Retrieving delegated admin roles for OrganizationalUnitId '{}'", organizationalUnitId);

        OrganizationalUnitEntity organizationalUnitEntity =
                organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
        if (organizationalUnitEntity != null) {
            LOG.info("Fetch delegated admin roles from Keycloak for organizational unit {}", organizationalUnitEntity.getOrganizationalUnitId());
            Map<String, Set<GroupRepresentation>> roleDelegateGroups = keycloakAdminService.getRoleDelegatesSortedByRoleName(organizationalUnitEntity);

            Map<String, List<AdminRoleType>> roleDelegateResultMap = new HashMap<>();
            roleDelegateGroups.forEach((k, v) -> {
                v.forEach(g -> {
                    try {
                        GroupRepresentation groupRep = keycloakAdminService.getGroupById(g.getId());
                        List<String> kommonitorIdAttr = groupRep.getAttributes().get(KeycloakAdminService.KOMMONITOR_ID_ATTRIBUTE);
                        if (kommonitorIdAttr == null) {
                            LOG.warn("Role delegate Keycloak group with ID '{}' does not contain '{}' attribute. " +
                                    "Can not reference KomMonitor Organizational Unit.", groupRep.getId(), KeycloakAdminService.KOMMONITOR_ID_ATTRIBUTE);
                        } else {
                            String kommonitorId = kommonitorIdAttr.get(0);
                            if (roleDelegateResultMap.containsKey(kommonitorId)) {
                                roleDelegateResultMap.get(kommonitorId).add(AdminRoleType.fromValue(k));
                            } else {
                                roleDelegateResultMap.put(kommonitorId, new ArrayList<>(List.of(AdminRoleType.fromValue(k))));
                            }
                        }
                    } catch (KeycloakException ex) {
                        LOG.error(String.format("Error while handling role delegate for group with ID '%s'", g.getId()), ex);
                    }
                });
            });
            return roleDelegateResultMap.entrySet().stream()
                    .map(e -> new GroupAdminRolesType(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to fetch Keycloak roles, but no OrganizationalUnit existes with id " +
                            organizationalUnitId);
        }
    }

    public OrganizationalUnitPermissionOverviewType getOrganizationalUnitPermissionsById(String organizationalUnitId) throws ResourceNotFoundException {
        LOG.info("Retrieving OrganizationalUnit->permissions for organizationalUnitId '{}'", organizationalUnitId);

        OrganizationalUnitEntity organizationalUnitEntity =
                organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);

        if (organizationalUnitEntity != null) {
            return OrganizationalUnitMapper.mapToSwapperOUPermissionOverviewType(organizationalUnitEntity);
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database. Delete request has no effect.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete OrganizationalUnit, but no OrganizationalUnit " +
                            "existes with id " +
                            organizationalUnitId);
        }
    }

    public String updateOrganizationalUnit(OrganizationalUnitInputType newData,
                                           String organizationalUnitId) throws Exception {
        LOG.info("Trying to update OrganizationalUnit with organizationalUnitId '{}'", organizationalUnitId);
        if (organizationalUnitRepository.existsByOrganizationalUnitId(organizationalUnitId)) {
            OrganizationalUnitEntity organizationalUnitEntity =
                    organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);

            keycloakAdminService.updateGroupAndRoles(organizationalUnitEntity, newData);

            organizationalUnitEntity.setName(newData.getName());
            organizationalUnitEntity.setContact(newData.getContact());
            organizationalUnitEntity.setDescription(newData.getDescription());
            if (newData.getKeycloakId() != null) {
                organizationalUnitEntity.setKeycloakId(UUID.fromString(newData.getKeycloakId()));
            }
            organizationalUnitEntity.setIsMandant(newData.getMandant());

            if (organizationalUnitEntity.getParent() != null) {

                if (newData.getParentId() == null) {
                    // child group becomes a root group
                    organizationalUnitEntity.setParent(null);

                } else {
                    // child group gets a new parent group
                    OrganizationalUnitEntity parent =
                            organizationalUnitRepository.findByOrganizationalUnitId(newData.getParentId());
                    if (parent == null) {
                        LOG.error(
                                "parent with given id {} does not exist.",
                                newData.getParentId());
                        throw new Exception("parent with given id does not exist");
                    }
                    organizationalUnitEntity.setParent(parent);
                }
            }
            //TODO check if we want to allow moving a root group to a child group
            organizationalUnitRepository.saveAndFlush(organizationalUnitEntity);
            return organizationalUnitEntity.getOrganizationalUnitId();
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database. Update request has no effect.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update OrganizationalUnit, but no OrganizationalUnit " +
                            "exists with id " + organizationalUnitId);
        }
    }

    public void updateDelegatedGroupAdminRoles(String organizationalUnitId, List<GroupAdminRolesPUTInputType> organizationalUnitData) throws ResourceNotFoundException {
        LOG.info("Updating delegated admin roles for OrganizationalUnitId '{}'", organizationalUnitId);

        OrganizationalUnitEntity organizationalUnitEntity =
                organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);

        if (organizationalUnitEntity != null) {
            // GroupAdminRoles should come with an organization name, since Keycloak roles are only findable via name
            List<GroupAdminRolesPUTInputType> preparedOrganizationalUnitData = organizationalUnitData.stream()
                    .map(o -> {
                        if (o.getOrganizationalUnitName() == null || o.getKeycloakId() == null) {
                            OrganizationalUnitEntity delegatedOrga =
                                    organizationalUnitRepository.findByOrganizationalUnitId(o.getOrganizationalUnitId());
                            if (delegatedOrga == null) {
                                LOG.warn("No delegated OrganizationalUnit with id '{}' was found in database.", organizationalUnitId);
                                return new GroupAdminRolesPUTInputType(o.getOrganizationalUnitId(), o.getAdminRoles());
                            } else {
                                return new GroupAdminRolesPUTInputType(o.getOrganizationalUnitId(), o.getAdminRoles())
                                        .organizationalUnitName(delegatedOrga.getName())
                                        .keycloakId(delegatedOrga.getKeycloakId().toString());
                            }
                        } else {
                            return new GroupAdminRolesPUTInputType(o.getOrganizationalUnitId(), o.getAdminRoles())
                                    .organizationalUnitName(o.getOrganizationalUnitName())
                                    .keycloakId(o.getKeycloakId());
                        }
                    })
                    .filter(o -> o.getOrganizationalUnitName() != null && o.getKeycloakId() != null).toList();

            keycloakAdminService.updateRoleDelegates(organizationalUnitEntity, preparedOrganizationalUnitData);
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update Keycloak roles, but no OrganizationalUnit existes with id " +
                            organizationalUnitId);
        }
    }

    public void initializeKeycloakGroup(OrganizationalUnitEntity entity) throws KeycloakException, ApiException {
        String keycloakId;
        if (entity.getParent() != null) {
            keycloakId = keycloakAdminService.addSubGroup(entity, entity.getParent());
        } else {
            keycloakId = keycloakAdminService.addGroup(entity);
        }

        try {
            keycloakAdminService.createRolesForGroup(entity);
            keycloakAdminService.createRolePolicies(entity, entity.getParent());

            entity.setKeycloakId(UUID.fromString(keycloakId));
            organizationalUnitRepository.saveAndFlush(entity);

            // Reference roles
            keycloakAdminService.referenceOrganizationalUnitWithGroup(entity);
            // Reference groups
            keycloakAdminService.referenceOrganizationalUnitWithRoles(entity);

        } catch (ClientErrorException | KeycloakException ex) {
            LOG.error(String.format("Creating roles and policies for OrganizationalUnit %s and Keycloak ID %s failed." +
                    "Group creation aborted.", entity.getName(), keycloakId));
            keycloakAdminService.deleteGroup(keycloakId);
            keycloakAdminService.deleteRolesForGroupName(entity.getName());

            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Creating group and roles in Keycloak failed " +
                    "due to internal Keycloak conflicts.");
        }
    }
    
    protected void syncAllOrganizationalUnits() {
        organizationalUnitRepository.findAll().forEach(this::syncOrganizationalUnit);
    }

    protected void syncOrganizationalUnit(String organizationalUnitId) throws ResourceNotFoundException {

        OrganizationalUnitEntity entity = organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
        if (entity != null) {
            syncOrganizationalUnit(entity);
        } else {
            LOG.error("No OrganizationalUnit with id '{}' was found in database. Sync request has no effect.",
                    organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to sync OrganizationalUnit, but no OrganizationalUnit existes with id " + organizationalUnitId);
        }
    }

    protected void syncOrganizationalUnit(OrganizationalUnitEntity entity) {

        LOG.debug("Trying to sync OrganizationalUnit '{}' with Keycloak group.", entity.getOrganizationalUnitId());
        if (entity.getKeycloakId() == null) {
            try {
                GroupRepresentation groupRep = keycloakAdminService.getGroupByName(entity.getName());
                entity.setKeycloakId(UUID.fromString(groupRep.getId()));
                organizationalUnitRepository.saveAndFlush(entity);
                LOG.debug("Successfully updated OrganizationalUnit with Keycloak ID '{}'.", groupRep.getId());

            } catch (KeycloakException e) {
                LOG.error("Can not find Keycloak group with name '{}'", entity.getName());
            }
        }
        try {
            LOG.debug("Trying to sync Keycloak group to OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
            keycloakAdminService.syncGroup(entity);
            LOG.debug("Successfully synced Keycloak group to OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
            LOG.debug("Trying to sync Keycloak roles to OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
            keycloakAdminService.syncRoles(entity);
            LOG.debug("Trying to sync Keycloak role policies to OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
            keycloakAdminService.syncRolePolicies(entity);
            LOG.debug("Successfully synced Keycloak role policies to OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
            LOG.debug("Trying to sync Keycloak upper group associations for OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
            keycloakAdminService.syncUpperGroupAssociation(entity);
            LOG.debug("Successfully syncedKeycloak upper group associations for OrganizationalUnit '{}'", entity.getOrganizationalUnitId());
        } catch (KeycloakException e) {
            LOG.error(String.format("Error while trying to sync Keycloak group with ID '%s'.", entity.getKeycloakId()));
        }
    }

}
