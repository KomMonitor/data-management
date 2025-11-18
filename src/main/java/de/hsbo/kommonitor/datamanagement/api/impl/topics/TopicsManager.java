package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;

@Transactional
@Repository
@Component
public class TopicsManager {

	private static final Logger LOG = LoggerFactory.getLogger(TopicsManager.class);

	@Autowired
	TopicsRepository topicsRepo;

	@Autowired
	TopicsOrderModeRepository topicsOrderModeRepo;

	public TopicOverviewType addTopic(TopicInputType topicData) throws Exception {
		String topicName = topicData.getTopicName();
		TopicResourceEnum topicResource = topicData.getTopicResource();
		TopicTypeEnum topicType = topicData.getTopicType();
		if(topicsRepo.existsByTopicNameAndTopicTypeAndTopicResource(topicName, topicType, topicResource)){
			LOG.error("The topic with topicName '{}' and topicType '{}' and topicResource '{}' already exists. Thus aborting add topic request.", topicName, topicType, topicResource);
			throw new Exception("Topic with the combination of topicType, topicResource and topicName already exists. Aborting add topic request.");
		}
		
		TopicsEntity topic = tryPersistTopicWithoutSubtopics(topicData);		
		
		topic = handleSubTopics(topic, topicData.getSubTopics());
		
		topicsRepo.saveAndFlush(topic);

		return getTopicById(topic.getTopicId());
	}

	private TopicsEntity handleSubTopics(TopicsEntity topicEntity, List<TopicInputType> subTopics) {
		/*
		 * for each subtopic we must check if it already exists as topic entity
		 * 
		 * if not then create it
		 * 
		 * and after that add it as subtopic
		 */
		
		/*
		 * first we make an empty list of subtopics
		 * 
		 * as we assume that in each request (POST or PUT) the whole topics hierarchy is delivered
		 * --> hence we build it each time anew
		 */
		Collection<TopicsEntity> currentSubTopics = topicEntity.getSubTopics();

		topicEntity.setSubTopics(new ArrayList<>());
		
		for (TopicInputType subTopic : subTopics) {
			
			TopicsEntity subTopicEntity;
			
//			if (! topicsRepo.existsByTopicName(subTopic.getTopicName())){
//				subTopicEntity = tryPersistTopicWithoutSubtopics(subTopic);
//				
//			}
			if (! isAlreadyInSubtopics_byName(subTopic, currentSubTopics)){
				subTopicEntity = tryPersistTopicWithoutSubtopics(subTopic, topicEntity);
			}
			else{
//				subTopicEntity = topicsRepo.findByTopicName(subTopic.getTopicName());
				subTopicEntity = getCurrentSubTopic_byName(subTopic, currentSubTopics);
			}
			
			topicEntity.addSubTopicIfNotExists(subTopicEntity);
			subTopicEntity = handleSubTopics(subTopicEntity, subTopic.getSubTopics());
			
			topicsRepo.saveAndFlush(subTopicEntity);
		}
		
		return topicEntity;
	}

	private TopicsEntity getCurrentSubTopic_byName(TopicInputType subTopicCandidate, Collection<TopicsEntity> currentSubTopics) {
		for (TopicsEntity currentSubTopic : currentSubTopics) {
			if(currentSubTopic.getTopicName().equals(subTopicCandidate.getTopicName())) {
				return currentSubTopic;
			}
		}
		return null;
	}

	private boolean isAlreadyInSubtopics_byName(TopicInputType subTopicCandidate, Collection<TopicsEntity> currentSubTopics) {
		
		for (TopicsEntity currentSubTopic : currentSubTopics) {
			if(currentSubTopic.getTopicName().equals(subTopicCandidate.getTopicName())) {
				return true;
			}
		}
		return false;
	}

	private TopicsEntity tryPersistTopicWithoutSubtopics(TopicInputType topicData) {
		TopicsEntity topic = createTopicEntity(topicData);

		topic.setDisplayOrder(getNextDisplayOrder(topicData.getTopicResource()));

		topicsRepo.saveAndFlush(topic);
		return topic;
	}

	private TopicsEntity tryPersistTopicWithoutSubtopics(TopicInputType topicData, TopicsEntity parent) {
		TopicsEntity topic = createTopicEntity(topicData);

		topic.setDisplayOrder(getNextDisplayOrder(parent));

		topicsRepo.saveAndFlush(topic);
		return topic;
	}

	private TopicsEntity createTopicEntity(TopicInputType topicData) {
		String topicName = topicData.getTopicName();
		TopicResourceEnum topicResource = topicData.getTopicResource();
		TopicTypeEnum topicType = topicData.getTopicType();
		LOG.info("Trying to persist topic with topicName '{}' and topicType '{}' and topicResource '{}'", topicName, topicType, topicResource);

		/*
		 * analyse input type
		 *
		 * make instance of TopicOverviewType
		 *
		 * save TopicOverviewType instance to db
		 *
		 * return id
		 */


		TopicsEntity topic = new TopicsEntity();
		topic.setTopicName(topicName);
		topic.setTopicDescription(topicData.getTopicDescription());
		topic.setTopicType(topicData.getTopicType());
		topic.setTopicResource(topicResource);
		return topic;
	}

