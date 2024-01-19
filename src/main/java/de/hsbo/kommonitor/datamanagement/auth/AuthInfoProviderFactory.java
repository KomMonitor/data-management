package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class AuthInfoProviderFactory {

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String adminRolePrefix;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;


    public AuthInfoProvider createAuthInfoProvider() {
        return createAuthInfoProvider(SecurityContextHolder.getContext().getAuthentication());
    }

    public AuthInfoProvider createAuthInfoProvider(Principal principal) {
        if (principal instanceof KeycloakAuthenticationToken && Principal.class.isAssignableFrom(((KeycloakAuthenticationToken) principal).getPrincipal().getClass())) { //get real principal instance
            KeycloakAuthenticationToken token = ((KeycloakAuthenticationToken) principal);
            principal = (Principal) token.getPrincipal();
        }

        if (principal instanceof KeycloakPrincipal) {
            return new KeycloakAuthInfoProvider((KeycloakPrincipal) principal, adminRolePrefix, publicRole);
        } else if (principal instanceof JwtAuthenticationToken) {
            return new JwtTokenAuthInfoProvider((JwtAuthenticationToken) principal, adminRolePrefix, publicRole);
        } else {
            throw new IllegalStateException(String.format("Cannot create an AuthInfoProvider because the "
                    + "principal type %s is not supported.", principal.getClass()));
        }
    }

}
