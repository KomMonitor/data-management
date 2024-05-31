package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.model.AdminRoleType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;

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
     * Checks if the current user has the permission to manage an Organization
     * @param entity Organization that should be managed i.e., perform operations to chang the hierarchy or metadata
     */
    boolean checkOrganizationalUnitPermissions(OrganizationalUnitEntity entity);

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

    /**
     * Checks if the current user has the given permission level for the given resource type
     *
     * @param permissionResourceType resource type what the permission refers to
     * @return True if user has the required permission
     */
    boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel, PermissionResourceType permissionResourceType);

    boolean checkOrganizationalUnitCreationPermissions(OrganizationalUnitEntity parent);

    List<AdminRoleType> getOrganizationalUnitCreationPermissions(OrganizationalUnitEntity entity);
}
