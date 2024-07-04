package de.hsbo.kommonitor.datamanagement.auth;

import com.google.common.collect.Lists;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.model.AdminRoleType;
import de.hsbo.kommonitor.datamanagement.model.GroupAdminRolesPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.GroupAdminRolesType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.keycloak.representations.idm.authorization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminService {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakAdminService.class);

    private static final String CLIENT_RESOURCES_ADMIN_ROLE_NAME = "client-resources-creator";
    private static final String UNIT_RESOURCES_ADMIN_ROLE_NAME = "unit-resources-creator";
    private static final String CLIENT_THEMES_ADMIN_ROLE_NAME = "client-themes-creator";
    private static final String UNIT_THEMES_ADMIN_ROLE_NAME = "unit-themes-creator";
    private static final String CLIENT_USERS_ADMIN_ROLE_NAME = "client-users-creator";
    private static final String UNIT_USERS_ADMIN_ROLE_NAME = "unit-users-creator";

    private static final String REALM_MANAGEMENT_CLIENT_NAME = "realm-management";

    private static final String QUERY_USERS_ROLE_NAME = "query-users";
    private static final String QUERY_GROUPS_ROLE_NAME = "query-groups";

    public static final String KOMMONITOR_ID_ATTRIBUTE = "kommonitorId";
    public static final String KOMMONITOR_ROLE_TYPE_ATTRIBUTE = "roleType";
    public static final String KOMMONITOR_MANDANT_ATTRIBUTE = "mandant";

    private static final List<String> ADMIN_ROLES = Arrays.asList(
            CLIENT_RESOURCES_ADMIN_ROLE_NAME,
            UNIT_RESOURCES_ADMIN_ROLE_NAME,
            CLIENT_THEMES_ADMIN_ROLE_NAME,
            UNIT_THEMES_ADMIN_ROLE_NAME,
            CLIENT_USERS_ADMIN_ROLE_NAME,
            UNIT_USERS_ADMIN_ROLE_NAME
            );

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private Keycloak keycloak;

    @Autowired
    public KeycloakAdminService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String addGroup(OrganizationalUnitInputType inputOrganizationalUnit) throws KeycloakException {
        LOG.info("Trying to create Keycloak group for OrganizationalUnit '{}'.", inputOrganizationalUnit.getName());
        GroupsResource groupsResource = getRealmResource().groups();

        Response response = groupsResource.add(createGroupRepresentation(inputOrganizationalUnit));
        String keycloakGroupId = handleGroupCreationResponse(response);
        LOG.info("Successfully created Keycloak group with ID '{}'.", keycloakGroupId);
        return keycloakGroupId;
    }

    public String addSubGroup(OrganizationalUnitInputType inputOrganizationalUnit, OrganizationalUnitEntity parent) throws KeycloakException {
        LOG.info("Trying to create Keycloak group for OrganizationalUnit '{}' as child of Keycloak group '{}'.", inputOrganizationalUnit.getName(), parent.getKeycloakId().toString());
        GroupsResource groupsResource = getRealmResource().groups();
        GroupResource parentGroup = groupsResource.group(parent.getKeycloakId().toString());

        Response response = parentGroup.subGroup(createGroupRepresentation(inputOrganizationalUnit));
        String keycloakGroupId = handleGroupCreationResponse(response);
        LOG.info("Successfully created Keycloak group with ID '{}'.", keycloakGroupId);
        return keycloakGroupId;
    }

    public GroupRepresentation createGroupRepresentation(OrganizationalUnitInputType inputOrganizationalUnit) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());
        group.singleAttribute(KOMMONITOR_MANDANT_ATTRIBUTE, inputOrganizationalUnit.getMandant().toString());
        return group;
    }

    public void addRole(RoleRepresentation roleRep) {
        getRealmResource().roles().create(roleRep);
    }

    public void updateRole(String roleId, RoleRepresentation roleRep) {
        getRealmResource().rolesById().updateRole(roleId, roleRep);
    }

    public void updateGroup(String groupId, GroupRepresentation groupRep) {
        getRealmResource().groups().group(groupId).update(groupRep);
    }

    public void deleteGroup(String groupId) {
        getRealmResource().groups().group(groupId).remove();
    }

    public void deleteRole(String roleName) {
        getRealmResource().roles().get(roleName).remove();
    }

    public void deleteRolesForGroupName(String name) {
        ADMIN_ROLES.stream()
                .map(r -> String.join(".",name, r))
                .forEach(r -> {
                    try {
                        deleteRole(r);
                        LOG.debug("Successfully deleted role '{}'", r);
                    } catch (NotFoundException ex) {
                        LOG.warn("Role '{}' does not exists. Deletion will be skipped.", r);
                    }
                });
    }

    public void deleteGroupAndRoles(OrganizationalUnitEntity entity) {
        LOG.info("Trying to delete Keycloak group for OrganizationalUnit '{}' and Keycloak group '{}'.", entity.getName(), entity.getKeycloakId().toString());
        try {
            deleteGroup(entity.getKeycloakId().toString());
        } catch (NotFoundException ex) {
            LOG.warn("Group '{}' does not exist. Deletion will be skipped.", entity.getKeycloakId().toString());
        }
        LOG.info("Successfully deleted Keycloak group for OrganizationalUnit '{}' and Keycloak group '{}'.", entity.getName(), entity.getKeycloakId().toString());

        LOG.info("Trying to delete roles for OrganizationalUnit '{}'.", entity.getName());
        deleteRolesForGroupName(entity.getName());
        LOG.info("Successfully deleted roles for OrganizationalUnit '{}'.", entity.getName());
    }

    public void updateGroupAndRoles(OrganizationalUnitEntity entity, OrganizationalUnitInputType newInputOrganizationalUnit) {
        LOG.info("Trying to update Keycloak group for OrganizationalUnit '{}' and Keycloak group '{}'.", entity.getName(), entity.getKeycloakId().toString());
        try {
            GroupRepresentation groupRep = getGroupById(entity.getKeycloakId().toString());
            groupRep.setName(newInputOrganizationalUnit.getName());
            groupRep.singleAttribute(KOMMONITOR_MANDANT_ATTRIBUTE, newInputOrganizationalUnit.getMandant().toString());
            updateGroup(groupRep.getId(), groupRep);
        } catch (NotFoundException | KeycloakException ex) {
            LOG.warn("Group '{}' does not exist. Deletion will be skipped.", entity.getKeycloakId().toString());
        }
        LOG.info("Successfully updated Keycloak group for OrganizationalUnit '{}' and Keycloak group '{}'.", entity.getName(), entity.getKeycloakId().toString());

        LOG.info("Trying to update roles for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                entity.getName(), entity.getKeycloakId());

        // Update all admin roles for group
        ADMIN_ROLES.forEach(r -> {
            String oldRoleName = "";
            try {
                oldRoleName = String.join(".", entity.getName(), r);
                RoleRepresentation roleRepresentation = getRoleByName(oldRoleName);
                roleRepresentation.setName(String.join(".", newInputOrganizationalUnit.getName(), r));
                updateRole(roleRepresentation.getId(), roleRepresentation);
                LOG.debug("Successfully updated role '{}'", roleRepresentation.getName());
            } catch (KeycloakException | NotFoundException ex) {
                LOG.warn("Role '{}' does not exists. Update will be skipped.", oldRoleName);
            }
        });

        LOG.info("Successfully updated roles for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                entity.getOrganizationalUnitId(), entity.getKeycloakId());
    }

    public ManagementPermissionReference enablePermissions(OrganizationalUnitInputType inputOrganizationalUnit) {
        GroupResource groupResource = getRealmResource().groups().group(inputOrganizationalUnit.getKeycloakId());
        ManagementPermissionRepresentation managementPermission = new ManagementPermissionRepresentation(true);
        return groupResource.setPermissions(managementPermission);
    }

    public GroupRepresentation getGroupById(String id) throws KeycloakException {
        try {
            return getRealmResource().groups().group(id).toRepresentation();
        } catch (NotFoundException ex) {
            LOG.debug("Group with ID `{}` not found.", id);
            throw new KeycloakException("Specified group not found.", ex);
        }
    }

    public ClientRepresentation getClientByName(String name) throws KeycloakException {
        List<ClientRepresentation> clientList = getRealmResource().clients().findByClientId(name);
        if (clientList.size() == 0) {
            throw new KeycloakException(String.format("No client exists with name %s.", name));
        }
        if (clientList.size() > 1) {
            LOG.warn("More than one client with name {} exists. Only first client will be returned.", name);
        }
        return clientList.get(0);
    }

    public RoleRepresentation getRoleByName(String name) throws  KeycloakException {
        List<RoleRepresentation> roleList = getRealmResource().roles().list(name, true);
        if (roleList.size() == 0) {
            throw new KeycloakException(String.format("No role exists with name %s.", name));
        }
        if (roleList.size() > 1) {
            LOG.warn("More than one role with name {} exists. Only first role will be returned.", name);
        }
        return roleList.get(0);
    }

    public RoleRepresentation getRoleById(String id) throws KeycloakException {
        try {
            return getRealmResource().rolesById().getRole(id);
        } catch (NotFoundException ex) {
            LOG.debug("Role with ID `{}` not found.", id);
            throw new KeycloakException("Specified role not found.", ex);
        }
    }

    public RoleRepresentation getClientRoleByName(String clientId, String name) throws  KeycloakException {
        List<RoleRepresentation> roleList = getRealmResource().clients().get(clientId).roles().list(name, true);
        if (roleList.size() == 0) {
            throw new KeycloakException(String.format("No role exists with for client %s with name %s.", clientId, name));
        }
        if (roleList.size() > 1) {
            LOG.warn("More than one role for client {} with name {} exists. Only first role will be returned.", clientId, name);
        }
        return roleList.get(0);
    }

    public PolicyRepresentation getPolicyByName(String clientId, String policyName) {
        return  getRealmResource().clients().get(clientId).authorization().policies().findByName(policyName);
    }

    private RolePolicyRepresentation.RoleDefinition createClientRoleDef(String roleName) throws KeycloakException {
        RolePolicyRepresentation.RoleDefinition roleDef = new RolePolicyRepresentation.RoleDefinition();
        roleDef.setRequired(true);
        roleDef.setId(getRoleByName(roleName).getId());
        return roleDef;
    }

    private RolePolicyRepresentation createRolePolicyRepresentation(String keycloakId, String roleName, String groupName) throws KeycloakException {
        RolePolicyRepresentation rolePolicyRepresentation = new RolePolicyRepresentation();
        rolePolicyRepresentation.setLogic(Logic.POSITIVE);
        rolePolicyRepresentation.setName("member-of-" + keycloakId + "." + roleName);
        rolePolicyRepresentation.setDescription("memberOf(" + keycloakId + "." + roleName + ")");
        rolePolicyRepresentation.setRoles(Set.of(createClientRoleDef(groupName + "." + roleName)));
        return rolePolicyRepresentation;
    }

    private Set<String> getParentPolicies(ClientRepresentation clientRepresentation, OrganizationalUnitEntity parent) {
        Set<String> policySet = new HashSet<>();
        while (parent != null) {
            String policyName = "member-of-" + parent.getKeycloakId() + "." + CLIENT_USERS_ADMIN_ROLE_NAME;
            PolicyRepresentation policyRepresentation = getPolicyByName(clientRepresentation.getId(), policyName);
            policySet.add(policyRepresentation.getId());
            parent = parent.getParent();
        }
        return policySet;
    }

    private RoleRepresentation mapToRoleRepresentation(OrganizationalUnitInputType organizationalUnitInputType, String roleName) {
        RoleRepresentation role = new RoleRepresentation();
        String name = String.join(".", organizationalUnitInputType.getName(), roleName);
        role.setName(name);
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(KOMMONITOR_ID_ATTRIBUTE, Collections.singletonList(organizationalUnitInputType.getOrganizationalUnitId()));
        role.setAttributes(attributes);
        return role;
    }

    private RoleRepresentation mapToRoleRepresentation(String orgaName, String roleName) {
        RoleRepresentation role = new RoleRepresentation();
        String name = String.join(".", orgaName, roleName);
        role.setName(name);
        return role;
    }

    public void createRolesForGroup(OrganizationalUnitInputType inputOrganizationalUnit) throws KeycloakException {
        LOG.info("Trying to create roles for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                inputOrganizationalUnit.getName(), inputOrganizationalUnit.getKeycloakId());
        // Fetch 'realm-admin' client
        ClientRepresentation realmAdminClient = getClientByName(REALM_MANAGEMENT_CLIENT_NAME);
        RoleRepresentation queryUsersRole = getClientRoleByName(realmAdminClient.getId(), QUERY_USERS_ROLE_NAME);
        RoleRepresentation queryGroupsRole = getClientRoleByName(realmAdminClient.getId(), QUERY_GROUPS_ROLE_NAME);

        // Create all admin roles for group
        ADMIN_ROLES.stream()
                .map(r -> mapToRoleRepresentation(inputOrganizationalUnit, r))
                .forEach(this::addRole);

        // Associate user admin roles with query-users and query-group roles
        String unitUserRoleName = String.join(".", inputOrganizationalUnit.getName(), UNIT_USERS_ADMIN_ROLE_NAME);
        getRealmResource().roles().get(unitUserRoleName).addComposites(List.of(queryUsersRole, queryGroupsRole));

        String clientUserRoleName = String.join(".", inputOrganizationalUnit.getName(), CLIENT_USERS_ADMIN_ROLE_NAME);
        getRealmResource().roles().get(clientUserRoleName).addComposites(List.of(queryUsersRole, queryGroupsRole));

        LOG.info("Successfully created roles for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                inputOrganizationalUnit.getOrganizationalUnitId(), inputOrganizationalUnit.getKeycloakId());
    }

    public void createRolePolicies(OrganizationalUnitInputType inputOrganizationalUnit, OrganizationalUnitEntity parent) throws KeycloakException {
        LOG.info("Trying to create policies for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                inputOrganizationalUnit.getName(), inputOrganizationalUnit.getKeycloakId());
        ManagementPermissionRepresentation managementPermission = new ManagementPermissionRepresentation(true);

        // 1. enable fine grain permissions on new group
        // Enable scope permissions for group
        ManagementPermissionReference groupManagementPermissionRef = enablePermissions(inputOrganizationalUnit);

        // 2. create policies for new associated group (unit-users-creator and client-users-creator)

        // Enable Permissions for *.unit-users-creator role
        getRealmResource().roles().get(inputOrganizationalUnit.getName() + "." + UNIT_USERS_ADMIN_ROLE_NAME).setPermissions(managementPermission);

        // Create Role Policy for *.unit-users-creator role
        RolePolicyRepresentation unitRolePolicyRepresentation = createRolePolicyRepresentation(inputOrganizationalUnit.getKeycloakId(), UNIT_USERS_ADMIN_ROLE_NAME, inputOrganizationalUnit.getName());

        // Enable Permissions for *.client-users-creator role
        getRealmResource().roles().get(inputOrganizationalUnit.getName() + "." + CLIENT_USERS_ADMIN_ROLE_NAME).setPermissions(managementPermission);

        // Create Role Policy for *.client-users-creator role
        RolePolicyRepresentation clientRolePolicyRepresentation = createRolePolicyRepresentation(inputOrganizationalUnit.getKeycloakId(), CLIENT_USERS_ADMIN_ROLE_NAME, inputOrganizationalUnit.getName());

        // Fetch 'realm-admin' client
        ClientRepresentation realmAdminClient = getClientByName(REALM_MANAGEMENT_CLIENT_NAME);
        RolePoliciesResource rolePoliciesResource = getRealmResource().clients().get(realmAdminClient.getId()).authorization().policies().role();

        // Create role policies for client 'realm-admin'
        String unitRolePolicyId = handleRolePolicyCreationResponse(rolePoliciesResource.create(unitRolePolicyRepresentation));
        String clientRolePolicyId = handleRolePolicyCreationResponse(rolePoliciesResource.create(clientRolePolicyRepresentation));

        Set<String> policies = new HashSet<>();
        policies.add(unitRolePolicyId);
        policies.add(clientRolePolicyId);

        if(parent != null) {
            Set<String> parentPolicies = getParentPolicies(realmAdminClient, parent);
            policies.addAll(parentPolicies);
        }

        // 3. set policies for new group to enable group and subgroup management for admins with associated roles
        Map<String, String> scopePermissions = groupManagementPermissionRef.getScopePermissions();
        scopePermissions.forEach((k,v) -> {
            try {
                ScopeRepresentation policyScope = getPolicyScope(realmAdminClient, v);
                String name = policyScope.getName().replace("-", ".") + ".permission.group." + inputOrganizationalUnit.getKeycloakId();
                putRolePolicyForKeycloakGroupResourceScope(realmAdminClient, name, policyScope.getId(), groupManagementPermissionRef.getResource(), v, policies);

            } catch (KeycloakException e) {
                LOG.error("Error while setting role policy for group.", e);
            }
        });

        // 4. set same policies for associated user-creator roles for scope map-role
        ADMIN_ROLES.forEach(r -> {
            String roleName = inputOrganizationalUnit.getName() + "." + r;
            RoleResource roleResource = getRealmResource().roles().get(roleName);
            try {
                RoleRepresentation roleRepresentation = roleResource.toRepresentation();

                ManagementPermissionReference roleManagementPermissionRef = roleResource.setPermissions(managementPermission);
                String scopePermissionId = roleManagementPermissionRef.getScopePermissions().get("map-role");

                ScopeRepresentation policyScope =  getPolicyScope(realmAdminClient, scopePermissionId);

                String name = "map-role.permission." + roleRepresentation.getId();
                putRolePolicyForKeycloakGroupResourceScope(realmAdminClient, name, policyScope.getId(), roleManagementPermissionRef.getResource(), scopePermissionId, policies);

            } catch (NotFoundException ex) {
                LOG.error(String.format("Role with name '%s' does not exist. Policy creation will be skipped.", roleName), ex);
            } catch (KeycloakException ex) {
                LOG.error("Error while setting role policy for role.", ex);
            }
        });
        LOG.info("Successfully created policies for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                inputOrganizationalUnit.getName(), inputOrganizationalUnit.getKeycloakId());
    }

    public void putRolePolicyForKeycloakGroupResourceScope(ClientRepresentation clientRepresentation, String scopePermissionName, String scopePermissionId, String resourceId, String scopeId, Set<String> policies) {
        ScopePermissionRepresentation scopePermRep = new ScopePermissionRepresentation();
        scopePermRep.setName(scopePermissionName);
        scopePermRep.setType("scope");
        scopePermRep.setLogic(Logic.POSITIVE);
        scopePermRep.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
        scopePermRep.setId(scopeId);
        scopePermRep.setResources(Set.of(resourceId));
        scopePermRep.setScopes(Set.of(scopePermissionId));
        scopePermRep.setPolicies(policies);
        scopePermRep.setDescription("");

        getRealmResource().clients().get(clientRepresentation.getId()).authorization().permissions().scope().findById(scopeId).update(scopePermRep);
    }

    public ScopeRepresentation getPolicyScope(ClientRepresentation clientRepresentation, String policyId) throws KeycloakException {
        List<ScopeRepresentation> scopeList = getRealmResource().clients().get(clientRepresentation.getId()).authorization().policies().policy(policyId).scopes();
        if (scopeList.size() == 0) {
            throw new KeycloakException(String.format("No scope exists for policy id %s.", policyId));
        }
        if (scopeList.size() > 1) {
            LOG.warn("More than one scope for policy id {} exists. Only first role will be returned.", policyId);
        }
        return scopeList.get(0);
    }

    public String handleGroupCreationResponse(Response response) throws KeycloakException {
        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            String [] parts = response.getLocation().getPath().split("/");
            return parts[parts.length - 1];
        } else {
            throw new KeycloakException(String.format("Error while requesting Keycloak Admin REST API to create group. " +
                    "Response status code %s", response.getStatus()));
        }
    }

    public String handleRolePolicyCreationResponse(Response response) throws KeycloakException {
        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            Map entity = response.readEntity(Map.class);
            return (String) entity.get("id");
        } else {
            throw new KeycloakException(String.format("Error while requesting Keycloak Admin REST API to create role policy. " +
                    "Response status code %s", response.getStatus()));
        }
    }

    public RealmResource getRealmResource() {
        return keycloak.realm(keycloakRealm);
    }

    public List<RoleRepresentation> getRolesForGroup(String groupId) throws KeycloakException{
        try {
            GroupResource groupResource = getRealmResource().groups().group(groupId);
            return groupResource.roles().realmLevel().listAll();
        } catch (NotFoundException ex) {
            LOG.debug("No Keycloak group with ID '{}' found.", groupId);
            throw new KeycloakException("Keycloak group not found.", ex);
        }
    }

    public Set<GroupRepresentation> getAssociatedGroupsForRole(String roleName) throws KeycloakException {
        try {
            return getRealmResource().roles().get(roleName).getRoleGroupMembers();
        } catch (NotFoundException ex) {
            LOG.debug("No Keycloak role with ID '{}' found.", roleName);
            throw new KeycloakException("Keycloak role not found.", ex);
        }
    }

    public Map<String, Set<GroupRepresentation>> getRoleDelegatesSortedByRoleName(OrganizationalUnitEntity entity)  {
        Map<String, Set<GroupRepresentation>> roleDelegates = new HashMap<>();
        ADMIN_ROLES.forEach(r -> {
            String roleName = "";
            try {
                roleName = String.join(".", entity.getName(), r);
                Set<GroupRepresentation> groups = getAssociatedGroupsForRole(roleName);
                roleDelegates.put(r, groups);
            } catch (NotFoundException | KeycloakException ex) {
                LOG.warn("Role '{}' does not exists. Update will be skipped.", roleName);
            }
        });
        return roleDelegates;
    }

    public Map<String, Set<String>> getRoleDelegatesSortedByGroup(OrganizationalUnitEntity entity)  {
        Map<String, Set<String>> roleDelegates = new HashMap<>();
        ADMIN_ROLES.forEach(r -> {
            String roleName = "";
            try {
                roleName = String.join(".", entity.getName(), r);
                Set<GroupRepresentation> groups = getAssociatedGroupsForRole(roleName);
                String finalRoleName = roleName;
                groups.forEach(g -> {
                    try {
                        GroupRepresentation groupRep = getGroupById(g.getId());
                        if(roleDelegates.containsKey(entity.getOrganizationalUnitId())) {
                            roleDelegates.get(entity.getOrganizationalUnitId()).add(finalRoleName);
                        } else {
                            Set<String> roleSet = new HashSet<>();
                            roleSet.add(finalRoleName);
                            roleDelegates.put(entity.getOrganizationalUnitId(), roleSet);
                        }
                    } catch (KeycloakException e) {
                        throw new RuntimeException(e);
                    }

                });
            } catch (NotFoundException | KeycloakException ex) {
                LOG.warn("Role '{}' does not exists. Update will be skipped.", roleName);
            }
        });
        return roleDelegates;
    }


    /**
     * This methods references OrganizationalUnitEntity with the corresponding Keycloak roles
     * by setting the KomMonitor group ID as an extra parameter to the Keycloak role entities
     *
     * @param entity the OrganizationalUnitEntity representing a KomMonitor group
     */
    public void referenceOrganizationalUnitWithRoles(OrganizationalUnitEntity entity){
        ADMIN_ROLES.forEach(r -> {
            String roleName = "";
            try {
                roleName = String.join(".", entity.getName(), r);
                RoleResource roleResource = getRealmResource().roles().get(roleName);
                RoleRepresentation roleRep = roleResource.toRepresentation();
                roleRep.singleAttribute(KOMMONITOR_ID_ATTRIBUTE, entity.getOrganizationalUnitId());
                roleRep.singleAttribute(KOMMONITOR_ROLE_TYPE_ATTRIBUTE, r);
                roleResource.update(roleRep);
                LOG.debug("Successfully referenced KomMonitor group ID '{}' with Keycloak role '{}'", entity.getOrganizationalUnitId(), roleRep.getName());
            } catch (NotFoundException ex) {
                LOG.warn("Role '{}' does not exists. Update will be skipped.", roleName);
            }
        });
    }

    /**
     * This methods references OrganizationalUnitEntity with the corresponding Keycloak roles
     * by setting the KomMonitor group ID as an extra parameter to the Keycloak role entities
     *
     * @param entity the OrganizationalUnitEntity representing a KomMonitor group
     */
    public void referenceOrganizationalUnitWithGroup(OrganizationalUnitEntity entity){
        try {
            GroupResource groupResource = getRealmResource().groups().group(entity.getKeycloakId().toString());

            GroupRepresentation groupRep = new GroupRepresentation();
            groupRep.setName(entity.getName());
            groupRep.singleAttribute(KOMMONITOR_MANDANT_ATTRIBUTE, String.valueOf(entity.isMandant()));
            groupRep.singleAttribute(KOMMONITOR_ID_ATTRIBUTE, entity.getOrganizationalUnitId());
            groupResource.update(groupRep);
            LOG.debug("Successfully referenced KomMonitor group ID '{}' with Keycloak group '{}'", entity.getOrganizationalUnitId(), groupRep.getName());
        } catch (NotFoundException ex) {
            LOG.warn("Group '{}' does not exists. Update will be skipped.", entity.getName());
        }
    }

    private Map<String, Set<AdminRoleType>> groupRoleDelegatesByKeycloakId(List<GroupAdminRolesPUTInputType> roleDelegates) {
        return roleDelegates
                .stream()
                .collect(Collectors.toMap(GroupAdminRolesPUTInputType::getKeycloakId, rD -> new HashSet<>(rD.getAdminRoles())));
    }

    public void updateRoleDelegates(OrganizationalUnitEntity entity, List<GroupAdminRolesPUTInputType> roleDelegates) {
        Map<String, Set<AdminRoleType>> roleDelegateMap = groupRoleDelegatesByKeycloakId(roleDelegates);
        // 1. Check for all possible admin roles for the selected OrganizationalUnit, which Keycloak groups are
        // associated with it and check if the provided list of role delegates still have the same association.
        // If there is a match, remove the admin role from the provided list so that only new role delegates remain in
        // the list, which can be newly assigned to the associated group.
        ADMIN_ROLES.forEach(aR -> {
                    String roleName = String.join(".", entity.getName(), aR);
                    try {
                        Set<GroupRepresentation> groups = getAssociatedGroupsForRole(roleName);
                        groups.forEach(g -> {
                            try {
                                // if an admin role is not presented in the provided roles delegates, it must
                                // be removed from the delegated group
                                if(!roleDelegateMap.containsKey(g.getId())
                                        || !roleDelegateMap.get(g.getId()).contains(AdminRoleType.fromValue(aR))) {
                                    RoleRepresentation roleRep = getRoleByName(roleName);
                                    GroupResource groupResource = getRealmResource().groups().group(g.getId());
                                    groupResource.roles().realmLevel().remove(List.of(roleRep));
                                } else {
                                    // do nothing, if the admin role is still present in the role delegates, but remove
                                    // the role from the list, so that only new role delegates will remain in the end
                                    roleDelegateMap.get(g.getId()).remove(AdminRoleType.fromValue(aR));
                                }

                            } catch (KeycloakException ex) {
                                LOG.error(String.format("Error while fetching Keycloak group '%s'.", g.getId()), ex);
                            }
                        });
                    } catch (KeycloakException e) {
                        throw new RuntimeException(e);
                    }

                });
        // 2. For alle remaining role delegates associate a Keycloak role with the delegated Keycloak group
        roleDelegates.stream()
                .filter(r -> !r.getAdminRoles().isEmpty())
                .forEach(r -> {
                    GroupResource groupResource = getRealmResource().groups().group(r.getKeycloakId());
                    List<RoleRepresentation> roleRepList = r.getAdminRoles().stream()
                            .map(aR -> {
                                String roleName = String.join(".", entity.getName(), aR.getValue());
                                try {
                                    return getRoleByName(roleName);
                                } catch (KeycloakException ex) {
                                    LOG.error(String.format("Error while fetching role with name %s", roleName), ex);
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    groupResource.roles().realmLevel().add(roleRepList);
                });
    }
}
