package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import de.hsbo.kommonitor.datamanagement.model.TopicOrderModeEnum;
import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicsOrderModeRepository extends JpaRepository<TopicsOrderModeEntity, Long> {

	TopicsOrderModeEntity findByTopicResource(TopicResourceEnum topicResource);

	List<TopicsOrderModeEntity> findByOrderMode(TopicOrderModeEnum orderMode);

	boolean existsByTopicResource(TopicResourceEnum topicResource);

}