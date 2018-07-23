package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;

public interface IndicatorsMetadataRepository extends JpaRepository<MetadataSpatialUnitsEntity, Long> {
	MetadataSpatialUnitsEntity findByDatasetId(String datasetId);
	
	MetadataSpatialUnitsEntity findByDatasetName(String datasetName);

	boolean existsByDatasetId(String datasetId);

	boolean existsByDatasetName(String datasetName);

	void deleteByDatasetName(String datasetName);

	

}