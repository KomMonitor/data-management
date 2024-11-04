package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;

public interface GeoresourcesMetadataRepository extends JpaRepository<MetadataGeoresourcesEntity, Long> {

	MetadataGeoresourcesEntity findByDatasetId(String datasetId);

	MetadataGeoresourcesEntity findByDatasetName(String datasetName);

	boolean existsByDatasetName(String datasetName);

	boolean existsByDatasetId(String datasetId);

	void deleteByDatasetId(String datasetId);

}
