/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import java.math.BigDecimal;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitOverviewType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:10.685766091+01:00[Europe/Berlin]")
@Validated
@Tag(name = "spatial-units-public", description = "the public SpatialUnits API")
public interface SpatialUnitsPublicApi {

    /**
     * GET /public/spatial-units/{spatialUnitId}/allFeatures : retrieve all feature entries for all applicable periods of validity for the selected spatial unit/level (hence might contain each feature multiple times if they exist for different periods of validity)
     * retrieve all feature entries for all applicable periods of validity for the selected spatial unit/level (hence might contain each feature multiple times if they exist for different periods of validity)
     *
     * @param spatialUnitId the unique identifier of the spatial level (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification. (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getAllPublicSpatialUnitFeaturesById",
        summary = "retrieve all feature entries for all applicable periods of validity for the selected spatial unit/level (hence might contain each feature multiple times if they exist for different periods of validity)",
        description = "retrieve all feature entries for all applicable periods of validity for the selected spatial unit/level (hence might contain each feature multiple times if they exist for different periods of validity)",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = byte[].class))
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
        value = "/public/spatial-units/{spatialUnitId}/allFeatures",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getAllPublicSpatialUnitFeaturesById(
        @Parameter(name = "spatialUnitId", description = "the unique identifier of the spatial level", required = true, in = ParameterIn.PATH) @PathVariable("spatialUnitId") String spatialUnitId,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/spatial-units/{spatialUnitId}/singleFeature/{featureId} : retrieve single feature database records for all applicable periods of validity for the selected spatial-unit dataset (hence might contain the target feature multiple times if it exists for different periods of validity)
     * retrieve single feature database records for all applicable periods of validity for the selected spatial-unit dataset (hence might contain the target feature multiple times if it exists for different periods of validity)
     *
     * @param featureId the identifier of the spatial-unit dataset feature (required)
     * @param spatialUnitId the identifier of the spatial-unit dataset (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification. (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSingleSpatialUnitFeatureById",
        summary = "retrieve single feature database records for all applicable periods of validity for the selected spatial-unit dataset (hence might contain the target feature multiple times if it exists for different periods of validity)",
        description = "retrieve single feature database records for all applicable periods of validity for the selected spatial-unit dataset (hence might contain the target feature multiple times if it exists for different periods of validity)",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = byte[].class))
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
        value = "/public/spatial-units/{spatialUnitId}/singleFeature/{featureId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getPublicSingleSpatialUnitFeatureById(
        @Parameter(name = "featureId", description = "the identifier of the spatial-unit dataset feature", required = true, in = ParameterIn.PATH) @PathVariable("featureId") String featureId,
        @Parameter(name = "spatialUnitId", description = "the identifier of the spatial-unit dataset", required = true, in = ParameterIn.PATH) @PathVariable("spatialUnitId") String spatialUnitId,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/spatial-units/{spatialUnitId}/singleFeature/{featureId}/singleFeatureRecord/{featureRecordId} : retrieve single feature database record specified by its unique database primary key id
     * retrieve single feature database record specified by its unique database primary key id
     *
     * @param featureId the identifier of the spatial-unit dataset feature (required)
     * @param featureRecordId the unique database record identifier of the spatial-unit dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity (required)
     * @param spatialUnitId the identifier of the spatial-unit dataset (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification. (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSingleSpatialUnitFeatureRecordById",
        summary = "retrieve single feature database record specified by its unique database primary key id",
        description = "retrieve single feature database record specified by its unique database primary key id",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = byte[].class))
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
        value = "/public/spatial-units/{spatialUnitId}/singleFeature/{featureId}/singleFeatureRecord/{featureRecordId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getPublicSingleSpatialUnitFeatureRecordById(
        @Parameter(name = "featureId", description = "the identifier of the spatial-unit dataset feature", required = true, in = ParameterIn.PATH) @PathVariable("featureId") String featureId,
        @Parameter(name = "featureRecordId", description = "the unique database record identifier of the spatial-unit dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity", required = true, in = ParameterIn.PATH) @PathVariable("featureRecordId") String featureRecordId,
        @Parameter(name = "spatialUnitId", description = "the identifier of the spatial-unit dataset", required = true, in = ParameterIn.PATH) @PathVariable("spatialUnitId") String spatialUnitId,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/spatial-units : retrieve information about available features of different spatial units/levels
     * retrieve information about available features of different spatial units/levels
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSpatialUnits",
        summary = "retrieve information about available features of different spatial units/levels",
        description = "retrieve information about available features of different spatial units/levels",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SpatialUnitOverviewType.class)))
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
        value = "/public/spatial-units",
        produces = { "application/json" }
    )
    
    ResponseEntity<List<SpatialUnitOverviewType>> getPublicSpatialUnits(
        
    );


    /**
     * GET /public/spatial-units/{spatialUnitId} : retrieve information about available features of the selected spatial unit/level
     * retrieve information about available features of the selected spatial unit/level
     *
     * @param spatialUnitId the unique identifier of the spatial level (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSpatialUnitsById",
        summary = "retrieve information about available features of the selected spatial unit/level",
        description = "retrieve information about available features of the selected spatial unit/level",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SpatialUnitOverviewType.class))
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
        value = "/public/spatial-units/{spatialUnitId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<SpatialUnitOverviewType> getPublicSpatialUnitsById(
        @Parameter(name = "spatialUnitId", description = "the unique identifier of the spatial level", required = true, in = ParameterIn.PATH) @PathVariable("spatialUnitId") String spatialUnitId
    );


    /**
     * GET /public/spatial-units/{spatialUnitId}/{year}/{month}/{day} : retrieve the features according to the selected spatial unit/level and selected year and month as GeoJSON
     * retrieve the features according to the selected spatial unit/level and selected year and month as GeoJSON
     *
     * @param spatialUnitId the unique identifier of the spatial level (required)
     * @param year year for which datasets shall be queried (required)
     * @param month month for which datasets shall be queried (required)
     * @param day day for which datasets shall be queried (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification. (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSpatialUnitsByIdAndYearAndMonth",
        summary = "retrieve the features according to the selected spatial unit/level and selected year and month as GeoJSON",
        description = "retrieve the features according to the selected spatial unit/level and selected year and month as GeoJSON",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/octed-stream", schema = @Schema(implementation = byte[].class))
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
        value = "/public/spatial-units/{spatialUnitId}/{year}/{month}/{day}",
        produces = { "application/octed-stream" }
    )
    
    ResponseEntity<byte[]> getPublicSpatialUnitsByIdAndYearAndMonth(
        @Parameter(name = "spatialUnitId", description = "the unique identifier of the spatial level", required = true, in = ParameterIn.PATH) @PathVariable("spatialUnitId") String spatialUnitId,
        @Parameter(name = "year", description = "year for which datasets shall be queried", required = true, in = ParameterIn.PATH) @PathVariable("year") BigDecimal year,
        @Parameter(name = "month", description = "month for which datasets shall be queried", required = true, in = ParameterIn.PATH) @PathVariable("month") BigDecimal month,
        @Parameter(name = "day", description = "day for which datasets shall be queried", required = true, in = ParameterIn.PATH) @PathVariable("day") BigDecimal day,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/spatial-units/{spatialUnitId}/schema : retrieve the JSON schema for the selected spatial unit/level
     * retrieve the JSON schema for the selected spatial unit/level. The JSON schema indicates the property structure of the dataset.
     *
     * @param spatialUnitId the unique identifier of the spatial level (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSpatialUnitsSchemaById",
        summary = "retrieve the JSON schema for the selected spatial unit/level",
        description = "retrieve the JSON schema for the selected spatial unit/level. The JSON schema indicates the property structure of the dataset.",
        tags = { "spatial-units-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
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
        value = "/public/spatial-units/{spatialUnitId}/schema",
        produces = { "application/json" }
    )
    
    ResponseEntity<String> getPublicSpatialUnitsSchemaById(
        @Parameter(name = "spatialUnitId", description = "the unique identifier of the spatial level", required = true, in = ParameterIn.PATH) @PathVariable("spatialUnitId") String spatialUnitId
    );

}
