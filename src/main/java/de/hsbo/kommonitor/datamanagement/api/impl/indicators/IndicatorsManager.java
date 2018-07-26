package de.hsbo.kommonitor.datamanagement.api.impl.indicators;



import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
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
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
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
	
	
	//TODO References to georesources and other indicators
	
	private static Logger logger = LoggerFactory.getLogger(IndicatorsManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	IndicatorsMetadataRepository indicatorsMetadataRepo;
	
	public String updateMetadata(IndicatorPATCHInputType metadata, String indicatorId) throws Exception {
		logger.info("Trying to update indicator metadata for datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetName(indicatorId)) {
			MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

			/*
			 * call DB tool to update features
			 */
			updateMetadata(metadata, metadataEntity);

			indicatorsMetadataRepo.save(metadataEntity);
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
		indicatorsMetadataRepo.save(entity);
		
	}

	public String updateFeatures(IndicatorPUTInputType indicatorData, String indicatorId) throws ResourceNotFoundException, IOException {
		logger.info("Trying to update indicator features for datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetName(indicatorId)) {
			MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);
			String dbTableName = metadataEntity.getDbTableName();
			/*
			 * call DB tool to update features
			 */
			IndicatorDatabaseHandler.updateIndicatorFeatures(indicatorData, dbTableName);

			// set lastUpdate in metadata in case of successful update
			metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

			indicatorsMetadataRepo.save(metadataEntity);
			return indicatorId;
		} else {
			logger.error(
					"No indicator dataset with datasetId '{}' was found in database. Update request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public IndicatorOverviewType getIndicatorById(String indicatorId) {
		logger.info("Retrieving indicator metadata for datasetId '{}'", indicatorId);
		MetadataIndicatorsEntity indicatorsMetadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);
		
		
		List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(indicatorsMetadataEntity.getDatasetId());
		List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(indicatorsMetadataEntity.getDatasetId());
		
	
		IndicatorOverviewType swaggerIndicatorMetadata = IndicatorsMapper
				.mapToSwaggerIndicator(indicatorsMetadataEntity);
		swaggerIndicatorMetadata.setReferencedIndicators(indicatorReferences);
		swaggerIndicatorMetadata.setReferencedGeoresources(georesourcesReferences);
		
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
				.mapToSwaggerIndicator(indicatorsMeatadataEntities);

		for (IndicatorOverviewType indicatorOverviewType : swaggerIndicatorsMetadata) {
			
			List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(indicatorOverviewType.getIndicatorId());
			List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(indicatorOverviewType.getIndicatorId());
			
			indicatorOverviewType.setReferencedIndicators(indicatorReferences);
			indicatorOverviewType.setReferencedGeoresources(georesourcesReferences);	
		}
		
		
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

	public String getValidIndicatorFeatures(String indicatorId, String spatialUnitLevel, BigDecimal year,
			BigDecimal month, BigDecimal day)throws Exception {
		Calendar calender = Calendar.getInstance();
		calender.set(year.intValue(), month.intValueExact() - 1, day.intValue());
		java.util.Date date = calender.getTime();
		logger.info("Retrieving valid georesource features from Dataset with id '{}'for spatialUnit '{}' for date '{}'", indicatorId, spatialUnitLevel,
				date);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

			String dbTableName = metadataEntity.getDbTableName();
			String spatialUnit = metadataEntity.getAssociatedSpatialUnitMetadataId();

			String json = IndicatorDatabaseHandler.getValidFeatures(date, dbTableName, spatialUnit);
			return json;

		} else {
			logger.error(
					"No indicator dataset with datasetName '{}' was found in database. Get request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public String getValidIndicatorFeatures(String indicatorId, String spatialUnitLevel) throws Exception{
		logger.info("Retrieving valid georesource features from Dataset with id '{}'for spatialUnit '{}' ", indicatorId, spatialUnitLevel);

		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

			String dbTableName = metadataEntity.getDbTableName();
			String spatialUnit = metadataEntity.getAssociatedSpatialUnitMetadataId();

			String json = IndicatorDatabaseHandler.getValidFeatures(dbTableName, spatialUnit);
			return json;

		} else {
			logger.error(
					"No indicator dataset with datasetName '{}' was found in database. Get request has no effect.",
					indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get indicator features, but no dataset existes with datasetId " + indicatorId);
		}
	}

	public boolean deleteIndicatorDatasetById(String indicatorId) throws ResourceNotFoundException, IOException {
		logger.info("Trying to delete indicator dataset with datasetId '{}'", indicatorId);
		if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
			String dbTableName = indicatorsMetadataRepo.findByDatasetId(indicatorId).getDbTableName();
			/*
			 * delete featureTable
			 */
			IndicatorDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.INDICATOR, dbTableName);
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
		String datasetName = indicatorData.getDatasetName();
		logger.info("Trying to persist indicator with name '{}'", datasetName);

		/*
		 * analyse input type
		 * 
		 * store metadata entry for georesource
		 * 
		 * create db table for actual features
		 * 
		 * create reference to topics
		 * 
		 * return metadata id
		 */

		if (indicatorsMetadataRepo.existsByDatasetName(datasetName)) {
			logger.error(
					"The indicator metadataset with datasetName '{}' already exists. Thus aborting add georesource request.",
					datasetName);
			throw new Exception("Indicator already exists. Aborting add georesource request.");
		}

		String metadataId = createMetadata(indicatorData);

		boolean isCreated = createFeatureTable(indicatorData.getIndicatorValues(),
				metadataId);
		ReferenceManager.createReferences(indicatorData.getRefrencesToGeoresources(), indicatorData.getRefrencesToOtherIndicators(), metadataId);

		if (!isCreated) {
			/*
			 * TODO FIXME check if any resource has been persisted (metadata or
			 * features) --> remove it
			 */
			throw new Exception("An error occured during creation of indicators dataset.");
		}

		return metadataId;
	}

	private boolean createFeatureTable(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, String metadataId) throws CQLException, IOException, SQLException {
		/*
		 * write indicator values to a new unique db table
		 * 
		 * and update metadata entry with the name of that table
		 */
		logger.info("Trying to create unique table for indicator values.");

		String dbTableName = IndicatorDatabaseHandler.writeIndicatorsToDatabase(indicatorValues, metadataId);

		logger.info("Completed creation of indicator values table corresponding to datasetId {}. Table name is {}.",
				metadataId, dbTableName);

		updateMetadataWithAssociatedindicatorValueTable(metadataId, dbTableName);

		return true;
	}

	private void updateMetadataWithAssociatedindicatorValueTable(String metadataId, String dbTableName) {
		logger.info(
				"Updating indicator metadataset with datasetId {}. set dbTableName of associated value table with name {}.",
				metadataId, dbTableName);

		MetadataIndicatorsEntity metadataset = indicatorsMetadataRepo.findByDatasetId(metadataId);

		metadataset.setDbTableName(dbTableName);

		indicatorsMetadataRepo.save(metadataset);

		logger.info("Updating successful");
		
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
		
		entity.setAssociatedSpatialUnitMetadataId(indicatorData.getApplicableSpatialUnit());
		entity.setProcessDescription(indicatorData.getProcessDescription());
		entity.setUnit(indicatorData.getUnit());

		/*
		 * the remaining properties cannot be set initially!
		 */
		entity.setDbTableName(null);
		entity.setWfsUrl(null);
		entity.setWmsUrl(null);

		// persist in db
		indicatorsMetadataRepo.save(entity);
		logger.info("Completed to add indicator metadata entry for indicator dataset with id {}.",
				entity.getDatasetId());

		return entity.getDatasetId();
	}

	public boolean deleteIndicatorDatasetByIdAndDate(String indicatorId, BigDecimal year, BigDecimal month,
			BigDecimal day) throws ResourceNotFoundException, IOException {
		// TODO Auto-generated method stub
		return false;
	}


}
