package de.hsbo.kommonitor.datamanagement.api.impl.indicators;



import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

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
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorReferenceType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

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
	OGCWebServiceManager ogcServiceManager;
	
	public String updateMetadata(IndicatorPATCHInputType metadata, String indicatorId) throws Exception {
		logger.info("Trying to update indicator metadata for datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetName(indicatorId)) {
			MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

			/*
			 * call DB tool to update features
			 */
			updateMetadata(metadata, metadataEntity);

			indicatorsMetadataRepo.saveAndFlush(metadataEntity);
			ReferenceManager.updateReferences(metadata.getRefrencesToGeoresources(), metadata.getRefrencesToOtherIndicators(),metadataEntity.getDatasetId());
			
			return indicatorId;
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Update request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update indicator metadata, but no dataset existes with datasetId " + indicatorId);
		}
	}

	private void updateMetadata(IndicatorPATCHInputType metadata, MetadataIndicatorsEntity entity) throws Exception{
		CommonMetadataType genericMetadata = metadata.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());
		entity.setProcessDescription(metadata.getProcessDescription());
		entity.setUnit(metadata.getUnit());
	

		/*
		 * add topic to referenced topics, bu only if topic is not yet included!
		 */
		entity.addTopicsIfNotExist(metadata.getApplicableTopics());

		// persist in db
		indicatorsMetadataRepo.saveAndFlush(entity);
		
	}

	public String updateFeatures(IndicatorPUTInputType indicatorData, String indicatorId) throws Exception {
		logger.info("Trying to update indicator features for datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetName(indicatorId)) {
			String spatialUnitName = indicatorData.getApplicableSpatialUnit();
			
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName);
				String dbTableName = indicatorSpatialsUnitsEntity.getIndicatorValueTableName();
				/*
				 * call DB tool to update features
				 */
				IndicatorDatabaseHandler.updateIndicatorFeatures(indicatorData, dbTableName);

				// set lastUpdate in metadata in case of successful update
				MetadataIndicatorsEntity indicatorMetadataEntry = indicatorsMetadataRepo.findByDatasetId(indicatorId);
				
				String indicatorfeatureViewName = createOrReplaceIndicatorFeatureView(dbTableName, spatialUnitName, indicatorMetadataEntry.getDatasetId());
				
				indicatorMetadataEntry.setLastUpdate(java.util.Calendar.getInstance().getTime());

				indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);
				
				// handle OGC web service
				ogcServiceManager.publishDbLayerAsOgcService(indicatorfeatureViewName, ResourceTypeEnum.INDICATOR);
				
				/*
				 * set wms and wfs urls within metadata
				 */
				updateMetadataWithOgcServiceUrls(indicatorMetadataEntry.getDatasetId(), indicatorfeatureViewName);
				
				return indicatorId;
			} else{
				logger.error(
						"No indicator dataset for the given indicatorId '{}' and spatialUnitName '{}' was found in database. Update request has no effect.",
						indicatorId, spatialUnitName);
				throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
						"Tried to update indicator features, but there is no table for the combination of indicatorId " 
								+ indicatorId + " and spatialUnitName " + spatialUnitName);
			}
			
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Update request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public IndicatorOverviewType getIndicatorById(String indicatorId) throws IOException {
		logger.info("Retrieving indicator metadata for datasetId '{}'", indicatorId);
		MetadataIndicatorsEntity indicatorsMetadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);
		
		
		List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(indicatorsMetadataEntity.getDatasetId());
		List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(indicatorsMetadataEntity.getDatasetId());
		
	
		IndicatorOverviewType swaggerIndicatorMetadata = IndicatorsMapper
				.mapToSwaggerIndicator(indicatorsMetadataEntity, indicatorReferences, georesourcesReferences);
		
		return swaggerIndicatorMetadata;
	}

	public List<IndicatorOverviewType> getAllIndicatorsMetadata(String topic) throws IOException, SQLException  {
		logger.info("Retrieving all indicators metadata for optional topic {} from db", topic);

		List<MetadataIndicatorsEntity> indicatorsMeatadataEntities = indicatorsMetadataRepo.findAll();
		
		if (topic != null) {
			/*
			 * remove all entities that do not correspond to the topic
			 */
			indicatorsMeatadataEntities = removeEntitiesNotAssociatedToTopic(indicatorsMeatadataEntities, topic);
		}
		List<IndicatorOverviewType> swaggerIndicatorsMetadata = IndicatorsMapper
				.mapToSwaggerIndicators(indicatorsMeatadataEntities);
		
		return swaggerIndicatorsMetadata;
	}

	private List<MetadataIndicatorsEntity> removeEntitiesNotAssociatedToTopic(
			List<MetadataIndicatorsEntity> indicatorsMeatadataEntities, String topic) {
		boolean isTopicIncluded = false;

		for (MetadataIndicatorsEntity metadataIndicatorsEntity : indicatorsMeatadataEntities) {
			Collection<TopicsEntity> indicatorsTopics = metadataIndicatorsEntity.getIndicatorTopics();

			for (TopicsEntity topicsEntity : indicatorsTopics) {
				if (topicsEntity.getTopicName().equals(topic)) {
					isTopicIncluded = true;
					break;
				}
			}

			if (!isTopicIncluded)
				indicatorsMeatadataEntities.remove(metadataIndicatorsEntity);

			// reset boolean value for the next iteration / element
			isTopicIncluded = false;
		}

		return indicatorsMeatadataEntities;
	}

	public String getValidIndicatorFeatures(String indicatorId, String spatialUnitId, BigDecimal year,
			BigDecimal month, BigDecimal day)throws Exception {
		logger.info("Retrieving valid indicator features from Dataset with id '{}'for spatialUnit '{}' for date '{}-{}-{}'", indicatorId, spatialUnitId,
				year, month, day);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
				String featureViewTableName = indicatorSpatialsUnitsEntity.getFeatureViewTableName();

				String json = IndicatorDatabaseHandler.getValidFeatures(featureViewTableName, year, month, day);
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

	public String getIndicatorFeatures(String indicatorId, String spatialUnitId) throws Exception{
		logger.info("Retrieving all indicator features from Dataset with id '{}'for spatialUnitId '{}' ", indicatorId, spatialUnitId);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			if(indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)){
				IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
				String featureViewTableName = indicatorSpatialsUnitsEntity.getFeatureViewTableName();

				String json = IndicatorDatabaseHandler.getIndicatorFeatures(featureViewTableName);
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
			List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits = indicatorsSpatialUnitsRepo.findByIndicatorMetadataId(indicatorId);

			/*
			 * delete featureTables and views for each spatial unit
			 */
			for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {
				IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorSpatialUnitJoinEntity.getIndicatorValueTableName());
				String featureViewTableName = indicatorSpatialUnitJoinEntity.getFeatureViewTableName();
				IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);
				
				// handle OGC web service
				ogcServiceManager.unpublishDbLayer(featureViewTableName, ResourceTypeEnum.INDICATOR);
			}
			
			/*
			 * delete entries from indicatorsMetadataRepo
			 */
			indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataId(indicatorId);
			
			/*
			 * delete metadata entry
			 */
			indicatorsMetadataRepo.deleteByDatasetId(indicatorId);
			ReferenceManager.removeReferences(indicatorId);
			return true;
		} else {
			logger.error(
					"No indicator dataset with datasetName '{}' was found in database. Delete request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public String addIndicator(IndicatorPOSTInputType indicatorData) throws Exception {
		/*
		 * addIndicator can be called multiple times, i.e. for each spatialUnitName!
		 * (there is only 1 metadata entry for the indicator, but for each spatial unit there is one indicator value table
		 * and one feature view. Thus add will be called for each combination of indicator and spatial unit)
		 */
		String indicatorName = indicatorData.getDatasetName();
		String spatialUnitName = indicatorData.getApplicableSpatialUnit();
		logger.info("Trying to persist indicator with name '{}' and associated spatialUnitName '{}'", indicatorName, spatialUnitName);

		/*
		 * analyse input type
		 * 
		 * store metadata entry for indicatore
		 * 
		 * create db table and view for actual values and features
		 * 
		 * return metadata id
		 */

		if (indicatorsMetadataRepo.existsByDatasetName(indicatorName) && 
				indicatorsSpatialUnitsRepo.existsByIndicatorNameAndSpatialUnitName(indicatorName, spatialUnitName)) {
			logger.error(
					"The indicator metadataset with datasetName '{}' already exists for spatialUnitName '{}'. Thus aborting add indicator request.",
					indicatorName, spatialUnitName);
			throw new Exception("Indicator for applied spatialUnitName already exists. Aborting add indicator request.");
		}
		
		String metadataId = null;
		
		/*
		 * create metadata only if not exists
		 * 
		 * e.g. when it is called for the first spatial unit, then it will be created
		 * but when it is called for the same indicator but a different spatial unit, then
		 * is must not be recreated
		 */
		if(!indicatorsMetadataRepo.existsByDatasetName(indicatorName)){
			metadataId = createMetadata(indicatorData);

			ReferenceManager.createReferences(indicatorData.getRefrencesToGeoresources(), 
					indicatorData.getRefrencesToOtherIndicators(), metadataId);

		}

		String indicatorValueTableName = createIndicatorValueTable(indicatorData.getIndicatorValues(), metadataId);
		String indicatorfeatureViewName = createOrReplaceIndicatorFeatureView(indicatorValueTableName, spatialUnitName, metadataId);
		
		persistNamesOfCreatedTablesInJoinTable(metadataId, indicatorName, spatialUnitName, indicatorValueTableName, indicatorfeatureViewName);
		
		// handle OGC web service
		ogcServiceManager.publishDbLayerAsOgcService(indicatorfeatureViewName, ResourceTypeEnum.INDICATOR);
		
		/*
		 * set wms and wfs urls within metadata
		 */
		updateMetadataWithOgcServiceUrls(metadataId, indicatorfeatureViewName);
		
		return metadataId;
	}
	
	private void updateMetadataWithOgcServiceUrls(String metadataId, String dbTableName) {
		MetadataIndicatorsEntity metadata = indicatorsMetadataRepo.findByDatasetId(metadataId);
		
		metadata.setWmsUrl(ogcServiceManager.getWmsUrl(dbTableName));
		metadata.setWfsUrl(ogcServiceManager.getWfsUrl(dbTableName));
		
		indicatorsMetadataRepo.saveAndFlush(metadata);
	}

	private String createOrReplaceIndicatorFeatureView(String indicatorValueTableName, String spatialUnitName,
			String metadataId) throws IOException, SQLException {
		/*
		 * create view joining indicator values and spatial unit features
		 */
		logger.info("Trying to create unique view joining indicator values and spatial unit features.");

		String dbViewName = IndicatorDatabaseHandler.createOrReplaceIndicatorFeatureView(indicatorValueTableName, spatialUnitName);

		logger.info("Completed creation of indicator feature view corresponding to datasetId {}. View name is {}.",
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

	private void persistNamesOfCreatedTablesInJoinTable(String indicatorMetadataId, String indicatorName, String spatialUnitName, 
			String indicatorValueTableName, String indicatorFeatureViewName) {
		logger.info(
				"Create new entry in indicator spatial units join table for indicatorId '{}', and spatialUnitName '{}'. Set indicatorValueTable with name '{}'  and feature view with name '{}'.",
				indicatorMetadataId, spatialUnitName, indicatorValueTableName, indicatorFeatureViewName);
		
		MetadataSpatialUnitsEntity spatialUnitMetadataEntity = DatabaseHelperUtil.getSpatialUnitMetadataEntity(spatialUnitName);
		
		IndicatorSpatialUnitJoinEntity entity = new IndicatorSpatialUnitJoinEntity();
		entity.setFeatureViewTableName(indicatorFeatureViewName);
		entity.setIndicatorMetadataId(indicatorMetadataId);
		entity.setIndicatorName(indicatorName);
		entity.setIndicatorValueTableName(indicatorValueTableName);
		entity.setSpatialUnitId(spatialUnitMetadataEntity.getDatasetId());
		entity.setSpatialUnitName(spatialUnitName);

//		MetadataIndicatorsEntity metadataset = indicatorsMetadataRepo.findByDatasetId(metadataId);
//
//		metadataset.setDbTableName(indicatorValueTable);
//
//		indicatorsMetadataRepo.saveAndFlush(metadataset);
//
		logger.info("Creation of join entry successful.");
		
	}

	private String createMetadata(IndicatorPOSTInputType indicatorData) throws Exception {
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

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());

		/*
		 * add topic to referenced topics, but only if topic is not yet included!
		 */
		entity.addTopicsIfNotExist(indicatorData.getApplicableTopics());
		entity.setProcessDescription(indicatorData.getProcessDescription());
		entity.setUnit(indicatorData.getUnit());

		/*
		 * the remaining properties cannot be set initially!
		 */
		entity.setDbTableName(null);
		entity.setWfsUrl(null);
		entity.setWmsUrl(null);

		// persist in db
		indicatorsMetadataRepo.saveAndFlush(entity);
		logger.info("Completed to add indicator metadata entry for indicator dataset with id {}.",
				entity.getDatasetId());

		return entity.getDatasetId();
	}

	public boolean deleteIndicatorDatasetByIdAndDate(String indicatorId, String spatialUnitId, BigDecimal year, BigDecimal month,
			BigDecimal day) throws ResourceNotFoundException, IOException {
		// TODO Auto-generated method stub
		return false;
	}


}
