package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;

public class SpatialUnitsMapper {

	public static SpatialUnitOverviewType mapToSwaggerSpatialUnit(MetadataSpatialUnitsEntity spatialUnitEntity) throws IOException, SQLException {
		SpatialUnitOverviewType dataset = new SpatialUnitOverviewType();
		
		dataset.setSpatialUnitId(spatialUnitEntity.getDatasetId());
		
		dataset.setAvailablePeriodOfValidity(SpatialFeatureDatabaseHandler.getAvailablePeriodOfValidity(spatialUnitEntity.getDbTableName()));
		
		CommonMetadataType commonMetadata = new CommonMetadataType();
		commonMetadata.setContact(spatialUnitEntity.getContact());
		commonMetadata.setDatasource(spatialUnitEntity.getDataSource());
		commonMetadata.setDescription(spatialUnitEntity.getDescription());
		commonMetadata.setLastUpdate(DateTimeUtil.toLocalDate(spatialUnitEntity.getLastUpdate()));
		commonMetadata.setSridEPSG(new BigDecimal(spatialUnitEntity.getSridEpsg()));
		commonMetadata.setUpdateInterval(spatialUnitEntity.getUpdateIntervall());
		dataset.setMetadata(commonMetadata);
		
		dataset.setNextLowerHierarchyLevel(spatialUnitEntity.getNextLowerHierarchyLevel());
		dataset.setNextUpperHierarchyLevel(spatialUnitEntity.getNextUpperHierarchyLevel());
		dataset.setSpatialUnitLevel(spatialUnitEntity.getDatasetName());
		
		dataset.setWmsUrl(spatialUnitEntity.getWmsUrl());
		dataset.setWfsUrl(spatialUnitEntity.getWfsUrl());

		return dataset;
	}

	public static List<SpatialUnitOverviewType> mapToSwaggerSpatialUnits(
			List<MetadataSpatialUnitsEntity> spatialUnitMeatadataEntities) throws IOException, SQLException {
		List<SpatialUnitOverviewType> metadatasets = new ArrayList<SpatialUnitOverviewType>(spatialUnitMeatadataEntities.size());

		for (MetadataSpatialUnitsEntity metadataEntity : spatialUnitMeatadataEntities) {
			metadatasets.add(mapToSwaggerSpatialUnit(metadataEntity));
		}
		return metadatasets;
	}
}
