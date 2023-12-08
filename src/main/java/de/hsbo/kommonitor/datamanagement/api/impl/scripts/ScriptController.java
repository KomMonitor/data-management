package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.legacy.ProcessScriptsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.legacy.scripts.ProcessScriptOverviewType;
import de.hsbo.kommonitor.datamanagement.model.legacy.scripts.ProcessScriptPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.legacy.scripts.ProcessScriptPUTInputType;

@Controller
public class ScriptController extends BasePathController implements ProcessScriptsApi {

	private static Logger logger = LoggerFactory.getLogger(ScriptController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	ScriptManager scriptManager;

	@Autowired
	AuthInfoProviderFactory authInfoProviderFactory;
	
	@Autowired
    private LastModificationManager lastModManager;

	@org.springframework.beans.factory.annotation.Autowired
	public ScriptController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('publisher')")
	public ResponseEntity<ProcessScriptOverviewType> addProcessScriptAsBody(@RequestBody ProcessScriptPOSTInputType processScriptData) {
		logger.info("Received request to insert new process script");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		ProcessScriptOverviewType script;
		try {
			script = scriptManager.addScript(processScriptData);
			lastModManager.updateLastDatabaseModification_processScripts();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (script != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = script.getScriptId();
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<ProcessScriptOverviewType>(script, responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('creator')")
	public ResponseEntity deleteProcessScript(@PathVariable("indicatorId") String indicatorId) {
		logger.info("Received request to delete process scripts for indicatorId '{}'", indicatorId);
		
		String accept = request.getHeader("Accept");

		/*
		 * delete role with the specified id
		 */
			
			boolean isDeleted;
			try {
				isDeleted = scriptManager.deleteScriptByIndicatorId(indicatorId);
				lastModManager.updateLastDatabaseModification_processScripts();
			
			if(isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);
			
			} catch (Exception e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<byte[]> getProcessScriptTemplate() {
		// TODO implement getTemplate!
		/*
		 * TODO FIXME it is not yet clear how to store and deliver the template, make individual REST endpoint?
		 */
		return null;
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<List<ProcessScriptOverviewType>> getProcessScripts(Principal principal) {
		logger.info("Received request to get all process script metadata");
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

		if (accept != null && accept.contains("application/json")){
			
			List<ProcessScriptOverviewType> roles = scriptManager.getAllScriptsMetadata(provider);
			
			return new ResponseEntity<>(roles, HttpStatus.OK);
			
		} else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<ProcessScriptOverviewType> getProcessScriptForIndicator(@PathVariable("indicatorId") String indicatorId, Principal principal) {
		logger.info("Received request to get process script metadata for indicatorId '{}'", indicatorId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

		if (accept != null && accept.contains("application/json")) {

			ProcessScriptOverviewType script = null;

			try {
				script = scriptManager.getScriptMetadataByIndicatorId(indicatorId, provider);
			} catch (ResourceNotFoundException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(script, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('editor')")
	public ResponseEntity updateProcessScriptAsBody(@PathVariable("indicatorId") String indicatorId, @RequestBody ProcessScriptPUTInputType processScriptData) {
		logger.info("Received request to update process script with indicatorId '{}'", indicatorId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		try {
			indicatorId = scriptManager.updateScriptForIndicatorId(processScriptData, indicatorId);
			lastModManager.updateLastDatabaseModification_processScripts();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (indicatorId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = indicatorId;
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

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<byte[]> getProcessScriptCodeForIndicator(@PathVariable("indicatorId") String indicatorId, Principal principal) {
		logger.info("Received request to get scriptCode for associated indicatorId '{}'", indicatorId);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

		try {
			byte[] scriptCode = scriptManager.getScriptCodeForIndicatorId(indicatorId, provider);
			
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
	@PreAuthorize("hasRequiredPermissionLevel('creator')")
	public ResponseEntity deleteProcessScriptByScriptId(@PathVariable("scriptId") String scriptId) {
		logger.info("Received request to delete process scripts for scriptId '{}'", scriptId);
		
		String accept = request.getHeader("Accept");

		/*
		 * delete role with the specified id
		 */
			
			boolean isDeleted;
			try {
				isDeleted = scriptManager.deleteScriptByScriptId(scriptId);
				lastModManager.updateLastDatabaseModification_processScripts();
			
			if(isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);
			
			} catch (Exception e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<byte[]> getProcessScriptCode(@PathVariable("scriptId") String scriptId, Principal principal) {
		logger.info("Received request to get scriptCode for scriptId '{}'", scriptId);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

		try {
			byte[] scriptCode = scriptManager.getScriptCodeForScriptId(scriptId, provider);
			
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
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<ProcessScriptOverviewType> getProcessScriptForScriptId(@PathVariable("scriptId") String scriptId, Principal principal) {
		logger.info("Received request to get process script metadata for scriptId '{}'", scriptId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

		if (accept != null && accept.contains("application/json")) {

			ProcessScriptOverviewType script = null;

			try {
				script = scriptManager.getScriptMetadataByScriptId(scriptId, provider);
			} catch (ResourceNotFoundException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(script, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('editor')")
	public ResponseEntity updateProcessScriptAsBodyByScriptId(@PathVariable("scriptId") String scriptId,
			@RequestBody ProcessScriptPUTInputType processScriptData) {
		logger.info("Received request to update process script with scriptId '{}'", scriptId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		try {
			scriptId = scriptManager.updateScriptForScriptId(processScriptData, scriptId);
			lastModManager.updateLastDatabaseModification_processScripts();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (scriptId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = scriptId;
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
}
