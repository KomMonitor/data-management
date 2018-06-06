package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public interface TopicsRepository extends JpaRepository<TopicsEntity, Long> {
	TopicsEntity findByTopicId(String topicId);
    
	TopicsEntity findByTopicName(String topicName);
    
    boolean existsByTopicId(String topicId);
    
    boolean existsByTopicName(String topicName);
    
    void deleteByTopicId(String topicId);
}