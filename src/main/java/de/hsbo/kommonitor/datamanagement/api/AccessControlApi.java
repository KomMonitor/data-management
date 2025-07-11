/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import de.hsbo.kommonitor.datamanagement.model.GroupAdminRolesPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitRoleAuthorityType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitRoleDelegateType;
import de.hsbo.kommonitor.datamanagement.model.ResourceType;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-04T15:23:19.921001600+01:00[Europe/Berlin]")
@Validated
@Tag(name = "access-control", description = "the AccessControl API")
public interface AccessControlApi {

    /**
     * POST /organizationalUnits : Register a new organizationalUnit and create corresponding Roles
     * Add/Register a organizationalUnit and create corresponding Roles
     *
     * @param organizationalUnitData data (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "addOrganizationalUnit",
        summary = "Register a new organizationalUnit and create corresponding Roles",
        description = "Add/Register a organizationalUnit and create corresponding Roles",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/organizationalUnits",
        consumes = { "application/json" }
    )
    
    ResponseEntity<Void> addOrganizationalUnit(
        @Parameter(name = "organizationalUnitData", description = "data", required = true) @Valid @RequestBody OrganizationalUnitInputType organizationalUnitData
    );


    /**
     * DELETE /organizationalUnits/{organizationalUnitId} : Delete the organizationalUnit and its associated roles
     * Delete the organizationalUnit and its associated roles
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @return OK (status code 200)
     *         or No Content (status code 204)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     */
    @Operation(
        operationId = "deleteOrganizationalUnit",
        summary = "Delete the organizationalUnit and its associated roles",
        description = "Delete the organizationalUnit and its associated roles",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/organizationalUnits/{organizationalUnitId}"
    )
    
    ResponseEntity<Void> deleteOrganizationalUnit(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId
    );


