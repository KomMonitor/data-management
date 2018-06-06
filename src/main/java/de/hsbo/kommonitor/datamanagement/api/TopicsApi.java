/**
 * NOTE: This class is auto generated by the swagger code generator program (unset).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;

import java.util.List;
@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

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


    @ApiOperation(value = "retrieve information about the selected topic", nickname = "getTopicById", notes = "retrieve information about the selected topic", response = TopicOverviewType.class, authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = TopicOverviewType.class),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/topics/{topicId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<TopicOverviewType> getTopicById(@ApiParam(value = "unique identifier of the topic",required=true) @PathVariable("topicId") String topicId);


    @ApiOperation(value = "retrieve information about available topics", nickname = "getTopics", notes = "retrieve information about available topics", response = TopicOverviewType.class, responseContainer = "array", authorizations = {
        @Authorization(value = "basicAuth")
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = TopicOverviewType.class, responseContainer = "array"),
        @ApiResponse(code = 400, message = "Invalid status value"),
        @ApiResponse(code = 401, message = "API key is missing or invalid") })
    @RequestMapping(value = "/topics",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<TopicOverviewType>> getTopics();

}
