package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitsEntity;

public interface SpatialUnitsRepository extends JpaRepository<SpatialUnitsEntity, Long> {
	SpatialUnitsEntity findBySpatialUnitId(String spatialUnitId);
	
	
}
