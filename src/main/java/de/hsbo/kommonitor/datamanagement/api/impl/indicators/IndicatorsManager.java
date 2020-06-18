package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javax.transaction.Transactional;

import de.hsbo.kommonitor.datamanagement.api.impl.roles.RolesRepository;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
import org.geotools.data.DataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.scripts.ScriptManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.indicators.CreationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.indicators.DefaultClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPropertiesWithoutGeomType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorTypeEnum;

@Transactional
@Repository
@Component
public class IndicatorsManager {
	
	private static Logger logger = LoggerFactory.getLogger(IndicatorsManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	private IndicatorsMetadataRepository indicatorsMetadataRepo;
	
	@Autowired
	private IndicatorSpatialUnitsRepository indicatorsSpatialUnitsRepo;

	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	OGCWebServiceManager ogcServiceManager;
	
	@Autowired
	ScriptManager scriptManager;
	
	public String updateMetadata(IndicatorPATCHInputType metadata, String indicatorId) throws Exception {
		logger.info("Trying to update indicator metadata for datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);
			
			String indicatorName = metadata.getDatasetName();
			String characteristicValue = metadata.getCharacteristicValue();
			IndicatorTypeEnum indicatorType = metadata.getIndicatorType();
			CreationTypeEnum creationType = metadata.getCreationType();
			
			logger.info("Trying to update indicator using follwing parameters: name '{}', characteristicValue '{}', indicatorType '{}', creationType '{}'", indicatorName, characteristicValue, indicatorType, creationType.toString());

			/*
			 * check if there are changes to key-properties 
			 * 
			 * if there are changes then we must check, if the combination of three key properties already exists!
			 */
			if(keyPropertiesHaveChanged(metadataEntity, indicatorName, characteristicValue, indicatorType)){
				if (indicatorsMetadataRepo.existsByDatasetNameAndCharacteristicValueAndIndicatorType(indicatorName, characteristicValue, indicatorType)) {
					logger.error(
							"The indicator metadataset with datasetName '{}', characteristicValue '{}' and indicatorType '{}' already exists. Thus aborting update indicator request.",
							indicatorName, characteristicValue, indicatorType);
					throw new Exception("Indicator for indicatorName, characteristicValue and indicatorType already exists. Aborting update indicator request.");
				}
			}

			/*
			 * call DB tool to update features
			 */
			updateMetadata(metadata, metadataEntity);

			indicatorsMetadataRepo.saveAndFlush(metadataEntity);
			ReferenceManager.updateReferences(metadata.getRefrencesToGeoresources(),
					metadata.getRefrencesToOtherIndicators(), metadataEntity.getDatasetId());

			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits = indicatorsSpatialUnitsRepo
					.findByIndicatorMetadataId(indicatorId);

			for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {

				String datasetTitle = createTitleForWebService(indicatorSpatialUnitJoinEntity.getSpatialUnitName(),
						indicatorSpatialUnitJoinEntity.getIndicatorName());
				
				String styleName;
				if (metadata.getDefaultClassificationMapping() != null
						&& metadata.getDefaultClassificationMapping().getItems() != null
						&& metadata.getDefaultClassificationMapping().getItems().size() > 0) {
					styleName = publishDefaultStyleForWebServices(metadata.getDefaultClassificationMapping(),
							datasetTitle, indicatorSpatialUnitJoinEntity.getIndicatorValueTableName());
				} else {
					DefaultClassificationMappingType defaultClassificationMapping = IndicatorsMapper
							.extractDefaultClassificationMappingFromMetadata(metadataEntity);
					styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle,
							indicatorSpatialUnitJoinEntity.getIndicatorValueTableName());
				}

				ogcServiceManager.publishDbLayerAsOgcService(
						indicatorSpatialUnitJoinEntity.getIndicatorValueTableName(), datasetTitle, styleName,
						ResourceTypeEnum.INDICATOR);

				persistNamesOfIndicatorTablesAndServicesInJoinTable(indicatorId,
						indicatorSpatialUnitJoinEntity.getIndicatorName(),
						indicatorSpatialUnitJoinEntity.getSpatialUnitName(),
						indicatorSpatialUnitJoinEntity.getIndicatorValueTableName(), styleName);
			}

			return indicatorId;
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Update request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update indicator metadata, but no dataset existes with datasetId " + indicatorId);
		}
	}

	private boolean keyPropertiesHaveChanged(MetadataIndicatorsEntity metadataEntity, String indicatorName,
			String characteristicValue, IndicatorTypeEnum indicatorType) {
		
		if(! metadataEntity.getDatasetName().equalsIgnoreCase(indicatorName)){
			return true;
		}
		// characteristic value might be null, so check for that first
		if(metadataEntity.getCharacteristicValue() != null){
			if(! metadataEntity.getCharacteristicValue().equalsIgnoreCase(characteristicValue)){
				return true;
			}
		}
		
		if(! metadataEntity.getIndicatorType().equals(indicatorType)){
			return true;
		}
		
		return false;
	}

