package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.auth.Group;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.springframework.data.util.Pair;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

public class GroupBasedAuthInfoProvider implements AuthInfoProvider{

    private static final String ADMIN_ROLE_NAME = "kommonitor-creator";

    private Principal principal;

    private TokenParser tokenParser;

    public GroupBasedAuthInfoProvider() {
    }

    public GroupBasedAuthInfoProvider(Principal principal, TokenParser<?> tokenParser) {
        this.principal = principal;
        this.tokenParser = tokenParser;
    }

    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean checkPermissions(RestrictedEntity entity, PermissionLevelType neededLevel) {
        Set<PermissionEntity> allowedRoleEntities = entity.getPermissions();

        // User is global administrator
        if (tokenParser.hasRealmAdminRole(getPrincipal(), ADMIN_ROLE_NAME)) {
            return true;
        }

        // disallow access if now organizational user has access rights
        if (allowedRoleEntities == null || allowedRoleEntities.isEmpty()) {
            return false;
        }

        Set<Group> groups = tokenParser.getGroupMemberships(getPrincipal());

        // also disallow access if now user does not belong to a group
        if(groups == null || groups.isEmpty()) {
            return false;
        }

        Set<Pair<OrganizationalUnitEntity, PermissionLevelType>> allowedRoles = allowedRoleEntities.stream()
                .map(e -> Pair.of(e.getOrganizationalUnit(), e.getPermissionLevel()))
                .collect(Collectors.toSet());

        return allowedRoles.stream()
                .filter(r -> r.getSecond().compareTo(neededLevel) <= 0)
                .anyMatch(r -> groups
                        .stream()
                        .anyMatch(g -> g.getName().equals(r.getFirst().getName()))
                );

    }

    @Override
    public List<PermissionLevelType> getPermissions(RestrictedEntity entity) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel) {
        // TODO: Implement more fine-grained evaluation
        if (neededLevel.equals("viewer")) {
            return true;
        }
        else {
            return tokenParser.hasRealmAdminRole(principal, ADMIN_ROLE_NAME);
        }
    }
}
