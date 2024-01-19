package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.KeycloakPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

/**
 * Provider that holds authorization information for a {@link KeycloakPrincipal}
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@Component
@RequestScope
public class KeycloakAuthInfoProvider extends AuthInfoProvider<KeycloakPrincipal> {

    public KeycloakAuthInfoProvider(KeycloakPrincipal principal, String adminRolePrefix, String publicRole) {
        super(principal, adminRolePrefix, publicRole);
    }

    @Override
    public Set<String> getOwnedRoles(KeycloakPrincipal principal) {
        return principal
                .getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .getRoles();
    }

    @Override
    public boolean hasRealmAdminRole(KeycloakPrincipal principal) {
        return principal.getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .isUserInRole(getAdminRolePrefix() + "-creator");
    }

}
