package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.PeriodOfValidityEntity_spatialUnits;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.SpatialUnitsPeriodsOfValidityRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;

public class SpatialUnitsMapper {
	
	private static SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

	private static SpatialUnitsPeriodsOfValidityRepository periodsOfValidityRepo;

	public SpatialUnitsMapper(SpatialUnitsMetadataRepository spatialUnitsRepo,
			SpatialUnitsPeriodsOfValidityRepository spatialUnitsPeriodsOfValidityRepo) {
		spatialUnitsMetadataRepo = spatialUnitsRepo;
		periodsOfValidityRepo = spatialUnitsPeriodsOfValidityRepo;
	}

	public static SpatialUnitOverviewType mapToSwaggerSpatialUnit(MetadataSpatialUnitsEntity spatialUnitEntity) throws Exception {
		SpatialUnitOverviewType dataset = new SpatialUnitOverviewType();
		
		dataset.setSpatialUnitId(spatialUnitEntity.getDatasetId());
		
		/*
		 * TODO FIXME quick and dirty database modification of spatialUnits periodsOfValidity
		 * 
		 * here a quick and dirty way is commented out that can reset spatialUnits periodsOfValidity by accessing spatialUnits layer and inspecting the available timestamps
		 * it can be reenabled to quickly overwrite/reset the associated metadata within spatialUnits metadata entity
		 */
//		AvailablePeriodsOfValidityType availablePeriodsOfValidity = SpatialFeatureDatabaseHandler.getAvailablePeriodsOfValidity(spatialUnitEntity.getDbTableName());		
////		periodsOfValidityRepo.deleteAll();
//		for (PeriodOfValidityType periodOfValidityType : availablePeriodsOfValidity) {
//			PeriodOfValidityEntity_spatialUnits periodEntity = new PeriodOfValidityEntity_spatialUnits(periodOfValidityType);
//			periodsOfValidityRepo.saveAndFlush(periodEntity);
//			spatialUnitEntity.addPeriodOfValidityIfNotExists(periodEntity);
//		}
//		spatialUnitsMetadataRepo.saveAndFlush(spatialUnitEntity);	
		
		Collection<PeriodOfValidityEntity_spatialUnits> spatialUnitsPeriodsOfValidityEntities = spatialUnitEntity.getSpatialUnitsPeriodsOfValidity();		
		AvailablePeriodsOfValidityType availablePeriodsOfValidityType = new AvailablePeriodsOfValidityType();
		for (PeriodOfValidityEntity_spatialUnits periodOfValidityEntity_spatialUnits : spatialUnitsPeriodsOfValidityEntities) {
			availablePeriodsOfValidityType.add(new PeriodOfValidityType(periodOfValidityEntity_spatialUnits));
		}
		
		dataset.setAvailablePeriodsOfValidity(
				availablePeriodsOfValidityType);
		
		CommonMetadataType commonMetadata = new CommonMetadataType();
		commonMetadata.setContact(spatialUnitEntity.getContact());
		commonMetadata.setDatasource(spatialUnitEntity.getDataSource());
		commonMetadata.setDescription(spatialUnitEntity.getDescription());
		commonMetadata.setLastUpdate(DateTimeUtil.toLocalDate(spatialUnitEntity.getLastUpdate()));
		commonMetadata.setSridEPSG(new BigDecimal(spatialUnitEntity.getSridEpsg()));
		commonMetadata.setUpdateInterval(spatialUnitEntity.getUpdateIntervall());
		commonMetadata.setDatabasis(spatialUnitEntity.getDataBasis());
		commonMetadata.setNote(spatialUnitEntity.getNote());
		commonMetadata.setLiterature(spatialUnitEntity.getLiterature());
		dataset.setMetadata(commonMetadata);
		
		dataset.setNextLowerHierarchyLevel(spatialUnitEntity.getNextLowerHierarchyLevel());
		dataset.setNextUpperHierarchyLevel(spatialUnitEntity.getNextUpperHierarchyLevel());
		dataset.setSpatialUnitLevel(spatialUnitEntity.getDatasetName());
		
		dataset.setWmsUrl(spatialUnitEntity.getWmsUrl());
		dataset.setWfsUrl(spatialUnitEntity.getWfsUrl());

		return dataset;
	}

	public static List<SpatialUnitOverviewType> mapToSwaggerSpatialUnits(
			List<MetadataSpatialUnitsEntity> spatialUnitMeatadataEntities) throws Exception {
		List<SpatialUnitOverviewType> metadatasets = new ArrayList<SpatialUnitOverviewType>(spatialUnitMeatadataEntities.size());

		for (MetadataSpatialUnitsEntity metadataEntity : spatialUnitMeatadataEntities) {
			metadatasets.add(mapToSwaggerSpatialUnit(metadataEntity));
		}
		return metadatasets;
	}
}
