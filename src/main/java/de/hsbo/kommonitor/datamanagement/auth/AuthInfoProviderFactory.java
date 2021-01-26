package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class AuthInfoProviderFactory {

    @Value("${keycloak.resource}")
    private String keycloakClientId;

    @Value("${kommonitor.roles.admin:administrator}")
    private String adminRole;

    public AuthInfoProvider createAuthInfoProvider(Principal principal) {
        if (principal instanceof KeycloakPrincipal) {
            return new KeycloakAuthInfoProvider((KeycloakPrincipal) principal, keycloakClientId, adminRole);
        } else {
            throw new IllegalStateException(String.format("Cannot create an AuthInfoProvider because the " +
                    "principal type %s is not supported.", principal.getClass()));
        }
    }

}
