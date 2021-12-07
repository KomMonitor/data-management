package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;
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

    @Value("${kommonitor.roles.admin:administrator}")
    private String adminRole;

    public KeycloakAuthInfoProvider(KeycloakPrincipal principal, String clientId, String adminRole) {
        super(principal);
        this.clientId = clientId;
        this.adminRole = adminRole;
    }

    /**
     * checks whether the current principal allows access to an entity with restricted access
     *
     * @param entity entity that has access rights defined by a role
     * @return
     */
    public boolean checkPermissions(final RestrictedByRole entity, final PermissionLevelType neededLevel) {
        Set<RolesEntity> allowedRoleEntites = entity.getRoles();

        // User is global administrator
        if (hasClientAdminRole()) {
            return true;
        }

        // disallow access by default
        // TODO: should this be allow all by default?
        if (allowedRoleEntites == null || allowedRoleEntites.isEmpty()) {
            return false;
        }

        Set<String> ownedRoles = getPrincipal()
            .getKeycloakSecurityContext()
            .getToken()
            .getRealmAccess()
            .getRoles();

        Set<Pair<?, ?>> allowedRoles = allowedRoleEntites.stream()
            .map(e -> Pair.of(e.getOrganizationalUnit(), e.getPermissionLevel()))
            .collect(Collectors.toSet());

        return ownedRoles.stream()
            // Split into OrganizationalUnit and PermissionLevel
            .map(kcRole -> {
                String[] split = kcRole.split("-(?=[crud]{0,4}$)", 2);
                return Pair.of(split[0], PermissionLevelType.fromValue(split[1]));
            })
            // we do not look at roles that do not give the required level
            .filter(r -> r.getSecond().compareTo(neededLevel) <= 0)
            // check the leftover roles for a match
            .anyMatch(allowedRoles::contains);
    }

    private boolean hasClientAdminRole() {
        return getPrincipal().getKeycloakSecurityContext()
            .getToken()
            .getResourceAccess(clientId)
            .isUserInRole(adminRole);
    }
}
