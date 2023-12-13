package de.hsbo.kommonitor.datamanagement.api.impl.database;

import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
@Component
public class LastModificationManager {

	private static Logger logger = LoggerFactory.getLogger(LastModificationManager.class);

	@Autowired
	LastModificationRepository lastModificationRepo;

	public void updateLastDatabaseModification_indicators() throws Exception {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);

			lastModificationEntity.setIndicators(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity();

			lastModificationEntity.setIndicators(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		}

	}

	public void updateLastDatabaseModification_georesources() throws Exception {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);

			lastModificationEntity.setGeoresources(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity();

			lastModificationEntity.setGeoresources(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		}

	}

	public void updateLastDatabaseModification_spatialUnits() throws Exception {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);

			lastModificationEntity.setSpatialUnits(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity();

			lastModificationEntity.setSpatialUnits(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		}

	}

	public void updateLastDatabaseModification_accessControl() throws Exception {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);

			lastModificationEntity.setAccessControl(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity();

			lastModificationEntity.setAccessControl(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		}

	}

	public void updateLastDatabaseModification_topics() throws Exception {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);

			lastModificationEntity.setTopics(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity();

			lastModificationEntity.setTopics(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		}

	}

	public void updateLastDatabaseModification_processScripts() throws Exception {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);

			lastModificationEntity.setProcessScripts(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity();

			lastModificationEntity.setProcessScripts(new Date());
			lastModificationRepo.saveAndFlush(lastModificationEntity);
		}

	}

	public LastModificationEntity getLastModifcationInfo() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);
			
			lastModificationEntity = checkLastModificationEntity();

			return lastModificationEntity;
		} else {
			checkLastModificationEntity();
			return getLastModifcationInfo();
		}
	}

	private LastModificationEntity checkLastModificationEntity() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		Date now = new Date();
		if (all.size() > 0) {
			LastModificationEntity lastModificationEntity = all.get(0);
			
			if (lastModificationEntity.getGeoresources() == null) {
				lastModificationEntity.setGeoresources(now);
			}
			if (lastModificationEntity.getIndicators() == null) {
				lastModificationEntity.setIndicators(now);
			}
			if (lastModificationEntity.getSpatialUnits() == null) {
				lastModificationEntity.setSpatialUnits(now);
			}
			if (lastModificationEntity.getTopics() == null) {
				lastModificationEntity.setTopics(now);
			}
			if (lastModificationEntity.getAccessControl() == null) {
				lastModificationEntity.setAccessControl(now);
			}
			if (lastModificationEntity.getProcessScripts() == null) {
				lastModificationEntity.setProcessScripts(now);
			}
			
			lastModificationRepo.saveAndFlush(lastModificationEntity);
			return lastModificationEntity;
		} else {
			LastModificationEntity lastModificationEntity = new LastModificationEntity(now);			

			lastModificationRepo.saveAndFlush(lastModificationEntity);
			
			return lastModificationEntity;
		}
	}

}
