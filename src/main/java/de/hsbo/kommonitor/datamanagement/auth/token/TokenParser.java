package de.hsbo.kommonitor.datamanagement.auth.token;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.Set;

public abstract class TokenParser<T extends Principal> {

    private Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    public abstract Set<String> getOwnedRoles(T principal);

    public abstract boolean hasRealmAdminRole(T principal, String adminRolePrefix);
}
