package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.model.AdminRoleType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.hsbo.kommonitor.datamanagement.model.AdminRoleType.CLIENT_USERS_CREATOR;

/**
 * Interface that provides authentication and authorization information
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class RoleBasedAuthInfoProvider implements AuthInfoProvider {

    private Principal principal;

    private TokenParser tokenParser;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String adminRolePrefix;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;

    private SortedSet<PermissionLevelType> permissionSet;

    private static final Pattern roleExtractorRegex = Pattern.compile("-(?=(creator)|(publisher)|(editor)|(viewer)$)");

    public RoleBasedAuthInfoProvider() {
    }

    public RoleBasedAuthInfoProvider(Principal principal, TokenParser<?> tokenParser, String adminRolePrefix, String publicRole) {
        this.principal = principal;
        this.tokenParser = tokenParser;
        this.adminRolePrefix = adminRolePrefix;
        this.publicRole = publicRole;
        permissionSet = new TreeSet<>();
        permissionSet.add(PermissionLevelType.CREATOR);
        permissionSet.add(PermissionLevelType.EDITOR);
        permissionSet.add(PermissionLevelType.VIEWER);
    }

    public Principal getPrincipal() {
        return principal;
    }

    protected Pattern getRoleExtractorRegex() {
        return roleExtractorRegex;
    }

    protected String getAdminRolePrefix() {
        return adminRolePrefix;
    }

    @Override
    public boolean hasGlobalAdminPermissions() {
        return hasRealmAdminRole(getPrincipal());
    }

    /**
     * checks whether the current principal allows access to an entity with
     * restricted access
     *
     * @param entity entity that has access rights defined by a role
     * @return true if access is allowed
     */
    public boolean checkPermissions(final RestrictedEntity entity, final PermissionLevelType neededLevel) {
        Set<PermissionEntity> allowedRoleEntities = entity.getPermissions();

        // User is global administrator
        if (hasRealmAdminRole(getPrincipal())) {
            return true;
        }

        // disallow access by default
        if (allowedRoleEntities == null || allowedRoleEntities.isEmpty()) {
            return false;
        }

        Set<String> ownedRoles = getOwnedRoles(getPrincipal());

        Set<Pair<OrganizationalUnitEntity, PermissionLevelType>> allowedRoles = allowedRoleEntities.stream()
                .map(e -> Pair.of(e.getOrganizationalUnit(), e.getPermissionLevel()))
                .collect(Collectors.toSet());

        return ownedRoles.stream()
                // Split into OrganizationalUnit and PermissionLevel
                .filter(kcRole -> roleExtractorRegex.split(kcRole, 2).length == 2)
                .map(kcRole -> {
                    String[] split = roleExtractorRegex.split(kcRole, 2);
                    return Pair.of(split[0], PermissionLevelType.fromValue(split[1]));
                })
                // we do not look at roles that do not give the required level
                .filter(r -> r.getSecond().compareTo(neededLevel) <= 0)
                // check the leftover roles for a match
                .anyMatch(r -> allowedRoles
                        .stream()
                        .anyMatch(ar -> (ar.getFirst().getName().equals(r.getFirst())
                                && ar.getSecond().compareTo(neededLevel) <= 0)
                                || (ar.getFirst().getName().equals(publicRole)
                                && ar.getSecond().compareTo(neededLevel) <= 0)));
    }

    @Override
    public boolean checkOrganizationalUnitPermissions(OrganizationalUnitEntity entity) {
        return hasRealmAdminRole(getPrincipal());
    }

    public List<PermissionLevelType> getPermissions(RestrictedEntity entity) {
        // User is global administrator
        if (hasRealmAdminRole(getPrincipal())) {
            return Arrays.asList(
                    PermissionLevelType.CREATOR,
                    PermissionLevelType.EDITOR,
                    PermissionLevelType.VIEWER);
        }

        Set<PermissionEntity> allowedRoleEntities = entity.getPermissions();

        if (allowedRoleEntities == null || allowedRoleEntities.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> ownedRoles = getOwnedRoles(getPrincipal());

        Set<Pair<OrganizationalUnitEntity, PermissionLevelType>> allowedRoles = allowedRoleEntities.stream()
                .map(e -> Pair.of(e.getOrganizationalUnit(), e.getPermissionLevel()))
                .collect(Collectors.toSet());

        PermissionLevelType max_permission = ownedRoles.stream()
                // Split into OrganizationalUnit and PermissionLevel
                .filter(kcRole -> roleExtractorRegex.split(kcRole, 2).length == 2)
                .map(kcRole -> {
                    String[] split = roleExtractorRegex.split(kcRole, 2);
                    return Pair.of(split[0], PermissionLevelType.fromValue(split[1]));
                })
                // check the leftover roles for a match
                .filter(r -> allowedRoles
                        .stream()
                        .anyMatch(ar -> ((ar.getFirst().getName().equals(r.getFirst()))
                                && ar.getSecond().compareTo(r.getSecond()) <= 0)
                                || (ar.getFirst().getName().equals(publicRole)
                                && ar.getSecond().compareTo(r.getSecond()) <= 0)))
                .map(Pair::getSecond)
                //Permission enum ist sorted descending: highest permission -> 0, lowest permission -> 4
                .min(Comparator.comparing(PermissionLevelType::ordinal))
                .orElseThrow(NoSuchElementException::new);
        return new ArrayList(permissionSet.tailSet(max_permission));
    }

    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel) {
        // User is global administrator
        if (hasRealmAdminRole(getPrincipal())) {
            return true;
        }

        Set<String> ownedRoles = getOwnedRoles(getPrincipal());

        return ownedRoles.stream()
                // Split into OrganizationalUnit and PermissionLevel
                .filter(kcRole -> roleExtractorRegex.split(kcRole, 2).length == 2)
                .map(kcRole -> {
                    String[] split = roleExtractorRegex.split(kcRole, 2);
                    return Pair.of(split[0], PermissionLevelType.fromValue(split[1]));
                })
                // check if role with min. permission level is present for user
                .anyMatch(r -> r.getSecond().compareTo(neededLevel) <= 0);
    }

    @Override
    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel, PermissionResourceType permissionResourceType) {
        return false;
    }

    @Override
    public Set<String> getGroupNames() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean checkOrganizationalUnitCreationPermissions(OrganizationalUnitEntity parent) {
        return hasRealmAdminRole(getPrincipal());
    }

    @Override
    public List<AdminRoleType> getOrganizationalUnitCreationPermissions(OrganizationalUnitEntity entity) {
        if (hasRealmAdminRole(getPrincipal())) {
            return Collections.singletonList(CLIENT_USERS_CREATOR);
        }
        return Collections.emptyList();
    }

    public Set<String> getOwnedRoles(Principal principal) {
        return tokenParser.getOwnedRoles(principal);
    }

    public boolean hasRealmAdminRole(Principal principal) {
        return tokenParser.hasRealmAdminRole(principal, adminRolePrefix);
    }

}
