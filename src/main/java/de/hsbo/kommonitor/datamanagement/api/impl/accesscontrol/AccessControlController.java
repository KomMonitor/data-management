package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.AccessControlApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitOverviewType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class AccessControlController extends BasePathController implements AccessControlApi {

    private static Logger logger = LoggerFactory.getLogger(AccessControlController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    OrganizationalUnitManager organizationalUnitManager;

    @Autowired
    private LastModificationManager lastModManager;

    @Autowired
    public AccessControlController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<List<OrganizationalUnitOverviewType>> getOrganizationalUnits() {
        logger.debug("Received request to get all organizationalUnits");
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            List<OrganizationalUnitOverviewType> roles = organizationalUnitManager.getOrganizationalUnits();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<OrganizationalUnitOverviewType> getOrganizationalUnitById(String organizationalUnitId) {
        logger.debug("Received request to get organizationalUnit with id '{}'", organizationalUnitId);
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            OrganizationalUnitOverviewType unit =
                organizationalUnitManager.getOrganizationalUnitById(organizationalUnitId);
            return new ResponseEntity<>(unit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('creator')")
    public ResponseEntity<Void> addOrganizationalUnit(OrganizationalUnitInputType organizationalUnitData) {
        logger.info("Received request to insert new organizationalUnit with associated Roles");

        String accept = request.getHeader("Accept");

        OrganizationalUnitOverviewType persisted;
        try {
            persisted = organizationalUnitManager.addOrganizationalUnit(organizationalUnitData);
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

    @PreAuthorize("hasRequiredPermissionLevel('creator')")
    @Override public ResponseEntity<Void> updateOrganizationalUnit(
            String organizationalUnitId,
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

    @PreAuthorize("hasRequiredPermissionLevel('creator')")
    @Override public ResponseEntity deleteOrganizationalUnit(String organizationalUnitId) {
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

}
