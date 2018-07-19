package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.features.management.GeoJSON2DatabaseTool;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;

public class SpatialUnitsMapper {

	public static SpatialUnitOverviewType mapToSwaggerSpatialUnit(MetadataSpatialUnitsEntity georesourcesEntities) {
		SpatialUnitOverviewType dataset = new SpatialUnitOverviewType();
		
		dataset.setAvailablePeriodOfValidity(GeoJSON2DatabaseTool.getAvailablePeriodOfValidity(georesourcesEntities.getDbTableName()));
		
		CommonMetadataType commonMetadata = new CommonMetadataType();
		commonMetadata.setContact(georesourcesEntities.getContact());
		commonMetadata.setDatasource(georesourcesEntities.getDataSource());
		commonMetadata.setDescription(georesourcesEntities.getDescription());
		commonMetadata.setLastUpdate(new java.sql.Date(georesourcesEntities.getLastUpdate().getTime()).toLocalDate());
		commonMetadata.setSridEPSG(new BigDecimal(georesourcesEntities.getSridEpsg()));
		commonMetadata.setUpdateInterval(georesourcesEntities.getUpdateIntervall());
		dataset.setMetadata(commonMetadata);
		
		dataset.setNextLowerHierarchyLevel(georesourcesEntities.getNextLowerHierarchyLevel());
		dataset.setNextUpperHierarchyLevel(georesourcesEntities.getNextUpperHierarchyLevel());
		dataset.setSpatialUnitLevel(georesourcesEntities.getDatasetName());

		return dataset;
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
