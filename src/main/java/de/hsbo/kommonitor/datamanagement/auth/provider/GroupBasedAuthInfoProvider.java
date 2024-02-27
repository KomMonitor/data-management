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

    private SortedSet<PermissionLevelType> permissionSet;

    public GroupBasedAuthInfoProvider() {
        groups = null;
    }

    public GroupBasedAuthInfoProvider(Principal principal, TokenParser<Principal> tokenParser) {
        this.principal = principal;
        this.tokenParser = tokenParser;

        groups = tokenParser.getGroupMemberships(principal);
        permissionSet = new TreeSet();
        permissionSet.add(PermissionLevelType.CREATOR);
        permissionSet.add(PermissionLevelType.EDITOR);
        permissionSet.add(PermissionLevelType.VIEWER);
    }

    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean checkPermissions(RestrictedEntity entity, PermissionLevelType neededLevel) {
        Set<PermissionEntity> permissionEntities = entity.getPermissions();

        // Entity is public
        if (neededLevel.equals(PermissionLevelType.VIEWER) && (entity.isPublic() != null && entity.isPublic())) {
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

        if (entity.getOwner() != null) {
            // User is in owning group and has all permissions
            if (groups.stream().anyMatch(group -> entity.getOwner().getName().equals(group.getName()))) {
                return true;
            }
            // User has resource administrator permissions for the owning group
            if (hasResourceAdministrationPermission(entity)) {
                return true;
            }
        }

        // For non-owning groups, check resource permissions
        Set<Pair<OrganizationalUnitEntity, PermissionLevelType>> permissions = permissionEntities.stream()
                .map(e -> Pair.of(e.getOrganizationalUnit(), e.getPermissionLevel()))
                .collect(Collectors.toSet());

        return permissions.stream()
                .filter(r -> r.getSecond().compareTo(neededLevel) <= 0)
                .anyMatch(r -> groups
                        .stream()
                        .anyMatch(g -> g.getName().equals(r.getFirst().getName()))
                );

    }

    @Override
    public List<PermissionLevelType> getPermissions(RestrictedEntity entity) {
        OrganizationalUnitEntity ownerEntity = entity.getOwner();
        List<PermissionLevelType> fullPermissions = Arrays.asList(
                PermissionLevelType.CREATOR,
                PermissionLevelType.EDITOR,
                PermissionLevelType.VIEWER
        );

        // Global administrators hav full permissions
        if (tokenParser.hasRealmAdminRole(getPrincipal(), ADMIN_ROLE_NAME)) {
            return fullPermissions;
        }

        // Since all permissions are tied to groups, users without a group can't have permissions on a resource
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }

        if (ownerEntity != null) {
            String ownerName = entity.getOwner().getName();

            // Owning groups have full permission
            if (groups.stream().anyMatch(group -> ownerName.equals(group.getName()))) {
                return fullPermissions;
            }

            // Resource administrator permission for the owning group also includes full permission
            if (hasResourceAdministrationPermission(entity)) {
                return fullPermissions;
            }
        }

        // For non-owning groups, check resource permissions
        Set<PermissionEntity> permissionEntities = entity.getPermissions();
        Set<Pair<OrganizationalUnitEntity, PermissionLevelType>> permissions = permissionEntities.stream()
                .map(e -> Pair.of(e.getOrganizationalUnit(), e.getPermissionLevel()))
                .collect(Collectors.toSet());

        // Check permissions for a matching permission
        // 1. for all groups of the user try to permission defined for the group
        // 2. Aggregate and keep the highest level
        PermissionLevelType max_permission = permissions.stream()
                .filter(r -> groups
                        .stream()
                        .anyMatch(g -> g.getName().equals(r.getFirst().getName()))
                )
                .map(Pair::getSecond)
                .min(Comparator.comparing(PermissionLevelType::ordinal))
                .orElseThrow(NoSuchElementException::new)
        ;

        return new ArrayList<>(permissionSet.tailSet(max_permission));
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

    public boolean checkOrganizationalUnitPermissions(OrganizationalUnitEntity entity) {

        // User is global administrator
        if (tokenParser.hasRealmAdminRole(getPrincipal(), ADMIN_ROLE_NAME)) {
            return true;
        }

        return hasUsersAdministrationPermission(entity);
    }

    public List<Group> getResourceAdminGroups() {
        //TODO check for group hierarchies
        Set<String> ownedRoles = tokenParser.getOwnedRoles(getPrincipal());
        return ownedRoles.stream()
                .filter(r -> roleExtractorRegex.split(r, 2).length == 2)
                .map(r -> {
                    String[] split = roleExtractorRegex.split(r, 2);
                    return  Pair.of(split[0], split[1]);
                })
                .filter(r -> hasAdminPermissionForResourceType(r.getSecond(), PermissionResourceType.RESOURCES))
                .map(r -> new Group(r.getFirst()))
                .collect(Collectors.toList());
    }

    private boolean hasResourceAdministrationPermission(RestrictedEntity entity) {
        Set<String> ownedRoles = tokenParser.getOwnedRoles(getPrincipal());
        return ownedRoles.stream()
                .filter(r -> roleExtractorRegex.split(r, 2).length == 2)
                .map(r -> {
                    String[] split = roleExtractorRegex.split(r, 2);
                    return Pair.of(split[0], split[1]);
                })
                .anyMatch(r -> hasUnitResourceAdminPermissionForOwningGroup(r.getFirst(), r.getSecond(), entity) ||
                        hasClientResourceAdminPermissionForOwningGroup(r.getFirst(), r.getSecond(), entity));
    }

    private boolean hasAdminPermissionForResourceType(String adminRole, PermissionResourceType resourceType) {
        return switch (resourceType) {
            case RESOURCES ->
                    (adminRole.equals(CLIENT_RESOURCES_ADMIN_ROLE_NAME) || adminRole.equals(UNIT_RESOURCES_ADMIN_ROLE_NAME));
            case THEMES ->
                    (adminRole.equals(CLIENT_THEMES_ADMIN_ROLE_NAME) || adminRole.equals(UNIT_THEMES_ADMIN_ROLE_NAME));
            case USERS ->
                    (adminRole.equals(CLIENT_USERS_ADMIN_ROLE_NAME) || adminRole.equals(UNIT_USERS_ADMIN_ROLE_NAME));
        };
    }

    private boolean hasUnitResourceAdminPermissionForOwningGroup (String kcGroup, String kcRole, RestrictedEntity entity) {
        return kcGroup.equals(entity.getOwner().getName())
                && hasAdminPermissionForResourceType(kcRole, PermissionResourceType.RESOURCES);
    }

    /**
     * Check if owning group is a subgroup of a group with *.client-resource-creator permission
     *
     * @param kcGroup Keycloak group
     * @param kcRole Keycloak role for group
     * @param entity Resource to check
     * @return true if the Keycloak group has admin permission for resources of the owning group
     */
    private boolean hasClientResourceAdminPermissionForOwningGroup(String kcGroup, String kcRole, RestrictedEntity entity) {
        return kcRole.equals(CLIENT_RESOURCES_ADMIN_ROLE_NAME) && roleGroupIsParent(kcGroup, entity.getOwner());
    }

    private boolean hasUsersAdministrationPermission(OrganizationalUnitEntity entity) {
        Set<String> ownedRoles = tokenParser.getOwnedRoles(getPrincipal());
        return ownedRoles.stream()
                .filter(r -> roleExtractorRegex.split(r, 2).length == 2)
                .map(r -> {
                    String[] split = roleExtractorRegex.split(r, 2);
                    return Pair.of(split[0], split[1]);
                })
                .anyMatch(r -> hasUnitUsersAdminPermissionForOwningGroup(r.getFirst(), r.getSecond(), entity) ||
                        hasClientUsersAdminPermissionForOwningGroup(r.getFirst(), r.getSecond(), entity));
    }

    private boolean hasUnitUsersAdminPermissionForOwningGroup(String kcGroup, String kcRole, OrganizationalUnitEntity entity) {
        return kcGroup.equals(entity.getName())
                && hasAdminPermissionForResourceType(kcRole, PermissionResourceType.USERS);
    }

    private boolean hasClientUsersAdminPermissionForOwningGroup(String kcGroup, String kcRole, OrganizationalUnitEntity entity) {
        return kcRole.equals(CLIENT_USERS_ADMIN_ROLE_NAME) && roleGroupIsParent(kcGroup, entity);
    }

    /**
     * Checks if a Keycloak group is parent of an OrganizationalUnit
     *
     * @param kcGroup Keycloak Group
     * @param entity OrganizationalUnit
     * @return true if the OrganizationalUnit has a parent, which has the same name as the Keycloak group
     */
    private boolean roleGroupIsParent(String kcGroup, OrganizationalUnitEntity entity) {
        OrganizationalUnitEntity currentOrga = entity;
        while (currentOrga != null) {
            if (currentOrga.getName().equals(kcGroup)) {
                return true;
            }
            currentOrga = currentOrga.getParent();
        }
        return false;
    }




}
