package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public class TopicsMapper {

	public static List<TopicOverviewType> mapToSwaggerTopics(List<TopicsEntity> topicEntities) {
		List<TopicOverviewType> topics = new ArrayList<TopicOverviewType>(topicEntities.size());
		
		for (TopicsEntity topicEntity : topicEntities) {
			topics.add(mapToSwaggerTopic(topicEntity));
		}
		return topics;
	}

	public static TopicOverviewType mapToSwaggerTopic(TopicsEntity topicEntity) {
		TopicOverviewType topic = new TopicOverviewType(topicEntity.getTopicId());
		
		topic.setTopicName(topicEntity.getTopicName());
		topic.setTopicDescription(topicEntity.getTopicDescription());
		topic.setTopicType(topicEntity.getTopicType());
		topic.setTopicResource(topicEntity.getTopicResource());
		topic.setSubTopics(mapToSwaggerSubTopics(topicEntity.getSubTopics()));
		
		return topic;
	}

	private static List<TopicOverviewType> mapToSwaggerSubTopics(Collection<TopicsEntity> subTopics) {
		ArrayList<TopicOverviewType> swaggerSubTopics = new ArrayList<TopicOverviewType>();
		
		for (TopicsEntity topicEntityType : subTopics) {
			TopicOverviewType swaggerSubTopic = new TopicOverviewType(topicEntityType.getTopicId());
			swaggerSubTopic.setTopicName(topicEntityType.getTopicName());
			swaggerSubTopic.setTopicDescription(topicEntityType.getTopicDescription());
			swaggerSubTopic.setTopicType(topicEntityType.getTopicType());
			swaggerSubTopic.setSubTopics(mapToSwaggerSubTopics(topicEntityType.getSubTopics()));
			
			swaggerSubTopics.add(swaggerSubTopic);
		}
		return swaggerSubTopics;
	}

}
