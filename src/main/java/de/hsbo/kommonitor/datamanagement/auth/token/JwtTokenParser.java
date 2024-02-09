package de.hsbo.kommonitor.datamanagement.auth.token;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JwtTokenParser extends TokenParser<JwtAuthenticationToken> {

    @Override
    public Set<String> getOwnedRoles(JwtAuthenticationToken principal) {
        List<String> roles = ((Map<String, List<String>>)principal.getTokenAttributes().get("realm_access")).get("roles");
        return new HashSet<>(roles);
    }

    @Override
    public boolean hasRealmAdminRole(JwtAuthenticationToken principal, String adminRolePrefix) {
        return getOwnedRoles(principal).stream().anyMatch(r -> r.equals(adminRolePrefix + "-creator"));
    }
}
