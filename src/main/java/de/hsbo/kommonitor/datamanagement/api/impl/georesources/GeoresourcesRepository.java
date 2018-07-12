package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcesEntity;

public interface GeoresourcesRepository extends JpaRepository<GeoresourcesEntity, Long> {
	GeoresourcesEntity findByGeoresourceId(String georesourceId);
	
	
}
