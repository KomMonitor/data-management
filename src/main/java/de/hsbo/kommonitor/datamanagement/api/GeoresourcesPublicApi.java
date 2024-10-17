/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import java.math.BigDecimal;
import de.hsbo.kommonitor.datamanagement.model.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.ResourceFilterType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T15:25:06.709590100+02:00[Europe/Berlin]")
@Validated
@Tag(name = "georesources-public", description = "the public Georesources API")
public interface GeoresourcesPublicApi {

    /**
     * POST /public/georesources/filter : Filter public georesources
     * Filter public georesource datasets according to the specified filter
     *
     * @param resourceFilterType filter data (required)
     * @return OK (status code 200)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Invalid input (status code 405)
     */
    @Operation(
        operationId = "filterPublicGeoresources",
        summary = "Filter public georesources",
        description = "Filter public georesource datasets according to the specified filter",
        tags = { "georesources-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GeoresourceOverviewType.class)))
            }),
            @ApiResponse(responseCode = "401", description = "API key is missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/public/georesources/filter",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    
    ResponseEntity<List<GeoresourceOverviewType>> filterPublicGeoresources(
        @Parameter(name = "ResourceFilterType", description = "filter data", required = true) @Valid @RequestBody ResourceFilterType resourceFilterType
    );


    /**
     * GET /public/georesources/{georesourceId}/allFeatures : retrieve all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)
     * retrieve all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)
     *
     * @param georesourceId the identifier of the public geo-resource dataset (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification.\&quot; (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getAllPublicGeoresourceFeaturesById",
        summary = "retrieve all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)",
        description = "retrieve all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/allFeatures",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getAllPublicGeoresourceFeaturesById(
        @Parameter(name = "georesourceId", description = "the identifier of the public geo-resource dataset", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.\"", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/georesources/{georesourceId}/allFeatures/without-geometry : retrieve only the properties without geometry of all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)
     * retrieve only the properties without geometry of all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)
     *
     * @param georesourceId georesourceId (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getAllPublicGeoresourceFeaturesByIdWithoutGeometry",
        summary = "retrieve only the properties without geometry of all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)",
        description = "retrieve only the properties without geometry of all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/allFeatures/without-geometry",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getAllPublicGeoresourceFeaturesByIdWithoutGeometry(
        @Parameter(name = "georesourceId", description = "georesourceId", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId
    );


    /**
     * GET /public/georesources/{georesourceId} : retrieve information about available features of the selected public geo-resource dataset
     * retrieve information about available features of the selected public geo-resource dataset
     *
     * @param georesourceId identifier of the public geo-resource dataset (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicGeoresourceById",
        summary = "retrieve information about available features of the selected public geo-resource dataset",
        description = "retrieve information about available features of the selected public geo-resource dataset",
        tags = { "georesources-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = GeoresourceOverviewType.class))
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
        value = "/public/georesources/{georesourceId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<GeoresourceOverviewType> getPublicGeoresourceById(
        @Parameter(name = "georesourceId", description = "identifier of the public geo-resource dataset", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId
    );


    /**
     * GET /public/georesources/{georesourceId}/{year}/{month}/{day} : retrieve the features according to the selected public geo-resource dataset and selected year and month as GeoJSON
     * retrieve the features according to the selected public geo-resource dataset and selected year and month as GeoJSON
     *
     * @param georesourceId identifier of the public geo-resource dataset (required)
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
        operationId = "getPublicGeoresourceByIdAndYearAndMonth",
        summary = "retrieve the features according to the selected public geo-resource dataset and selected year and month as GeoJSON",
        description = "retrieve the features according to the selected public geo-resource dataset and selected year and month as GeoJSON",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/{year}/{month}/{day}",
        produces = { "application/octed-stream" }
    )
    
    ResponseEntity<byte[]> getPublicGeoresourceByIdAndYearAndMonth(
        @Parameter(name = "georesourceId", description = "identifier of the public geo-resource dataset", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId,
        @Parameter(name = "year", description = "year for which datasets shall be queried", required = true, in = ParameterIn.PATH) @PathVariable("year") BigDecimal year,
        @Parameter(name = "month", description = "month for which datasets shall be queried", required = true, in = ParameterIn.PATH) @PathVariable("month") BigDecimal month,
        @Parameter(name = "day", description = "day for which datasets shall be queried", required = true, in = ParameterIn.PATH) @PathVariable("day") BigDecimal day,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/georesources/{georesourceId}/{year}/{month}/{day}/without-geometry : retrieve only the properties without geometry of the features according to the selected public geo-resource dataset and selected year and month as GeoJSON
     * retrieve only the properties without geometry of the features according to the selected public geo-resource dataset and selected year and month as GeoJSON
     *
     * @param georesourceId georesourceId (required)
     * @param year year (required)
     * @param month month (required)
     * @param day day (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicGeoresourceByIdAndYearAndMonthWithoutGeometry",
        summary = "retrieve only the properties without geometry of the features according to the selected public geo-resource dataset and selected year and month as GeoJSON",
        description = "retrieve only the properties without geometry of the features according to the selected public geo-resource dataset and selected year and month as GeoJSON",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/{year}/{month}/{day}/without-geometry",
        produces = { "application/octed-stream" }
    )
    
    ResponseEntity<byte[]> getPublicGeoresourceByIdAndYearAndMonthWithoutGeometry(
        @Parameter(name = "georesourceId", description = "georesourceId", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId,
        @Parameter(name = "year", description = "year", required = true, in = ParameterIn.PATH) @PathVariable("year") BigDecimal year,
        @Parameter(name = "month", description = "month", required = true, in = ParameterIn.PATH) @PathVariable("month") BigDecimal month,
        @Parameter(name = "day", description = "day", required = true, in = ParameterIn.PATH) @PathVariable("day") BigDecimal day
    );


    /**
     * GET /public/georesources/{georesourceId}/schema : retrieve the JSON schema for the selected public geo-resource dataset
     * retrieve the JSON schema for the selected public geo-resource dataset. The JSON schema indicates the property structure of the dataset.
     *
     * @param georesourceId the identifier of the public geo-resource dataset (required)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicGeoresourceSchemaByLevel",
        summary = "retrieve the JSON schema for the selected public geo-resource dataset",
        description = "retrieve the JSON schema for the selected public geo-resource dataset. The JSON schema indicates the property structure of the dataset.",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/schema",
        produces = { "application/json" }
    )
    
    ResponseEntity<String> getPublicGeoresourceSchemaByLevel(
        @Parameter(name = "georesourceId", description = "the identifier of the public geo-resource dataset", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId
    );


    /**
     * GET /public/georesources : retrieve information about available features of different public geo-resource datasets
     * retrieve information about available features of different public geo-resource datasets
     *
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicGeoresources",
        summary = "retrieve information about available features of different public geo-resource datasets",
        description = "retrieve information about available features of different public geo-resource datasets",
        tags = { "georesources-public" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GeoresourceOverviewType.class)))
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
        value = "/public/georesources",
        produces = { "application/json" }
    )
    
    ResponseEntity<List<GeoresourceOverviewType>> getPublicGeoresources(
        
    );


    /**
     * GET /public/georesources/{georesourceId}/singleFeature/{featureId} : retrieve single feature database records for all applicable periods of validity for the selected geo-resource dataset (hence might contain the target feature multiple times if it exists for different periods of validity)
     * retrieve single feature database records for all applicable periods of validity for the selected geo-resource dataset (hence might contain the target feature multiple times if it exists for different periods of validity)
     *
     * @param georesourceId the identifier of the geo-resource dataset (required)
     * @param featureId the identifier of the geo-resource dataset feature (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification. (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSingleGeoresourceFeatureById",
        summary = "retrieve single feature database records for all applicable periods of validity for the selected geo-resource dataset (hence might contain the target feature multiple times if it exists for different periods of validity)",
        description = "retrieve single feature database records for all applicable periods of validity for the selected geo-resource dataset (hence might contain the target feature multiple times if it exists for different periods of validity)",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/singleFeature/{featureId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getPublicSingleGeoresourceFeatureById(
        @Parameter(name = "georesourceId", description = "the identifier of the geo-resource dataset", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId,
        @Parameter(name = "featureId", description = "the identifier of the geo-resource dataset feature", required = true, in = ParameterIn.PATH) @PathVariable("featureId") String featureId,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );


    /**
     * GET /public/georesources/{georesourceId}/singleFeature/{featureId}/singleFeatureRecord/{featureRecordId} : retrieve single feature database record specified by its unique database primary key id
     * retrieve single feature database record specified by its unique database primary key id
     *
     * @param georesourceId the identifier of the geo-resource dataset (required)
     * @param featureId the identifier of the geo-resource dataset feature (required)
     * @param featureRecordId the unique database record identifier of the geo-resource dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity (required)
     * @param simplifyGeometries Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from &#39;weak&#39; to &#39;strong&#39;, while &#39;original&#39; will return original feature geometries without any simplification. (optional, default to original)
     * @return OK (status code 200)
     *         or Invalid status value (status code 400)
     *         or API key is missing or invalid (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     */
    @Operation(
        operationId = "getPublicSingleGeoresourceFeatureRecordById",
        summary = "retrieve single feature database record specified by its unique database primary key id",
        description = "retrieve single feature database record specified by its unique database primary key id",
        tags = { "georesources-public" },
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
        value = "/public/georesources/{georesourceId}/singleFeature/{featureId}/singleFeatureRecord/{featureRecordId}",
        produces = { "application/json" }
    )
    
    ResponseEntity<byte[]> getPublicSingleGeoresourceFeatureRecordById(
        @Parameter(name = "georesourceId", description = "the identifier of the geo-resource dataset", required = true, in = ParameterIn.PATH) @PathVariable("georesourceId") String georesourceId,
        @Parameter(name = "featureId", description = "the identifier of the geo-resource dataset feature", required = true, in = ParameterIn.PATH) @PathVariable("featureId") String featureId,
        @Parameter(name = "featureRecordId", description = "the unique database record identifier of the geo-resource dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity", required = true, in = ParameterIn.PATH) @PathVariable("featureRecordId") String featureRecordId,
        @Parameter(name = "simplifyGeometries", description = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries
    );

}
