package de.hsbo.kommonitor.datamanagement.api.impl.users;

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

import de.hsbo.kommonitor.datamanagement.api.UsersApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.users.UserInputType;
import de.hsbo.kommonitor.datamanagement.model.users.UserOverviewType;

@Controller
public class UsersController extends BasePathController implements UsersApi {

	private static Logger logger = LoggerFactory.getLogger(UsersController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	UsersManager usersManager;

	@org.springframework.beans.factory.annotation.Autowired
	public UsersController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity addUser(@RequestBody UserInputType userData) {
		logger.info("Received request to insert new user");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		String userId;
		try {
			userId = usersManager.addUser(userData);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (userId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = userId;
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
	public ResponseEntity deleteUser(@PathVariable("userId") String userId) {
		logger.info("Received request to delete user for userId '{}'", userId);

		String accept = request.getHeader("Accept");

		/*
		 * delete user with the specified id
		 */

			boolean isDeleted;
			try {
				isDeleted = usersManager.deleteUserById(userId);

				if (isDeleted)
					return new ResponseEntity<>(HttpStatus.OK);

			} catch (ResourceNotFoundException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<UserOverviewType> getUserById(@PathVariable("userId") String userId) {
		logger.info("Received request to get user for userId '{}'", userId);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */

		if (accept != null && accept.contains("application/json")) {

			UserOverviewType user = usersManager.getUserById(userId);

			return new ResponseEntity<>(user, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<List<UserOverviewType>> getUsers() {
		logger.info("Received request to get all users");
		String accept = request.getHeader("Accept");

		/*
		 * retrieve all available users
		 * 
		 * return them to client
		 */

		if (accept != null && accept.contains("application/json")) {

			List<UserOverviewType> users = usersManager.getUsers();

			return new ResponseEntity<>(users, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity updateUser(@PathVariable("userId") String userId, @RequestBody UserInputType userData) {
		logger.info("Received request to update user with UserId '{}'", userId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			userId = usersManager.updateUser(userData, userId);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (userId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = userId;
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
