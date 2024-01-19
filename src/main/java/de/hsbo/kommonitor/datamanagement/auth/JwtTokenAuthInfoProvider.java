package de.hsbo.kommonitor.datamanagement.auth;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JwtTokenAuthInfoProvider extends AuthInfoProvider<JwtAuthenticationToken> {

    public JwtTokenAuthInfoProvider(JwtAuthenticationToken principal,String adminRolePrefix, String publicRole) {
        super(principal, adminRolePrefix, publicRole);
    }

    @Override
    public Set<String> getOwnedRoles(JwtAuthenticationToken principal) {
        List<String> roles = ((Map<String, List<String>>)principal.getTokenAttributes().get("realm_access")).get("roles");
        return new HashSet<>(roles);
    }

    @Override
    public boolean hasRealmAdminRole(JwtAuthenticationToken principal) {
        return getOwnedRoles(getPrincipal()).stream().anyMatch(r -> r.equals(getAdminRolePrefix() + "-creator"));
    }

}
