package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;

public interface SpatialUnitsMetadataRepository extends JpaRepository<MetadataSpatialUnitsEntity, Long> {
	MetadataSpatialUnitsEntity findByDatasetId(String datasetId);

	MetadataSpatialUnitsEntity findByDatasetName(String datasetName);

	/**
	 * Spatial unit names are unique only within a mandant, so name-based lookups
	 * must be scoped to a mandant to be unambiguous.
	 */
	MetadataSpatialUnitsEntity findByDatasetNameAndMandant_OrganizationalUnitId(String datasetName, String mandantId);

	List<MetadataSpatialUnitsEntity> findByMandant_OrganizationalUnitId(String mandantId);

	boolean existsByDatasetId(String datasetId);

	boolean existsByDatasetName(String datasetName);

	boolean existsByDatasetNameAndMandant_OrganizationalUnitId(String datasetName, String mandantId);

	void deleteByDatasetName(String datasetName);

	void deleteByDatasetId(String datasetId);

}
