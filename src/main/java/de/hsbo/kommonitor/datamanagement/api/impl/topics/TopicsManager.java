package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.model.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.TopicOverviewType;

@Transactional
@Repository
@Component
public class TopicsManager {

	/**
	*
	*/
	private static Logger logger = LoggerFactory.getLogger(TopicsManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	TopicsRepository topicsRepo;

	public String addTopic(TopicInputType topicData) {
		logger.info("Trying to persist topic with topicName '{}'", topicData.getTopicName());
		/*
		 * analyse input type
		 * 
		 * make instance of TopicOverviewType
		 * 
		 * save TopicOverviewType instance to db
		 * 
		 * return id
		 */
		TopicOverviewType topic = new TopicOverviewType();
		topic.setTopicName(topicData.getTopicName());
		topic.setTopicDescription(topicData.getTopicDescription());
		/*
		 * ID will be autogenerated from JPA / Hibernate
		 */

		topicsRepo.save(topic);
		
		return topic.getTopicId();

	}

	public List<TopicOverviewType> getTopics() {
		logger.info("Retrieving all topics from db");
		return topicsRepo.findAll();
	}

	public TopicOverviewType getTopicById(String topicId) {
		logger.info("Retrieving topic for topicId '{}'", topicId);
		return topicsRepo.findByTopicId(topicId);
	}

	public boolean deleteTopicById(String topicId) throws ResourceNotFoundException {
		logger.info("Trying to delete topic with topicId '{}'", topicId);
		if (topicsRepo.existsByTopicId(topicId)){
			topicsRepo.deleteByTopicId(topicId);
			return true;
		}else{
			logger.error("No topic with id '{}' was found in database", topicId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), "Tried to delete topic, but no topic existes with id " + topicId);
		}
	}

}
