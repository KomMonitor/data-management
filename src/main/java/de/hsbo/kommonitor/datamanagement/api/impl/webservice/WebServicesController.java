package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.WebServicesApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesController;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class WebServicesController extends BasePathController implements WebServicesApi {

    private static Logger LOG = LoggerFactory.getLogger(WebServicesController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private LastModificationManager lastModManager;

    @Autowired
    WebServiceManager webServiceManager;

    @Autowired
    AuthInfoProviderFactory authInfoProviderFactory;

    public WebServicesController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('creator', 'resources')")
    public ResponseEntity<WebServiceOverviewType> addWebServiceAsBody(WebServiceType webServiceType) {
        LOG.info("Received request to insert new georesource");

        WebServiceOverviewType webServiceMetadata;
        try {
            webServiceMetadata = webServiceManager.addWebService(webServiceType);
            lastModManager.updateLastDatabaseModificationWebServices();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (webServiceMetadata != null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            String location = webServiceMetadata.getId();
            try {
                responseHeaders.setLocation(new URI(location));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<WebServiceOverviewType>(webServiceMetadata, responseHeaders, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#webServiceId, 'web-service', 'creator')")
    public ResponseEntity<Void> deleteWebServiceById(@P("webServiceId")String webServiceId) {
        LOG.info("Received request to delete web service with ID '{}'", webServiceId);

        boolean isDeleted;
        try {
            isDeleted = webServiceManager.deleteWebServiceDatasetById(webServiceId);
            lastModManager.updateLastDatabaseModificationWebServices();

            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#georesourceId, 'web-service', 'viewer')")
    public ResponseEntity<WebServiceOverviewType> getWebServiceById(@P("webServiceId")String webServiceId) {
        LOG.info("Received request to get web service metadata for datasetId '{}' test", webServiceId);
        String accept = request.getHeader("Accept");
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        try {
            if (accept != null && accept.contains("application/json")) {
                WebServiceOverviewType webService = webServiceManager.getWebServiceById(webServiceId, provider);
                return new ResponseEntity<>(webService, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<PermissionLevelType>> getWebServicePermissionsById(String webServiceId) {
        LOG.info("Received request to list access rights for web service with ID '{}'", webServiceId);
        String accept = request.getHeader("Accept");
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        try {
            if (accept != null && accept.contains("application/json")) {
                List<PermissionLevelType> permissions = webServiceManager.getWebServicePermissionsById(webServiceId, provider);
                return new ResponseEntity<>(permissions, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<List<WebServiceOverviewType>> getWebServices() {
        LOG.info("Received request to get all web service metadata");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        String accept = request.getHeader("Accept");
        try {
            if (accept != null && accept.contains("application/json")) {
                List<WebServiceOverviewType> webServices = webServiceManager
                        .getAllWebServicesMetadata(provider);
                return new ResponseEntity<>(webServices, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#webServiceId, 'web-service', 'editor')")
    public ResponseEntity<Void> updateWebServiceAsBody(@P("webServiceId")String webServiceId, WebServiceType webServiceData) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateWebServiceMetadataAsBody(String webServiceId, WebServiceType metadata) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#webServiceId, 'web-service', 'creator')")
    public ResponseEntity<Void> updateWebServiceOwnership(@P("webServiceId")String webServiceId, OwnerInputType ownerInputType) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#webServiceId, 'web-service', 'creator')")
    public ResponseEntity<Void> updateWebServicePermissions(@P("webServiceId")String webServiceId, PermissionLevelInputType permissionLevelInputType) {
        return null;
    }
}
