package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;

import java.util.List;

/**
 * Interface that provides authentication and authorization information
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface AuthInfoProvider {

    /**
     * Checks if the current user has at least the given level of permission on the given entitiy
     *
     * @param entity entity to be checked
     * @param neededLevel level to be checked
     * @return True if the user has at least the given level on the given entity
     */
    boolean checkPermissions(final RestrictedEntity entity, final PermissionLevelType neededLevel);

    /**
     * Lists all permissions the current user has on given entity
     * @param entity entity to be checked
     * @return list of permissions
     */
    List<PermissionLevelType> getPermissions(RestrictedEntity entity);

    /**
     * Checks if the current user has the given permission level on at least one entity in the database
     *
     * @param neededLevel level to be checked
     * @return True if user has at least one permissionlevel lower or equal the given level
     */
    boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel);

}