	private int getNextDisplayOrder(TopicResourceEnum topicResource) {
		return (detectLastMainTopicDisplayOrder(topicResource) + 1);
	}

	private int getNextDisplayOrder(TopicsEntity parent) {
		return (detectLastSubTopicDisplayOrder(parent) + 1);
	}

	private int detectLastMainTopicDisplayOrder(TopicResourceEnum topicResource) {
		return topicsRepo.findAll().stream()
				.filter(t -> t.getTopicType().equals(TopicTypeEnum.MAIN))
				.filter(t -> t.getTopicResource().equals(topicResource))
				.sorted(Comparator.comparing(TopicsEntity::getDisplayOrder).reversed())
				.map(TopicsEntity::getDisplayOrder)
				.findFirst()
				.orElse(-1);
	}

	private int detectLastSubTopicDisplayOrder(TopicsEntity topic) {
		return topic.getSubTopics().stream()
				.sorted(Comparator.comparing(TopicsEntity::getDisplayOrder).reversed())
				.map(TopicsEntity::getDisplayOrder)
				.findFirst()
				.orElse(-1);
	}

	public List<TopicOverviewType> getTopics(String topicResourceType) {
		LOG.info("Retrieving all topics from db");
		
		// only return main topics as they include the sub topics in use!!!
		// hence, do not show sub topics on first tier of returned JSON structure but only as subTopics of the respective main topics
		List<TopicsEntity> topicEntities = topicsRepo.findByTopicType(TopicTypeEnum.MAIN);
		if(topicResourceType != null) {
			topicEntities = topicEntities.stream()
					.filter(t -> t.getTopicResource().equals(TopicResourceEnum.fromValue(topicResourceType)))
					.toList();
		}
		List<TopicOverviewType> topics = TopicsMapper.mapToSwaggerTopics(topicEntities);
		
		topics.sort(Comparator.comparing(TopicOverviewType::getDisplayOrder));
		
		return topics;
	}

	public TopicOverviewType getTopicById(String topicId) {
		LOG.info("Retrieving topic for topicId '{}'", topicId);
		
		TopicsEntity topicEntity = topicsRepo.findByTopicId(topicId);

        return TopicsMapper.mapToSwaggerTopic(topicEntity);
	}

