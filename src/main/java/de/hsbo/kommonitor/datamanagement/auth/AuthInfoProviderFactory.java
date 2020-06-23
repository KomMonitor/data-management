package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.KeycloakPrincipal;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class AuthInfoProviderFactory {

    public AuthInfoProvider createAuthInfoProvider(Principal principal) {
        if (principal instanceof KeycloakPrincipal) {
            return new KeycloakAuthInfoProvider((KeycloakPrincipal) principal);
        } else {
            throw new IllegalStateException(String.format("Cannot create an AuthInfoProvider because the " +
                    "principal type %s is not supported.", principal.getClass()));
        }
    }

}
