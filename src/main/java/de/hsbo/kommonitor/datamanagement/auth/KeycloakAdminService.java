package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Service
public class KeycloakAdminService {

    private static final String REALM_NAME = "kommonitor";

    private static final String CLIENT_RESOURCES_ADMIN_ROLE_NAME = "client-resources-creator";
    private static final String UNIT_RESOURCES_ADMIN_ROLE_NAME = "unit-resources-creator";
    private static final String CLIENT_THEMES_ADMIN_ROLE_NAME = "client-themes-creator";
    private static final String UNIT_THEMES_ADMIN_ROLE_NAME = "unit-themes-creator";
    private static final String CLIENT_USERS_ADMIN_ROLE_NAME = "client-users-creator";
    private static final String UNIT_USERS_ADMIN_ROLE_NAME = "unit-users-creator";

    private static final List ADMIN_ROLES = Arrays.asList(
            CLIENT_RESOURCES_ADMIN_ROLE_NAME,
            UNIT_RESOURCES_ADMIN_ROLE_NAME,
            CLIENT_THEMES_ADMIN_ROLE_NAME,
            UNIT_THEMES_ADMIN_ROLE_NAME,
            CLIENT_USERS_ADMIN_ROLE_NAME,
            UNIT_USERS_ADMIN_ROLE_NAME
            );

    @Autowired
    private Keycloak keycloak;

    public String addGroup(OrganizationalUnitInputType inputOrganizationalUnit) throws KeycloakException {
        GroupsResource groupsResource = keycloak.realm(REALM_NAME).groups();
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());

        Response response = groupsResource.add(group);

        return handleResponse(response);
    }

    public String addSubGroup(OrganizationalUnitInputType inputOrganizationalUnit, OrganizationalUnitEntity parent) throws KeycloakException {
        GroupsResource groupsResource = keycloak.realm(REALM_NAME).groups();
        GroupRepresentation group = new GroupRepresentation();
        group.setName(inputOrganizationalUnit.getName());

        GroupResource parentGroup = groupsResource.group(parent.getKeycloakId().toString());
        Response response = parentGroup.subGroup(group);

        return handleResponse(response);
    }

    public void createRolesForGroup(OrganizationalUnitInputType inputOrganizationalUnit) {
        RolesResource rolesResource = keycloak.realm(REALM_NAME).roles();


    }

    public String handleResponse(Response response) throws KeycloakException {
        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            String [] parts = response.getLocation().getPath().split("/");
            return parts[parts.length - 1];
        } else {
            throw new KeycloakException(response.getStatus(), "Error while requesting Keycloak Admin CLI.");
        }
    }



}
