package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import java.util.ArrayList;
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
		
		return topic;
	}

}
