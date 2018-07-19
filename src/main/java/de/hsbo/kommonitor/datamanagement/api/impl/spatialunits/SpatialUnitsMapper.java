package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.users.UserOverviewType;
import de.hsbo.kommonitor.datamanagement.model.users.UsersEntity;

public class SpatialUnitsMapper {

	public static SpatialUnitOverviewType mapToSwaggerSpatialUnit(MetadataSpatialUnitsEntity georesourcesEntities) {
		SpatialUnitOverviewType dataset = new SpatialUnitOverviewType();
		
//		dataset.setAvailablePeriodsOfValidity(availablePeriodsOfValidity);

		return null;
	}

	public static List<SpatialUnitOverviewType> mapToSwaggerSpatialUnits(
			List<MetadataSpatialUnitsEntity> spatialUnitMeatadataEntities) {
		List<SpatialUnitOverviewType> metadatasets = new ArrayList<SpatialUnitOverviewType>(spatialUnitMeatadataEntities.size());

		for (MetadataSpatialUnitsEntity metadataEntity : spatialUnitMeatadataEntities) {
			metadatasets.add(mapToSwaggerSpatialUnit(metadataEntity));
		}
		return metadatasets;
	}
}
