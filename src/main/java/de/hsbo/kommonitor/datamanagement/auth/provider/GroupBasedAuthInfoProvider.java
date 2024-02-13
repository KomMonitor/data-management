package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;

import java.security.Principal;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GroupBasedAuthInfoProvider implements AuthInfoProvider{

    private Principal principal;

    private TokenParser tokenParser;

    private SortedSet permissionSet;

    public GroupBasedAuthInfoProvider() {
    }

    public GroupBasedAuthInfoProvider(Principal principal, TokenParser<?> tokenParser) {
        this.principal = principal;
        this.tokenParser = tokenParser;

        permissionSet = new TreeSet();
        permissionSet.add(PermissionLevelType.CREATOR);
        permissionSet.add(PermissionLevelType.PUBLISHER);
        permissionSet.add(PermissionLevelType.EDITOR);
        permissionSet.add(PermissionLevelType.VIEWER);
    }

    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean checkPermissions(RestrictedByRole entity, PermissionLevelType neededLevel) {
        return false;
    }

    @Override
    public List<PermissionLevelType> getPermissions(RestrictedByRole entity) {
        return null;
    }

    @Override
    public boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel) {
        return false;
    }
}
