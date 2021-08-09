/**
 * NOTE: This class is auto generated by the swagger code generator program (unset).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePUTInputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import javax.servlet.ServletRequest;

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-04-05T10:56:22.201+02:00")

@Api(value = "Georesources", description = "the Georesources API")
public interface GeoresourcesApi {

    @ApiOperation(value = "Add a new geo-resource", nickname = "addGeoresourceAsBody", notes = "Add/Register a geo-resource dataset for a certain period of time", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/georesources",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<GeoresourceOverviewType> addGeoresourceAsBody(@ApiParam(value = "feature data" ,required=true )   @RequestBody GeoresourcePOSTInputType featureData);

	@ApiOperation(value = "Delete all features/contents of the selected geo-resource dataset", nickname = "deleteAllGeoresourceFeaturesById", notes = "Delete all features/contents of the selected geo-resource dataset", authorizations = {
			@Authorization(value = "basicAuth") }, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "API key is missing or invalid") })
	@RequestMapping(value = "/georesources/{georesourceId}/allFeatures", method = RequestMethod.DELETE)
	ResponseEntity deleteAllGeoresourceFeaturesById(
			@ApiParam(value = "the identifier of the geo-resource dataset", required = true) @PathVariable("georesourceId") String georesourceId);

    @ApiOperation(value = "Delete the features/contents of the selected geo-resource dataset", nickname = "deleteGeoresourceById", notes = "Delete the features/contents of the selected geo-resource dataset", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources/{georesourceId}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteGeoresourceById(@ApiParam(value = "identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId);

    @ApiOperation(value = "Delete the features/contents of the selected geo-resource dataset, selected by year and month", nickname = "deleteGeoresourceByIdAndYearAndMonth", notes = "Delete the features/contents of the selected geo-resource dataset, selected by year and month", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources/{georesourceId}/{year}/{month}/{day}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteGeoresourceByIdAndYearAndMonth(@ApiParam(value = "identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId,@ApiParam(value = "year for which datasets shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which datasets shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day);

    @ApiOperation(value = "retrieve all feature entries for all applicable periods of validity for the selected geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)", nickname = "getAllGeoresourceFeaturesById", notes = "retrieve all feature entries for all applicable periods of validity for the selected geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)", response = String.class, authorizations = {
            @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources/{georesourceId}/allFeatures",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<byte[]> getAllGeoresourceFeaturesById(@ApiParam(value = "the identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId,@ApiParam(value = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", allowableValues = "original, weak, medium, strong", defaultValue = "original")  @RequestParam(value = "simplifyGeometries", required = false, defaultValue="original") String simplifyGeometries, Principal principal);

    @ApiOperation(value = "retrieve information about available features of different geo-resource datasets", nickname = "getGeoresources", notes = "retrieve information about available features of different geo-resource datasets", response = GeoresourceOverviewType.class, responseContainer = "array", authorizations = {
            @Authorization(value = "basicAuth") }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = GeoresourceOverviewType.class, responseContainer = "array"),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources", produces = { "application/json" }, method = RequestMethod.GET)
    ResponseEntity<List<GeoresourceOverviewType>> getGeoresources(Principal principal);

    @ApiOperation(value = "retrieve information about available features of the selected geo-resource dataset", nickname = "getGeoresourceById", notes = "retrieve information about available features of the selected geo-resource dataset", response = GeoresourceOverviewType.class, authorizations = {
            @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = GeoresourceOverviewType.class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources/{georesourceId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<GeoresourceOverviewType> getGeoresourceById(@ApiParam(value = "identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId, Principal principal);

    @ApiOperation(value = "retrieve the features according to the selected geo-resource dataset and selected year and month as GeoJSON", nickname = "getGeoresourceByIdAndYearAndMonth", notes = "retrieve the features according to the selected geo-resource dataset and selected year and month as GeoJSON", response = byte[].class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = byte[].class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources/{georesourceId}/{year}/{month}/{day}",
        produces = { "application/octed-stream" }, 
        method = RequestMethod.GET)
    ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonth(@ApiParam(value = "identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId,@ApiParam(value = "year for which datasets shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which datasets shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day,@ApiParam(value = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", allowableValues = "original, weak, medium, strong", defaultValue = "original")  @RequestParam(value = "simplifyGeometries", required = false, defaultValue="original") String simplifyGeometries, Principal principal);

    @ApiOperation(value = "retrieve only the properties without geometry of all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)", nickname = "getAllGeoresourceFeaturesByIdWithoutGeometry", notes = "retrieve only the properties without geometry of all feature entries for all applicable periods of validity for the selected public geo-resource dataset (hence might contain each feature multiple times if they exist for different periods of validity)", response = String.class, authorizations = {
            @Authorization(value = "kommonitor-data-access_oauth", scopes = {
                
                })
        }, tags={ "georecources-controller", })
        @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
        @RequestMapping(value = "/management/georesources/{georesourceId}/allFeatures/without-geometry",
            produces = { "application/json" }, 
            method = RequestMethod.GET)
        ResponseEntity<byte[]> getAllGeoresourceFeaturesByIdWithoutGeometry(@ApiParam(value = "georesourceId",required=true) @PathVariable("georesourceId") String georesourceId, Principal principal);

    @ApiOperation(value = "retrieve only the properties without geometry of the features according to the selected public geo-resource dataset and selected year and month as GeoJSON", nickname = "getGeoresourceByIdAndYearAndMonthWithoutGeometry", notes = "retrieve only the properties without geometry of the features according to the selected public geo-resource dataset and selected year and month as GeoJSON", response = byte[].class, authorizations = {
            @Authorization(value = "kommonitor-data-access_oauth", scopes = {
                
                })
        }, tags={ "georecources-controller", })
        @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "OK", response = byte[].class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
        @RequestMapping(value = "/management/georesources/{georesourceId}/{year}/{month}/{day}/without-geometry",
            produces = { "application/octed-stream" }, 
            method = RequestMethod.GET)
        ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonthWithoutGeometry(@ApiParam(value = "day",required=true) @PathVariable("day") BigDecimal day,@ApiParam(value = "georesourceId",required=true) @PathVariable("georesourceId") String georesourceId,@ApiParam(value = "month",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "year",required=true) @PathVariable("year") BigDecimal year, Principal principal);

    
    @ApiOperation(value = "retrieve the JSON schema for the selected geo-resource dataset", nickname = "getGeoresourceSchemaByLevel", notes = "retrieve the JSON schema for the selected geo-resource dataset. The JSON schema indicates the property structure of the dataset.", response = String.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = String.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/georesources/{georesourceId}/schema",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> getGeoresourceSchemaByLevel(@ApiParam(value = "the identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId, Principal principal);


    @ApiOperation(value = "Modify/Update the features of the selected geo-resource dataset", nickname = "updateGeoresourceAsBody", notes = "Modify/Update the features of the selected geo-resource dataset.  The interface expects a full upload of all geometries for the spatial unit. Internally, those geometries are compared to the existing ones to mark 'old' geometries that are no longer in use as outdated. Hence, each geometric object is only persisted once and its use is controlled by time validity marks.", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/georesources/{georesourceId}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity updateGeoresourceAsBody(@ApiParam(value = "identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId,@ApiParam(value = "feature data" ,required=true )   @RequestBody GeoresourcePUTInputType featureData);


    @ApiOperation(value = "Modify/Update the metadata of the selected geo-resource dataset", nickname = "updateGeoresourceMetadataAsBody", notes = "Modify/Update the metadata of the selected geo-resource dataset. This replaces the formerly stored metadata.", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/georesources/{georesourceId}",
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity updateGeoresourceMetadataAsBody(@ApiParam(value = "identifier of the geo-resource dataset",required=true) @PathVariable("georesourceId") String georesourceId,@ApiParam(value = "metadata input" ,required=true )   @RequestBody GeoresourcePATCHInputType metadata);

}
