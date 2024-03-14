package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.AccessControlController;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
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

@Service
public class KeycloakAdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessControlController.class);

    private static final String CLIENT_RESOURCES_ADMIN_ROLE_NAME = "client-resources-creator";
    private static final String UNIT_RESOURCES_ADMIN_ROLE_NAME = "unit-resources-creator";
    private static final String CLIENT_THEMES_ADMIN_ROLE_NAME = "client-themes-creator";
    private static final String UNIT_THEMES_ADMIN_ROLE_NAME = "unit-themes-creator";
    private static final String CLIENT_USERS_ADMIN_ROLE_NAME = "client-users-creator";
    private static final String UNIT_USERS_ADMIN_ROLE_NAME = "unit-users-creator";

    private static final String REALM_MANAGEMENT_CLIENT_NAME = "realm-management";

    private static final String QUERY_USERS_ROLE_NAME = "query-users";
    private static final String QUERY_GROUPS_ROLE_NAME = "query-groups";

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
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());
        group.setAttributes(Map.of("mandant", List.of(inputOrganizationalUnit.getMandant().toString())));

        Response response = groupsResource.add(group);
        String keycloakGroupId = handleGroupCreationResponse(response);
        LOG.info("Successfully created Keycloak group with ID '{}'.", keycloakGroupId);
        return keycloakGroupId;
    }

    public String addSubGroup(OrganizationalUnitInputType inputOrganizationalUnit, OrganizationalUnitEntity parent) throws KeycloakException {
        LOG.info("Trying to create Keycloak group for OrganizationalUnit '{}' as child of Keycloak group '{}'.", inputOrganizationalUnit.getName(), parent.getKeycloakId().toString());
        GroupsResource groupsResource = getRealmResource().groups();
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());
        group.setAttributes(Map.of("mandant", List.of(inputOrganizationalUnit.getMandant().toString())));

        GroupResource parentGroup = groupsResource.group(parent.getKeycloakId().toString());

        Response response = parentGroup.subGroup(group);
        String keycloakGroupId = handleGroupCreationResponse(response);
        LOG.info("Successfully created Keycloak group with ID '{}'.", keycloakGroupId);
        return keycloakGroupId;
    }

    public void addRole(RoleRepresentation roleRep) {
        getRealmResource().roles().create(roleRep);
    }

    public void deleteGroup(String groupId) {
        getRealmResource().groups().group(groupId).remove();
    }

    public void deleteRole(String roleId) {
        getRealmResource().roles().get(roleId).remove();
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
        ADMIN_ROLES.stream()
                .map(r -> String.join(".", entity.getName(), r))
                .forEach(r -> {
                    try {
                        deleteRole(r);
                        LOG.debug("Successfully deleted role '{}'", r);
                    } catch (NotFoundException ex) {
                        LOG.warn("Role '{}' does not exists. Deletion will be skipped.", r);
                    }
                });
        LOG.info("Successfully deleted roles for OrganizationalUnit '{}'.", entity.getName());
    }

    public ManagementPermissionReference enablePermissions(OrganizationalUnitInputType inputOrganizationalUnit) {
        GroupResource groupResource = getRealmResource().groups().group(inputOrganizationalUnit.getKeycloakId());
        ManagementPermissionRepresentation managementPermission = new ManagementPermissionRepresentation(true);
        return groupResource.setPermissions(managementPermission);
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

    private RolePolicyRepresentation createRolePolicyRepresentation(String orgaName, String roleName) throws KeycloakException {
        RolePolicyRepresentation rolePolicyRepresentation = new RolePolicyRepresentation();
        rolePolicyRepresentation.setLogic(Logic.POSITIVE);
        rolePolicyRepresentation.setName("member-of-" + orgaName + "." + roleName);
        rolePolicyRepresentation.setDescription("memberOf(" + orgaName + "." + roleName + ")");
        rolePolicyRepresentation.setRoles(Set.of(createClientRoleDef(orgaName + "." + roleName)));
        return rolePolicyRepresentation;
    }

    private Set<String> getParentPolicies(ClientRepresentation clientRepresentation, OrganizationalUnitEntity parent) {
        Set<String> policySet = new HashSet<>();
        while (parent != null) {
            String policyName = "member-of-" + parent.getName() + "." + CLIENT_USERS_ADMIN_ROLE_NAME;
            PolicyRepresentation policyRepresentation = getPolicyByName(clientRepresentation.getId(), policyName);
            policySet.add(policyRepresentation.getId());
            parent = parent.getParent();
        }
        return policySet;
    }

    private RoleRepresentation mapToRoleRepresentation(String name) {
        RoleRepresentation role = new RoleRepresentation();
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
                .map(r -> mapToRoleRepresentation(String.join(".",inputOrganizationalUnit.getName(), r)))
                .forEach(this::addRole);

        // Associate user admin roles with query-users and query-group roles
        String unitUserRoleName = String.join(".", inputOrganizationalUnit.getName(), UNIT_USERS_ADMIN_ROLE_NAME);
        getRealmResource().roles().get(unitUserRoleName).addComposites(List.of(queryUsersRole, queryGroupsRole));

        String clientUserRoleName = String.join(".", inputOrganizationalUnit.getName(), CLIENT_USERS_ADMIN_ROLE_NAME);
        getRealmResource().roles().get(clientUserRoleName).addComposites(List.of(queryUsersRole, queryGroupsRole));

        LOG.info("Successfully created roles for OrganizationalUnit '{}' and Keycloak group ID '{}'.",
                inputOrganizationalUnit.getName(), inputOrganizationalUnit.getKeycloakId());
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
        RolePolicyRepresentation unitRolePolicyRepresentation = createRolePolicyRepresentation(inputOrganizationalUnit.getName(), UNIT_USERS_ADMIN_ROLE_NAME);

        // Enable Permissions for *.client-users-creator role
        getRealmResource().roles().get(inputOrganizationalUnit.getName() + "." + CLIENT_USERS_ADMIN_ROLE_NAME).setPermissions(managementPermission);

        // Create Role Policy for *.client-users-creator role
        RolePolicyRepresentation clientRolePolicyRepresentation = createRolePolicyRepresentation(inputOrganizationalUnit.getName(), CLIENT_USERS_ADMIN_ROLE_NAME);

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



}
