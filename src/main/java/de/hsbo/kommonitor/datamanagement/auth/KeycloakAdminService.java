package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.AccessControlController;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.keycloak.representations.idm.authorization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class KeycloakAdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessControlController.class);

    private static final String REALM_NAME = "kommonitor";

    private static final String CLIENT_RESOURCES_ADMIN_ROLE_NAME = "client-resources-creator";
    private static final String UNIT_RESOURCES_ADMIN_ROLE_NAME = "unit-resources-creator";
    private static final String CLIENT_THEMES_ADMIN_ROLE_NAME = "client-themes-creator";
    private static final String UNIT_THEMES_ADMIN_ROLE_NAME = "unit-themes-creator";
    private static final String CLIENT_USERS_ADMIN_ROLE_NAME = "client-users-creator";
    private static final String UNIT_USERS_ADMIN_ROLE_NAME = "unit-users-creator";

    private static final String REALM_MANAGEMENT_CLIENT_NAME = "realm-management";

    private static final List ADMIN_ROLES = Arrays.asList(
            CLIENT_RESOURCES_ADMIN_ROLE_NAME,
            UNIT_RESOURCES_ADMIN_ROLE_NAME,
            CLIENT_THEMES_ADMIN_ROLE_NAME,
            UNIT_THEMES_ADMIN_ROLE_NAME,
            CLIENT_USERS_ADMIN_ROLE_NAME,
            UNIT_USERS_ADMIN_ROLE_NAME
            );

    private Keycloak keycloak;

    @Autowired
    public KeycloakAdminService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String addGroup(OrganizationalUnitInputType inputOrganizationalUnit) throws KeycloakException {
        GroupsResource groupsResource = keycloak.realm(REALM_NAME).groups();
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());

        Response response = groupsResource.add(group);

        return handleGroupCreationResponse(response);
    }

    public String addSubGroup(OrganizationalUnitInputType inputOrganizationalUnit, OrganizationalUnitEntity parent) throws KeycloakException {
        GroupsResource groupsResource = keycloak.realm(REALM_NAME).groups();
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());

        GroupResource parentGroup = groupsResource.group(parent.getKeycloakId().toString());
        Response response = parentGroup.subGroup(group);

        return handleGroupCreationResponse(response);
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
        // "Stadt Viersen.client-users-creator"
        List<RoleRepresentation> roleList = getRealmResource().roles().list(name, true);
        if (roleList.size() == 0) {
            throw new KeycloakException(String.format("No role exists with name %s.", name));
        }
        if (roleList.size() > 1) {
            LOG.warn("More than one role with name {} exists. Only first role will be returned.", name);
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

    public void createRolePolicies(OrganizationalUnitInputType inputOrganizationalUnit, OrganizationalUnitEntity parent) throws KeycloakException {
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
        return keycloak.realm(REALM_NAME);
    }



}
