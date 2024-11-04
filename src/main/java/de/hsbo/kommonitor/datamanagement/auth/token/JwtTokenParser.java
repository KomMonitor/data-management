package de.hsbo.kommonitor.datamanagement.auth.token;

import de.hsbo.kommonitor.datamanagement.auth.Group;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenParser extends TokenParser<JwtAuthenticationToken> {

    @Override
    public Set<String> getOwnedRoles(JwtAuthenticationToken principal) {
        List<String> roles;
        Object realmAccessClaim = principal.getTokenAttributes().get("realm_access");
        if (realmAccessClaim != null) {
            roles = ((Map<String, List<String>>)realmAccessClaim).get("roles");
        } else {
            roles = Collections.EMPTY_LIST;
        }
        return new HashSet<>(roles);
    }

    @Override
    public boolean hasRealmAdminRole(JwtAuthenticationToken principal, String adminRole) {
        return getOwnedRoles(principal).stream().anyMatch(r -> r.equals(adminRole));
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
        Object groupsClaim = principal.getTokenAttributes().get("groups");
        if(groupsClaim == null) {
            return Collections.emptySet();
        }
        List<String> groups = (List<String>) groupsClaim;
        return new HashSet<>(groups);
    }
}
