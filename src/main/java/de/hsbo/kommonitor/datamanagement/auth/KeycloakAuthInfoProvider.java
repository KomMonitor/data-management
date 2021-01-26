package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

/**
 * Provider that holds authorization information for a {@link KeycloakPrincipal}
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
@RequestScope
public class KeycloakAuthInfoProvider extends AuthInfoProvider<KeycloakPrincipal> {

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${kommonitor.roles.admin:administrator}")
    private String adminRole;

    public KeycloakAuthInfoProvider(KeycloakPrincipal principal, String clientId, String adminRole) {
        super(principal);
        this.clientId = clientId;
        this.adminRole = adminRole;
    }

    @Override
    public Set<String> getRealmRoles() {
        return getPrincipal().getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
    }

    @Override
    public Set<String> getClientRoles() {
        return getPrincipal().getKeycloakSecurityContext().getToken().getResourceAccess(clientId).getRoles();
    }

    @Override
    public boolean hasRealmRole(String role) {
        return getPrincipal().getKeycloakSecurityContext().getToken().getRealmAccess().isUserInRole(role);
    }

    @Override
    public boolean hasRealmAdminRole() {
        return getPrincipal().getKeycloakSecurityContext().getToken().getRealmAccess().isUserInRole(adminRole);
    }

    @Override
    public boolean hasClientRole(String role) {
        return getPrincipal().getKeycloakSecurityContext().getToken().getResourceAccess(clientId).isUserInRole(role);
    }

    @Override
    public boolean hasClientAdminRole() {
        return getPrincipal().getKeycloakSecurityContext().getToken().getResourceAccess(clientId).isUserInRole(adminRole);
    }
}
