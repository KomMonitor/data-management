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
import org.springframework.web.bind.annotation.RequestParam;

import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPropertiesWithoutGeomType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-04-05T10:56:22.201+02:00")

@Api(value = "Indicators", description = "the Indicators API")
public interface IndicatorsApi {

    @ApiOperation(value = "Add a new indicator dataset", nickname = "addIndicatorAsBody", notes = "Add/Register an indicator dataset for a certain period of time and spatial unit/level", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/indicators",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity addIndicatorAsBody(@ApiParam(value = "indicator data" ,required=true )   @RequestBody IndicatorPOSTInputType indicatorData);


    @ApiOperation(value = "Delete the features/contents of the selected indicator dataset", nickname = "deleteIndicatorById", notes = "Delete the features/contents of the selected indicator dataset", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteIndicatorById(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId);

	@ApiOperation(value = "Delete the features/contents of the selected indicator dataset for the selected spatial unit", nickname = "deleteIndicatorByIdAndSpatialUnitId", notes = "Delete the features/contents of the selected indicator dataset for the selected spatial unit", authorizations = {
			@Authorization(value = "basicAuth") }, tags = {})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "API key is missing or invalid") })
	@RequestMapping(value = "/indicators/{indicatorId}/{spatialUnitId}", method = RequestMethod.DELETE)
	ResponseEntity deleteIndicatorByIdAndSpatialUnitId(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId) throws Exception;

    @ApiOperation(value = "Delete the features/contents of the selected indicator dataset, selected by year and month", nickname = "deleteIndicatorByIdAndYearAndMonth", notes = "Delete the features/contents of the selected indicator dataset, selected by year and month", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}/{spatialUnitId}/{year}/{month}/{day}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteIndicatorByIdAndYearAndMonth(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "year for which the indicator shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which the indicator shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day) throws Exception;


    @ApiOperation(value = "retrieve information about the selected indicator", nickname = "getIndicatorById", notes = "retrieve information about the selected indicator", response = IndicatorOverviewType.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = IndicatorOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<IndicatorOverviewType> getIndicatorById(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId);


    @ApiOperation(value = "retrieve the indicator for the selected spatial unit as GeoJSON", nickname = "getIndicatorBySpatialUnitIdAndId", notes = "retrieve the indicator for the selected spatial unit as GeoJSON", response = byte[].class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = byte[].class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}/{spatialUnitId}",
        produces = { "application/octed-stream" }, 
        method = RequestMethod.GET)
    ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndId(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", allowableValues = "original, weak, medium, strong", defaultValue = "original")  @RequestParam(value = "simplifyGeometries", required = false, defaultValue="original") String simplifyGeometries);


    @ApiOperation(value = "retrieve the indicator for the selected spatial unit, year and month as GeoJSON", nickname = "getIndicatorBySpatialUnitIdAndIdAndYearAndMonth", notes = "retrieve the indicator for the selected spatial unit, year and month as GeoJSON", response = byte[].class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = byte[].class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}/{spatialUnitId}/{year}/{month}/{day}",
        produces = { "application/octed-stream" }, 
        method = RequestMethod.GET)
    ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndIdAndYearAndMonth(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "year for which the indicator shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which the indicator shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day,@ApiParam(value = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", allowableValues = "original, weak, medium, strong", defaultValue = "original")  @RequestParam(value = "simplifyGeometries", required = false, defaultValue="original") String simplifyGeometries);


    @ApiOperation(value = "retrieve the indicator values and other properties for the selected spatial unit, year and month. It does not include the spatial geometries!", nickname = "getIndicatorBySpatialUnitIdAndIdAndYearAndMonthWithoutGeometry", notes = "retrieve the indicator values and other properties for the selected spatial unit, year and month. It does not include the spatial geometries!", response = IndicatorPropertiesWithoutGeomType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = IndicatorPropertiesWithoutGeomType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}/{spatialUnitId}/{year}/{month}/{day}/without-geometry",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getIndicatorBySpatialUnitIdAndIdAndYearAndMonthWithoutGeometry(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId,@ApiParam(value = "year for which the indicator shall be queried",required=true) @PathVariable("year") BigDecimal year,@ApiParam(value = "month for which the indicator shall be queried",required=true) @PathVariable("month") BigDecimal month,@ApiParam(value = "day for which datasets shall be queried",required=true) @PathVariable("day") BigDecimal day);


    @ApiOperation(value = "retrieve the indicator values and other properties for the selected spatial unit. It does not include the spatial geometries!", nickname = "getIndicatorBySpatialUnitIdAndIdWithoutGeometry", notes = "retrieve the indicator values and other properties for the selected spatial unit. It does not include the spatial geometries!", response = IndicatorPropertiesWithoutGeomType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = IndicatorPropertiesWithoutGeomType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators/{indicatorId}/{spatialUnitId}/without-geometry",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getIndicatorBySpatialUnitIdAndIdWithoutGeometry(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "the unique identifier of the spatial level",required=true) @PathVariable("spatialUnitId") String spatialUnitId);


    @ApiOperation(value = "retrieve information about available indicators", nickname = "getIndicators", notes = "retrieve information about available indicators", response = IndicatorOverviewType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = IndicatorOverviewType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/indicators",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<IndicatorOverviewType>> getIndicators(@ApiParam(value = "thematic topic to filter available indicators", allowableValues = "demography, environment, habitation, migration, social")  @RequestParam(value = "topic", required = false) String topic);


    @ApiOperation(value = "Modify/Update the contents of the selected indicator dataset", nickname = "updateIndicatorAsBody", notes = "Modify/Update the contents of the selected indicator dataset", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK - Updated"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/indicators/{indicatorId}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity updateIndicatorAsBody(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "indicator data" ,required=true )   @RequestBody IndicatorPUTInputType indicatorData);


    @ApiOperation(value = "Modify/Update the metadata of the selected indicator dataset", nickname = "updateIndicatorMetadataAsBody", notes = "Modify/Update the metadata of the selected indicator dataset. This replaces the formerly stored metadata.", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/indicators/{indicatorId}",
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity updateIndicatorMetadataAsBody(@ApiParam(value = "unique identifier of the selected indicator dataset",required=true) @PathVariable("indicatorId") String indicatorId,@ApiParam(value = "metadata input" ,required=true )   @RequestBody IndicatorPATCHInputType metadata);

}
