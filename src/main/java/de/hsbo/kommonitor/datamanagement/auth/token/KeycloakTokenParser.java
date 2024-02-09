package de.hsbo.kommonitor.datamanagement.auth.token;

import org.keycloak.KeycloakPrincipal;

import java.util.Set;

public class KeycloakTokenParser extends TokenParser<KeycloakPrincipal> {
    @Override
    public Set<String> getOwnedRoles(KeycloakPrincipal principal) {
        return principal
                .getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .getRoles();
    }

    @Override
    public boolean hasRealmAdminRole(KeycloakPrincipal principal, String adminRolePrefix) {
        return principal.getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .isUserInRole(adminRolePrefix + "-creator");
    }
}
