package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.model.DefaultResourcePermissionType;
import de.hsbo.kommonitor.datamanagement.model.TopicDefaultPermissionType;
import de.hsbo.kommonitor.datamanagement.model.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.TopicOverviewType;
import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.TopicTypeEnum;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

    public TopicOverviewType addTopic(TopicInputType topicData) throws Exception {
        String topicName = topicData.getTopicName();
        TopicResourceEnum topicResource = topicData.getTopicResource();
        TopicTypeEnum topicType = topicData.getTopicType();
        if (topicsRepo.existsByTopicNameAndTopicTypeAndTopicResource(topicName, topicType, topicResource)) {
            logger.error("The topic with topicName '{}' and topicType '{}' and topicResource '{}' already exists. Thus aborting add topic request.", topicName, topicType, topicResource);
            throw new Exception("Topic with the combination of topicType, topicResource and topicName already exists. Aborting add topic request.");
        }

        TopicsEntity topic = tryPersistTopicWithoutSubtopics(topicData);

        topic = handleSubTopics(topic, topicData.getSubTopics());

        topicsRepo.saveAndFlush(topic);

        return getTopicById(topic.getTopicId());
    }

    private TopicsEntity handleSubTopics(TopicsEntity topicEntity, List<TopicInputType> subTopics) throws Exception {
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
         * as we assume that in each requests (POST or PUT) the whole topics hierarchy is delivered
         * --> hence we build it each time anew
         */
        Collection<TopicsEntity> currentSubTopics = topicEntity.getSubTopics();


        topicEntity.setSubTopics(new ArrayList<TopicsEntity>());

        for (TopicInputType subTopic : subTopics) {

            TopicsEntity subTopicEntity = null;

//			if (! topicsRepo.existsByTopicName(subTopic.getTopicName())){
//				subTopicEntity = tryPersistTopicWithoutSubtopics(subTopic);
//				
//			}
            if (!isAlreadyInSubtopics_byName(subTopic, currentSubTopics)) {
                subTopicEntity = tryPersistTopicWithoutSubtopics(subTopic);

            } else {
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
            if (currentSubTopic.getTopicName().equals(subTopicCandidate.getTopicName())) {
                return currentSubTopic;
            }
        }
        return null;
    }

    private boolean isAlreadyInSubtopics_byName(TopicInputType subTopicCandidate, Collection<TopicsEntity> currentSubTopics) {

        for (TopicsEntity currentSubTopic : currentSubTopics) {
            if (currentSubTopic.getTopicName().equals(subTopicCandidate.getTopicName())) {
                return true;
            }
        }
        return false;
    }

    private TopicsEntity tryPersistTopicWithoutSubtopics(TopicInputType topicData) throws Exception {
        String topicName = topicData.getTopicName();
        TopicResourceEnum topicResource = topicData.getTopicResource();
        TopicTypeEnum topicType = topicData.getTopicType();
        logger.info("Trying to persist topic with topicName '{}' and topicType '{}' and topicResource '{}'", topicName, topicType, topicResource);

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

        DefaultResourcePermissionType defaultPermissions = topicData.getDefaultPermissions();
        if (defaultPermissions != null) {
            topic.setDefaultPermissions(defaultPermissions.getPermissions());
            topic.setDefaultPermissionsIsPublic(defaultPermissions.getIsPublic());
        }

        /*
         * ID will be autogenerated from JPA / Hibernate
         */

        topicsRepo.saveAndFlush(topic);
        return topic;
    }

    public List<TopicOverviewType> getTopics() {
        logger.info("Retrieving all topics from db");

        // only return main topics as they include the sub topics in use!!!
        // hence, do not show sub topics on first tier of returned JSON structure but only as subTopics of the respective main topics
        List<TopicsEntity> topicEntities = topicsRepo.findByTopicType(TopicTypeEnum.MAIN);
        List<TopicOverviewType> topics = TopicsMapper.mapToSwaggerTopics(topicEntities);

        topics.sort(Comparator.comparing(TopicOverviewType::getTopicName));

        return topics;
    }

    public TopicOverviewType getTopicById(String topicId) {
        logger.info("Retrieving topic for topicId '{}'", topicId);

        TopicsEntity topicEntity = topicsRepo.findByTopicId(topicId);
        TopicOverviewType topic = TopicsMapper.mapToSwaggerTopic(topicEntity);

        return topic;
    }

    public DefaultResourcePermissionType getTopicDefaultPermissionsById(String topicId) {
        logger.info("Retrieving topic for topicId '{}'", topicId);
        TopicsEntity topicEntity = topicsRepo.findByTopicId(topicId);
        if (topicEntity == null) {
            return null;
        }

        DefaultResourcePermissionType result = new DefaultResourcePermissionType();
        result.setPermissions(topicEntity.getDefaultPermissions());
        result.setIsPublic(topicEntity.isDefaultPermissionsPublic());

        return result;
    }

    public List<TopicDefaultPermissionType> getTopicDefaultPermissions() {
        logger.info("Retrieving defaultPermissions for all topics");
        List<TopicsEntity> topics = topicsRepo.findAll();

        List<TopicDefaultPermissionType> result = new ArrayList<>();

        for (TopicsEntity topic : topics) {
            TopicDefaultPermissionType topicDefaultPermissionType = new TopicDefaultPermissionType();
            topicDefaultPermissionType.setTopicId(topic.getTopicId());

            DefaultResourcePermissionType rpt = new DefaultResourcePermissionType();
            rpt.setPermissions(topic.getDefaultPermissions());
            rpt.setIsPublic(topic.isDefaultPermissionsPublic());

            topicDefaultPermissionType.setDefaultPermissions(rpt);
            result.add(topicDefaultPermissionType);
        }
        return result;
    }

    public boolean deleteTopicById(String topicId) throws ResourceNotFoundException {
        logger.info("Trying to delete topic with topicId '{}'", topicId);

        if (topicsRepo.existsByTopicId(topicId)) {
            TopicsEntity topic = topicsRepo.findByTopicId(topicId);

            // delete any associated entries in topics_subtopics table
            // by iterating over all main topics and check if specified topic has a subtopic relation
            if (topic.getTopicType().equals(TopicTypeEnum.SUB)) {
                deleteSubTopicEntriesFromParentTopics(topicId);
            }

            deleteAllSubTopicsAndRelations(topic);

            topicsRepo.delete(topic);
            return true;
        } else {
            logger.error("No topic with id '{}' was found in database. Delete request has no effect.", topicId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), "Tried to delete topic, but no topic existes with id " + topicId);
        }
    }

    private void deleteSubTopicEntriesFromParentTopics(String subTopicId) {
        List<TopicsEntity> allTopics = topicsRepo.findAll();

        for (TopicsEntity topicsEntity : allTopics) {
            // for all main topics check if Topic with subTopicIf must be removed
//			if(topicsEntity.getTopicType().equals(TopicTypeEnum.MAIN)){

            Collection<TopicsEntity> subTopics = topicsEntity.getSubTopics();

            for (Iterator i = subTopics.iterator(); i.hasNext(); ) {
                TopicsEntity nextSubTopic = (TopicsEntity) i.next();
                if (nextSubTopic.getTopicId().equals(subTopicId)) {
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
        for (Iterator i = subTopics.iterator(); i.hasNext(); ) {
            TopicsEntity topicEntity = (TopicsEntity) i.next();
            deleteAllSubTopicsAndRelations(topicEntity);

            // delete subTopic entity
            topicsRepo.delete(topicEntity);
            i.remove();
        }

        topic.setSubTopics(subTopics);

        topicsRepo.saveAndFlush(topic);

    }

    public String updateTopic(TopicInputType topicData, String topicId) throws Exception {
        logger.info("Trying to update topic with topicId '{}'", topicId);

        if (topicsRepo.existsByTopicId(topicId)) {

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

            DefaultResourcePermissionType defaultPermissions = topicData.getDefaultPermissions();
            if (defaultPermissions != null) {
                topic.setDefaultPermissions(defaultPermissions.getPermissions());
                topic.setDefaultPermissionsIsPublic(defaultPermissions.getIsPublic());
            }

            handleSubTopics(topic, topicData.getSubTopics());

            topicsRepo.saveAndFlush(topic);

            return topic.getTopicId();
        } else {
            logger.error("No topic with topicId '{}' was found. Thus aborting update topic request.", topicId);
            throw new ResourceNotFoundException(404, "No topic was found for specified topicId. Aborting update topic request.");
        }
    }

}
