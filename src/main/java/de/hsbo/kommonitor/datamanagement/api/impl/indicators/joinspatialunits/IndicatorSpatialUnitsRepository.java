package de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorSpatialUnitsRepository extends JpaRepository<IndicatorSpatialUnitJoinEntity, Long> {
	List<IndicatorSpatialUnitJoinEntity> findByIndicatorMetadataId(String indicatorMetadataId);

	List<IndicatorSpatialUnitJoinEntity> findBySpatialUnitId(String spatialUnitId);

	List<IndicatorSpatialUnitJoinEntity> findBySpatialUnitName(String spatialUnitName);

	IndicatorSpatialUnitJoinEntity findByIndicatorMetadataIdAndSpatialUnitName(String indicatorMetadataId,
			String spatialUnitName);

	IndicatorSpatialUnitJoinEntity findByIndicatorMetadataIdAndSpatialUnitId(String indicatorId, String spatialUnitId);

	boolean existsByIndicatorMetadataId(String indicatorMetadataId);

	boolean existsBySpatialUnitId(String spatialUnitId);

	boolean existsBySpatialUnitName(String spatialUnitName);

	boolean existsByIndicatorMetadataIdAndSpatialUnitName(String indicatorMetadataId, String spatialUnitName);

	boolean existsByIndicatorNameAndSpatialUnitName(String indicatorName, String spatialUnitName);

	boolean existsByIndicatorMetadataIdAndSpatialUnitId(String indicatorId, String spatialUnitId);

	void deleteByIndicatorMetadataId(String indicatorMetadataId);

	void deleteBySpatialUnitId(String spatialUnitId);

	void deleteBySpatialUnitName(String spatialUnitName);

	void deleteByIndicatorMetadataIdAndSpatialUnitName(String indicatorMetadataId, String spatialUnitName);
	
	void deleteByIndicatorMetadataIdAndSpatialUnitId(String indicatorMetadataId, String spatialUnitId);

}
