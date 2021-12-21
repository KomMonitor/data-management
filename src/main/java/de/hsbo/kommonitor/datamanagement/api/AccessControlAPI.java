package de.hsbo.kommonitor.datamanagement.api;

import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitInputType;
import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-12-10T00:30:46.583Z")
@Api(value = "Roles", description = "the AccessControl API")
public interface AccessControlAPI {

    @ApiOperation(value = "Retrieve information about available roles",
                  nickname = "getRoles",
                  notes = "retrieve information about available roles",
                  response = RoleOverviewType.class,
                  authorizations = {
                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {

                      })
                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = RoleOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(value = "/roles",
                    produces = {"application/json"},
                    method = RequestMethod.GET)
    ResponseEntity<List<RoleOverviewType>> getRoles();

    @ApiOperation(value = "Retrieve information about the selected role",
                  nickname = "getRoleById",
                  notes = "retrieve information about the selected role",
                  response = RoleOverviewType.class,
//                  authorizations = {
//                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {
//
//                      })
//                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = RoleOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(value = "/roles/{roleId}",
                    produces = {"application/json"},
                    method = RequestMethod.GET)
    ResponseEntity<RoleOverviewType> getRoleById(
        @ApiParam(value = "roleId", required = true) @PathVariable("roleId") String roleId);

    @ApiOperation(value = "Retrieve information about available organizationalUnits",
                  nickname = "getOrganizationalUnits",
                  notes = "retrieve information about available organizationalUnits",
                  response = OrganizationalUnitOverviewType.class,
//                  authorizations = {
//                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {
//
//                      })
//                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = OrganizationalUnitOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(value = "/organizationalUnits",
                    produces = {"application/json"},
                    method = RequestMethod.GET)
    ResponseEntity<List<OrganizationalUnitOverviewType>> getOrganizationalUnits();

    @ApiOperation(value = "Retrieve information about selected organizationalUnit",
                  nickname = "getOrganizationalUnitById",
                  notes = "retrieve information about selected organizationalUnit",
                  response = OrganizationalUnitOverviewType.class,
//                  authorizations = {
//                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {
//
//                      })
//                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = OrganizationalUnitOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(value = "/organizationalUnits/{organizationalUnitId}",
                    produces = {"application/json"},
                    method = RequestMethod.GET)
    ResponseEntity<OrganizationalUnitOverviewType> getOrganizationalUnitById(
        @ApiParam(value = "organizationalUnitId", required = true) @PathVariable("organizationalUnitId")
            String organizationalUnitId);

    @ApiOperation(value = "Register a new organizationalUnit and create corresponding Roles",
                  nickname = "addOrganizationalUnit",
                  notes = "Add/Register a organizationalUnit and create corresponding Roles",
                  response = OrganizationalUnitOverviewType.class,
//                  authorizations = {
//                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {
//
//                      })
//                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class),
        @ApiResponse(code = 201, message = "Created", response = ResponseEntity.class),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Invalid input")
    })
    @RequestMapping(value = "/organizationalUnits",
                    produces = {"application/json"},
                    consumes = {"application/json"},
                    method = RequestMethod.POST)
    ResponseEntity<OrganizationalUnitOverviewType> addOrganizationalUnit(
        @ApiParam(value = "data", required = true) @Valid @RequestBody
            OrganizationalUnitInputType organizationalUnitData);

    @ApiOperation(value = "Modify organizationalUnit information",
                  nickname = "updateOrganizationalUnit",
                  notes = "Modify organizationalUnit information",
                  response = OrganizationalUnitOverviewType.class,
//                  authorizations = {
//                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {
//
//                      })
//                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class),
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 405, message = "Invalid input")
    })
    @RequestMapping(value = "/organizationalUnits/{organizationalUnitId}",
                    produces = {"application/json"},
                    consumes = {"application/json"},
                    method = RequestMethod.PUT)
    ResponseEntity updateOrganizationalUnit(
        @ApiParam(value = "roleData", required = true) @Valid @RequestBody OrganizationalUnitInputType roleData,
        @ApiParam(value = "organizationalUnitId", required = true) @PathVariable("organizationalUnitId")
            String organizationalUnitId);

    @ApiOperation(value = "Delete the organizationalUnit and its associated roles",
                  nickname = "deleteOrganizationalUnit",
                  notes = "Delete the organizationalUnit and its associated roles",
                  response = OrganizationalUnitOverviewType.class,
//                  authorizations = {
//                      @Authorization(value = "kommonitor-data-access_oauth", scopes = {
//
//                      })
//                  },
                  tags = {"access-control-controller",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = OrganizationalUnitOverviewType.class),
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @RequestMapping(value = "/organizationalUnits/{organizationalUnitId}",
                    produces = {"application/json"},
                    method = RequestMethod.DELETE)
    ResponseEntity deleteOrganizationalUnit(
        @ApiParam(value = "organizationalUnitId", required = true) @PathVariable("organizationalUnitId")
            String organizationalUnitId);

}
