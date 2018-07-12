package de.hsbo.kommonitor.datamanagement.api.impl.roles;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.RolesApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleInputType;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;

@Controller
public class RolesController extends BasePathController implements RolesApi {

	private static Logger logger = LoggerFactory.getLogger(RolesController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	RolesManager rolesManager;

	@org.springframework.beans.factory.annotation.Autowired
	public RolesController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity addRole(@RequestBody RoleInputType roleData) {
		logger.info("Received request to insert new role");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		String roleId;
		try {
			roleId = rolesManager.addRole(roleData);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (roleId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = roleId;
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
	public ResponseEntity deleteRole(@PathVariable("roleId") String roleId) {
logger.info("Received request to delete role for roleId '{}'", roleId);
		
		String accept = request.getHeader("Accept");

		/*
		 * delete role with the specified id
		 */

		if (accept != null && accept.contains("application/json")){
			
			boolean isDeleted;
			try {
				isDeleted = rolesManager.deleteRoleById(roleId);
			
			if(isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);
			
			} catch (ResourceNotFoundException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
			
			
		} else{
			return ApiUtils.createResponseEntityFromException(new Exception("False or missing Accept Header! Expected Accept Header is 'application/json'"));
		}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	
	}

	@Override
	public ResponseEntity<RoleOverviewType> getRoleById(@PathVariable("roleId") String roleId) {
		logger.info("Received request to get role for roleId '{}'", roleId);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the role for the specified id
		 */

		if (accept != null && accept.contains("application/json")){
			
			RoleOverviewType role = rolesManager.getRoleById(roleId);
			
			return new ResponseEntity<>(role, HttpStatus.OK);
			
		} else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<List<RoleOverviewType>> getRoles() {
		logger.info("Received request to get all roles");
		String accept = request.getHeader("Accept");

		/*
		 * retrieve all available roles
		 * 
		 * return them to client
		 */

		if (accept != null && accept.contains("application/json")){
			
			List<RoleOverviewType> roles = rolesManager.getRoles();
			
			return new ResponseEntity<>(roles, HttpStatus.OK);
			
		} else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity updateRole(@PathVariable("roleId") String roleId,@RequestBody RoleInputType roleData) {
		logger.info("Received request to update role with RoleId '{}'", roleId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		
		try {
			roleId = rolesManager.updateRole(roleData, roleId);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (roleId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = roleId;
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
