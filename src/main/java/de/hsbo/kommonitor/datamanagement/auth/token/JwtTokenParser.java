package de.hsbo.kommonitor.datamanagement.auth.token;

import de.hsbo.kommonitor.datamanagement.auth.Group;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenParser extends TokenParser<JwtAuthenticationToken> {

    @Override
    public Set<String> getOwnedRoles(JwtAuthenticationToken principal) {
        List<String> roles = ((Map<String, List<String>>) principal.getTokenAttributes().get("realm_access")).get("roles");
        return new HashSet<>(roles);
    }

    @Override
    public boolean hasRealmAdminRole(JwtAuthenticationToken principal, String adminRolePrefix) {
        return getOwnedRoles(principal).stream().anyMatch(r -> r.equals(adminRolePrefix + "-creator"));
    }

    @Override
    public Set<Group> getGroupMemberships(JwtAuthenticationToken principal) {
        Set<String> groups = getGroupsClaim(principal);
        return groups.stream().map(g -> {
            List<String> subGroups = Arrays.asList(g.split("/"));
            subGroups = subGroups.subList(1, subGroups.size());
            Group group = new Group(subGroups.get(subGroups.size()-1));
            Group currentGroup = group;
            for (int i = subGroups.size() - 2; i >= 0; i--) {
                Group parent = new Group(subGroups.get(i));
                currentGroup.setParentGroup(parent);

                currentGroup = parent;
            }
            return group;
        }).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getGroupsClaim(JwtAuthenticationToken principal) {
        List<String> groups = (List<String>) principal.getTokenAttributes().get("groups");
        return new HashSet<>(groups);
    }
}
