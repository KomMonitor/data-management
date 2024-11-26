package de.hsbo.kommonitor.datamanagement.api.impl.users;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.AbstractMetadata;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsEntity;
import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.UserInfoOverviewType;

import java.util.Collection;
import java.util.List;

public class UserInfoMapper {
    public static UserInfoOverviewType mapToSwaggerUserInfo(UserInfoEntity userInfoEntity) {
        UserInfoOverviewType userInfo = new UserInfoOverviewType();
        userInfo.setUserInfoId(userInfoEntity.getUserInfoId());
        userInfo.setKeycloakId(userInfoEntity.getKeycloakId());
        userInfo.setGeoresourceFavourites(mapGeoresourceFavourites(userInfoEntity.getGeoresourceFavourites()));
        userInfo.setIndicatorFavourites(mapIndicatorFavourites(userInfoEntity.getIndicatorFavourites()));
        userInfo.setGeoresourceTopicFavourites(mapGeoresourceTopicFavourites(userInfoEntity.getTopicFavourites()));
        userInfo.setIndicatorFavourites(mapIndicatorTopicFavourites(userInfoEntity.getTopicFavourites()));
        return userInfo;
    }

    private static List<String> mapGeoresourceFavourites(Collection<MetadataGeoresourcesEntity> georesourceFavourites) {
        return georesourceFavourites.stream().map(AbstractMetadata::getDatasetId).toList();
    }

    private static List<String> mapIndicatorFavourites(Collection<MetadataIndicatorsEntity> indicatorFavourites) {
        return indicatorFavourites.stream().map(AbstractMetadata::getDatasetId).toList();
    }

    private static List<String> mapGeoresourceTopicFavourites(Collection<TopicsEntity> georesourceTopicFavourites) {
        return georesourceTopicFavourites.stream()
                .filter(i -> i.getTopicResource() == TopicResourceEnum.GEORESOURCE)
                .map(TopicsEntity::getTopicId).toList();
    }

    private static List<String> mapIndicatorTopicFavourites(Collection<TopicsEntity> indicatorTopicFavourites) {
        return indicatorTopicFavourites.stream()
                .filter(i -> i.getTopicResource() == TopicResourceEnum.INDICATOR)
                .map(TopicsEntity::getTopicId).toList();
    }

    public static List<UserInfoOverviewType> mapToSwaggerUserInfo(List<UserInfoEntity> userInfoEntityList) {
        return userInfoEntityList.stream().map(UserInfoMapper::mapToSwaggerUserInfo).toList();
    }
}
