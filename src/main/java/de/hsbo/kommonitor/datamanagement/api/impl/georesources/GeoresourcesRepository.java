package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public interface GeoresourcesRepository extends JpaRepository<TopicsEntity, Long> {
	GeoresourcesEntity findByGeoresourceId(String georesourceId);
	
	
}
