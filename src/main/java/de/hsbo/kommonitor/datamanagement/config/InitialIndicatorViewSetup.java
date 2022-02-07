package de.hsbo.kommonitor.datamanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsManager;
import de.hsbo.kommonitor.datamanagement.api.impl.roles.RolesRepository;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Component
public class InitialIndicatorViewSetup implements ApplicationListener<ContextRefreshedEvent> {

	Logger logger = LoggerFactory.getLogger(InitialIndicatorViewSetup.class);

	boolean alreadySetup = false;

	@Autowired
	private IndicatorsManager indicatorsManager;
	
	@Value("${kommonitor.recreateAllViewsOnStartup:false}")
	private boolean isRecreateAllViewsOnStartup;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if(isRecreateAllViewsOnStartup) {
			logger.info("Begin initial recreation of indicator views to ensure that modified view definition is applied.");

			try {
				indicatorsManager.recreateAllViews();
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Recreation of indicator views failed. Error message: " + e.getMessage());
			}

			logger.info("Initial recreation of indicator views finished.");
		}
		else {
			logger.info("Initial recreation of indicator views is skipped according to config parameter 'kommonitor.recreateAllViewsOnStartup'.");
		}
	}

}
