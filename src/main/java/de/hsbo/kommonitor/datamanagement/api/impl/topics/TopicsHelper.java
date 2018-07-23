package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import org.springframework.beans.factory.annotation.Autowired;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public class TopicsHelper {
	
	@Autowired
	private static TopicsRepository topicsRepo;

	public static TopicsEntity getTopicByName(String topic) throws Exception {
		if (topicsRepo.existsByTopicName(topic))
			return topicsRepo.findByTopicName(topic);
		else
			throw new Exception("Topic with name " + topic + " does not exist.");
	}

}
