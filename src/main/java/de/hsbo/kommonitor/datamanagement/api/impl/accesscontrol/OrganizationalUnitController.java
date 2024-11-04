package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.AccessControlApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class OrganizationalUnitController extends BasePathController implements AccessControlApi {

    private static Logger logger = LoggerFactory.getLogger(OrganizationalUnitController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    OrganizationalUnitManager organizationalUnitManager;

    @Autowired
    private LastModificationManager lastModManager;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    AuthInfoProviderFactory authInfoProviderFactory;

    @Autowired
    public OrganizationalUnitController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    // @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<List<OrganizationalUnitOverviewType>> getOrganizationalUnits() {
        logger.debug("Received request to get all organizationalUnits");

        AuthInfoProvider authInfo = authInfoProviderFactory.createAuthInfoProvider();
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            List<OrganizationalUnitOverviewType> roles = organizationalUnitManager.getOrganizationalUnits(authInfo);
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('editor')")
    public ResponseEntity<OrganizationalUnitOverviewType> getOrganizationalUnitById(String organizationalUnitId) {
        logger.debug("Received request to get organizationalUnit with id '{}'", organizationalUnitId);

        AuthInfoProvider authInfo = authInfoProviderFactory.createAuthInfoProvider();
        String accept = request.getHeader("Accept");
        try {
            if (accept != null && accept.contains("application/json")) {
                OrganizationalUnitOverviewType unit =
                        organizationalUnitManager.getOrganizationalUnitById(organizationalUnitId, authInfo);
                return new ResponseEntity<>(unit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('creator', 'users')")
    public ResponseEntity<Void> addOrganizationalUnit(OrganizationalUnitInputType organizationalUnitData) {
        logger.info("Received request to insert new organizationalUnit with associated Roles");
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        OrganizationalUnitOverviewType persisted;
        try {
            persisted = organizationalUnitManager.addOrganizationalUnit(organizationalUnitData, provider);
            lastModManager.updateLastDatabaseModification_accessControl();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (persisted != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = persisted.getOrganizationalUnitId();
            try {
                responseHeaders.setLocation(new URI(location));
            } catch (URISyntaxException e) {
                // return ApiResponseUtil.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthorizedForOrganization(#organizationalUnitId)")
    @Override public ResponseEntity<Void> updateOrganizationalUnit(
            @P("organizationalUnitId") String organizationalUnitId,
            OrganizationalUnitInputType inputData) {
        logger.info("Received request to update new organizationalUnit with associated Roles");
        String accept = request.getHeader("Accept");

        String persistedId;
        try {
            persistedId = organizationalUnitManager.updateOrganizationalUnit(inputData, organizationalUnitId);
            lastModManager.updateLastDatabaseModification_accessControl();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (persistedId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            try {
                responseHeaders.setLocation(new URI(persistedId));
            } catch (URISyntaxException e) {
                // return ApiResponseUtil.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthorizedForOrganization(#organizationalUnitId)")
    @Override public ResponseEntity deleteOrganizationalUnit(@P("organizationalUnitId") String organizationalUnitId) {
        logger.info("Received request to delete organizationalUnit and associated roles for id '{}'",
                    organizationalUnitId);
        try {
            boolean isDeleted = organizationalUnitManager.deleteOrganizationalUnitAndRolesById(organizationalUnitId);
            lastModManager.updateLastDatabaseModification_accessControl();

            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @PreAuthorize("hasRequiredPermissionLevel('creator', 'users')")
    @Override public ResponseEntity<OrganizationalUnitPermissionOverviewType> getOrganizationalUnitPermissions(
            String organizationalUnitId,
            ResourceType resourceType) {
        logger.info("Received request to get organizationalUnit permissions for id '{}'", organizationalUnitId);
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                OrganizationalUnitPermissionOverviewType permissions =
                        organizationalUnitManager.getOrganizationalUnitPermissionsById(organizationalUnitId);
                return new ResponseEntity<>(permissions, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @PreAuthorize("isAuthorizedForOrganization(#organizationalUnitId)")
    @Override
    public ResponseEntity<OrganizationalUnitRoleAuthorityType> getOrganizationalUnitRoleAuthorities(
            @P("organizationalUnitId") String organizationalUnitId) {
        logger.info("Received request to get organizationalUnit role authorities for id '{}'", organizationalUnitId);
        String accept = request.getHeader("Accept");
        try {
            if (accept != null && accept.contains("application/json")) {
                OrganizationalUnitRoleAuthorityType roleAuthority = new OrganizationalUnitRoleAuthorityType(
                        organizationalUnitManager.getGroupAdminRoles(organizationalUnitId)
                );
                        ;
                return new ResponseEntity<>(roleAuthority, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @PreAuthorize("isAuthorizedForOrganization(#organizationalUnitId)")
    @Override
    public ResponseEntity<OrganizationalUnitRoleDelegateType> getOrganizationalUnitRoleDelegates(
            @P("organizationalUnitId")String organizationalUnitId) {
        logger.info("Received request to get organizationalUnit role delegates for id '{}'", organizationalUnitId);
        String accept = request.getHeader("Accept");
        try {
            if (accept != null && accept.contains("application/json")) {
                OrganizationalUnitRoleDelegateType roleAuthority = new OrganizationalUnitRoleDelegateType(
                        organizationalUnitManager.getDelegatedGroupAdminRoles(organizationalUnitId)
                );
                return new ResponseEntity<>(roleAuthority, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @PreAuthorize("isAuthorizedForOrganization(#organizationalUnitId)")
    @Override
    public ResponseEntity<Void> updateRoleDelegates(@P("organizationalUnitId")String organizationalUnitId,
                                                    List<GroupAdminRolesPUTInputType> organizationalUnitData) {
        logger.info("Received request to update organizationalUnit role delegates for id '{}'", organizationalUnitId);
        try {
            organizationalUnitManager.updateDelegatedGroupAdminRoles(organizationalUnitId, organizationalUnitData);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @PreAuthorize("isAuthorizedForAdminOperations()")
    @Override
    public ResponseEntity<Void> syncAllOrganizationalUnits() {
        logger.debug("Received request to synchronize all organizationalUnits");
        organizationalUnitManager.syncAllOrganizationalUnits();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthorizedForAdminOperations()")
    @Override
    public ResponseEntity<Void> syncOrganizationalUnit(String organizationalUnitId) {
        logger.debug("Received request to synchronize organizationalUnit with ID '{}'.", organizationalUnitId);
        try {
            organizationalUnitManager.syncOrganizationalUnit(organizationalUnitId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

}
