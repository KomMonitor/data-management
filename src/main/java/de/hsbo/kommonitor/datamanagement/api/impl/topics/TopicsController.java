package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.TopicsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.TopicOverviewType;
import de.hsbo.kommonitor.datamanagement.model.TopicPATCHDisplayOrderInputType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class TopicsController extends BasePathController implements TopicsApi {
	
	private final static Logger LOG = LoggerFactory.getLogger(TopicsController.class);

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
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'themes')")
	public ResponseEntity<TopicOverviewType> addTopic(TopicInputType topicData) {
		
		LOG.info("Received request to insert new topic");

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
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(topic, responseHeaders, HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'themes')")
	public ResponseEntity deleteTopic(String topicId) {
		LOG.info("Received request to delete topic for topicId '{}'", topicId);

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
	public ResponseEntity<Void> updateMainTopicDisplayOrder(List<@Valid TopicPATCHDisplayOrderInputType> mainTopicOrderArray) {
		LOG.info("Received request to update main topic display order ");
		boolean update;

		try {
			update = topicsManager.updateMainTopicOrder(mainTopicOrderArray);
			lastModManager.updateLastDatabaseModification_topics();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}

		if (update) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'themes')")
	public ResponseEntity updateSubtopicDisplayOrder(String topicId, List<@Valid TopicPATCHDisplayOrderInputType> subtopicOrderArray) {
		LOG.info("Received request to update subtopic display order ");

		try {
			topicId = topicsManager.updateSubtopicsOrder(topicId, subtopicOrderArray);
			lastModManager.updateLastDatabaseModification_topics();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}

		return createResponseEntityWithLocationHeader(topicId);

	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'themes')")
	public ResponseEntity<Void> updateTopic(String topicId, TopicInputType topicData) {
		LOG.info("Received request to update topic with topicId '{}'", topicId);

		/*
		 * analyse input data and save it within database
		 */
		
		try {
			topicId = topicsManager.updateTopic(topicData, topicId);
			lastModManager.updateLastDatabaseModification_topics();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		return createResponseEntityWithLocationHeader(topicId);
	}

	private ResponseEntity<Void> createResponseEntityWithLocationHeader(String topicId) {
		if (topicId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			try {
				responseHeaders.setLocation(new URI(topicId));
			} catch (URISyntaxException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
