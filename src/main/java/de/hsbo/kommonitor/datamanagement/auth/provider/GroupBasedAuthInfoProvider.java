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

public class GroupBasedAuthInfoProvider implements AuthInfoProvider {

    private static final String ADMIN_ROLE_NAME = "kommonitor-creator";
    private final Set<Group> groups;

    private Principal principal;

    private TokenParser<Principal> tokenParser;

    public GroupBasedAuthInfoProvider() {
        groups = null;
    }

    public GroupBasedAuthInfoProvider(Principal principal, TokenParser<Principal> tokenParser) {
        this.principal = principal;
        this.tokenParser = tokenParser;

        groups = tokenParser.getGroupMemberships(principal);
    }

    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean checkPermissions(RestrictedEntity entity, PermissionLevelType neededLevel) {
        Set<PermissionEntity> allowedRoleEntities = entity.getPermissions();

        // Entity is public
        if (neededLevel.equals(PermissionLevelType.VIEWER) && entity.isPublic()) {
            return true;
        }

        // User is global administrator
        if (tokenParser.hasRealmAdminRole(getPrincipal(), ADMIN_ROLE_NAME)) {
            return true;
        }


        // Disallow access if user does not belong to a group as all permissions are tied to groups
        if (groups == null || groups.isEmpty()) {
            return false;
        }

        // User is in owning group and has all permissions
        //if (groups.stream().anyMatch(group -> entity.getOwner().getOrganizationalUnitId().equals(group.getIdentifier()))) {
        //    return true;
        // }

        // Parse permissions
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
        String owningId = entity.getOwner().getOrganizationalUnitId();

        // User is in owning group and has all permissions
        if (groups.stream().anyMatch(group -> owningId.equals(group.getIdentifier()))) {
            return Arrays.asList(
                    PermissionLevelType.CREATOR,
                    PermissionLevelType.EDITOR,
                    PermissionLevelType.VIEWER);
        }

        // Check permissions for a matching permission
        // 1. for all groups of the user try to permission defined for the group
        // 2. Aggregate and keep the highest level
        
        //return entity.getPermissions()
        //        .stream()
        //        .filter(pe -> pe.getOrganizationalUnit().getOrganizationalUnitId())
        //        .map(PermissionEntity::getPermissionLevel).collect(Collectors.toList());
        return Collections.emptyList();
    }

    @Override
    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel) {
        // TODO: Implement more fine-grained evaluation
        if (neededLevel.equals("viewer")) {
            return true;
        } else {
            return tokenParser.hasRealmAdminRole(principal, ADMIN_ROLE_NAME);
        }
    }
}
