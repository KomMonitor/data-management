/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import de.hsbo.kommonitor.datamanagement.model.ProcessScriptOverviewType;
import de.hsbo.kommonitor.datamanagement.model.ProcessScriptPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.ProcessScriptPUTInputType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-03-08T11:42:47.407280514+01:00[Europe/Berlin]")
@Validated
@Tag(name = "process-scripts", description = "the ProcessScripts API")
public interface ProcessScriptsApi {

    /**
     * POST /process-scripts : Register a new process script
     * Register a process script associated to a certain indicator
     *
     * @param processScriptData details necessary to register the process script (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "addProcessScriptAsBody",
        summary = "Register a new process script",
        description = "Register a process script associated to a certain indicator",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessScriptOverviewType.class))
            }),
            @ApiResponse(responseCode = "201", description = "Created", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessScriptOverviewType.class))
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
        value = "/process-scripts",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    ResponseEntity<ProcessScriptOverviewType> addProcessScriptAsBody(
        @Parameter(name = "processScriptData", description = "details necessary to register the process script", required = true) @Valid @RequestBody ProcessScriptPOSTInputType processScriptData
    );


    /**
     * DELETE /process-scripts/usingIndicatorId/{indicatorId} : Delete the process script
     * Delete the process script associated to the specified indicator
     *
     * @param indicatorId unique identifier of the selected indicator dataset (required)
     * @return OK (status code 200)
     *         or No Content (status code 204)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     */
    @Operation(
        operationId = "deleteProcessScript",
        summary = "Delete the process script",
        description = "Delete the process script associated to the specified indicator",
        tags = { "process-scripts" },
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
        value = "/process-scripts/usingIndicatorId/{indicatorId}"
    )
    
    ResponseEntity<Void> deleteProcessScript(
        @Parameter(name = "indicatorId", description = "unique identifier of the selected indicator dataset", required = true, in = ParameterIn.PATH) @PathVariable("indicatorId") String indicatorId
    );


    /**
     * DELETE /process-scripts/{scriptId} : Delete the process script
     * Delete the process script associated to the specified scriptId
     *
     * @param scriptId unique identifier of the selected script (required)
     * @return OK (status code 200)
     *         or No Content (status code 204)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     */
    @Operation(
        operationId = "deleteProcessScriptByScriptId",
        summary = "Delete the process script",
        description = "Delete the process script associated to the specified scriptId",
        tags = { "process-scripts" },
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
        value = "/process-scripts/{scriptId}"
    )
    
    ResponseEntity<Void> deleteProcessScriptByScriptId(
        @Parameter(name = "scriptId", description = "unique identifier of the selected script", required = true, in = ParameterIn.PATH) @PathVariable("scriptId") String scriptId
    );


