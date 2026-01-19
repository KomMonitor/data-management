package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.WebServicesPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.WebServiceOverviewType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebServicePublicController extends BasePathController implements WebServicesPublicApi {

    private static final Logger LOG = LoggerFactory.getLogger(WebServicePublicController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    WebServiceManager webServiceManager;

    @Autowired
    public WebServicePublicController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<WebServiceOverviewType>> getPublicWebServices(String resourceType) {
        LOG.info("Received request to get all public web service metadata");

        String accept = request.getHeader("Accept");
        try {
            if (accept != null && accept.contains("application/json")) {
                List<WebServiceOverviewType> webServiceMetadata = webServiceManager.getAllWebServicesMetadata(resourceType);
                return new ResponseEntity<>(webServiceMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<WebServiceOverviewType> getWebPublicServiceById(String webServiceId) {
        LOG.info("Received request to get public web service metadata for ID '{}'", webServiceId);

        String accept = request.getHeader("Accept");
        try {
            if (accept != null && accept.contains("application/json")) {
                WebServiceOverviewType webServiceMetadata = webServiceManager.getWebServiceById(webServiceId);
                return new ResponseEntity<>(webServiceMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }
}
