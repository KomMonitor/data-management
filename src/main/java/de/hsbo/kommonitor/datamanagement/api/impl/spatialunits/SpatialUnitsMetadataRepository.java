package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;

public interface SpatialUnitsMetadataRepository extends JpaRepository<MetadataSpatialUnitsEntity, Long> {
	MetadataSpatialUnitsEntity findByDatasetId(String datasetId);
	
	MetadataSpatialUnitsEntity findByDatasetName(String datasetName);
	
	List<MetadataSpatialUnitsEntity> findByNextLowerHierarchyLevel(String hierarchyLevel);
	
	List<MetadataSpatialUnitsEntity> findByNextUpperHierarchyLevel(String hierarchyLevel);

	boolean existsByDatasetId(String datasetId);

	boolean existsByDatasetName(String datasetName);

	void deleteByDatasetName(String datasetName);

	void deleteByDatasetId(String datasetId);

	

}
