package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.ProcessScriptsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptOverviewType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPUTInputType;

@Controller
public class ScriptController extends BasePathController implements ProcessScriptsApi {

	private static Logger logger = LoggerFactory.getLogger(ScriptController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	ScriptManager scriptManager;

	@org.springframework.beans.factory.annotation.Autowired
	public ScriptController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity addProcessScriptAsBody(ProcessScriptPOSTInputType processScriptData) {
		logger.info("Received request to insert new process script");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		String scriptId;
		try {
			scriptId = scriptManager.addScript(processScriptData);
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

			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity deleteProcessScript(String indicatorId) {
		logger.info("Received request to delete process scripts for indicatorId '{}'", indicatorId);
		
		String accept = request.getHeader("Accept");

		/*
		 * delete role with the specified id
		 */
			
			boolean isDeleted;
			try {
				isDeleted = scriptManager.deleteScriptByIndicatorId(indicatorId);
			
			if(isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);
			
			} catch (ResourceNotFoundException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<String> getProcessScriptTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<ProcessScriptOverviewType>> getProcessScripts() {
		logger.info("Received request to get all process script metadata");
		String accept = request.getHeader("Accept");

		/*
		 * retrieve all available roles
		 * 
		 * return them to client
		 */

		if (accept != null && accept.contains("application/json")){
			
			List<ProcessScriptOverviewType> roles = scriptManager.getAllScriptsMetadata();
			
			return new ResponseEntity<>(roles, HttpStatus.OK);
			
		} else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<List<ProcessScriptOverviewType>> getProcessScriptsForIndicator(String indicatorId) {
		logger.info("Received request to get process script metadata for indicatorId '{}'", indicatorId);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the role for the specified id
		 */

		if (accept != null && accept.contains("application/json")){
			
			List<ProcessScriptOverviewType> scripts = scriptManager.getScriptMetadataByIndicatorId(indicatorId);
			
			return new ResponseEntity<>(scripts, HttpStatus.OK);
			
		} else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity updateProcessScriptAsBody(String indicatorId, ProcessScriptPUTInputType processScriptData) {
		logger.info("Received request to update process script with indicatorId '{}'", indicatorId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		try {
			indicatorId = scriptManager.updateScriptForIndicatorId(processScriptData, indicatorId);
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
}
