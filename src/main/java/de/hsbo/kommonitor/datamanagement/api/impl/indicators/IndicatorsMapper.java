package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.indicators.DefaultClassificationMappingItemType;
import de.hsbo.kommonitor.datamanagement.model.indicators.DefaultClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.OgcServicesType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public class IndicatorsMapper {

	private static IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepo;
	
	public IndicatorsMapper(IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepository){
		indicatorSpatialUnitsRepo = indicatorSpatialUnitsRepository;
	}

	public static List<IndicatorOverviewType> mapToSwaggerIndicators(
			List<MetadataIndicatorsEntity> indicatorsMetadataEntity) throws IOException {
		List<IndicatorOverviewType> indicatorOverviews = new ArrayList<IndicatorOverviewType>(
				indicatorsMetadataEntity.size());
		for (MetadataIndicatorsEntity metadataIndicatorsEntity : indicatorsMetadataEntity) {
			List<IndicatorReferenceType> indicatorReferences = ReferenceManager
					.getIndicatorReferences(metadataIndicatorsEntity.getDatasetId());
			List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager
					.getGeoresourcesReferences(metadataIndicatorsEntity.getDatasetId());

			indicatorOverviews
					.add(mapToSwaggerIndicator(metadataIndicatorsEntity, indicatorReferences, georesourcesReferences));
		}
		return indicatorOverviews;
	}

	public static IndicatorOverviewType mapToSwaggerIndicator(MetadataIndicatorsEntity indicatorsMetadataEntity,
			List<IndicatorReferenceType> indicatorReferences, List<GeoresourceReferenceType> georesourcesReferences)
			throws IOException {
		IndicatorOverviewType indicatorOverviewType = new IndicatorOverviewType();

		/*
		 * FIXME here we simply assume that the indicator has the same timestamps for each spatial unit
		 */
		List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnitEntities = indicatorSpatialUnitsRepo.findByIndicatorMetadataId(indicatorsMetadataEntity.getDatasetId());
		if(indicatorSpatialUnitEntities != null && indicatorSpatialUnitEntities.size() > 0){
			indicatorOverviewType.setApplicableDates(
					IndicatorDatabaseHandler.getAvailableDates(indicatorSpatialUnitEntities.get(0).getIndicatorValueTableName()));
			indicatorOverviewType.setApplicableSpatialUnits(getApplicableSpatialUnitsNames(indicatorSpatialUnitEntities));
			
		}
		
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
		
		indicatorOverviewType.setOgcServices(generateOgcServiceOverview(indicatorSpatialUnitEntities));
		
		Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems = indicatorsMetadataEntity.getDefaultClassificationMappingItems();
//		List<DefaultClassificationMappingItemType> list = new ArrayList<>(defaultClassificationMappingItems);
//		Collections.sort(list);
		
		DefaultClassificationMappingType defaultClassification = new DefaultClassificationMappingType();
		for (DefaultClassificationMappingItemType classificationItem : defaultClassificationMappingItems) {
			defaultClassification.addItemsItem(classificationItem);
		}
		
		indicatorOverviewType.setDefaultClassificationMapping(defaultClassification);

		return indicatorOverviewType;
	}

	private static List<OgcServicesType> generateOgcServiceOverview(
			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnitEntities) {
		List<OgcServicesType> ogcServices = new ArrayList<OgcServicesType>();
		
		for (IndicatorSpatialUnitJoinEntity entity : indicatorSpatialUnitEntities) {
			OgcServicesType ogcServicesInstance = new OgcServicesType();
			ogcServicesInstance.setSpatialUnit(entity.getSpatialUnitName());
			ogcServicesInstance.setWfsUrl(entity.getWfsUrl());
			ogcServicesInstance.setWmsUrl(entity.getWmsUrl());
			ogcServicesInstance.setDefaultStyleName(entity.getDefaultStyleName());
			
			ogcServices.add(ogcServicesInstance);
		}
		
		return ogcServices;
	}

	private static List<String> getApplicableSpatialUnitsNames(
			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits) {
		List<String> spatialUnitsNames = new ArrayList<String>(indicatorSpatialUnits.size());
		for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {
			spatialUnitsNames.add(indicatorSpatialUnitJoinEntity.getSpatialUnitName());
		}
		return spatialUnitsNames;
	}

	private static List<String> getTopicNames(Collection<TopicsEntity> indicatorTopics) {
		List<String> topicNames = new ArrayList<String>(indicatorTopics.size());

		for (TopicsEntity topicEntity : indicatorTopics) {
			topicNames.add(topicEntity.getTopicName());
		}

		return topicNames;
	}

}
