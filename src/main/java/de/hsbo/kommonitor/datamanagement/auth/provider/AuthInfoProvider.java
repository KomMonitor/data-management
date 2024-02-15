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

    boolean checkPermissions(final RestrictedEntity entity, final PermissionLevelType neededLevel);

    List<PermissionLevelType> getPermissions(RestrictedEntity entity);

    boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel);

}
