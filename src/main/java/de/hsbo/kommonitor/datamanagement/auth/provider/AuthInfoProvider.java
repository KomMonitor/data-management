package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedByRole;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesEntity;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Interface that provides authentication and authorization information
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface AuthInfoProvider {

    boolean checkPermissions(final RestrictedByRole entity, final PermissionLevelType neededLevel);

    List<PermissionLevelType> getPermissions(RestrictedByRole entity);

    boolean hasRequiredPermissionLevel(PermissionLevelType neededLevel);

}
