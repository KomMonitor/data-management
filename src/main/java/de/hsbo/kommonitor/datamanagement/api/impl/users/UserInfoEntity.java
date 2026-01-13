package de.hsbo.kommonitor.datamanagement.api.impl.users;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataWebServicesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;
import java.util.List;

@Entity(name = "UserInfo")
public class UserInfoEntity {

    @Id
    @UuidGenerator
    private String userInfoId = null;

    @Column(nullable = false, unique = true)
    private String keycloakId;

    @ManyToMany
    @JoinTable(name="georesources_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="dataset_id",referencedColumnName="datasetid"))
    private Collection<MetadataGeoresourcesEntity> georesourceFavourites;

    @ManyToMany
    @JoinTable(name="indicators_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="dataset_id",referencedColumnName="datasetid"))
    private Collection<MetadataIndicatorsEntity> indicatorFavourites;

    @ManyToMany
    @JoinTable(name="topics_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="topic_id",referencedColumnName="topicId"))
    private Collection<TopicsEntity> topicFavourites;

    @ManyToMany
    @JoinTable(name="webservices_favourites",
            joinColumns = @JoinColumn(name="user_id",referencedColumnName="userInfoId"),
            inverseJoinColumns = @JoinColumn(name="webservice_id",referencedColumnName="id"))
    private Collection<MetadataWebServicesEntity> webServiceFavourites;

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

    public Collection<MetadataGeoresourcesEntity> getGeoresourceFavourites() {
        return georesourceFavourites;
    }

    public void setGeoresourceFavourites(Collection<MetadataGeoresourcesEntity> georesourceFavourites) {
        this.georesourceFavourites = georesourceFavourites;
    }

    public void removeGeoresourceFavourite(MetadataGeoresourcesEntity entity) {
        this.georesourceFavourites.remove(entity);
    }

    public Collection<MetadataIndicatorsEntity> getIndicatorFavourites() {
        return indicatorFavourites;
    }

    public void setIndicatorFavourites(Collection<MetadataIndicatorsEntity> indicatorFavourites) {
        this.indicatorFavourites = indicatorFavourites;
    }

    public void removeIndicatorFavourite(MetadataIndicatorsEntity entity) {
        this.indicatorFavourites.remove(entity);
    }

    public Collection<TopicsEntity> getTopicFavourites() {
        return topicFavourites;
    }

    public void setTopicFavourites(Collection<TopicsEntity> topicFavourites) {
        this.topicFavourites = topicFavourites;
    }

    public void removeTopicFavourite(TopicsEntity entity) {
        this.topicFavourites.remove(entity);
    }

    public Collection<MetadataWebServicesEntity> getWebServiceFavourites() {
        return webServiceFavourites;
    }

    public void setWebServiceFavourites(Collection<MetadataWebServicesEntity> webServiceFavourites) {
        this.webServiceFavourites = webServiceFavourites;
    }

    public void removeWebServiceFavourite(MetadataWebServicesEntity entity) {
        this.webServiceFavourites.remove(entity);
    }
}
