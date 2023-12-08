package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesEntity;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provider that holds authorization information for a {@link KeycloakPrincipal}
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@Component
@RequestScope
public class KeycloakAuthInfoProvider extends AuthInfoProvider<KeycloakPrincipal> {

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String adminRolePrefix;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;

    private static final Pattern roleExtractorRegex = Pattern.compile("-(?=(creator)|(publisher)|(editor)|(viewer)$)");

    private SortedSet permissionSet;

    public KeycloakAuthInfoProvider(KeycloakPrincipal principal, String clientId, String adminRolePrefix, String publicRole) {
        super(principal);
        this.clientId = clientId;
        this.adminRolePrefix = adminRolePrefix;
        this.publicRole = publicRole;
        permissionSet = new TreeSet();
        permissionSet.add(PermissionLevelType.CREATOR);
        permissionSet.add(PermissionLevelType.PUBLISHER);
        permissionSet.add(PermissionLevelType.EDITOR);
        permissionSet.add(PermissionLevelType.VIEWER);
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
        if (hasRealmAdminRole()) {
            return true;
        }

        // disallow access by default
        // TODO: should this be allow all by default?
        if (allowedRoleEntities == null || allowedRoleEntities.isEmpty()) {
            return false;
        }

        Set<String> ownedRoles = getPrincipal()
                .getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .getRoles();

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
    public List<PermissionLevelType> getPermissions(RestrictedByRole entity) {
        // User is global administrator
        if (hasRealmAdminRole()) {
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

        Set<String> ownedRoles = getPrincipal()
                .getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .getRoles();

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

    @Override
    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel) {
        // User is global administrator
        if (hasRealmAdminRole()) {
            return true;
        }

        Set<String> ownedRoles = getPrincipal()
                .getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .getRoles();

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

    private boolean hasRealmAdminRole() {
        return getPrincipal().getKeycloakSecurityContext()
                .getToken()
                .getRealmAccess()
                .isUserInRole(adminRolePrefix + "-creator");
    }
}
