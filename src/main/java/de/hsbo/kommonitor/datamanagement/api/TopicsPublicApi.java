/**
 * NOTE: This class is auto generated by the swagger code generator program (unset).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.hsbo.kommonitor.datamanagement.api;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "Topics", description = "the public Topics API")
public interface TopicsPublicApi {

    @ApiOperation(value = "retrieve information about the selected topic",
            nickname = "getTopicById",
            notes = "retrieve information about the selected topic",
            response = TopicOverviewType.class,
            authorizations = {
                    @Authorization(value = "basicAuth")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TopicOverviewType.class),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/topics/{topicId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<TopicOverviewType> getTopicById(@ApiParam(value = "unique identifier of the topic", required = true) @PathVariable("topicId") String topicId);


    @ApiOperation(value = "retrieve information about available topics",
            nickname = "getTopics", notes = "retrieve information about available topics",
            response = TopicOverviewType.class,
            responseContainer = "array",
            authorizations = {
                    @Authorization(value = "basicAuth")
            }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TopicOverviewType.class, responseContainer = "array"),
            @ApiResponse(code = 400, message = "Invalid status value"),
            @ApiResponse(code = 401, message = "API key is missing or invalid")})
    @RequestMapping(value = "/topics",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<TopicOverviewType>> getTopics();
}