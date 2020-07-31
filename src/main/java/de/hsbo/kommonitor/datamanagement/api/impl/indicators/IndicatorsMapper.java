package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.util.*;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.indicators.*;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndicatorsMapper {

	@Autowired
	private IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepo;

	@Autowired
	private IndicatorsMetadataRepository indicatorMetadataRepo;

	@Autowired
	private SpatialUnitsMetadataRepository spatialUnitsRepo;

	public IndicatorsMapper(IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepository,
			IndicatorsMetadataRepository indicatorMetadataRepository,
			SpatialUnitsMetadataRepository spatialUnitsRepository) {
		indicatorSpatialUnitsRepo = indicatorSpatialUnitsRepository;
		indicatorMetadataRepo = indicatorMetadataRepository;
		spatialUnitsRepo = spatialUnitsRepository;
	}

	public List<IndicatorOverviewType> mapToSwaggerIndicators(
			List<MetadataIndicatorsEntity> indicatorsMetadataEntity, List<MetadataSpatialUnitsEntity> spatialUnitsMetadataArray) throws Exception {
		List<IndicatorOverviewType> indicatorOverviews = new ArrayList<IndicatorOverviewType>(
				indicatorsMetadataEntity.size());
		for (MetadataIndicatorsEntity metadataIndicatorsEntity : indicatorsMetadataEntity) {
			List<IndicatorReferenceType> indicatorReferences = ReferenceManager
					.getIndicatorReferences(metadataIndicatorsEntity.getDatasetId());
			List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager
					.getGeoresourcesReferences(metadataIndicatorsEntity.getDatasetId());

			indicatorOverviews
					.add(mapToSwaggerIndicator(metadataIndicatorsEntity, indicatorReferences, georesourcesReferences, spatialUnitsMetadataArray));
		}
		return indicatorOverviews;
	}

	public IndicatorOverviewType mapToSwaggerIndicator(MetadataIndicatorsEntity indicatorsMetadataEntity,
			List<IndicatorReferenceType> indicatorReferences, List<GeoresourceReferenceType> georesourcesReferences, List<MetadataSpatialUnitsEntity> spatialUnitsMetadataArray)
			throws Exception {
		IndicatorOverviewType indicatorOverviewType = new IndicatorOverviewType();

		/*
		 * FIXME here we simply assume that the indicator has the same
		 * timestamps for each spatial unit
		 */
		List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnitEntities = indicatorSpatialUnitsRepo
				.findByIndicatorMetadataId(indicatorsMetadataEntity.getDatasetId());
		if (indicatorSpatialUnitEntities != null && indicatorSpatialUnitEntities.size() > 0) {
			/*
			 * TODO FIXME quick and dirty database modification of indicator
			 * timestamps
			 *
			 * here a quick and dirty way is commented out that can reset
			 * indicator timestamps by accessing an exemplar indicator layer and
			 * inspecting the available timestamps it can be reenabled to
			 * quickly overwrite/reset the associated metadata within indicator
			 * metadata entity
			 */
			// List<String> availableDates =
			// IndicatorDatabaseHandler.getAvailableDates(indicatorSpatialUnitEntities.get(0).getIndicatorValueTableName());
			// indicatorsMetadataEntity.setAvailableTimestamps(availableDates);
			// indicatorMetadataRepo.saveAndFlush(indicatorsMetadataEntity);

			Collection<String> availableTimestamps_collection = indicatorsMetadataEntity.getAvailableTimestamps();
			List<String> availableTimestamps = new ArrayList<>(availableTimestamps_collection);
			availableTimestamps.sort(Comparator.naturalOrder());
			indicatorOverviewType.setApplicableDates(availableTimestamps);

			indicatorOverviewType
					.setApplicableSpatialUnits(getApplicableSpatialUnitsNames(indicatorSpatialUnitEntities, spatialUnitsMetadataArray));
		}

		indicatorOverviewType.setTopicReference(indicatorsMetadataEntity.getTopicReference());
		indicatorOverviewType.setIndicatorId(indicatorsMetadataEntity.getDatasetId());
		indicatorOverviewType.setIndicatorName(indicatorsMetadataEntity.getDatasetName());

		CommonMetadataType metadata = new CommonMetadataType();
		metadata.setContact(indicatorsMetadataEntity.getContact());
		metadata.setDatasource(indicatorsMetadataEntity.getDataSource());
		metadata.setDescription(indicatorsMetadataEntity.getDescription());
		metadata.setLastUpdate(DateTimeUtil.toLocalDate(indicatorsMetadataEntity.getLastUpdate()));
		metadata.setSridEPSG(null);
		metadata.setUpdateInterval(indicatorsMetadataEntity.getUpdateIntervall());
		metadata.setDatabasis(indicatorsMetadataEntity.getDataBasis());
		metadata.setNote(indicatorsMetadataEntity.getNote());
		metadata.setLiterature(indicatorsMetadataEntity.getLiterature());

		indicatorOverviewType.setMetadata(metadata);

		indicatorOverviewType.setProcessDescription(indicatorsMetadataEntity.getProcessDescription());
		indicatorOverviewType.setReferencedGeoresources(georesourcesReferences);
		indicatorOverviewType.setReferencedIndicators(indicatorReferences);
		indicatorOverviewType.setUnit(indicatorsMetadataEntity.getUnit());
		indicatorOverviewType.setCreationType(indicatorsMetadataEntity.getCreationType());
		indicatorOverviewType.setIndicatorType(indicatorsMetadataEntity.getIndicatorType());
		indicatorOverviewType.setCharacteristicValue(indicatorsMetadataEntity.getCharacteristicValue());
		indicatorOverviewType
				.setLowestSpatialUnitForComputation(indicatorsMetadataEntity.getLowestSpatialUnitForComputation());

		indicatorOverviewType.setOgcServices(generateOgcServiceOverview(indicatorSpatialUnitEntities));

		DefaultClassificationMappingType defaultClassification = extractDefaultClassificationMappingFromMetadata(
				indicatorsMetadataEntity);

		indicatorOverviewType.setDefaultClassificationMapping(defaultClassification);

		indicatorOverviewType.setAbbreviation(indicatorsMetadataEntity.getAbbreviation());
		indicatorOverviewType.setIsHeadlineIndicator(indicatorsMetadataEntity.isHeadlineIndicator());
		;
		indicatorOverviewType.setInterpretation(indicatorsMetadataEntity.getInterpretation());
		indicatorOverviewType.setTags(new ArrayList<String>(indicatorsMetadataEntity.getTags()));
		indicatorOverviewType.setAllowedRoles(getAllowedRoleIds(indicatorsMetadataEntity.getRoles()));

		return indicatorOverviewType;
	}

	private List<String> getAllowedRoleIds(HashSet<RolesEntity> roles) {
		return roles
				.stream()
				.map(r -> r.getRoleId())
				.collect(Collectors.toList());
	}

	public DefaultClassificationMappingType extractDefaultClassificationMappingFromMetadata(
			MetadataIndicatorsEntity indicatorsMetadataEntity) {
		DefaultClassificationMappingType defaultClassification = new DefaultClassificationMappingType();
		defaultClassification.setColorBrewerSchemeName(indicatorsMetadataEntity.getColorBrewerSchemeName());

		Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems = indicatorsMetadataEntity
				.getDefaultClassificationMappingItems();
		// List<DefaultClassificationMappingItemType> list = new
		// ArrayList<>(defaultClassificationMappingItems);
		// Collections.sort(list);
		for (DefaultClassificationMappingItemType classificationItem : defaultClassificationMappingItems) {
			defaultClassification.addItemsItem(classificationItem);
		}
		return defaultClassification;
	}

	private List<OgcServicesType> generateOgcServiceOverview(
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

	private List<IndicatorSpatialUnitJoinItem> getApplicableSpatialUnitsNames(
			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits, List<MetadataSpatialUnitsEntity> spatialUnitsMetadataArray) throws Exception {
		List<IndicatorSpatialUnitJoinItem> indicatorSpatialUnitJoinItems = new ArrayList<IndicatorSpatialUnitJoinItem>(indicatorSpatialUnits.size());
		for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {
			IndicatorSpatialUnitJoinItem item = new IndicatorSpatialUnitJoinItem();
			item.setSpatialUnitId(indicatorSpatialUnitJoinEntity.getSpatialUnitId());
			item.setSpatialUnitName(indicatorSpatialUnitJoinEntity.getSpatialUnitName());

			List<String> allowedRoles = indicatorSpatialUnitJoinEntity.getRoles().stream()
					.map(r -> r.getRoleId())
					.collect(Collectors.toList());
			item.setAllowedRoles(allowedRoles);

			indicatorSpatialUnitJoinItems.add(item);
			// This is a QAD to prevent shared collection references for spatial unit roles
			// within the IndicatorSpatialUnitJoinEntity and the below requested MetadataSpatialUnitsEntities
//			indicatorSpatialUnitJoinEntity.clearSpatialUnitRoles();
		}

//		List<SpatialUnitOverviewType> swaggerSpatialUnitsMetadata = SpatialUnitsMapper.mapToSwaggerSpatialUnits(spatialUnitsMetadataArray);
//
//		swaggerSpatialUnitsMetadata = SpatialUnitsManager.sortSpatialUnitsHierarchically(swaggerSpatialUnitsMetadata);

//		List<String> orderedSpatialUnitNames = new ArrayList<String>();

//		for (SpatialUnitOverviewType metadataSpatialUnitsEntity : swaggerSpatialUnitsMetadata) {
//			if (spatialUnitsNames.contains(metadataSpatialUnitsEntity.getSpatialUnitLevel())){
//				orderedSpatialUnitNames.add(metadataSpatialUnitsEntity.getSpatialUnitLevel());
//			}
//		}

		return indicatorSpatialUnitJoinItems;
	}

	private List<String> getTopicNames(Collection<TopicsEntity> indicatorTopics) {
		List<String> topicNames = new ArrayList<String>(indicatorTopics.size());

		for (TopicsEntity topicEntity : indicatorTopics) {
			topicNames.add(topicEntity.getTopicName());
		}

		return topicNames;
	}

}
