package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.ProcessScriptsPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathPublicController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.ProcessScriptOverviewType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ScriptPublicController extends BasePathPublicController implements ProcessScriptsPublicApi {

    private static Logger logger = LoggerFactory.getLogger(ScriptPublicController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    ScriptManager scriptManager;

    @Autowired
    AuthInfoProviderFactory authInfoProviderFactory;

    @Autowired
    public ScriptPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<ProcessScriptOverviewType>> getPublicProcessScripts() {
        logger.info("Received request to get all process script metadata");
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {

            List<ProcessScriptOverviewType> roles = scriptManager.getAllScriptsMetadata();

            return new ResponseEntity<>(roles, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ProcessScriptOverviewType> getProcessScriptForPublicIndicator(@PathVariable("indicatorId") String indicatorId) {
        logger.info("Received request to get process script metadata for indicatorId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {

            ProcessScriptOverviewType script = null;

            try {
                script = scriptManager.getScriptMetadataByIndicatorId(indicatorId);
            } catch (ResourceNotFoundException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(script, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> getProcessScriptCodeForPublicIndicator(@PathVariable("indicatorId") String indicatorId) {
        logger.info("Received request to get scriptCode for associated indicatorId '{}'", indicatorId);

        try {
            byte[] scriptCode = scriptManager.getScriptCodeForIndicatorId(indicatorId);

            HttpHeaders headers = new HttpHeaders();

            String fileName = "ScriptCode.js";
            headers.add("content-disposition", "attachment; filename=" + fileName);

            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/javascript"))
                    .body(scriptCode);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<byte[]> getPublicProcessScriptCode(@PathVariable("scriptId") String scriptId) {
        logger.info("Received request to get scriptCode for scriptId '{}'", scriptId);

        try {
            byte[] scriptCode = scriptManager.getScriptCodeForScriptId(scriptId);

            HttpHeaders headers = new HttpHeaders();

            String fileName = "ScriptCode.js";
            headers.add("content-disposition", "attachment; filename=" + fileName);

            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/javascript"))
                    .body(scriptCode);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<ProcessScriptOverviewType> getPublicProcessScriptForScriptId(@PathVariable("scriptId") String scriptId) {
        logger.info("Received request to get process script metadata for scriptId '{}'", scriptId);
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {

            ProcessScriptOverviewType script = null;

            try {
                script = scriptManager.getScriptMetadataByScriptId(scriptId);
            } catch (ResourceNotFoundException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(script, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
