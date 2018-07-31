/**
 * NOTE: This class is auto generated by the swagger code generator program (unset).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPUTInputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-31T10:37:09.246+02:00")

@Api(value = "SpatialUnits", description = "the SpatialUnits API")
public interface SpatialUnitsApi {

    @ApiOperation(value = "Add a new spatial-unit", nickname = "addSpatialUnitAsBody", notes = "Add/Register a spatial unit for a certain period of time", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/spatial-units",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity addSpatialUnitAsBody(@ApiParam(value = "feature data" ,required=true )   @RequestBody SpatialUnitPOSTInputType featureData);


    @ApiOperation(value = "Delete the features/contents of the selected spatial-unit", nickname = "deleteSpatialUnitById", notes = "Delete the features/contents of the selected spatial-unit", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteSpatialUnitById(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId);


    @ApiOperation(value = "Delete the features/contents of the selected spatial-unit, year and month", nickname = "deleteSpatialUnitByIdAndYearAndMonth", notes = "Delete the features/contents of the selected spatial-unit, year and month", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}/{year}/{month}/{day}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteSpatialUnitByIdAndYearAndMonth(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "year for which datasets shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which datasets shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day);
    

    @ApiOperation(value = "retrieve information about available features of different spatial units/levels", nickname = "getSpatialUnits", notes = "retrieve information about available features of different spatial units/levels", response = SpatialUnitOverviewType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SpatialUnitOverviewType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/spatial-units",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<SpatialUnitOverviewType>> getSpatialUnits();


    @ApiOperation(value = "retrieve information about available features of the selected spatial unit/level", nickname = "getSpatialUnitsById", notes = "retrieve information about available features of the selected spatial unit/level", response = SpatialUnitOverviewType.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SpatialUnitOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<SpatialUnitOverviewType> getSpatialUnitsById(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId);


    @ApiOperation(value = "retrieve the features according to the selected spatial unit/level and selected year and month as GeoJSON", nickname = "getSpatialUnitsByIdAndYearAndMonth", notes = "retrieve the features according to the selected spatial unit/level and selected year and month as GeoJSON", response = byte[].class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = byte[].class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}/{year}/{month}/{day}",
        produces = { "application/octed-stream" }, 
        method = RequestMethod.GET)
    ResponseEntity<byte[]> getSpatialUnitsByIdAndYearAndMonth(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "year for which datasets shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which datasets shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day);


    @ApiOperation(value = "retrieve the JSON schema for the selected spatial unit/level", nickname = "getSpatialUnitsSchemaById", notes = "retrieve the JSON schema for the selected spatial unit/level. The JSON schema indicates the property structure of the dataset.", response = String.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = String.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}/schema",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> getSpatialUnitsSchemaById(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId);


    @ApiOperation(value = "Modify/Update the features of the selected spatial-unit", nickname = "updateSpatialUnitAsBody", notes = "Modify/Update the features of the selected spatial-unit. The interface expects a full upload of all geometries for the spatial unit. Internally, those geometries are compared to the existing ones to mark 'old' geometries that are no longer in use as outdated. Hence, each geometric object is only persisted once and its use is controlled by time validity marks.", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity updateSpatialUnitAsBody(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "feature data" ,required=true )   @RequestBody SpatialUnitPUTInputType featureData);


    @ApiOperation(value = "Modify/Update the metadata of the selected spatial-unit", nickname = "updateSpatialUnitMetadataAsBody", notes = "Modify/Update the metadata of the selected spatial-unit. This replaces the formerly stored metadata.", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/spatial-units/{spatialUnitId}",
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity updateSpatialUnitMetadataAsBody(@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "metadata input" ,required=true )   @RequestBody SpatialUnitPATCHInputType metadata);

}
