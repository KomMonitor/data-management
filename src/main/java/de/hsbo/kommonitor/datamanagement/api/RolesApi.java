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

import de.hsbo.kommonitor.datamanagement.model.roles.RoleInputType;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

@Api(value = "Roles", description = "the Roles API")
public interface RolesApi {

    @ApiOperation(value = "Register a new role", nickname = "addRole", notes = "Add/Register a role", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/roles",
        consumes = MediaType.ALL_VALUE,
        method = RequestMethod.POST)
    ResponseEntity<RoleOverviewType> addRole(@ApiParam(value = "role input data" ,required=true )   @RequestBody RoleInputType roleData);


    @ApiOperation(value = "Delete the role", nickname = "deleteRole", notes = "Delete the role", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/roles/{roleId}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteRole(@ApiParam(value = "unique identifier of the role",required=true) @PathVariable("roleId") String roleId);


    @ApiOperation(value = "retrieve information about the selected role", nickname = "getRoleById", notes = "retrieve information about the selected role", response = RoleOverviewType.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = RoleOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/roles/{roleId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<RoleOverviewType> getRoleById(@ApiParam(value = "unique identifier of the role",required=true) @PathVariable("roleId") String roleId);


    @ApiOperation(value = "retrieve information about available roles", nickname = "getRoles", notes = "retrieve information about available roles", response = RoleOverviewType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = RoleOverviewType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/roles",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<RoleOverviewType>> getRoles();


    @ApiOperation(value = "Modify role information", nickname = "updateRole", notes = "Modify role information", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/roles/{roleId}",
        consumes = MediaType.ALL_VALUE,
        method = RequestMethod.PUT)
    ResponseEntity updateRole(@ApiParam(value = "unique identifier of the role",required=true) @PathVariable("roleId") String roleId,@ApiParam(value = "role input data" ,required=true )   @RequestBody RoleInputType roleData);

}
