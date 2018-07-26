package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorReferenceType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public class IndicatorsMapper {

	public static List <IndicatorOverviewType> mapToSwaggerIndicators(List<MetadataIndicatorsEntity> indicatorsMetadataEntity) throws IOException {
		List <IndicatorOverviewType> indicatorOverviews = new ArrayList<IndicatorOverviewType>(indicatorsMetadataEntity.size());
		for (MetadataIndicatorsEntity metadataIndicatorsEntity : indicatorsMetadataEntity) {
			List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(metadataIndicatorsEntity.getDatasetId());
			List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(metadataIndicatorsEntity.getDatasetId());
			
			indicatorOverviews.add(mapToSwaggerIndicator(metadataIndicatorsEntity, indicatorReferences, georesourcesReferences));
		}
		return indicatorOverviews;
	}

	public static IndicatorOverviewType mapToSwaggerIndicator(MetadataIndicatorsEntity indicatorsMetadataEntity, List<IndicatorReferenceType> indicatorReferences, List<GeoresourceReferenceType> georesourcesReferences) throws IOException {
		IndicatorOverviewType indicatorOverviewType = new IndicatorOverviewType();
		
//		indicatorOverviewType.setAllowedRoles(indicatorsMetadataEntity.get);
		indicatorOverviewType.setApplicableDates(IndicatorDatabaseHandler.getAvailableDates(indicatorsMetadataEntity.getDbTableName()));
//		indicatorOverviewType.setApplicableSpatialUnits(applicableSpatialUnits);
		indicatorOverviewType.setApplicableTopics(getTopicNames(indicatorsMetadataEntity.getIndicatorTopics()));
		indicatorOverviewType.setIndicatorId(indicatorsMetadataEntity.getDatasetId());
		indicatorOverviewType.setIndicatorName(indicatorsMetadataEntity.getDatasetName());
		
		CommonMetadataType metadata = new CommonMetadataType();
		metadata.setContact(indicatorsMetadataEntity.getContact());
		metadata.setDatasource(indicatorsMetadataEntity.getDataSource());
		metadata.setDescription(indicatorsMetadataEntity.getDescription());
		metadata.setLastUpdate(DateTimeUtil.toLocalDate(indicatorsMetadataEntity.getLastUpdate()));
		metadata.setSridEPSG(null);
		metadata.setUpdateInterval(indicatorsMetadataEntity.getUpdateIntervall());
	
		indicatorOverviewType.setMetadata(metadata);
		
		indicatorOverviewType.setProcessDescription(indicatorsMetadataEntity.getProcessDescription());
		indicatorOverviewType.setReferencedGeoresources(georesourcesReferences);
		indicatorOverviewType.setReferencedIndicators(indicatorReferences);
		indicatorOverviewType.setUnit(indicatorsMetadataEntity.getUnit());
		indicatorOverviewType.setCreationType(indicatorsMetadataEntity.getCreationType());
		indicatorOverviewType.setWmsUrl(indicatorsMetadataEntity.getWmsUrl());
		indicatorOverviewType.setWfsUrl(indicatorsMetadataEntity.getWfsUrl());
		
		return indicatorOverviewType;
	}

	private static List<String> getTopicNames(Collection<TopicsEntity> indicatorTopics) {
		List<String> topicNames = new ArrayList<String>(indicatorTopics.size());
		
		for (TopicsEntity topicEntity : indicatorTopics) {
			topicNames.add(topicEntity.getTopicName());
		}
		
		return topicNames;
	}


	
	
	
}
