package de.hsbo.kommonitor.datamanagement.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsRepository;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

@Component
public class InitialTopicsSetup implements ApplicationListener<ContextRefreshedEvent> {

	Logger logger = LoggerFactory.getLogger(InitialTopicsSetup.class);

	boolean alreadySetup = false;

	@Autowired
	private TopicsRepository topicsRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		logger.info("Begin initial setup of default topics for KomMonitor.");

		if (alreadySetup)
			return;

		TopicsEntity social = new TopicsEntity();
		social.setTopicName(InitialTopicsSetupConstants.SOCIAL_NAME);
		social.setTopicDescription(InitialTopicsSetupConstants.SOCIAL_DESCRIPTION);

		TopicsEntity demography = new TopicsEntity();
		demography.setTopicName(InitialTopicsSetupConstants.DEMOGRAPHY_NAME);
		demography.setTopicDescription(InitialTopicsSetupConstants.DEMOGRAPHY_DESCRIPTION);

		TopicsEntity environment = new TopicsEntity();
		environment.setTopicName(InitialTopicsSetupConstants.ENVIRONMENT_NAME);
		environment.setTopicDescription(InitialTopicsSetupConstants.ENVIRONMENT_DESCRIPTION);

		TopicsEntity habitation = new TopicsEntity();
		habitation.setTopicName(InitialTopicsSetupConstants.HABITATION_NAME);
		habitation.setTopicDescription(InitialTopicsSetupConstants.HABITATION_DESCRIPTION);

		TopicsEntity migration = new TopicsEntity();
		migration.setTopicName(InitialTopicsSetupConstants.MIGRATION_NAME);
		migration.setTopicDescription(InitialTopicsSetupConstants.MIGRATION_DESCRIPTION);

		if (!topicsRepository.existsByTopicName(social.getTopicName()))
			topicsRepository.save(social);
		if (!topicsRepository.existsByTopicName(demography.getTopicName()))
			topicsRepository.save(demography);
		if (!topicsRepository.existsByTopicName(environment.getTopicName()))
			topicsRepository.save(environment);
		if (!topicsRepository.existsByTopicName(habitation.getTopicName()))
			topicsRepository.save(habitation);
		if (!topicsRepository.existsByTopicName(migration.getTopicName()))
			topicsRepository.save(migration);

		alreadySetup = true;

		logger.info("Initial setup of default topics was succesful. Created following topics");
		List<TopicsEntity> topics = topicsRepository.findAll();
		for (TopicsEntity topicsEntity : topics) {
			logger.info("Created initial topic with name {}", topicsEntity.getTopicName());
		}

	}
}
