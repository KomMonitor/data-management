package de.hsbo.kommonitor.datamanagement.api.impl.users;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLJoinTableRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity(name = "UserInfo")
public class UserInfo {

    @Id
    @UuidGenerator
    private String userInfoId = null;

    @Column(nullable = false, unique = true)
    private String keycloakId;

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    @ManyToMany
    @JoinTable(name="georesources_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="dataset_id",referencedColumnName="datasetid"))
//    @SQLJoinTableRestriction( "type = 'GEORESOURCE' ")
    private List<MetadataGeoresourcesEntity> georesourcesEntities;

    @ManyToMany
    @JoinTable(name="indicators_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="dataset_id",referencedColumnName="datasetid"))
//    @SQLJoinTableRestriction( "type = 'INDICATOR' ")
    private List<MetadataIndicatorsEntity> indicatorEntities;

    @ManyToMany
    @JoinTable(name="topics_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="topic_id",referencedColumnName="topicId"))
//    @SQLJoinTableRestriction( "type = 'GEORESOURCE_TOPIC' ")
    private List<TopicsEntity> topicsEntities;
}
