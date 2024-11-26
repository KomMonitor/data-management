package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesEntity;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Interface that provides authentication and authorization information
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public abstract class AuthInfoProvider<T extends Principal> {

    private Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
        .getActualTypeArguments()[0];

    private T principal;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String adminRolePrefix;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;

    private SortedSet permissionSet;

    private static final Pattern roleExtractorRegex = Pattern.compile("-(?=(creator)|(publisher)|(editor)|(viewer)$)");

    public AuthInfoProvider() {
    }

    public AuthInfoProvider(T principal, String adminRolePrefix, String publicRole) {
        this.principal = principal;
        this.adminRolePrefix = adminRolePrefix;
        this.publicRole = publicRole;
        permissionSet = new TreeSet();
        permissionSet.add(PermissionLevelType.CREATOR);
        permissionSet.add(PermissionLevelType.PUBLISHER);
        permissionSet.add(PermissionLevelType.EDITOR);
        permissionSet.add(PermissionLevelType.VIEWER);
    }

    public AuthInfoProvider(T principal) {
        this.principal = principal;
    }

    public void setPrincipal(T principal) {
        this.principal = principal;
    }

    public boolean supportsPrincipal(Principal principal) {
        return type.isInstance(principal);
    }

    public T getPrincipal() {
        return principal;
    }

    protected Pattern getRoleExtractorRegex() {
        return roleExtractorRegex;
    }

    protected String getAdminRolePrefix() {
        return adminRolePrefix;
    }

    /**
     * checks whether the current principal allows access to an entity with
     * restricted access
     *
     * @param entity entity that has access rights defined by a role
     * @return
     */
    public boolean checkPermissions(final RestrictedByRole entity, final PermissionLevelType neededLevel) {
        Set<RolesEntity> allowedRoleEntities = entity.getRoles();

        // User is global administrator
        if (hasRealmAdminRole(getPrincipal())) {
            return true;
        }

        // disallow access by default
        // TODO: should this be allow all by default?
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

    public List<PermissionLevelType> getPermissions(RestrictedByRole entity) {
        // User is global administrator
        if (hasRealmAdminRole(getPrincipal())) {
            return Arrays.asList(
                    PermissionLevelType.CREATOR,
                    PermissionLevelType.PUBLISHER,
                    PermissionLevelType.EDITOR,
                    PermissionLevelType.VIEWER);
        }

        Set<RolesEntity> allowedRoleEntities = entity.getRoles();

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

    public String getUserId() {
        return getUserId(getPrincipal());
    }

    public abstract Set<String> getOwnedRoles(T principal);

    public abstract boolean hasRealmAdminRole(T principal);

    public abstract String getUserId(T principal);

    public abstract boolean hasGlobalAdminPermissions();
}
