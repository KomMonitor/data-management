/**
 * NOTE: This class is auto generated by the swagger code generator program (unset).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2020-01-05T01:37:49.273+01:00")

@Api(value = "Topics", description = "the Topics API")
public interface TopicsApi {

    @ApiOperation(value = "Register a new topic", nickname = "addTopic", notes = "Add/Register a topic", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/topics",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity addTopic(@ApiParam(value = "topic input data" ,required=true )   @RequestBody TopicInputType topicData);


    @ApiOperation(value = "Delete the topic", nickname = "deleteTopic", notes = "Delete the topic", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/topics/{topicId}",
        method = RequestMethod.DELETE)
    ResponseEntity deleteTopic(@ApiParam(value = "unique identifier of the topic",required=true) @PathVariable("topicId") String topicId);

    @ApiOperation(value = "Modify topic information", nickname = "updateTopic", notes = "Modify topic information", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "API key is missing or invalid"),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/topics/{topicId}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity updateTopic(@ApiParam(value = "unique identifier of the topic",required=true) @PathVariable("topicId") String topicId,@ApiParam(value = "topic input data" ,required=true )   @RequestBody TopicInputType topicData);

}
