package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;

public interface SpatialUnitsMetadataRepository extends JpaRepository<MetadataSpatialUnitsEntity, Long> {
	MetadataSpatialUnitsEntity findBySpatialUnitId(String spatialUnitId);
	
	MetadataSpatialUnitsEntity findByDatasetId(String datasetId);

	boolean existsByDatasetId(String datasetId);

	boolean existsByDatasetName(String datasetName);

	

}
