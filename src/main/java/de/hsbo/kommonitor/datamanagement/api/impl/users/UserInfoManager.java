package de.hsbo.kommonitor.datamanagement.api.impl.users;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.AccessControlMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.RolesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsRepository;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Repository
@Component
public class UserInfoManager {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    GeoresourcesMetadataRepository georesourcesRepository;

    @Autowired
    IndicatorsMetadataRepository indicatorsRepository;

    @Autowired
    TopicsRepository topicsRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoManager.class);

    public UserInfoOverviewType addUserInfo(UserInfoInputType inputUserInfo, AuthInfoProvider provider) throws Exception {
        String keycloakUserId = provider.getUserId();
        LOG.info("Trying to create user info for user with ID '{}'", keycloakUserId);

        UserInfoEntity userInfoEntity = userInfoRepository.findByKeycloakId(keycloakUserId);
        if(userInfoEntity != null) {
            LOG.error("User infos for user with ID '{}' already exists. Thus aborting add UserInfo request.", keycloakUserId);
            throw new Exception("User info already exists. Aborting addUserInfo request.");
        }

        userInfoEntity = createUserInfoEntity(keycloakUserId, inputUserInfo);

        return UserInfoMapper.mapToSwaggerOrganizationalUnit(userInfoEntity);
    }

    public UserInfoOverviewType getUserInfoByUserInfoId(String userInfoId, AuthInfoProvider authInfoProvider)
            throws Exception {
        LOG.info("Retrieving user info USer Info ID '{}'", userInfoId);

        UserInfoEntity userInfoEntity = userInfoRepository.findByUserInfoId(userInfoId);

        if (userInfoEntity == null || authInfoProvider == null || !userInfoEntity.getKeycloakId().equals(authInfoProvider.getUserId())) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested user info '%s' was not found.", userInfoId));
        }

        return UserInfoMapper.mapToSwaggerOrganizationalUnit(userInfoEntity);
    }

    public UserInfoOverviewType getUserInfoByKeycloakId(String keycloakId, AuthInfoProvider authInfoProvider)
            throws Exception {
        LOG.info("Retrieving user info Keycloak ID '{}'", keycloakId);

        UserInfoEntity userInfoEntity = userInfoRepository.findByKeycloakId(keycloakId);

        if (userInfoEntity == null || authInfoProvider == null || !keycloakId.equals(authInfoProvider.getUserId())) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested user info '%s' was not found.", keycloakId));
        }

        return UserInfoMapper.mapToSwaggerOrganizationalUnit(userInfoEntity);
    }

    private UserInfoEntity createUserInfoEntity(String keycloakUserId, UserInfoInputType inputUserInfo) throws Exception{
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setKeycloakId(keycloakUserId);
        if(inputUserInfo.getGeoresourceFavourites() != null) {
            userInfoEntity.setGeoresourceFavourites(retrieveGeoresources(inputUserInfo.getGeoresourceFavourites()));
        } else {
            userInfoEntity.setGeoresourceFavourites(Collections.emptyList());
        }

        if(inputUserInfo.getIndicatorFavourites() != null) {
            userInfoEntity.setIndicatorFavourites(retrieveIndicators(inputUserInfo.getIndicatorFavourites()));
        } else {
            userInfoEntity.setGeoresourceFavourites(Collections.emptyList());
        }

        Collection<TopicsEntity> indicatorTopicsList;
        Collection<TopicsEntity> georesourceTopicsList;
        if(inputUserInfo.getIndicatorFavourites() != null) {
            indicatorTopicsList = retrieveTopics(inputUserInfo.getIndicatorTopicFavourites(), TopicResourceEnum.INDICATOR);
        } else {
            indicatorTopicsList = Collections.emptyList();
        }
        if(inputUserInfo.getGeoresourceTopicFavourites() != null) {
            georesourceTopicsList = retrieveTopics(inputUserInfo.getGeoresourceTopicFavourites(), TopicResourceEnum.GEORESOURCE);
        } else {
            georesourceTopicsList = Collections.emptyList();
        }
        List<TopicsEntity> topicsList = Stream.concat(indicatorTopicsList.stream(), georesourceTopicsList.stream()).toList();
        userInfoEntity.setTopicFavourites(topicsList);
        userInfoRepository.saveAndFlush(userInfoEntity);
        return userInfoEntity;
    }

    private Collection<MetadataGeoresourcesEntity> retrieveGeoresources(List<String> georesourceIds)
            throws ResourceNotFoundException {
        Collection<MetadataGeoresourcesEntity> georesourcesList = new ArrayList<>();
        for (String id : georesourceIds) {
            MetadataGeoresourcesEntity georesourceEntity = georesourcesRepository.findByDatasetId(id);
            if (georesourceEntity == null) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                        String.format("The requested georesource '%s' was not found.", id));
            }
            if (!georesourcesList.contains(georesourceEntity)) {
                georesourcesList.add(georesourceEntity);
            }
        }
        return georesourcesList;
    }

    private Collection<MetadataIndicatorsEntity> retrieveIndicators(List<String> indicatorIds)
            throws ResourceNotFoundException {
        Collection<MetadataIndicatorsEntity> indicatorsList = new ArrayList<>();
        for (String id : indicatorIds) {
            MetadataIndicatorsEntity indicatorEntity = indicatorsRepository.findByDatasetId(id);
            if (indicatorEntity == null) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                        String.format("The requested indicator '%s' was not found.", id));
            }
            if (!indicatorsList.contains(indicatorEntity)) {
                indicatorsList.add(indicatorEntity);
            }
        }
        return indicatorsList;
    }

    private Collection<TopicsEntity> retrieveTopics(List<String> topicIds, TopicResourceEnum topicResource) throws ResourceNotFoundException {
        Collection<TopicsEntity> topicsList = new ArrayList<>();
        for (String id : topicIds) {
            TopicsEntity topicEntity = topicsRepository.findByTopicId(id);
            if (topicEntity == null || topicEntity.getTopicResource()!= topicResource) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                        String.format("The requested topic '%s' was not found.", id));
            }
            if (!topicsList.contains(topicEntity)) {
                topicsList.add(topicEntity);
            }
        }
        return topicsList;
    }

}
