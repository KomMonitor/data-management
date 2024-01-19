package de.hsbo.kommonitor.datamanagement.api.impl.topics;

public class TopicsHelper {
	
	private static TopicsRepository topicsRepo;

	public TopicsHelper(TopicsRepository topicsRepository) {
		topicsRepo = topicsRepository;
	}

	public static TopicsEntity getTopicByName(String topic) throws Exception {
		if (topicsRepo.existsByTopicName(topic))
			return topicsRepo.findByTopicName(topic);
		else
			throw new Exception("Topic with name " + topic + " does not exist.");
	}

}
