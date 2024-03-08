package de.hsbo.kommonitor.datamanagement.api.impl.topics;

import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.TopicTypeEnum;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "Topics")
public class TopicsEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String topicId = null;

    private String topicName = null;

    @Column(columnDefinition = "text")
    private String topicDescription = null;

    private TopicTypeEnum topicType = null;

    private TopicResourceEnum topicResource = null;

    private boolean defaultPermissionsIsPublic = false;

    @ElementCollection
    private List<String> defaultPermissions = new ArrayList<>();
    @ElementCollection()
//	  @OnDelete(action= OnDeleteAction.CASCADE)
//	  @JoinColumn(name = "main_topic_id", referencedColumnName = "topicId")
    @CollectionTable(name = "topics_subtopics", joinColumns = @JoinColumn(name = "main_topic_id", referencedColumnName = "topicId"))
    @Column(name = "sub_topic")
    private Collection<TopicsEntity> subTopics;


    /*
     * default constructor is required by hibernate / jpa
     */
    public TopicsEntity() {

    }


    public String getTopicId() {
        return topicId;
    }

    public TopicsEntity topicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public TopicsEntity topicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
        return this;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }


    public TopicTypeEnum getTopicType() {
        return topicType;
    }


    public void setTopicType(TopicTypeEnum topicType) {
        this.topicType = topicType;
    }


    public TopicResourceEnum getTopicResource() {
        return topicResource;
    }


    public void setTopicResource(TopicResourceEnum topicResource) {
        this.topicResource = topicResource;
    }


    public Collection<TopicsEntity> getSubTopics() {
        return subTopics;
    }


    public void setSubTopics(Collection<TopicsEntity> subTopics) {
        this.subTopics = subTopics;
    }

    public void addSubTopicsIfNotExists(Collection<TopicsEntity> subTopics) {
        for (TopicsEntity topicsEntity : subTopics) {
            if (!this.subTopics.contains(topicsEntity)) {
                this.subTopics.add(topicsEntity);
            }
        }
    }

    public void addSubTopicIfNotExists(TopicsEntity subTopic) {
        if (!this.subTopics.contains(subTopic)) {
            this.subTopics.add(subTopic);
        }
    }

    public boolean isDefaultPermissionsPublic() {
        return defaultPermissionsIsPublic;
    }

    public void setDefaultPermissionsIsPublic(boolean defaultPermissionsIsPublic) {
        this.defaultPermissionsIsPublic = defaultPermissionsIsPublic;
    }

    public List<String> getDefaultPermissions() {
        return defaultPermissions;
    }

    public void setDefaultPermissions(List<String> defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }

}
