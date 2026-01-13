package de.hsbo.kommonitor.datamanagement.api.impl.database;

import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
@Component
public class LastModificationManager {

	@Autowired
	LastModificationRepository lastModificationRepo;

	public void updateLastDatabaseModificationIndicators() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
        } else {
            lastModificationEntity = new LastModificationEntity();
        }
        lastModificationEntity.setIndicators(new Date());
        lastModificationRepo.saveAndFlush(lastModificationEntity);

    }

	public void updateLastDatabaseModificationGeoresources() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
        } else {
            lastModificationEntity = new LastModificationEntity();
        }
        lastModificationEntity.setGeoresources(new Date());
        lastModificationRepo.saveAndFlush(lastModificationEntity);

    }

	public void updateLastDatabaseModificationSpatialUnits() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
        } else {
            lastModificationEntity = new LastModificationEntity();
        }
        lastModificationEntity.setSpatialUnits(new Date());
        lastModificationRepo.saveAndFlush(lastModificationEntity);

    }

	public void updateLastDatabaseModificationAccessControl() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
        } else {
            lastModificationEntity = new LastModificationEntity();
        }
        lastModificationEntity.setAccessControl(new Date());
        lastModificationRepo.saveAndFlush(lastModificationEntity);

    }

	public void updateLastDatabaseModificationTopics() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
        } else {
            lastModificationEntity = new LastModificationEntity();
        }
        lastModificationEntity.setTopics(new Date());
        lastModificationRepo.saveAndFlush(lastModificationEntity);

    }

	public void updateLastDatabaseModificationProcessScripts() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
        } else {
            lastModificationEntity = new LastModificationEntity();
        }
        lastModificationEntity.setProcessScripts(new Date());
        lastModificationRepo.saveAndFlush(lastModificationEntity);

    }

	public void updateLastDatabaseModificationWebServices() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		LastModificationEntity lastModificationEntity;
		if (!all.isEmpty()) {
			lastModificationEntity = all.get(0);
		} else {
			lastModificationEntity = new LastModificationEntity();
		}
		lastModificationEntity.setWebServices(new Date());
		lastModificationRepo.saveAndFlush(lastModificationEntity);
	}

	public LastModificationEntity getLastModificationInfo() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		if (!all.isEmpty()) {
            return checkLastModificationEntity();
		} else {
			checkLastModificationEntity();
			return getLastModificationInfo();
		}
	}

	private LastModificationEntity checkLastModificationEntity() {
		List<LastModificationEntity> all = lastModificationRepo.findAll();

		Date now = new Date();
        LastModificationEntity lastModificationEntity;
        if (!all.isEmpty()) {
            lastModificationEntity = all.get(0);
			
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

        } else {
            lastModificationEntity = new LastModificationEntity(now);
        }
        lastModificationRepo.saveAndFlush(lastModificationEntity);
        return lastModificationEntity;
    }

}
