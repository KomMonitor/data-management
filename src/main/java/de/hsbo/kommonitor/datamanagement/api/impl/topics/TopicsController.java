package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.TopicsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;

@Controller
public class TopicsController extends BasePathController implements TopicsApi {
	
	private static Logger logger = LoggerFactory.getLogger(TopicsController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	TopicsManager topicsManager;
	
	@Autowired
    private LastModificationManager lastModManager;

	@org.springframework.beans.factory.annotation.Autowired
	public TopicsController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity<TopicOverviewType> addTopic(@RequestBody TopicInputType topicData) {
		
		logger.info("Received request to insert new topic");
		
		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		TopicOverviewType topic;
		try {
			topic = topicsManager.addTopic(topicData);
			lastModManager.updateLastDatabaseModification_topics();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
			
		}

		if (topic != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = topic.getTopicId();
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
//				return ApiResponseUtil.createResponseEntityFromException(e);
			}

			return new ResponseEntity<TopicOverviewType>(topic, responseHeaders, HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity deleteTopic(@PathVariable("topicId") String topicId) {
		logger.info("Received request to delete topic for topicId '{}'", topicId);
		
		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */
			
			boolean isDeleted;
			try {
				isDeleted = topicsManager.deleteTopicById(topicId);
				lastModManager.updateLastDatabaseModification_topics();
			
			if(isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);
			
			} catch (Exception e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity updateTopic(@PathVariable("topicId") String topicId, @RequestBody TopicInputType topicData) {
		logger.info("Received request to update topic with topicId '{}'", topicId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		
		try {
			topicId = topicsManager.updateTopic(topicData, topicId);
			lastModManager.updateLastDatabaseModification_topics();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (topicId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = topicId;
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
				// return ApiResponseUtil.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
