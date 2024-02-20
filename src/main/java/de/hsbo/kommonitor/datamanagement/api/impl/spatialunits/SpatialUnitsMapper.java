package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.PeriodOfValidityEntity_spatialUnits;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitOverviewType;

public class SpatialUnitsMapper {
	
	private static SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

	public SpatialUnitsMapper(SpatialUnitsMetadataRepository spatialUnitsRepo) {
		spatialUnitsMetadataRepo = spatialUnitsRepo;
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
		
		
		
		// sort periods from prior dates to later dates
		
		Comparator<PeriodOfValidityEntity_spatialUnits> compareByTimePeriod = new Comparator<PeriodOfValidityEntity_spatialUnits>() {
		    @Override
		    public int compare(PeriodOfValidityEntity_spatialUnits o1, PeriodOfValidityEntity_spatialUnits o2) {
		        int result =  o1.getStartDate().compareTo(o2.getStartDate());
				
				
		        if (result == 0) {
					try {
						if(o1.getEndDate() != null && o2.getEndDate() != null){						
							result = o1.getEndDate().compareTo(o2.getEndDate());
						}
						else{
							result = 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
		        }
		        
		        return result;
		    }
		};
		
				List<PeriodOfValidityEntity_spatialUnits> spatialUnitsPeriodsOfValidityEntities_asOrderedList = new ArrayList<>(spatialUnitsPeriodsOfValidityEntities);
				Collections.sort(spatialUnitsPeriodsOfValidityEntities_asOrderedList, compareByTimePeriod);		
				
				AvailablePeriodsOfValidityType availablePeriodsOfValidityType = new AvailablePeriodsOfValidityType();
				for (PeriodOfValidityEntity_spatialUnits periodOfValidityEntity_spatialUnits : spatialUnitsPeriodsOfValidityEntities_asOrderedList) {
					availablePeriodsOfValidityType.add(
							new PeriodOfValidityType()
									.startDate(periodOfValidityEntity_spatialUnits.getStartDate())
									.endDate(periodOfValidityEntity_spatialUnits.getEndDate())
					);
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

		dataset.setAllowedRoles(getRoleIds(spatialUnitEntity.getPermissions()));
		dataset.setUserPermissions(spatialUnitEntity.getUserPermissions());
		dataset.setOwnerId(spatialUnitEntity.getOwner().getOrganizationalUnitId());
		
		dataset.setIsOutlineLayer(spatialUnitEntity.isOutlineLayer());
		dataset.setOutlineColor(spatialUnitEntity.getOutlineColor());
		Integer outlineWidth = spatialUnitEntity.getOutlineWidth();
		if (outlineWidth == null) {
			dataset.setOutlineWidth(BigDecimal.valueOf(3));
		}
		else {
			dataset.setOutlineWidth(BigDecimal.valueOf(outlineWidth.intValue()));
		}		
		dataset.setOutlineDashArrayString(spatialUnitEntity.getOutlineDashArrayString());

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

	private static List<String> getRoleIds(HashSet<PermissionEntity> roles) {
		return roles.stream()
				.map(r -> r.getPermissionId())
				.collect(Collectors.toList());
	}
}
