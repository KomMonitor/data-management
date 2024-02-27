package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.KeycloakException;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
public class KeycloakAdminService {

    private static final String REALM_NAME = "kommonitor";

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

    public String handleResponse(Response response) throws KeycloakException {
        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            String [] parts = response.getLocation().getPath().split("/");
            return parts[parts.length - 1];
        } else {
            throw new KeycloakException(response.getStatus(), "Error while requesting Keycloak Admin CLI.");
        }
    }



}
