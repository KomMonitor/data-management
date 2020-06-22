package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

/**
 * Provider that holds authorization information for a {@link KeycloakPrincipal}
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class KeycloakAuthInfoProvider extends AuthInfoProvider<KeycloakPrincipal> {

    @Value("${keycloak.resource}")
    private String clientId;

    public KeycloakAuthInfoProvider(KeycloakPrincipal principal) {
        super(principal);
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
        return getPrincipal().getKeycloakSecurityContext().getToken().getRealmAccess().getRoles().contains(role);
    }

    @Override
    public boolean hasClientRole(String role) {
        return getPrincipal().getKeycloakSecurityContext().getToken().getResourceAccess(clientId).getRoles().contains(role);
    }
}
