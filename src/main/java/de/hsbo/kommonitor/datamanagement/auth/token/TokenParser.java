package de.hsbo.kommonitor.datamanagement.auth.token;

import de.hsbo.kommonitor.datamanagement.auth.Group;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.Set;

public abstract class TokenParser<T extends Principal> {

    private Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    public abstract Set<String> getOwnedRoles(T principal);

    public abstract boolean hasRealmAdminRole(T principal, String adminRole);

    public abstract Set<String> getGroupsClaim(T principal);

    public abstract Set<Group> getGroupMemberships(T principal);

    public abstract String getUserId(T principal);
}
