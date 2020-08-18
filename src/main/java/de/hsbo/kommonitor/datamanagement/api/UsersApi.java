/**
 * NOTE: This class is auto generated by the swagger code generator program (unset).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hsbo.kommonitor.datamanagement.model.users.UserInputType;
import de.hsbo.kommonitor.datamanagement.model.users.UserOverviewType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

@Api(value = "Users", description = "the Users API")
public interface UsersApi {

    @ApiOperation(value = "Register a new user", nickname = "addUser", notes = "Add/Register a user", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/users",
        consumes = MediaType.ALL_VALUE,
        method = RequestMethod.POST)
    ResponseEntity addUser(@ApiParam(value = "user data" ,required=true )   @RequestBody UserInputType userData);


    @ApiOperation(value = "Delete the user", nickname = "deleteUser", notes = "Delete the user", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/users/{userId}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteUser(@ApiParam(value = "unique identifier of the user",required=true) @PathVariable("userId") String userId);


    @ApiOperation(value = "retrieve information about the selected user and his/her role", nickname = "getUserById", notes = "retrieve information about the selected user and his/her role", response = UserOverviewType.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UserOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/users/{userId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<UserOverviewType> getUserById(@ApiParam(value = "unique identifier of the user",required=true) @PathVariable("userId") String userId);


    @ApiOperation(value = "retrieve information about available users and their roles", nickname = "getUsers", notes = "retrieve information about available users and their roles", response = UserOverviewType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UserOverviewType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<UserOverviewType>> getUsers();


    @ApiOperation(value = "Modify user information", nickname = "updateUser", notes = "Modify user information", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/users/{userId}",
        consumes = MediaType.ALL_VALUE,
        method = RequestMethod.PUT)
    ResponseEntity updateUser(@ApiParam(value = "unique identifier of the user",required=true) @PathVariable("userId") String userId,@ApiParam(value = "user data" ,required=true )   @RequestBody UserInputType userData);

}