    /**
     * GET /process-scripts/{scriptId}/scriptCode : retrieve the process script code associated to a certain indicator as JavaScript file
     * retrieve the process script code associated to a certain indicator as JavaScript file
     *
     * @param scriptId unique identifier of the selected script (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getProcessScriptCode",
        summary = "retrieve the process script code associated to a certain indicator as JavaScript file",
        description = "retrieve the process script code associated to a certain indicator as JavaScript file",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/javascript", schema = @Schema(implementation = byte[].class))
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
        value = "/process-scripts/{scriptId}/scriptCode",
        produces = { "application/javascript" }
    )
    
    ResponseEntity<byte[]> getProcessScriptCode(
        @Parameter(name = "scriptId", description = "unique identifier of the selected script", required = true, in = ParameterIn.PATH) @PathVariable("scriptId") String scriptId
    );


    /**
     * GET /process-scripts/usingIndicatorId/{indicatorId}/scriptCode : retrieve the process script code associated to a certain indicator as JavaScript file
     * retrieve the process script code associated to a certain indicator as JavaScript file
     *
     * @param indicatorId unique identifier of the selected indicator dataset (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getProcessScriptCodeForIndicator",
        summary = "retrieve the process script code associated to a certain indicator as JavaScript file",
        description = "retrieve the process script code associated to a certain indicator as JavaScript file",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/javascript", schema = @Schema(implementation = byte[].class))
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
        value = "/process-scripts/usingIndicatorId/{indicatorId}/scriptCode",
        produces = { "application/javascript" }
    )
    
    ResponseEntity<byte[]> getProcessScriptCodeForIndicator(
        @Parameter(name = "indicatorId", description = "unique identifier of the selected indicator dataset", required = true, in = ParameterIn.PATH) @PathVariable("indicatorId") String indicatorId
    );


    /**
     * GET /process-scripts/usingIndicatorId/{indicatorId} : retrieve information about the associated process script for a certain indicator
     * retrieve information about the associated process script for a certain indicator
     *
     * @param indicatorId unique identifier of the selected indicator dataset (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getProcessScriptForIndicator",
        summary = "retrieve information about the associated process script for a certain indicator",
        description = "retrieve information about the associated process script for a certain indicator",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessScriptOverviewType.class))
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
        value = "/process-scripts/usingIndicatorId/{indicatorId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<ProcessScriptOverviewType> getProcessScriptForIndicator(
        @Parameter(name = "indicatorId", description = "unique identifier of the selected indicator dataset", required = true, in = ParameterIn.PATH) @PathVariable("indicatorId") String indicatorId
    );


    /**
     * GET /process-scripts/{scriptId} : retrieve information about the associated process script for a certain scriptId
     * retrieve information about the associated process script for a certain scriptId
     *
     * @param scriptId unique identifier of the selected script (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getProcessScriptForScriptId",
        summary = "retrieve information about the associated process script for a certain scriptId",
        description = "retrieve information about the associated process script for a certain scriptId",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessScriptOverviewType.class))
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
        value = "/process-scripts/{scriptId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<ProcessScriptOverviewType> getProcessScriptForScriptId(
        @Parameter(name = "scriptId", description = "unique identifier of the selected script", required = true, in = ParameterIn.PATH) @PathVariable("scriptId") String scriptId
    );


    /**
     * GET /process-scripts/template : retrieve an empty script template, that defines how to implement process scripts for KomMonitor as JavaScript file.
     * retrieve an empty script template, that defines how to implement process scripts for KomMonitor. The script works as a template for a NodeJS module. Hence, it predefines required methods that are called by the executing processing engine (a NodeJS runtimne environment). As a script developer, those predefined methods have to be implemented. The template contains detailed documentation on how to implement those methods.
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getProcessScriptTemplate",
        summary = "retrieve an empty script template, that defines how to implement process scripts for KomMonitor as JavaScript file.",
        description = "retrieve an empty script template, that defines how to implement process scripts for KomMonitor. The script works as a template for a NodeJS module. Hence, it predefines required methods that are called by the executing processing engine (a NodeJS runtimne environment). As a script developer, those predefined methods have to be implemented. The template contains detailed documentation on how to implement those methods.",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/javascript", schema = @Schema(implementation = byte[].class))
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
        value = "/process-scripts/template",
        produces = { "application/javascript" }
    )
    
    ResponseEntity<byte[]> getProcessScriptTemplate(
        
    );


    /**
     * GET /process-scripts : retrieve information about available process scripts
     * retrieve information about available process scripts
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getProcessScripts",
        summary = "retrieve information about available process scripts",
        description = "retrieve information about available process scripts",
        tags = { "process-scripts" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProcessScriptOverviewType.class)))
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
        value = "/process-scripts",
        produces = { "application/json" }
    )
    
    ResponseEntity<List<ProcessScriptOverviewType>> getProcessScripts(
        
    );


    /**
     * PUT /process-scripts/usingIndicatorId/{indicatorId} : Modify/Update an existing process script
     * Modify/Update an existing process script associated to a certain indicator
     *
     * @param indicatorId unique identifier of the selected indicator dataset (required)
     * @param processScriptData details necessary to modify the process script (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "updateProcessScriptAsBody",
        summary = "Modify/Update an existing process script",
        description = "Modify/Update an existing process script associated to a certain indicator",
        tags = { "process-scripts" },
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
        value = "/process-scripts/usingIndicatorId/{indicatorId}",
        consumes = { "application/json" }
    )
    
    ResponseEntity<Void> updateProcessScriptAsBody(
        @Parameter(name = "indicatorId", description = "unique identifier of the selected indicator dataset", required = true, in = ParameterIn.PATH) @PathVariable("indicatorId") String indicatorId,
        @Parameter(name = "processScriptData", description = "details necessary to modify the process script", required = true) @Valid @RequestBody ProcessScriptPUTInputType processScriptData
    );


    /**
     * PUT /process-scripts/{scriptId} : Modify/Update an existing process script
     * Modify/Update an existing process script associated to a certain scriptId
     *
     * @param scriptId unique identifier of the selected script (required)
     * @param processScriptData details necessary to modify the process script (required)
     * @return OK (status code 200)
     *         or Created (status code 201)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "updateProcessScriptAsBodyByScriptId",
        summary = "Modify/Update an existing process script",
        description = "Modify/Update an existing process script associated to a certain scriptId",
        tags = { "process-scripts" },
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
        value = "/process-scripts/{scriptId}",
        consumes = { "application/json" }
    )
    
    ResponseEntity<Void> updateProcessScriptAsBodyByScriptId(
        @Parameter(name = "scriptId", description = "unique identifier of the selected script", required = true, in = ParameterIn.PATH) @PathVariable("scriptId") String scriptId,
        @Parameter(name = "processScriptData", description = "details necessary to modify the process script", required = true) @Valid @RequestBody ProcessScriptPUTInputType processScriptData
    );

}
