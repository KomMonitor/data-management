package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.RegionalReferenceValueEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.GeoresourceReferenceEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.GeoresourceReferenceMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.IndicatorReferenceEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.IndicatorReferenceMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingItemType;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorReferenceType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorSpatialUnitJoinItem;
import de.hsbo.kommonitor.datamanagement.model.OgcServicesType;
import de.hsbo.kommonitor.datamanagement.model.RegionalReferenceValueType;

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
		
		
		/*
		 * TODO: improve fetching of references for each indicator metadata object as this is time consuming 
		 * due to the fact that for each metadata entry and each reference fetching process a new database query and connection is established
		 * 
		 * --> fetch ALL references one time and then put them into a map to have O(1) access to the required references
		 */
		
		Map<String, List<IndicatorReferenceType>> indicatorReferenceMap = getIndicatorReferencesMap();
		Map<String, List<GeoresourceReferenceType>> georesourceReferenceMap = getGeoresourceReferencesMap();
		Map<String, List<IndicatorSpatialUnitJoinEntity>> indicatorSpatialUnitEntitiesMap = getIndicatorSpatialUnitsMap();
		
		for (MetadataIndicatorsEntity metadataIndicatorsEntity : indicatorsMetadataEntity) {

			indicatorOverviews
					.add(mapToSwaggerIndicator(metadataIndicatorsEntity, indicatorReferenceMap.get(metadataIndicatorsEntity.getDatasetId()), georesourceReferenceMap.get(metadataIndicatorsEntity.getDatasetId()), indicatorSpatialUnitEntitiesMap.get(metadataIndicatorsEntity.getDatasetId())));
		}
		return indicatorOverviews;
	}

	private Map<String, List<IndicatorSpatialUnitJoinEntity>> getIndicatorSpatialUnitsMap() {
		Map<String, List<IndicatorSpatialUnitJoinEntity>> indicatorSpatialUnitsMap = new HashMap<String, List<IndicatorSpatialUnitJoinEntity>>();
		
		List<IndicatorSpatialUnitJoinEntity> allEntities = indicatorSpatialUnitsRepo.findAll();

		for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitEntity : allEntities) {
			if(indicatorSpatialUnitEntity == null) {
				continue;
			}
			if (indicatorSpatialUnitsMap.containsKey(indicatorSpatialUnitEntity.getIndicatorMetadataId())) {
				List<IndicatorSpatialUnitJoinEntity> modifiedEntry = indicatorSpatialUnitsMap.get(indicatorSpatialUnitEntity.getIndicatorMetadataId());
				modifiedEntry.add(indicatorSpatialUnitEntity);
				indicatorSpatialUnitsMap.put(indicatorSpatialUnitEntity.getIndicatorMetadataId(), modifiedEntry);
			}
			else {
				List<IndicatorSpatialUnitJoinEntity> newEntry = new ArrayList<IndicatorSpatialUnitJoinEntity>();
				newEntry.add(indicatorSpatialUnitEntity);
				indicatorSpatialUnitsMap.put(indicatorSpatialUnitEntity.getIndicatorMetadataId(), newEntry);
			}	
		}
		
		return indicatorSpatialUnitsMap;
	}

	private Map<String, List<GeoresourceReferenceType>> getGeoresourceReferencesMap() {
		Map<String, List<GeoresourceReferenceType>> georesourceReferencesMap = new HashMap<String, List<GeoresourceReferenceType>>();
		
		List<GeoresourceReferenceEntity> allReferences = ReferenceManager.getAllGeoresourceReferences();
		
		for (GeoresourceReferenceEntity georesourceReferenceEntity : allReferences) {
			if (georesourceReferencesMap.containsKey(georesourceReferenceEntity.getMainIndicatorId())) {
				List<GeoresourceReferenceType> modifiedEntry = georesourceReferencesMap.get(georesourceReferenceEntity.getMainIndicatorId());
				modifiedEntry.add(GeoresourceReferenceMapper.mapToSwaggerModel(georesourceReferenceEntity));
				georesourceReferencesMap.put(georesourceReferenceEntity.getMainIndicatorId(), modifiedEntry);
			}
			else {
				List<GeoresourceReferenceType> newEntry = new ArrayList<GeoresourceReferenceType>();
				newEntry.add(GeoresourceReferenceMapper.mapToSwaggerModel(georesourceReferenceEntity));
				georesourceReferencesMap.put(georesourceReferenceEntity.getMainIndicatorId(), newEntry);
			}	
		}
		
		return georesourceReferencesMap;
	}

	private Map<String, List<IndicatorReferenceType>> getIndicatorReferencesMap() {
		Map<String, List<IndicatorReferenceType>> indicatorReferencesMap = new HashMap<String, List<IndicatorReferenceType>>();
		
		List<IndicatorReferenceEntity> allReferences = ReferenceManager.getAllIndicatorReferences();
		
		for (IndicatorReferenceEntity indicatorReferenceEntity : allReferences) {
			if (indicatorReferencesMap.containsKey(indicatorReferenceEntity.getIndicatorId())) {
				List<IndicatorReferenceType> modifiedEntry = indicatorReferencesMap.get(indicatorReferenceEntity.getIndicatorId());
				modifiedEntry.add(IndicatorReferenceMapper.mapToSwaggerModel(indicatorReferenceEntity));
				indicatorReferencesMap.put(indicatorReferenceEntity.getIndicatorId(), modifiedEntry);
			}
			else {
				List<IndicatorReferenceType> newEntry = new ArrayList<IndicatorReferenceType>();
				newEntry.add(IndicatorReferenceMapper.mapToSwaggerModel(indicatorReferenceEntity));
				indicatorReferencesMap.put(indicatorReferenceEntity.getIndicatorId(), newEntry);
			}	
		}
		
		return indicatorReferencesMap;
	}

	public IndicatorOverviewType mapToSwaggerIndicator(MetadataIndicatorsEntity indicatorsMetadataEntity,
			List<IndicatorReferenceType> indicatorReferences, List<GeoresourceReferenceType> georesourcesReferences, List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnitEntities)
			throws Exception {
		IndicatorOverviewType indicatorOverviewType = new IndicatorOverviewType();

		/*
		 * FIXME here we simply assume that the indicator has the same
		 * timestamps for each spatial unit
		 */
		
		/*
		 * TODO: improve fetching of associated spatial units for each indicator metadata object as this is time consuming 
		 * due to the fact that for each metadata entry and each spatial unit fetching process a new database query and connection is established
		 * 
		 * --> fetch ALL associated spatial units entries one time and then put them into a map to have O(1) access to the required references
		 */
		
		
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
					.setApplicableSpatialUnits(getApplicableSpatialUnitsNames(indicatorSpatialUnitEntities));
		}

		indicatorOverviewType.setTopicReference(indicatorsMetadataEntity.getTopicReference());
		indicatorOverviewType.setIndicatorId(indicatorsMetadataEntity.getDatasetId());
		indicatorOverviewType.setIndicatorName(indicatorsMetadataEntity.getDatasetName());
		
		indicatorOverviewType.setDisplayOrder(BigDecimal.valueOf(indicatorsMetadataEntity.getDisplayOrder()));
		indicatorOverviewType.setReferenceDateNote(indicatorsMetadataEntity.getReferenceDateNote());

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
		indicatorOverviewType.setUserPermissions(indicatorsMetadataEntity.getUserPermissions());

		List<RegionalReferenceValueType> refValues = new ArrayList<RegionalReferenceValueType>();
		for (RegionalReferenceValueEntity regionalReferenceValueEntity : indicatorsMetadataEntity.getRegionalReferenceValues()) {
			RegionalReferenceValueType regRefValueType = new RegionalReferenceValueType();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//			formatter = formatter.withLocale( putAppropriateLocaleHere );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
			LocalDate date = LocalDate.parse(regionalReferenceValueEntity.getReferenceDate(), formatter);

			regRefValueType.setReferenceDate(date);
			regRefValueType.setRegionalAverage(regionalReferenceValueEntity.getRegionalAverage());
			regRefValueType.setRegionalSum(regionalReferenceValueEntity.getRegionalSum());
			regRefValueType.setSpatiallyUnassignable(regionalReferenceValueEntity.getSpatiallyUnassignable());
			refValues.add(regRefValueType);
		}
		indicatorOverviewType.setRegionalReferenceValues(refValues);


		indicatorOverviewType.setPermissions(getAllowedRoleIds(indicatorsMetadataEntity.getPermissions()));
		if (indicatorsMetadataEntity.getOwner() != null) {
			indicatorOverviewType.setOwnerId(indicatorsMetadataEntity.getOwner().getOrganizationalUnitId());
		}
		indicatorOverviewType.setIsPublic(indicatorsMetadataEntity.isPublic());

		return indicatorOverviewType;
	}

	private List<String> getAllowedRoleIds(HashSet<PermissionEntity> roles) {
		return roles
				.stream()
				.map(r -> r.getPermissionId())
				.collect(Collectors.toList());
	}

	public static DefaultClassificationMappingType extractDefaultClassificationMappingFromMetadata(
			MetadataIndicatorsEntity indicatorsMetadataEntity) {
		DefaultClassificationMappingType defaultClassification = new DefaultClassificationMappingType();
		defaultClassification.setColorBrewerSchemeName(indicatorsMetadataEntity.getColorBrewerSchemeName());
		defaultClassification.setNumClasses(new BigDecimal(indicatorsMetadataEntity.getNumClasses()));
		defaultClassification.setClassificationMethod(indicatorsMetadataEntity.getClassificationMethod());

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

		if  (indicatorSpatialUnitEntities == null) {
			return ogcServices;
		}
		
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
			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits) throws Exception {
		List<IndicatorSpatialUnitJoinItem> indicatorSpatialUnitJoinItems = new ArrayList<IndicatorSpatialUnitJoinItem>(indicatorSpatialUnits.size());
		for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {
			IndicatorSpatialUnitJoinItem item = new IndicatorSpatialUnitJoinItem();
			item.setSpatialUnitId(indicatorSpatialUnitJoinEntity.getSpatialUnitId());
			item.setSpatialUnitName(indicatorSpatialUnitJoinEntity.getSpatialUnitName());

			List<String> allowedRoles = indicatorSpatialUnitJoinEntity.getPermissions().stream()
					.map(PermissionEntity::getPermissionId)
					.collect(Collectors.toList());
			item.setPermissions(allowedRoles);
			item.setUserPermissions(indicatorSpatialUnitJoinEntity.getUserPermissions());
			if (indicatorSpatialUnitJoinEntity.getOwner() != null) {
				item.setOwnerId(indicatorSpatialUnitJoinEntity.getOwner().getOrganizationalUnitId());
			}
			item.setIsPublic(indicatorSpatialUnitJoinEntity.isPublic());

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
