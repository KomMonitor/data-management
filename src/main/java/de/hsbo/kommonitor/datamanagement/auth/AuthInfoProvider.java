package de.hsbo.kommonitor.datamanagement.auth;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;

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

    public AuthInfoProvider() {
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

    public abstract boolean checkPermissions(RestrictedByRole entity, PermissionLevelType neededLevel);
    
    public abstract boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel);

}
