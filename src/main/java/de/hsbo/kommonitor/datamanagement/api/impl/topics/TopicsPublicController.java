package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.TopicsPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.model.TopicDisplayOrderModeOverviewType;
import de.hsbo.kommonitor.datamanagement.model.TopicOverviewType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TopicsPublicController extends BasePathController implements TopicsPublicApi {

    private static final Logger LOG = LoggerFactory.getLogger(TopicsPublicController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    TopicsManager topicsManager;

    @Autowired
    public TopicsPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<TopicOverviewType> getTopicById(String topicId) {
        LOG.info("Received request to get topic for topicId '{}'", topicId);
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            TopicOverviewType topic = topicsManager.getTopicById(topicId);
            return new ResponseEntity<>(topic, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<TopicOverviewType>> getTopics(String topicType) {
        LOG.info("Received request to get all topics");
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            List<TopicOverviewType> topics = topicsManager.getTopics(topicType);
            return new ResponseEntity<>(topics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<TopicDisplayOrderModeOverviewType>> getTopicsDisplayOrderMode() {
        LOG.info("Received request to get topic display order modes.");
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            List<TopicDisplayOrderModeOverviewType> topics = topicsManager.getTopicDisplayOrderModes();
            return new ResponseEntity<>(topics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
