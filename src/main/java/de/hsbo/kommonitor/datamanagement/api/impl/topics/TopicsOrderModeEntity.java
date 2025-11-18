package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import de.hsbo.kommonitor.datamanagement.model.TopicOrderModeEnum;
import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
import jakarta.persistence.*;

@Entity(name = "topicordermode")
public class TopicsOrderModeEntity {

    @Enumerated(EnumType.ORDINAL)
    @Column()
    @Id
    private TopicResourceEnum topicResource = null;

    @Enumerated(EnumType.ORDINAL)
    @Column()
    private TopicOrderModeEnum orderMode = null;


    public TopicsOrderModeEntity() {

    }

    public TopicResourceEnum getTopicResource() {
        return topicResource;
    }

    public void setTopicResource(TopicResourceEnum topicResource) {
        this.topicResource = topicResource;
    }

    public TopicOrderModeEnum getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(TopicOrderModeEnum orderMode) {
        this.orderMode = orderMode;
    }
}
