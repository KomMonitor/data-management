package de.hsbo.kommonitor.datamanagement.api;

import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptOverviewType;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "ProcessScripts", description = "the ProcessScripts API")
public interface ProcessScriptsPublicApi {

    @ApiOperation(value = "retrieve the process script code associated to a certain public indicator as JavaScript file",
            nickname = "getProcessScriptCode", notes = "retrieve the process script code associated to a certain public indicator as JavaScript file",
            response = byte[].class,
            authorizations = {
                    @Authorization(value = "basicAuth")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = byte[].class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/process-scripts/{scriptId}/scriptCode",
            produces = {"application/javascript"},
            method = RequestMethod.GET)
    ResponseEntity<byte[]> getPublicProcessScriptCode(@ApiParam(value = "unique identifier of the selected script", required = true) @PathVariable("scriptId") String scriptId);

    @ApiOperation(value = "retrieve the process script code associated to a certain public indicator as JavaScript file",
            nickname = "getProcessScriptCodeForIndicator",
            notes = "retrieve the process script code associated to a certain public indicator as JavaScript file",
            response = byte[].class,
            authorizations = {
                    @Authorization(value = "basicAuth")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = byte[].class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/process-scripts/usingIndicatorId/{indicatorId}/scriptCode",
            produces = {"application/javascript"},
            method = RequestMethod.GET)
    ResponseEntity<byte[]> getProcessScriptCodeForPublicIndicator(@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId);

    @ApiOperation(value = "retrieve information about the associated process script for a certain public indicator",
            nickname = "getProcessScriptForIndicator",
            notes = "retrieve information about the associated process script for a certain indicator",
            response = ProcessScriptOverviewType.class,
            authorizations = {
                    @Authorization(value = "basicAuth")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProcessScriptOverviewType.class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/process-scripts/usingIndicatorId/{indicatorId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<ProcessScriptOverviewType> getProcessScriptForPublicIndicator(@ApiParam(value = "unique identifier of the selected public indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId);

    @ApiOperation(value = "retrieve information about the associated process script for a certain scriptId associated to a public indicator",
            nickname = "getProcessScriptForScriptId",
            notes = "retrieve information about the associated process script for a certain scriptId associated to a public indicator",
            response = ProcessScriptOverviewType.class,
            authorizations = {
                    @Authorization(value = "basicAuth")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProcessScriptOverviewType.class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/process-scripts/{scriptId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<ProcessScriptOverviewType> getPublicProcessScriptForScriptId(@ApiParam(value = "unique identifier of the selected script", required = true) @PathVariable("scriptId") String scriptId);

    @ApiOperation(value = "retrieve information about available process scripts associated to public indicators",
            nickname = "getProcessScripts",
            notes = "retrieve information about available process scripts associated to public indicators",
            response = ProcessScriptOverviewType.class,
            responseContainer = "array", authorizations = {
            @Authorization(value = "basicAuth")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ProcessScriptOverviewType.class, responseContainer = "array"),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/process-scripts",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<ProcessScriptOverviewType>> getPublicProcessScripts();
}
