package de.hsbo.kommonitor.datamanagement.auth;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.Set;

/**
 * Interface that provides authentication and authorization information
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public abstract class AuthInfoProvider<T extends Principal> {

    private Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    private final T principal;

    public AuthInfoProvider(T principal) {
        this.principal = principal;
    }

    public abstract Set<String> getRealmRoles();

    public abstract Set<String> getClientRoles();

    public abstract boolean hasRealmRole(String role);

    public abstract boolean hasClientRole(String role);

    public boolean supportsPrincipal(Principal principal) {
        return type.isInstance(principal);
    }

    public T getPrincipal() {
        return principal;
    }

}
