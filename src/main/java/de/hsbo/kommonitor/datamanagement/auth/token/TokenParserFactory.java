package de.hsbo.kommonitor.datamanagement.auth.token;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class TokenParserFactory {

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String adminRolePrefix;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;


    public TokenParser<?> createTokenParser() {
        return createTokenParser(SecurityContextHolder.getContext().getAuthentication());
    }

    public TokenParser<?> createTokenParser(Principal principal) {
        if (principal instanceof KeycloakAuthenticationToken && Principal.class.isAssignableFrom(((KeycloakAuthenticationToken) principal).getPrincipal().getClass())) { //get real principal instance
            KeycloakAuthenticationToken token = ((KeycloakAuthenticationToken) principal);
            principal = (Principal) token.getPrincipal();
        }

        if (principal instanceof KeycloakPrincipal) {
            return new KeycloakTokenParser();
        } else if (principal instanceof JwtAuthenticationToken) {
            return new JwtTokenParser();
        } else {
            throw new IllegalStateException(String.format("Cannot create an AuthInfoProvider because the "
                    + "principal type %s is not supported.", principal.getClass()));
        }
    }

}
