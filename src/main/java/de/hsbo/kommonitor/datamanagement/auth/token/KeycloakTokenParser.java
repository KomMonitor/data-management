package de.hsbo.kommonitor.datamanagement.auth.token;

import de.hsbo.kommonitor.datamanagement.auth.Group;
import org.keycloak.KeycloakPrincipal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public boolean hasRealmAdminRole(KeycloakPrincipal principal, String adminRole) {
        return principal.getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .isUserInRole(adminRole);
    }

    @Override
    public Set<String> getGroupsClaim(KeycloakPrincipal principal) {
        List<String> groups = (List<String>) principal
                .getKeycloakSecurityContext()
                .getToken()
                .getOtherClaims().get("groups");
        return new HashSet<>(groups);
    }

    @Override
    public Set<Group> getGroupMemberships(KeycloakPrincipal principal) {
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
    public String getUserId(KeycloakPrincipal principal) {
        return principal.getKeycloakSecurityContext()
                .getToken()
                .getSubject();
    }
}