	private void updateMetadata(IndicatorPATCHInputType metadata, MetadataIndicatorsEntity entity) throws Exception{
		entity.setDatasetName(metadata.getDatasetName());
		entity.setCharacteristicValue(metadata.getCharacteristicValue());
		entity.setIndicatorType(metadata.getIndicatorType());
		entity.setCreationType(metadata.getCreationType());
		
		CommonMetadataType genericMetadata = metadata.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());
		entity.setDataBasis(genericMetadata.getDatabasis());
		entity.setNote(genericMetadata.getNote());
		entity.setLiterature(genericMetadata.getLiterature());

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());
		entity.setProcessDescription(metadata.getProcessDescription());
		entity.setUnit(metadata.getUnit());
		entity.setLowestSpatialUnitForComputation(metadata.getLowestSpatialUnitForComputation());
		entity.setRoles(retrieveRoles(metadata.getAllowedRoles()));
		
		if(metadata.getDefaultClassificationMapping() != null){
			entity.setDefaultClassificationMappingItems(metadata.getDefaultClassificationMapping().getItems());
			entity.setColorBrewerSchemeName(metadata.getDefaultClassificationMapping().getColorBrewerSchemeName());
		}
			
	

		/*
		 * add topic to referenced topics, bu only if topic is not yet included!
		 */
		entity.setTopicReference(metadata.getTopicReference());
		
		entity.setAbbreviation(metadata.getAbbreviation());
		entity.setHeadlineIndicator(metadata.isIsHeadlineIndicator());
		entity.setInterpretation(metadata.getInterpretation());
		entity.setTags(new HashSet<String>(metadata.getTags()));

		// persist in db
		indicatorsMetadataRepo.saveAndFlush(entity);
		
	}

	public String updateFeatures(IndicatorPUTInputType indicatorData, String indicatorId) throws Exception {
		logger.info("Trying to update indicator features for datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			String spatialUnitName = indicatorData.getApplicableSpatialUnit();
			
			MetadataIndicatorsEntity indicatorMetadataEntry = indicatorsMetadataRepo.findByDatasetId(indicatorId);
			String datasetTitle = createTitleForWebService(spatialUnitName, indicatorMetadataEntry.getDatasetName());
			
			// check if data contains null or NAN values
			/*
			 * DEACTIVATE FOR NOW AS WE WANT TO ALLOW NAN VALUES AS NODATA VALUES
			 */
//			checkInputData(indicatorData);
			
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName);
				String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorValueTableName();
				
				/*
				 * call DB tool to update features
				 */
				IndicatorDatabaseHandler.updateIndicatorFeatures(indicatorData, indicatorViewTableName);
			
				indicatorViewTableName = createOrReplaceIndicatorView_fromViewName(indicatorViewTableName, spatialUnitName, indicatorMetadataEntry.getDatasetId());
				
				// handle OGC web service
				String styleName;
				
				if(indicatorData.getDefaultClassificationMapping() != null && indicatorData.getDefaultClassificationMapping().getItems() != null && indicatorData.getDefaultClassificationMapping().getItems().size() > 0){
					styleName = publishDefaultStyleForWebServices(indicatorData.getDefaultClassificationMapping(), datasetTitle, indicatorViewTableName);
				}
				else{
					DefaultClassificationMappingType defaultClassificationMapping = IndicatorsMapper.extractDefaultClassificationMappingFromMetadata(indicatorMetadataEntry);
					styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle, indicatorViewTableName);
				}
				
				ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, datasetTitle, styleName, ResourceTypeEnum.INDICATOR);
				
				/*
				 * set wms and wfs urls within metadata
				 */
				persistNamesOfIndicatorTablesAndServicesInJoinTable(indicatorId, indicatorMetadataEntry.getDatasetName(), spatialUnitName, indicatorViewTableName, styleName);
				
			} else{
				logger.info(
						"No indicator dataset for the given indicatorId '{}' and spatialUnitName '{}' was found in database. Update request will create associated feature table for the first time. Also OGC publishment will be done",
						indicatorId, spatialUnitName);
				
				String indicatorViewTableName = null;
				boolean publishedAsService = false;
				try {
					String indicatorValueTable = createIndicatorValueTable(indicatorData.getIndicatorValues(), indicatorId);
					indicatorViewTableName = createOrReplaceIndicatorView_fromValueTableName(indicatorValueTable, spatialUnitName, indicatorId);
//					deleteIndicatorValueTable(indicatorValueTable);
					
					// handle OGC web service
					String styleName;
					
					if(indicatorData.getDefaultClassificationMapping() != null && indicatorData.getDefaultClassificationMapping().getItems() != null && indicatorData.getDefaultClassificationMapping().getItems().size() > 0){
						styleName = publishDefaultStyleForWebServices(indicatorData.getDefaultClassificationMapping(), datasetTitle, indicatorViewTableName);
					}
					else{
						DefaultClassificationMappingType defaultClassificationMapping = IndicatorsMapper.extractDefaultClassificationMappingFromMetadata(indicatorMetadataEntry);
						styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle, indicatorViewTableName);
					}
					publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, datasetTitle, styleName, ResourceTypeEnum.INDICATOR);
					
					persistNamesOfIndicatorTablesAndServicesInJoinTable(indicatorId, indicatorMetadataEntry.getDatasetName(), spatialUnitName, indicatorViewTableName, styleName);
				} catch (Exception e) {
					/*
					 * remove partially created resources and thrwo error
					 */
					logger.error("Error while creating indicator with id {} for spatialUnit {}. Error message: {}", indicatorId, spatialUnitName, e.getMessage());
					e.printStackTrace();
					
					logger.info("Deleting partially created resources");
					
					try {
						
						logger.info("Delete indicatorValue table if exists for tableName '{}'", indicatorViewTableName);
						if(indicatorViewTableName != null){
							IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorViewTableName);
						}
						
						logger.info("Unpublish OGC services if exists");
						if(publishedAsService){
							ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
						}
						
						
						logger.info("Delete indicatorSpatialUnitJoinEntities if exists for metadataId '{}'", indicatorId);
						if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName))
							indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName);
						
					} catch (Exception e2) {
						logger.error("Error while deleting partially created georesource. Error message: " + e.getMessage());
						e.printStackTrace();
						throw e;
					}
					throw e;
				}
				
			}
			indicatorMetadataEntry = addNewTimestampsToMetadataEntry(indicatorData.getIndicatorValues(), indicatorMetadataEntry);
			indicatorMetadataEntry.setLastUpdate(java.util.Calendar.getInstance().getTime());			
			indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);
			
			return indicatorId;
			
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Update request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	private String publishDefaultStyleForWebServices(DefaultClassificationMappingType defaultClassificationMappingType, String datasetTitle,
			String indicatorViewTableName) throws Exception {
		// sorted list of ascending dates
		List<String> availableDates = IndicatorDatabaseHandler.getAvailableDates(indicatorViewTableName);
		
		if(availableDates != null && availableDates.size() > 0) {
			//pick the most current date and use its property for default style
			String mostCurrentDate = availableDates.get(availableDates.size()-1);
//			mostCurrentDate = IndicatorDatabaseHandler.DATE_PREFIX + mostCurrentDate;
//			
//			
//			List<Float> indicatorValues = IndicatorDatabaseHandler.getAllIndicatorValues(indicatorValueTableName, mostCurrentDate);
//			
			// year-month-day
			String[] dateComponents = mostCurrentDate.split("-");
			
			DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();
			FeatureCollection validFeatures = IndicatorDatabaseHandler.getValidFeaturesAsFeatureCollection(dataStore, indicatorViewTableName,new BigDecimal(dateComponents[0]), new BigDecimal(dateComponents[1]), new BigDecimal(dateComponents[2]));
			
			String targetPropertyName = IndicatorDatabaseHandler.DATE_PREFIX + mostCurrentDate;
			
			// handle OGC web service
			String styleName = ogcServiceManager.createAndPublishStyle(datasetTitle, validFeatures, defaultClassificationMappingType, targetPropertyName);
			
			DatabaseHelperUtil.disposePostGisDataStore(dataStore);
			return styleName;
		}
		
		
		return null;
	}

	private void checkInputData(IndicatorPUTInputType indicatorData) throws Exception {
		List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = indicatorData.getIndicatorValues();
		
		for (IndicatorPOSTInputTypeIndicatorValues indicatorPOSTInputTypeIndicatorValues : indicatorValues) {
			List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorPOSTInputTypeIndicatorValues.getValueMapping();
			for (IndicatorPOSTInputTypeValueMapping indicatorPOSTInputTypeValueMapping : valueMapping) {
				Float indicatorValue = indicatorPOSTInputTypeValueMapping.getIndicatorValue();
				if(indicatorValue == null || Float.isNaN(indicatorValue))
					throw new Exception("Input contains NULL or NAN values as indicator value. Thus aborting request to update indicator features.");
			}
		}
		
	}

	private String createTitleForWebService(String spatialUnitName, String indicatorName) {
		return indicatorName + "_" + spatialUnitName;
	}

	public IndicatorOverviewType getIndicatorById(String indicatorId) throws Exception {
		logger.info("Retrieving indicator metadata for datasetId '{}'", indicatorId);
		MetadataIndicatorsEntity indicatorsMetadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);	
		
		List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(indicatorsMetadataEntity.getDatasetId());
		List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(indicatorsMetadataEntity.getDatasetId());
		
		IndicatorOverviewType swaggerIndicatorMetadata = IndicatorsMapper
				.mapToSwaggerIndicator(indicatorsMetadataEntity, indicatorReferences, georesourcesReferences);
		
		return swaggerIndicatorMetadata;
	}

	public List<IndicatorOverviewType> getAllIndicatorsMetadata() throws Exception  {
		logger.info("Retrieving all indicators metadata from db");

		List<MetadataIndicatorsEntity> indicatorsMeatadataEntities = indicatorsMetadataRepo.findAll();

		List<IndicatorOverviewType> swaggerIndicatorsMetadata = IndicatorsMapper
				.mapToSwaggerIndicators(indicatorsMeatadataEntities);
		
		swaggerIndicatorsMetadata.sort(Comparator.comparing(IndicatorOverviewType::getIndicatorName));
		
		return swaggerIndicatorsMetadata;
	}

	public String getValidIndicatorFeatures(String indicatorId, String spatialUnitId, BigDecimal year,
			BigDecimal month, BigDecimal day, String simplifyGeometries)throws Exception {
		logger.info("Retrieving valid indicator features from Dataset with id '{}'for spatialUnit '{}' for date '{}-{}-{}'", indicatorId, spatialUnitId,
				year, month, day);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
				String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorValueTableName();

				String json = IndicatorDatabaseHandler.getValidFeatures(indicatorViewTableName, year, month, day, simplifyGeometries);
				return json;

			} else{
				logger.error(
						"No indicator dataset for the given indicatorId '{}' and spatialUnitId '{}' was found in database. Get request has no effect.",
						indicatorId, spatialUnitId);
				throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
						"Tried to get valid indicator features, but there is no table for the combination of indicatorId " 
								+ indicatorId + " and spatialUnitId " + spatialUnitId);
			}
			
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Get request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public String getIndicatorFeatures(String indicatorId, String spatialUnitId, String simplifyGeometries) throws Exception{
		logger.info("Retrieving all indicator features from Dataset with id '{}'for spatialUnitId '{}' ", indicatorId, spatialUnitId);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
				String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorValueTableName();

				String json = IndicatorDatabaseHandler.getIndicatorFeatures(indicatorViewTableName, simplifyGeometries);
				return json;

			} else{
				logger.error(
						"No indicator dataset for the given indicatorId '{}' and spatialUnitId '{}' was found in database. Get request has no effect.",
						indicatorId, spatialUnitId);
				throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
						"Tried to get indicator features, but there is no table for the combination of indicatorId " 
								+ indicatorId + " and spatialUnitId " + spatialUnitId);
			}

		} else {
			logger.error(
					"No indicator dataset with indicatorId '{}' was found in database. Get request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public boolean deleteIndicatorDatasetById(String indicatorId) throws Exception {
		logger.info("Trying to delete indicator dataset with datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			
			ReferenceManager.removeReferences(indicatorId);
			
			boolean deleteScriptsForIndicators = scriptManager.deleteScriptsByIndicatorsId(indicatorId);
			
			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits = indicatorsSpatialUnitsRepo.findByIndicatorMetadataId(indicatorId);

			
			
			/*
			 * delete featureTables and views for each spatial unit
			 */
			for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {
				String indicatorViewTableName = indicatorSpatialUnitJoinEntity.getIndicatorValueTableName();
//				IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);
				
				IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorSpatialUnitJoinEntity.getIndicatorValueTableName());
				
				// handle OGC web service
				ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
			}
			
			/*
			 * delete entries from indicatorsMetadataRepo
			 */
			indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataId(indicatorId);
			
			/*
			 * delete metadata entry
			 */
			indicatorsMetadataRepo.deleteByDatasetId(indicatorId);
			return true;
		} else {
			logger.error(
					"No indicator dataset with datasetName '{}' was found in database. Delete request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
		}
	}
	
	public boolean deleteIndicatorDatasetByIdAndSpatialUnitId(String indicatorId, String spatialUnitId) throws Exception {
		logger.info("Trying to delete indicator dataset with datasetId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			IndicatorSpatialUnitJoinEntity indicatorForSpatialUnit = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);

//			ReferenceManager.removeReferences(indicatorId);
			
//			boolean deleteScriptsForIndicators = scriptManager.deleteScriptsByIndicatorsId(indicatorId);
			
			/*
			 * delete featureTable and views for each spatial unit
			 */
			String indicatorViewTableName = indicatorForSpatialUnit.getIndicatorValueTableName();
//			IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);
			
			IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorForSpatialUnit.getIndicatorValueTableName());
			
			// handle OGC web service
			ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
			
			
			/*
			 * delete entry from indicatorsMetadataRepo
			 */
			indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
			
			return true;
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Delete request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
		}
	}
	
	public boolean deleteIndicatorLayersForSpatialUnitId(String spatialUnitId) throws Exception{
		logger.info("Trying to delete all indicator layers associated with the spatialUnitId '{}'", spatialUnitId);
		if (indicatorsSpatialUnitsRepo.existsBySpatialUnitId(spatialUnitId)) {
			List<IndicatorSpatialUnitJoinEntity> indicatorDatasetsForSpatialUnit = indicatorsSpatialUnitsRepo.findBySpatialUnitId(spatialUnitId);
			int numberOfIndicatorLayersToDelete = indicatorDatasetsForSpatialUnit.size();
			
			List<String> indicatorNames = new ArrayList<String>();
			
			/*
			 * delete featureTables and views for each indicator dataset
			 */
			for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorDatasetsForSpatialUnit) {
				String indicatorViewTableName = indicatorSpatialUnitJoinEntity.getIndicatorValueTableName();
//				IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);
				
				IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorSpatialUnitJoinEntity.getIndicatorValueTableName());
				
				// handle OGC web service
				ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
								
				
				/*
				 * delete entry from indicatorsMetadataRepo
				 */
				indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataIdAndSpatialUnitId(indicatorSpatialUnitJoinEntity.getIndicatorMetadataId(), spatialUnitId);
				
				indicatorNames.add(indicatorSpatialUnitJoinEntity.getIndicatorName());
			}	
						
			logger.info("Deleted indicator layers associated to spatialUnitId '{}' for a total number of {} indicator datasets", spatialUnitId, numberOfIndicatorLayersToDelete);
			logger.info("The names of the affected indicators are: {}", indicatorNames);
				
			return true;
		} else {
			logger.error(
					"No indicator dataset associated to a spatial unit with id '{}' was found in database. Delete request has no effect.",
					spatialUnitId);
//			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
//					"Tried to delete indicator layers for spatial unit, but no dataset exists that is associated to a spatial unit with id " + spatialUnitId);
			return false;
		}
		
	}

	public boolean deleteIndicatorDatasetByIdAndDate(String indicatorId, String spatialUnitId, BigDecimal year, BigDecimal month,
			BigDecimal day) throws Exception {
		logger.info("Trying to delete indicator dataset with datasetId '{}' and spatialUnitId '{}' and date '{}-{}-{}'", indicatorId, spatialUnitId, year, month, day);
		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			MetadataIndicatorsEntity indicatorMetadataEntry = indicatorsMetadataRepo.findByDatasetId(indicatorId);
			IndicatorSpatialUnitJoinEntity indicatorForSpatialUnit = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
		
			/*
			 * delete featureTable and views for each spatial unit
			 */
			String indicatorViewTableName = indicatorForSpatialUnit.getIndicatorValueTableName();
//			IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);
			

			/*
			 * delete timestamp for indicator and spatial unit
			 */
			IndicatorDatabaseHandler.deleteIndicatorTimeStamp(indicatorForSpatialUnit.getIndicatorValueTableName(), year, month, day);
			indicatorMetadataEntry = deleteTimestampInMetadataEntry(year, month, day, indicatorMetadataEntry);	
			indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);
			
			indicatorViewTableName = IndicatorDatabaseHandler.createOrReplaceIndicatorView_fromViewTableName(indicatorViewTableName, indicatorForSpatialUnit.getSpatialUnitName());
			
			/*
			 * republish indicator layer as OGC service
			 */
			String spatialUnitName = indicatorForSpatialUnit.getSpatialUnitName();			
			
			String datasetTitle = createTitleForWebService(spatialUnitName, indicatorMetadataEntry.getDatasetName());
			
			String styleName;
			
			try {
				DefaultClassificationMappingType defaultClassificationMapping = IndicatorsMapper.extractDefaultClassificationMappingFromMetadata(indicatorMetadataEntry);
				styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle, indicatorViewTableName);
				
				// handle OGC web service
				ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, datasetTitle, styleName, ResourceTypeEnum.INDICATOR);
			} catch (Exception e) {
				logger.error("Error while publishing as OGC service. Error is: \n{}", e);
			}			
			return true;
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Delete request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public String addIndicator(IndicatorPOSTInputType indicatorData) throws Exception {
		String metadataId = null;
		String spatialUnitName = null;
		String indicatorViewTableName = null;
		boolean publishedAsService = false;
		try {
			/*
			 * addIndicator can be called multiple times, i.e. for each spatialUnitName!
			 * (there is only 1 metadata entry for the indicator, but for each spatial unit there is one indicator value table
			 * and one feature view. Thus add will be called for each combination of indicator and spatial unit)
			 */
			String indicatorName = indicatorData.getDatasetName();
			spatialUnitName = indicatorData.getApplicableSpatialUnit();
			CreationTypeEnum creationType = indicatorData.getCreationType();
			String characteristicValue = indicatorData.getCharacteristicValue();
			IndicatorTypeEnum indicatorType = indicatorData.getIndicatorType();
			logger.info("Trying to persist indicator with name '{}', characteristicValue '{}', indicatorType '{}', creationType '{}' and associated spatialUnitName '{}'", indicatorName, characteristicValue, indicatorType, creationType.toString(), spatialUnitName);

			/*
			 * analyse input type
			 * 
			 * store metadata entry for indicator
			 * 
			 * create db table and view for actual values and features
			 * 
			 * return metadata id
			 */

			if (indicatorsMetadataRepo.existsByDatasetNameAndCharacteristicValueAndIndicatorType(indicatorName, characteristicValue, indicatorType)) {
				logger.error(
						"The indicator metadataset with datasetName '{}', characteristicValue '{}' and indicatorType '{}' already exists. Thus aborting add indicator request.",
						indicatorName, characteristicValue, indicatorType);
				throw new Exception("Indicator for indicatorName, characteristicValue and indicatorType already exists. Aborting add indicator request.");
			}
			
			MetadataIndicatorsEntity indicatorMetadataEntity = null;
			metadataId = null;
			
			indicatorMetadataEntity = createMetadata(indicatorData);
			indicatorMetadataEntity.setRoles(retrieveRoles(indicatorData.getAllowedRoles()));
			metadataId = indicatorMetadataEntity.getDatasetId();

			ReferenceManager.createReferences(indicatorData.getRefrencesToGeoresources(), 
					indicatorData.getRefrencesToOtherIndicators(), metadataId);
			
			/*
			 * only if creationType == INSERTION then create table and view
			 */

			if(creationType.equals(CreationTypeEnum.INSERTION)){
				
				logger.info("As creationType is set to '{}', a featureTable and featureView will be created from indicator values. Also OGC publishing will be done.", creationType.toString());
				String indicatorValueTableName = createIndicatorValueTable(indicatorData.getIndicatorValues(), metadataId);
				indicatorViewTableName = createOrReplaceIndicatorView_fromValueTableName(indicatorValueTableName, spatialUnitName, metadataId);
//				deleteIndicatorValueTable(indicatorTempTableName);				
				
				// handle OGC web service
				String styleName = publishDefaultStyleForWebServices(indicatorData.getDefaultClassificationMapping(), createTitleForWebService(spatialUnitName, indicatorName), indicatorViewTableName);
				publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, createTitleForWebService(spatialUnitName, indicatorName), styleName, ResourceTypeEnum.INDICATOR);
				
				persistNamesOfIndicatorTablesAndServicesInJoinTable(metadataId, indicatorName, spatialUnitName, indicatorViewTableName, styleName);
				
			} else{
				logger.info("As creationType is set to '{}', Only the metadata entry was created. No featureTable and view have been created..", creationType.toString());
			}
		} catch (Exception e) {
			/*
			 * remove partially created resources and thrwo error
			 */
			logger.error("Error while creating indicator. Error message: " + e.getMessage());
			e.printStackTrace();
			
			logger.info("Deleting partially created resources");
			
			try {
				logger.info("Delete metadata entry if exists for id '{}'" + metadataId);
				if(metadataId != null){
					if (indicatorsMetadataRepo.existsByDatasetId(metadataId))
						indicatorsMetadataRepo.deleteByDatasetId(metadataId);
				}
				
				logger.info("Delete indicatorValue table if exists for tableName '{}'" + indicatorViewTableName);
				if(indicatorViewTableName != null){
					IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorViewTableName);
				}
				
				logger.info("Unpublish OGC services if exists");
				if(publishedAsService){
					ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
				}
				
				
				logger.info("Delete indicatorSpatialUnitJoinEntities if exists for metadataId '{}'" + metadataId);
				if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(metadataId, spatialUnitName))
					indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataIdAndSpatialUnitName(metadataId, spatialUnitName);
				
				logger.info("Delete references to other indicators and georesources if exists for metadataId '{}'" + metadataId);
				ReferenceManager.removeReferences(metadataId);
				
			} catch (Exception e2) {
				logger.error("Error while deleting partially created georesource. Error message: " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
			throw e;
		}
		
		return metadataId;
	}

	private Collection<RolesEntity> retrieveRoles(List<String> roleIds) throws ResourceNotFoundException {
	    Collection<RolesEntity> allowedRoles = new ArrayList<>();
	    for (String id : roleIds) {
	        RolesEntity role = rolesRepository.findByRoleId(id);
	        if(role == null) {
	            throw new ResourceNotFoundException(400, String.format("The requested role %s does not exist.", id));
            }
	        if (!allowedRoles.contains(role)) {
                allowedRoles.add(role);
            }
        }
	    return allowedRoles;
    }

