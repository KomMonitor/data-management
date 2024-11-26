/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import de.hsbo.kommonitor.datamanagement.model.UserInfoInputType;
import de.hsbo.kommonitor.datamanagement.model.UserInfoOverviewType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-26T09:44:13.410550500+01:00[Europe/Berlin]")
@Validated
@Tag(name = "user-info", description = "the user-info API")
public interface UserInfoApi {

    /**
     * POST /userInfos : Register new additional information about a user
     * Register new additional information about a user
     *
     * @param userInfoInputType user info data (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "addUserInfo",
        summary = "Register new additional information about a user",
        description = "Register new additional information about a user",
        tags = { "user-info" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoOverviewType.class))
            }),
            @ApiResponse(responseCode = "201", description = "Created", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoOverviewType.class))
            }),
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
        value = "/userInfos",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    ResponseEntity<UserInfoOverviewType> addUserInfo(
        @Parameter(name = "UserInfoInputType", description = "user info data", required = true) @Valid @RequestBody UserInfoInputType userInfoInputType
    );


    /**
     * GET /userInfos/{userId} : Retrieve additional information about a user
     * Retrieve additional information about a user
     *
     * @param userId User info ID (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getUserInfoById",
        summary = "Retrieve additional information about a user",
        description = "Retrieve additional information about a user",
        tags = { "user-info" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoOverviewType.class))
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
        value = "/userInfos/{userId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<UserInfoOverviewType> getUserInfoById(
        @Parameter(name = "userId", description = "User info ID", required = true, in = ParameterIn.PATH) @PathVariable("userId") String userId
    );


    /**
     * GET /userInfos/user : Retrieve additional information about the current user
     * Retrieve additional information about the current user
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getUserInfoForUser",
        summary = "Retrieve additional information about the current user",
        description = "Retrieve additional information about the current user",
        tags = { "user-info" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoOverviewType.class))
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
        value = "/userInfos/user",
        produces = { "application/json" }
    )
    
    ResponseEntity<UserInfoOverviewType> getUserInfoForUser(
        
    );


    /**
     * GET /userInfos : Retrieve additional information about registered users such as favourite lists
     * Retrieve additional information about registered users such as favourite lists
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getUserInfos",
        summary = "Retrieve additional information about registered users such as favourite lists",
        description = "Retrieve additional information about registered users such as favourite lists",
        tags = { "user-info" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserInfoOverviewType.class)))
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
        value = "/userInfos",
        produces = { "application/json" }
    )
    
    ResponseEntity<List<UserInfoOverviewType>> getUserInfos(
        
    );


    /**
     * PATCH /userInfos/{userId} : Modify/Update additional information of a user
     * Modify/Update additional information of a user
     *
     * @param userId identifier of the user information (required)
     * @param userInfoData metadauser info (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or No Content (status code 204)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "updateUserInfo",
        summary = "Modify/Update additional information of a user",
        description = "Modify/Update additional information of a user",
        tags = { "user-info" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoOverviewType.class))
            }),
            @ApiResponse(responseCode = "201", description = "Created", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoOverviewType.class))
            }),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
        },
        security = {
            @SecurityRequirement(name = "kommonitor-data-access_oauth", scopes={  })
        }
    )
    @RequestMapping(
        method = RequestMethod.PATCH,
        value = "/userInfos/{userId}",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    ResponseEntity<UserInfoOverviewType> updateUserInfo(
        @Parameter(name = "userId", description = "identifier of the user information", required = true, in = ParameterIn.PATH) @PathVariable("userId") String userId,
        @Parameter(name = "userInfoData", description = "metadauser info", required = true) @Valid @RequestBody UserInfoInputType userInfoData
    );

}