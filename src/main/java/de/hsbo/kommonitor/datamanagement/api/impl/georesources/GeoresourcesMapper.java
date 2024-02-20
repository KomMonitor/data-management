package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.PeriodOfValidityEntity_georesources;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;

public class GeoresourcesMapper {
	
private static GeoresourcesMetadataRepository georesourceMetadataRepo;
	
	public GeoresourcesMapper(GeoresourcesMetadataRepository georesourceMetadataRepository){
		georesourceMetadataRepo = georesourceMetadataRepository;
	}

	public static List<GeoresourceOverviewType> mapToSwaggerGeoresources(
			List<MetadataGeoresourcesEntity> georesourcesEntities) throws Exception {
		List<GeoresourceOverviewType> metadatasets = new ArrayList<GeoresourceOverviewType>(
				georesourcesEntities.size());

		for (MetadataGeoresourcesEntity metadataEntity : georesourcesEntities) {
			metadatasets.add(mapToSwaggerGeoresource(metadataEntity));
		}
		return metadatasets;
	}

	public static GeoresourceOverviewType mapToSwaggerGeoresource(MetadataGeoresourcesEntity georesourceMetadataEntity)
			throws Exception {
		GeoresourceOverviewType dataset = new GeoresourceOverviewType();

		
		/*
		 * TODO FIXME quick and dirty database modification of georesource periodsOfValidity
		 * 
		 * here a quick and dirty way is commented out that can reset georesource periodsOfValidity by accessing georesource layer and inspecting the available timestamps
		 * it can be reenabled to quickly overwrite/reset the associated metadata within georesource metadata entity
		 */
//		AvailablePeriodsOfValidityType availablePeriodsOfValidity = SpatialFeatureDatabaseHandler.getAvailablePeriodsOfValidity(georesourceMetadataEntity.getDbTableName());		
//		// periodsOfValidityRepo.deleteAll();
//		for (PeriodOfValidityType periodOfValidityType : availablePeriodsOfValidity) {
//			PeriodOfValidityEntity_georesources periodEntity = new PeriodOfValidityEntity_georesources(periodOfValidityType);
//			periodsOfValidityRepo.saveAndFlush(periodEntity);
//			georesourceMetadataEntity.addPeriodOfValidityIfNotExists(periodEntity);
//		}
//		georesourceMetadataRepo.saveAndFlush(georesourceMetadataEntity);	
		
		Collection<PeriodOfValidityEntity_georesources> georesourcesPeriodsOfValidityEntities = georesourceMetadataEntity.getGeoresourcesPeriodsOfValidity();		
		
		Comparator<PeriodOfValidityEntity_georesources> compareByTimePeriod = new Comparator<PeriodOfValidityEntity_georesources>() {
		    @Override
		    public int compare(PeriodOfValidityEntity_georesources o1, PeriodOfValidityEntity_georesources o2) {
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
		
		// sort periods from prior dates to later dates
		List<PeriodOfValidityEntity_georesources> georesourcesPeriodsOfValidityEntities_asOrderedList = new ArrayList<>(georesourcesPeriodsOfValidityEntities);
		Collections.sort(georesourcesPeriodsOfValidityEntities_asOrderedList, compareByTimePeriod);		
		
		AvailablePeriodsOfValidityType availablePeriodsOfValidityType = new AvailablePeriodsOfValidityType();
		for (PeriodOfValidityEntity_georesources periodOfValidityEntity_georesources : georesourcesPeriodsOfValidityEntities_asOrderedList) {
			availablePeriodsOfValidityType.add(
					new PeriodOfValidityType()
							.startDate(periodOfValidityEntity_georesources.getStartDate())
							.endDate(periodOfValidityEntity_georesources.getEndDate())
			);
		}
		
		
		dataset.setAvailablePeriodsOfValidity(
				availablePeriodsOfValidityType);

		CommonMetadataType commonMetadata = new CommonMetadataType();
		commonMetadata.setContact(georesourceMetadataEntity.getContact());
		commonMetadata.setDatasource(georesourceMetadataEntity.getDataSource());
		commonMetadata.setDescription(georesourceMetadataEntity.getDescription());
		commonMetadata
				.setLastUpdate(DateTimeUtil.toLocalDate(georesourceMetadataEntity.getLastUpdate()));
		commonMetadata.setSridEPSG(new BigDecimal(georesourceMetadataEntity.getSridEpsg()));
		commonMetadata.setUpdateInterval(georesourceMetadataEntity.getUpdateIntervall());
		commonMetadata.setDatabasis(georesourceMetadataEntity.getDataBasis());
		commonMetadata.setNote(georesourceMetadataEntity.getNote());
		commonMetadata.setLiterature(georesourceMetadataEntity.getLiterature());
		dataset.setMetadata(commonMetadata);

		dataset.datasetName(georesourceMetadataEntity.getDatasetName());
		dataset.setGeoresourceId(georesourceMetadataEntity.getDatasetId());
		dataset.setTopicReference(georesourceMetadataEntity.getTopicReference());
		dataset.setIsPOI(georesourceMetadataEntity.isPOI());
		dataset.setIsLOI(georesourceMetadataEntity.isLOI());
		dataset.setIsAOI(georesourceMetadataEntity.isAOI());
		dataset.setPoiSymbolBootstrap3Name(georesourceMetadataEntity.getPoiSymbolBootstrap3Name());
		dataset.setPoiMarkerColor(georesourceMetadataEntity.getPoiMarkerColor());
		dataset.setPoiSymbolColor(georesourceMetadataEntity.getPoiSymbolColor());
		dataset.setLoiColor(georesourceMetadataEntity.getLoiColor());
		dataset.setLoiWidth(new BigDecimal(georesourceMetadataEntity.getLoiWidth()));
		dataset.setLoiDashArrayString(georesourceMetadataEntity.getLoiDashArrayString());
		dataset.setAoiColor(georesourceMetadataEntity.getAoiColor());
		
		dataset.setWmsUrl(georesourceMetadataEntity.getWmsUrl());
		dataset.setWfsUrl(georesourceMetadataEntity.getWfsUrl());

		dataset.setAllowedRoles(getPermissions(georesourceMetadataEntity.getPermissions()));
		dataset.setUserPermissions(georesourceMetadataEntity.getUserPermissions());
		dataset.setOwnerId(georesourceMetadataEntity.getOwner().getOrganizationalUnitId());

		return dataset;
	}

	private static List<String> getPermissions(HashSet<PermissionEntity> roles) {
		return roles
				.stream()
				.map(PermissionEntity::getPermissionId)
				.collect(Collectors.toList());
	}
}
