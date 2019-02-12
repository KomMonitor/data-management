package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public class GeoresourcesMapper {

	public static List<GeoresourceOverviewType> mapToSwaggerGeoresources(
			List<MetadataGeoresourcesEntity> georesourcesEntities) throws IOException, SQLException {
		List<GeoresourceOverviewType> metadatasets = new ArrayList<GeoresourceOverviewType>(
				georesourcesEntities.size());

		for (MetadataGeoresourcesEntity metadataEntity : georesourcesEntities) {
			metadatasets.add(mapToSwaggerGeoresource(metadataEntity));
		}
		return metadatasets;
	}

	public static GeoresourceOverviewType mapToSwaggerGeoresource(MetadataGeoresourcesEntity georesourceMetadataEntity)
			throws IOException, SQLException {
		GeoresourceOverviewType dataset = new GeoresourceOverviewType();

		dataset.setAvailablePeriodOfValidity(
				SpatialFeatureDatabaseHandler.getAvailablePeriodOfValidity(georesourceMetadataEntity.getDbTableName()));

		CommonMetadataType commonMetadata = new CommonMetadataType();
		commonMetadata.setContact(georesourceMetadataEntity.getContact());
		commonMetadata.setDatasource(georesourceMetadataEntity.getDataSource());
		commonMetadata.setDescription(georesourceMetadataEntity.getDescription());
		commonMetadata
				.setLastUpdate(DateTimeUtil.toLocalDate(georesourceMetadataEntity.getLastUpdate()));
		commonMetadata.setSridEPSG(new BigDecimal(georesourceMetadataEntity.getSridEpsg()));
		commonMetadata.setUpdateInterval(georesourceMetadataEntity.getUpdateIntervall());
		dataset.setMetadata(commonMetadata);

		dataset.datasetName(georesourceMetadataEntity.getDatasetName());
		dataset.setGeoresourceId(georesourceMetadataEntity.getDatasetId());
		dataset.setApplicableTopics(getSwaggerTopicStrings(georesourceMetadataEntity.getGeoresourcesTopics()));
		dataset.setIsPOI(georesourceMetadataEntity.isPOI());
		dataset.setPoiSymbolBootstrap3Name(georesourceMetadataEntity.getPoiSymbolBootstrap3Name());
		dataset.setPoiMarkerColor(georesourceMetadataEntity.getPoiMarkerColor());
		dataset.setPoiSymbolColor(georesourceMetadataEntity.getPoiSymbolColor());
		
		dataset.setWmsUrl(georesourceMetadataEntity.getWmsUrl());
		dataset.setWfsUrl(georesourceMetadataEntity.getWfsUrl());

		return dataset;
	}

	private static List<String> getSwaggerTopicStrings(Collection<TopicsEntity> georesourcesTopics) {
		List<String> topicStrings = new ArrayList<String>(georesourcesTopics.size());

		for (TopicsEntity topicEntity : georesourcesTopics) {
			topicStrings.add(topicEntity.getTopicName());
		}
		return topicStrings;
	}
}
