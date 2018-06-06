package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;

public interface TopicsRepository extends JpaRepository<TopicOverviewType, Long> {
    TopicOverviewType findByTopicId(String topicId);
    
    TopicOverviewType findByTopicName(String topicName);
    
    boolean existsByTopicId(String topicId);
    
    void deleteByTopicId(String topicId);
}