	public boolean deleteTopicById(String topicId) throws ResourceNotFoundException {
		LOG.info("Trying to delete topic with topicId '{}'", topicId);
		
		if (topicsRepo.existsByTopicId(topicId)){
			TopicsEntity topic = topicsRepo.findByTopicId(topicId);
			
			// delete any associated entries in topics_subtopics table
			// by iterating over all main topics and check if specified topic has a subtopic relation
			if(topic.getTopicType().equals(TopicTypeEnum.SUB)){
				deleteSubTopicEntriesFromParentTopics(topicId);
			}			
			
			deleteAllSubTopicsAndRelations(topic);
			deleteTopicFavourites(topic);

			topicsRepo.delete(topic);
			return true;
		}else{
			LOG.error("No topic with id '{}' was found in database. Delete request has no effect.", topicId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), "Tried to delete topic, but no topic exists with id " + topicId);
		}
	}

	private void deleteTopicFavourites(TopicsEntity topic) {
		// remove user favorites
		try {
			topic.getUserFavorites().forEach(u -> u.removeTopicFavourite(topic));
			topicsRepo.saveAndFlush(topic);
		} catch (Exception e) {
			LOG.error("Error while deleting user favorites for topic", e);
		}
	}

	private void deleteSubTopicEntriesFromParentTopics(String subTopicId) {
		List<TopicsEntity> allTopics = topicsRepo.findAll();
		
		for (TopicsEntity topicsEntity : allTopics) {
			// for all main topics check if Topic with subTopicIf must be removed
//			if(topicsEntity.getTopicType().equals(TopicTypeEnum.MAIN)){
				
				Collection<TopicsEntity> subTopics = topicsEntity.getSubTopics();
				
				for (Iterator<?> i = subTopics.iterator(); i.hasNext();) {
					TopicsEntity nextSubTopic = (TopicsEntity)i.next();
					if(nextSubTopic.getTopicId().equals(subTopicId)){
					    i.remove();
					}
					
				}				
				
				topicsEntity.setSubTopics(subTopics);
				
				topicsRepo.saveAndFlush(topicsEntity);
//			}
		}
		
	}

	private void deleteAllSubTopicsAndRelations(TopicsEntity topic) {
		Collection<TopicsEntity> subTopics = topic.getSubTopics();
		
		// delete subTopic relation
		for (Iterator<?> i = subTopics.iterator(); i.hasNext();) {
			TopicsEntity topicEntity = (TopicsEntity)i.next();
			deleteAllSubTopicsAndRelations(topicEntity);
			
			// delete subTopic entity
			topicsRepo.delete(topicEntity);
		    i.remove();
		}
		
		topic.setSubTopics(subTopics);
		
		topicsRepo.saveAndFlush(topic);
		
	}

	public String updateTopic(TopicInputType topicData, String topicId) throws Exception {
		LOG.info("Trying to update topic with topicId '{}'", topicId);
		
		if(topicsRepo.existsByTopicId(topicId)){
			
			String topicName = topicData.getTopicName();
			TopicTypeEnum topicType = topicData.getTopicType();
			TopicResourceEnum topicResource = topicData.getTopicResource();
			
			// here code must at least check, if the information for the topicId was changed and only then throw error if the combination already exists.
			
			
//			if(topicsRepo.existsByTopicNameAndTopicTypeAndTopicResource(topicName, topicType, topicResource)){
//				logger.error("The topic with topicName '{}' and topicType '{}' and topicResource '{}' already exists. Thus aborting update topic request.", topicName, topicType, topicResource);
//				throw new Exception("Topic with the combination of topicType, topicResource and topicName already exists. Aborting update topic request.");
//			}
			
			TopicsEntity topic = topicsRepo.findByTopicId(topicId);
			topic.setTopicName(topicName);
			topic.setTopicDescription(topicData.getTopicDescription());
			topic.setTopicType(topicType);
			topic.setTopicResource(topicResource);
			
			handleSubTopics(topic, topicData.getSubTopics());
			
			topicsRepo.saveAndFlush(topic);
			
			return topic.getTopicId();
		}
		else{
			LOG.error("No topic with topicId '{}' was found. Thus aborting update topic request.", topicId);
			throw new ResourceNotFoundException(404, "No topic was found for specified topicId. Aborting update topic request.");
		}		
	}

	public String updateSubtopicsOrder(String topicId, List<@Valid TopicDisplayOrderInputType> subtopicOrderArray) throws Exception {
		LOG.info("Ordering subtopics for topic with topicId '{}'", topicId);

		if(topicsRepo.existsByTopicId(topicId)){

			TopicsEntity topic = topicsRepo.findByTopicId(topicId);
			Collection<TopicsEntity> subTopics = topic.getSubTopics();

			subtopicOrderArray.forEach(i -> {
				TopicsEntity subTopicEntity = subTopics
						.stream()
						.filter(s -> s.getTopicId().equals(i.getTopicId()))
						.findFirst()
						.orElse(null);
				if (subTopicEntity != null) {
					subTopicEntity.setDisplayOrder(i.getDisplayOrder());
				} else {
					LOG.warn("No subtopic with ID '{}' found for topic '{}'", i.getTopicId(), topicId);
				}
			});

			topicsRepo.saveAllAndFlush(subTopics);
			return topic.getTopicId();
		}
		else{
			LOG.error("No topic with topicId '{}' was found. Thus aborting updating subtopic order request.", topicId);
			throw new ResourceNotFoundException(404, "No topic was found for specified topicId. Aborting update topic request.");
		}
	}

	public boolean updateMainTopicOrder(List<@Valid TopicDisplayOrderInputType> mainTopicOrderArray, TopicResourceEnum topicResource) {
		LOG.info("Ordering main topics.");

		List<TopicsEntity> mainTopics = topicsRepo.findByTopicType(TopicTypeEnum.MAIN)
				.stream()
				.filter(t -> t.getTopicResource().equals(topicResource))
				.collect(Collectors.toList());

		mainTopicOrderArray.forEach(i -> {
			TopicsEntity mainTopicEntity = mainTopics
					.stream()
					.filter(m -> m.getTopicId().equals(i.getTopicId()))
					.findFirst()
					.orElse(null);
			if (mainTopicEntity != null) {
				mainTopicEntity.setDisplayOrder(i.getDisplayOrder());
			} else {
				LOG.warn("No main topic with ID '{}' found.", i.getTopicId());
			}
		});

		topicsRepo.saveAllAndFlush(mainTopics);
		return true;
	}

	public boolean updateTopicOrderMode(TopicDisplayOrderModeInputType topicOrderMode, TopicResourceEnum topicResourceEnum) throws Exception {
		LOG.info("Trying to update topic display order for '{}' topics", topicResourceEnum.getValue());

		if(topicsOrderModeRepo.existsByTopicResource(topicResourceEnum)){
			TopicsOrderModeEntity orderModeEntity = topicsOrderModeRepo.findByTopicResource(topicResourceEnum);
			if (!orderModeEntity.getOrderMode().equals(topicOrderMode.getOrderMode())) {
				orderModeEntity.setOrderMode(topicOrderMode.getOrderMode());
				topicsOrderModeRepo.saveAndFlush(orderModeEntity);
			}
			return true;
		}
		else{
			LOG.error("No topic order entry with resource type '{}' was found.", topicResourceEnum.getValue());
			throw new ResourceNotFoundException(404, "No topic order entry with specified resource type was found.");
		}
	}

	public List<TopicDisplayOrderModeOverviewType> getTopicDisplayOrderModes() {
		LOG.info("Retrieving topic display order modes from DB");

		List<TopicsOrderModeEntity> topicOrderModeEntities = topicsOrderModeRepo.findAll();

		return TopicsMapper.mapToSwaggerTopicOrderModes(topicOrderModeEntities);
	}
}
