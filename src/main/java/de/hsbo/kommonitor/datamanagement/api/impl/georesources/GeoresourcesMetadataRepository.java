package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;

public interface GeoresourcesMetadataRepository extends JpaRepository<MetadataGeoresourcesEntity, Long> {

	MetadataGeoresourcesEntity findByDatasetId(String datasetId);

}
