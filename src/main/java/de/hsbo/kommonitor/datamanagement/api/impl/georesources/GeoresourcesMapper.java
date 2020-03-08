package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.GeoresourcesPeriodsOfValidityRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.PeriodOfValidityEntity_georesources;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;

public class GeoresourcesMapper {
	
private static GeoresourcesMetadataRepository georesourceMetadataRepo;

private static GeoresourcesPeriodsOfValidityRepository periodsOfValidityRepo;
	
	public GeoresourcesMapper(GeoresourcesMetadataRepository georesourceMetadataRepository, GeoresourcesPeriodsOfValidityRepository periodsOfValidityRepository){
		georesourceMetadataRepo = georesourceMetadataRepository;
		periodsOfValidityRepo = periodsOfValidityRepository;
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
		AvailablePeriodsOfValidityType availablePeriodsOfValidityType = new AvailablePeriodsOfValidityType();
		for (PeriodOfValidityEntity_georesources periodOfValidityEntity_georesources : georesourcesPeriodsOfValidityEntities) {
			availablePeriodsOfValidityType.add(new PeriodOfValidityType(periodOfValidityEntity_georesources));
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

		return dataset;
	}
}
