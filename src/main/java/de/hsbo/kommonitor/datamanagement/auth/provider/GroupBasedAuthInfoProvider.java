package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.auth.Group;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
import org.springframework.data.util.Pair;

import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GroupBasedAuthInfoProvider implements AuthInfoProvider {

    private static final String ADMIN_ROLE_NAME = "kommonitor-creator";
    private static final String CLIENT_RESOURCES_ADMIN_ROLE_NAME = "client-resources-creator";
    private static final String UNIT_RESOURCES_ADMIN_ROLE_NAME = "unit-resources-creator";
    private static final String CLIENT_THEMES_ADMIN_ROLE_NAME = "client-themes-creator";
    private static final String UNIT_THEMES_ADMIN_ROLE_NAME = "unit-themes-creator";
    private static final String CLIENT_USERS_ADMIN_ROLE_NAME = "client-users-creator";
    private static final String UNIT_USERS_ADMIN_ROLE_NAME = "unit-users-creator";

//    private static final Pattern roleExtractorRegex = Pattern.compile(".(?=(client-resources-creator)|(unit-resources-creator)|(client-themes-creator)|(unit-themes-creator)|(client-users-creator)|(unit-users-creator)$)");
    private static final Pattern roleExtractorRegex = Pattern.compile(
            ".(?=(" + CLIENT_RESOURCES_ADMIN_ROLE_NAME + ")" +
                    "|(" + UNIT_RESOURCES_ADMIN_ROLE_NAME + ")" +
                    "|(" + CLIENT_THEMES_ADMIN_ROLE_NAME + ")" +
                    "|(" + UNIT_THEMES_ADMIN_ROLE_NAME + ")" +
                    "|(" + CLIENT_USERS_ADMIN_ROLE_NAME + ")" +
                    "|(" + UNIT_USERS_ADMIN_ROLE_NAME + ")$)");

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
        if (groups.stream().anyMatch(group -> entity.getOwner().getName().equals(group.getName()))) {
            return true;
         }

        // User has resource administrator permissions for the owning group
        Set<String> ownedRoles = tokenParser.getOwnedRoles(getPrincipal());
        if (ownedRoles.stream()
                .filter(r -> roleExtractorRegex.split(r, 2).length == 2)
                .map(r -> {
                    String[] split = roleExtractorRegex.split(r, 2);
                    return Pair.of(split[0], split[1]);
                })
                .anyMatch(r -> r.getFirst().equals(entity.getOwner().getName()) && hasAdminPermissionForResourceType(r.getSecond(), PermissionResourceType.RESOURCES))) {
            return true;
        }

        // For non-owning groups, check if resource permissions
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

        if (groups.stream().anyMatch(group -> owningId.equals(group.getName()))) {
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
        if (neededLevel.getValue().equals("viewer")) {
            return true;
        }

        return tokenParser.hasRealmAdminRole(principal, ADMIN_ROLE_NAME);
    }

    @Override
    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel, PermissionResourceType resourceType) {
        if (neededLevel.getValue().equals("viewer")) {
            return true;
        } else if (tokenParser.hasRealmAdminRole(principal, ADMIN_ROLE_NAME)) {
            return true;
        } else {
            Set<String> ownedRoles = tokenParser.getOwnedRoles(getPrincipal());

            return ownedRoles.stream()
                    .filter(kcRole -> roleExtractorRegex.split(kcRole, 2).length == 2)
                    .map(r -> {
                        String[] split = roleExtractorRegex.split(r, 2);
                        return split[1];

                    })
                    .anyMatch(r -> hasAdminPermissionForResourceType(r, resourceType));

        }
    }

    private boolean hasAdminPermissionForResourceType(String adminRole, PermissionResourceType resourceType) {
        return switch (resourceType) {
            case RESOURCES -> (adminRole.equals(CLIENT_RESOURCES_ADMIN_ROLE_NAME) || adminRole.equals(UNIT_RESOURCES_ADMIN_ROLE_NAME));
            case THEMES -> (adminRole.equals(CLIENT_THEMES_ADMIN_ROLE_NAME) || adminRole.equals(UNIT_THEMES_ADMIN_ROLE_NAME));
            case USERS -> (adminRole.equals(CLIENT_USERS_ADMIN_ROLE_NAME) || adminRole.equals(UNIT_USERS_ADMIN_ROLE_NAME));
        };
    }
}