    /**
     * GET /organizationalUnits/{organizationalUnitId} : Retrieve information about selected organizationalUnit
     * retrieve information about selected organizationalUnit
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getOrganizationalUnitById",
        summary = "Retrieve information about selected organizationalUnit",
        description = "retrieve information about selected organizationalUnit",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationalUnitOverviewType.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/organizationalUnits/{organizationalUnitId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<OrganizationalUnitOverviewType> getOrganizationalUnitById(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId
    );


    /**
     * GET /organizationalUnits/{organizationalUnitId}/permissions : Retrieve information about selected organizationalUnits permissions
     * retrieve information about selected organizationalUnits permissions
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @param resourceType resourceType (optional)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getOrganizationalUnitPermissions",
        summary = "Retrieve information about selected organizationalUnits permissions",
        description = "retrieve information about selected organizationalUnits permissions",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationalUnitPermissionOverviewType.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/organizationalUnits/{organizationalUnitId}/permissions",
        produces = { "application/json" }
    )
    
    ResponseEntity<OrganizationalUnitPermissionOverviewType> getOrganizationalUnitPermissions(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId,
        @Parameter(name = "resourceType", description = "resourceType", in = ParameterIn.QUERY) @Valid @RequestParam(value = "resourceType", required = false) ResourceType resourceType
    );


    /**
     * GET /organizationalUnits/{organizationalUnitId}/role-authorities : Fetch all role authorities for the selected organizationalUnit.
     * Fetch all role authorities for the selected organizationalUnit. In particular, these are all roles that have been assigned to the selected organizational that represent delegated administrative tasks for other organizational unit.
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getOrganizationalUnitRoleAuthorities",
        summary = "Fetch all role authorities for the selected organizationalUnit.",
        description = "Fetch all role authorities for the selected organizationalUnit. In particular, these are all roles that have been assigned to the selected organizational that represent delegated administrative tasks for other organizational unit.",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationalUnitRoleAuthorityType.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/organizationalUnits/{organizationalUnitId}/role-authorities",
        produces = { "application/json" }
    )
    
    ResponseEntity<OrganizationalUnitRoleAuthorityType> getOrganizationalUnitRoleAuthorities(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId
    );


    /**
     * GET /organizationalUnits/{organizationalUnitId}/role-delegates : Fetch all role delegates for the selected organizationalUnit.
     * Fetch all role delegates for the selected organizationalUnit. In particular, these are all roles that represent delegated administrative tasks and have been assigned to other organizational unit.
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getOrganizationalUnitRoleDelegates",
        summary = "Fetch all role delegates for the selected organizationalUnit.",
        description = "Fetch all role delegates for the selected organizationalUnit. In particular, these are all roles that represent delegated administrative tasks and have been assigned to other organizational unit.",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationalUnitRoleDelegateType.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/organizationalUnits/{organizationalUnitId}/role-delegates",
        produces = { "application/json" }
    )
    
    ResponseEntity<OrganizationalUnitRoleDelegateType> getOrganizationalUnitRoleDelegates(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId
    );


    /**
     * GET /organizationalUnits : Retrieve information about available organizationalUnits
     * retrieve information about available organizationalUnits
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getOrganizationalUnits",
        summary = "Retrieve information about available organizationalUnits",
        description = "retrieve information about available organizationalUnits",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrganizationalUnitOverviewType.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/organizationalUnits",
        produces = { "application/json" }
    )
    
    ResponseEntity<List<OrganizationalUnitOverviewType>> getOrganizationalUnits(
        
    );


    /**
     * POST /organizationalUnits/sync : Synchronize the all OrganizationalUnits and Keycloak entities
     * Synchronize the all OrganizationalUnis and Keycloak entities (group, roles and role policies) with each other.
     *
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "syncAllOrganizationalUnits",
        summary = "Synchronize the all OrganizationalUnits and Keycloak entities",
        description = "Synchronize the all OrganizationalUnis and Keycloak entities (group, roles and role policies) with each other.",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/organizationalUnits/sync"
    )
    
    ResponseEntity<Void> syncAllOrganizationalUnits(
        
    );


    /**
     * POST /organizationalUnits/{organizationalUnitId}/sync : Synchronize the specified OrganizationalUnit and Keycloak entities
     * Synchronize the specified OrganizationalUnit and Keycloak entities (group, roles and role policies) with each other.
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "syncOrganizationalUnit",
        summary = "Synchronize the specified OrganizationalUnit and Keycloak entities",
        description = "Synchronize the specified OrganizationalUnit and Keycloak entities (group, roles and role policies) with each other.",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/organizationalUnits/{organizationalUnitId}/sync"
    )
    
    ResponseEntity<Void> syncOrganizationalUnit(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId
    );


    /**
     * PUT /organizationalUnits/{organizationalUnitId} : Modify organizationalUnit information
     * Modify organizationalUnit information
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @param inputData organizationUnitMetadata (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "updateOrganizationalUnit",
        summary = "Modify organizationalUnit information",
        description = "Modify organizationalUnit information",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/organizationalUnits/{organizationalUnitId}",
        consumes = { "application/json" }
    )
    
    ResponseEntity<Void> updateOrganizationalUnit(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId,
        @Parameter(name = "inputData", description = "organizationUnitMetadata", required = true) @Valid @RequestBody OrganizationalUnitInputType inputData
    );


    /**
     * PUT /organizationalUnits/{organizationalUnitId}/role-delegates : Update role delegates for the selected Organizational Unit
     * Add role delegates for the selected Organizational Unit
     *
     * @param organizationalUnitId organizationalUnitId (required)
     * @param organizationalUnitData data (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "updateRoleDelegates",
        summary = "Update role delegates for the selected Organizational Unit",
        description = "Add role delegates for the selected Organizational Unit",
        tags = { "access-control" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/organizationalUnits/{organizationalUnitId}/role-delegates",
        consumes = { "application/json" }
    )
    
    ResponseEntity<Void> updateRoleDelegates(
        @Parameter(name = "organizationalUnitId", description = "organizationalUnitId", required = true, in = ParameterIn.PATH) @PathVariable("organizationalUnitId") String organizationalUnitId,
        @Parameter(name = "organizationalUnitData", description = "data", required = true) @Valid @RequestBody List<GroupAdminRolesPUTInputType> organizationalUnitData
    );

}