//	private void handleInitialIndicatorPersistanceAndPublishing(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, String indicatorName,
//			String spatialUnitName, String metadataId) throws CQLException, IOException, SQLException, Exception {
//		String indicatorValueTableName = createIndicatorValueTable(indicatorValues, metadataId);
//		String indicatorFeatureViewName = createOrReplaceIndicatorFeatureView(indicatorValueTableName, spatialUnitName, metadataId);
//		
//		// handle OGC web service
//		ogcServiceManager.publishDbLayerAsOgcService(indicatorFeatureViewName, ResourceTypeEnum.INDICATOR);
//		
//		persistNamesOfIndicatorTablesAndServicesInJoinTable(metadataId, indicatorName, spatialUnitName, indicatorValueTableName, indicatorFeatureViewName);
//		
//	}
//	
//	private void updateJoinTableWithOgcServiceUrls(String metadataId, String indicatorViewName, String indicatorValueTableName) {
////		MetadataIndicatorsEntity metadata = indicatorsMetadataRepo.findByDatasetId(metadataId);
////		
////		metadata.setWmsUrl(ogcServiceManager.getWmsUrl(indicatorViewName));
////		metadata.setWfsUrl(ogcServiceManager.getWfsUrl(indicatorViewName));
////		
////		indicatorsMetadataRepo.saveAndFlush(metadata);
//		
////		indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(metadataId, spatialUnitId)
//	}

	private MetadataIndicatorsEntity addNewTimestampsToMetadataEntry(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues,
			MetadataIndicatorsEntity indicatorMetadataEntity) throws Exception {
		
		List<String> timestamps = new ArrayList<String>();
		
		if (indicatorValues != null && indicatorValues.size() > 0){
			List<IndicatorPOSTInputTypeValueMapping> exampleValueMapping = indicatorValues.get(0).getValueMapping();
			
			for (IndicatorPOSTInputTypeValueMapping indicatorPOSTInputTypeValueMapping : exampleValueMapping) {
				LocalDate timestamp_localDate = indicatorPOSTInputTypeValueMapping.getTimestamp();
				Date timestamp_date = DateTimeUtil.fromLocalDate(timestamp_localDate);
				
				String timestamp_propertyName = IndicatorDatabaseHandler.createDateStringForDbProperty(timestamp_date);	
				// replace Date prefix!!!!!! we only require the date itself here
				timestamp_propertyName = timestamp_propertyName.replace(IndicatorDatabaseHandler.DATE_PREFIX, "");
				timestamps.add(timestamp_propertyName);
			}
			
			indicatorMetadataEntity.addTimestampsIfNotExist(timestamps);
		}
		
		return indicatorMetadataEntity;	
	}
	
	private MetadataIndicatorsEntity deleteTimestampInMetadataEntry(BigDecimal year, BigDecimal month, BigDecimal day,
			MetadataIndicatorsEntity indicatorMetadataEntry) throws Exception {
		Date date = new GregorianCalendar(year.intValue(), month.intValue() - 1, day.intValue()).getTime();
		logger.info("parsing date from submitted date components. Submitted components were 'year: {}, month: {}, day: {}'. As Java time treats month 0-based, the follwing date will be used: 'year-month(-1)-day {}-{}-{}'", year, month, day, year, month.intValue()-1, day);
		String datePropertyName = IndicatorDatabaseHandler.createDateStringForDbProperty(date);
		datePropertyName = datePropertyName.replace(IndicatorDatabaseHandler.DATE_PREFIX, "");
		
		indicatorMetadataEntry.removeTimestampIfExists(datePropertyName);
		
		return indicatorMetadataEntry;	
	}

	private void deleteIndicatorValueTable(String indicatorTempTableName) throws IOException, SQLException {
		logger.info("Deleting indicator table with name {}.", indicatorTempTableName);

		IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorTempTableName);;

		logger.info("Completed deletion.");
		
	}

	private String createOrReplaceIndicatorView_fromValueTableName(String indicatorValueableName, String spatialUnitName,
			String metadataId) throws IOException, SQLException {
		/*
		 * create view joining indicator values and spatial unit features
		 */
		logger.info("Trying to create unique table joining indicator values and spatial unit features.");

		String dbViewName = IndicatorDatabaseHandler.createOrReplaceIndicatorView_fromValueTableName(indicatorValueableName, spatialUnitName);

		logger.info("Completed creation of indicator feature table corresponding to datasetId {}. Table name is {}.",
				metadataId, dbViewName);

		return dbViewName;
	}
	
	private String createOrReplaceIndicatorView_fromViewName(String indicatorViewTableName, String spatialUnitName,
			String metadataId) throws IOException, SQLException {
		/*
		 * create view joining indicator values and spatial unit features
		 */
		logger.info("Trying to create unique table joining indicator values and spatial unit features.");

		String dbViewName = IndicatorDatabaseHandler.createOrReplaceIndicatorView_fromViewTableName(indicatorViewTableName, spatialUnitName);

		logger.info("Completed creation of indicator feature table corresponding to datasetId {}. Table name is {}.",
				metadataId, dbViewName);

		return dbViewName;
	}

	private String createIndicatorValueTable(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, 
			String metadataId) throws CQLException, IOException, SQLException {
		/*
		 * write indicator values to a new unique db table
		 */
		logger.info("Trying to create unique table for indicator values.");

		String dbTableName = IndicatorDatabaseHandler.createIndicatorValueTable(indicatorValues);

		logger.info("Completed creation of indicator values table corresponding to datasetId {}. Table name is {}.",
				metadataId, dbTableName);

		return dbTableName;
	}

	private void persistNamesOfIndicatorTablesAndServicesInJoinTable(String indicatorMetadataId, String indicatorName, String spatialUnitName, 
			String indicatorViewTableName, String styleName) {
		logger.info(
				"Create or modify entry in indicator spatial units join table for indicatorId '{}', and spatialUnitName '{}'. Set indicatorValueTable with name '{}'.",
				indicatorMetadataId, spatialUnitName, indicatorViewTableName);
		
		MetadataSpatialUnitsEntity spatialUnitMetadataEntity = DatabaseHelperUtil.getSpatialUnitMetadataEntityByName(spatialUnitName);
		String spatialUnitId = spatialUnitMetadataEntity.getDatasetId();
		
		IndicatorSpatialUnitJoinEntity entity = new IndicatorSpatialUnitJoinEntity();
		
		/*
		 * if an entity already exists for this combination of indicator and spatial unti then only modify those values
		 */
		if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorMetadataId, spatialUnitId))
			entity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorMetadataId, spatialUnitId);
		
		entity.setIndicatorMetadataId(indicatorMetadataId);
		entity.setIndicatorName(indicatorName);
		entity.setIndicatorValueTableName(indicatorViewTableName);
		entity.setSpatialUnitId(spatialUnitId);
		entity.setSpatialUnitName(spatialUnitName);
		entity.setWmsUrl(ogcServiceManager.getWmsUrl(indicatorViewTableName));
		entity.setWfsUrl(ogcServiceManager.getWfsUrl(indicatorViewTableName));
		entity.setDefaultStyleName(styleName);
		
		indicatorsSpatialUnitsRepo.saveAndFlush(entity);

		logger.info("Creation or modification of join entry successful.");
		
	}

	private MetadataIndicatorsEntity createMetadata(IndicatorPOSTInputType indicatorData) throws Exception {
		/*
		 * create instance of MetadataIndicatorEntity
		 * 
		 * persist in db
		 */
		logger.info("Trying to add indicator metadata entry.");

		MetadataIndicatorsEntity entity = new MetadataIndicatorsEntity();

		CommonMetadataType genericMetadata = indicatorData.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDatasetName(indicatorData.getDatasetName());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());
		entity.setDataBasis(genericMetadata.getDatabasis());
		entity.setNote(genericMetadata.getNote());
		entity.setLiterature(genericMetadata.getLiterature());

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());

		/*
		 * add topic to referenced topics, but only if topic is not yet included!
		 */
		entity.setTopicReference(indicatorData.getTopicReference());
		entity.setProcessDescription(indicatorData.getProcessDescription());
		entity.setUnit(indicatorData.getUnit());
		entity.setCreationType(indicatorData.getCreationType());
		entity.setIndicatorType(indicatorData.getIndicatorType());
		entity.setCharacteristicValue(indicatorData.getCharacteristicValue());
		entity.setLowestSpatialUnitForComputation(indicatorData.getLowestSpatialUnitForComputation());
		
		entity.setDefaultClassificationMappingItems(indicatorData.getDefaultClassificationMapping().getItems());
		entity.setColorBrewerSchemeName(indicatorData.getDefaultClassificationMapping().getColorBrewerSchemeName());
		
		entity.setAbbreviation(indicatorData.getAbbreviation());
		entity.setHeadlineIndicator(indicatorData.isIsHeadlineIndicator());
		entity.setInterpretation(indicatorData.getInterpretation());
		entity.setTags(new HashSet<String>(indicatorData.getTags()));
		

		/*
		 * the remaining properties cannot be set initially!
		 */
		entity.setDbTableName(null);
		entity.setWfsUrl(null);
		entity.setWmsUrl(null);
		
		/*
		 * process availableTimestamps property for indicator metadata entity
		 */
		entity = addNewTimestampsToMetadataEntry(indicatorData.getIndicatorValues(), entity);

		// persist in db
		indicatorsMetadataRepo.saveAndFlush(entity);
		logger.info("Completed to add indicator metadata entry for indicator dataset with id {}.",
				entity.getDatasetId());

		return entity;
	}

	public List<IndicatorPropertiesWithoutGeomType> getIndicatorFeaturePropertiesWithoutGeometry(String indicatorId,
			String spatialUnitId) throws SQLException, IOException, ResourceNotFoundException {
		logger.info("Retrieving all indicator feature properties without geometries from dataset with id '{}'for spatialUnitId '{}' ", indicatorId, spatialUnitId);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
				String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorValueTableName();

				List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = IndicatorDatabaseHandler.getIndicatorFeaturePropertiesWithoutGeometries(indicatorViewTableName);
				return indicatorFeaturePropertiesWithoutGeom;

			} else{
				logger.error(
						"No indicator dataset for the given indicatorId '{}' and spatialUnitId '{}' was found in database. Get request has no effect.",
						indicatorId, spatialUnitId);
				throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
						"Tried to get indicator features, but there is no table for the combination of indicatorId " 
								+ indicatorId + " and spatialUnitId " + spatialUnitId);
			}

		} else {
			logger.error(
					"No indicator dataset with indicatorId '{}' was found in database. Get request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public List<IndicatorPropertiesWithoutGeomType> getValidIndicatorFeaturePropertiesWithoutGeometry(
			String indicatorId, String spatialUnitId, BigDecimal year, BigDecimal month, BigDecimal day) throws ResourceNotFoundException, IOException, SQLException {
		logger.info("Retrieving valid indicator feature properties without geometries from dataset with id '{}'for spatialUnit '{}' for date '{}-{}-{}'", indicatorId, spatialUnitId,
				year, month, day);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
				String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorValueTableName();

				List<IndicatorPropertiesWithoutGeomType> validIndicatorFeaturePropertiesWithoutGeom = 
						IndicatorDatabaseHandler.getValidFeaturePropertiesWithoutGeometries(indicatorViewTableName, year, month, day);
				return validIndicatorFeaturePropertiesWithoutGeom;

			} else{
				logger.error(
						"No indicator dataset for the given indicatorId '{}' and spatialUnitId '{}' was found in database. Get request has no effect.",
						indicatorId, spatialUnitId);
				throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
						"Tried to get valid indicator features, but there is no table for the combination of indicatorId " 
								+ indicatorId + " and spatialUnitId " + spatialUnitId);
			}
			
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Get request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public boolean deleteIndicatorReferencesByGeoresource(String georesourceId) {		
		return ReferenceManager.removeReferencesByGeoresourceId(georesourceId);
	}


}
