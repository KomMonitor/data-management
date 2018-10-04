package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

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
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePUTInputType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

@Transactional
@Repository
@Component
public class GeoresourcesManager {

	private static Logger logger = LoggerFactory.getLogger(GeoresourcesManager.class);

	/**
	*
	*/
	// @PersistenceContext
	// EntityManager em;

	@Autowired
	GeoresourcesMetadataRepository georesourcesMetadataRepo;
	
	@Autowired
	OGCWebServiceManager ogcServiceManager;

	public String addGeoresource(GeoresourcePOSTInputType featureData) throws Exception {
		
		String metadataId = null;
		String dbTableName = null;
		boolean publishedAsService = false;
		try {
			String datasetName = featureData.getDatasetName();
			logger.info("Trying to persist georesource with name '{}'", datasetName);

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

			if (georesourcesMetadataRepo.existsByDatasetName(datasetName)) {
				logger.error(
						"The georesource metadataset with datasetName '{}' already exists. Thus aborting add georesource request.",
						datasetName);
				throw new Exception("Georesource already exists. Aborting add georesource request.");
			}

			metadataId = createMetadata(featureData);

			dbTableName = createFeatureTable(featureData.getGeoJsonString(), featureData.getPeriodOfValidity(),
					metadataId);

			// handle OGC web service
			publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, ResourceTypeEnum.GEORESOURCE);

			/*
			 * set wms and wfs urls within metadata
			 */
			updateMetadataWithOgcServiceUrls(metadataId, dbTableName);

			return metadataId;
		} catch (Exception e) {
			/*
			 * remove partially created resources and thrwo error
			 */
			logger.error("Error while creating georesource. Error message: " + e.getMessage());
			e.printStackTrace();
			
			logger.info("Deleting partially created resources");
			
			try {
				logger.info("Delete metadata entry if exists for id '{}'" + metadataId);
				if(metadataId != null){
					if (georesourcesMetadataRepo.existsByDatasetId(metadataId))
						georesourcesMetadataRepo.deleteByDatasetId(metadataId);
				}
				
				logger.info("Delete feature table if exists for tableName '{}'" + dbTableName);
				if(dbTableName != null){
					SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.GEORESOURCE, dbTableName);;
				}
				
				logger.info("Unpublish OGC services if exists");
				if(publishedAsService){
					ogcServiceManager.unpublishDbLayer(dbTableName, ResourceTypeEnum.GEORESOURCE);
				}
			} catch (Exception e2) {
				logger.error("Error while deleting partially created georesource. Error message: " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
			throw e;
		}
	}

	private void updateMetadataWithOgcServiceUrls(String metadataId, String dbTableName) {
		MetadataGeoresourcesEntity metadata = georesourcesMetadataRepo.findByDatasetId(metadataId);
		
		metadata.setWmsUrl(ogcServiceManager.getWmsUrl(dbTableName));
		metadata.setWfsUrl(ogcServiceManager.getWfsUrl(dbTableName));
		
		georesourcesMetadataRepo.saveAndFlush(metadata);
	}

	private String createFeatureTable(String geoJsonString, PeriodOfValidityType periodOfValidity, String metadataId)
			throws CQLException, IOException {
		/*
		 * write features to a new unique db table
		 * 
		 * and update metadata entry with the name of that table
		 */
		logger.info("Trying to create unique table for georesource features.");

		/*
		 * PERIOD OF VALIDITY: all features will be enriched with this global
		 * setting when they are created for the first time
		 * 
		 * when they will are updated, then each feature might have different
		 * validity periods
		 */

		String dbTableName = SpatialFeatureDatabaseHandler.writeGeoJSONFeaturesToDatabase(ResourceTypeEnum.GEORESOURCE,
				geoJsonString, periodOfValidity, metadataId);

		logger.info("Completed creation of georesource feature table corresponding to datasetId {}. Table name is {}.",
				metadataId, dbTableName);

		updateMetadataWithAssociatedFeatureTable(metadataId, dbTableName);

		return dbTableName;
	}

	private void updateMetadataWithAssociatedFeatureTable(String metadataId, String dbTableName) {
		logger.info(
				"Updating georesource metadataset with datasetId {}. set dbTableName of associated feature table with name {}.",
				metadataId, dbTableName);

		MetadataGeoresourcesEntity metadataset = georesourcesMetadataRepo.findByDatasetId(metadataId);

		metadataset.setDbTableName(dbTableName);

		georesourcesMetadataRepo.saveAndFlush(metadataset);

		logger.info("Updating successful");

	}

	private String createMetadata(GeoresourcePOSTInputType featureData) throws Exception {
		/*
		 * create instance of MetadataGeoresourceEntity
		 * 
		 * persist in db
		 */
		logger.info("Trying to add georesource metadata entry.");

		MetadataGeoresourcesEntity entity = new MetadataGeoresourcesEntity();

		CommonMetadataType genericMetadata = featureData.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDatasetName(featureData.getDatasetName());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());
		entity.setJsonSchema(featureData.getJsonSchema());

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());

		/*
		 * add topic to referenced topics, but only if topic is not yet included!
		 */
		entity.addTopicsIfNotExist(featureData.getApplicableTopics());

		/*
		 * the remaining properties cannot be set initially!
		 */
		entity.setDbTableName(null);
		entity.setWfsUrl(null);
		entity.setWmsUrl(null);

		// persist in db
		georesourcesMetadataRepo.saveAndFlush(entity);
		logger.info("Completed to add georesource metadata entry for georesource dataset with id {}.",
				entity.getDatasetId());

		return entity.getDatasetId();
	}

	public boolean deleteGeoresourceDatasetById(String georesourceId) throws Exception {
		logger.info("Trying to delete georesource dataset with datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			String dbTableName = georesourcesMetadataRepo.findByDatasetId(georesourceId).getDbTableName();
			/*
			 * delete featureTable
			 */
			SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.GEORESOURCE, dbTableName);
			/*
			 * delete metadata entry
			 */
			georesourcesMetadataRepo.deleteByDatasetId(georesourceId);
			
			// handle OGC web service
			ogcServiceManager.unpublishDbLayer(dbTableName, ResourceTypeEnum.GEORESOURCE);

			return true;
		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Delete request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete georesource dataset, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public boolean deleteGeoresourceFeaturesByIdAndDate(String georesourceId, BigDecimal year, BigDecimal month,
			BigDecimal day) throws ResourceNotFoundException, IOException {
		/*
		 * TODO implement
		 */
//		logger.info("Deleting georesource features for datasetId '{}' and date '{}-{}-{}'", georesourceId, year, month, day);
//		
//		try {
//			
//			if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
//				String dbTableName = georesourcesMetadataRepo.findByDatasetId(georesourceId).getDbTableName();
//				/*
//				 * delete featureTable
//				 */
//				SpatialFeatureDatabaseHandler.deleteFeaturesForDate(ResourceTypeEnum.GEORESOURCE, dbTableName);
//				/*
//				 * delete metadata entry
//				 */
//				georesourcesMetadataRepo.deleteByDatasetId(georesourceId);
//		
//				// handle OGC web service
//				ogcServiceManager.unpublishDbLayer(dbTableName, ResourceTypeEnum.GEORESOURCE);
//
//				return true;
//			} else {
//				logger.error(
//						"No georesource dataset with datasetName '{}' was found in database. Delete request has no effect.",
//						georesourceId);
//				throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
//						"Tried to delete georesource dataset, but no dataset existes with datasetId " + georesourceId);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("Error while deleting georesource features. Error message is '{}'", e.getMessage());
//			throw e;
//		}
		
		return false;
	}

	public GeoresourceOverviewType getGeoresourceByDatasetId(String georesourceId) throws IOException, SQLException {
		logger.info("Retrieving georesources metadata for datasetId '{}'", georesourceId);

		MetadataGeoresourcesEntity georesourceMetadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);
		GeoresourceOverviewType swaggerGeoresourceMetadata = GeoresourcesMapper
				.mapToSwaggerGeoresource(georesourceMetadataEntity);

		return swaggerGeoresourceMetadata;
	}

	public String getValidGeoresourceFeatures(String georesourceId, BigDecimal year, BigDecimal month, BigDecimal day)
			throws Exception {
		Calendar calender = Calendar.getInstance();
		calender.set(year.intValue(), month.intValueExact() - 1, day.intValue());
		java.util.Date date = calender.getTime();
		logger.info("Retrieving valid georesource features from Dataset with id '{}' for date '{}'", georesourceId,
				date);

		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			String dbTableName = metadataEntity.getDbTableName();

			String geoJson = SpatialFeatureDatabaseHandler.getValidFeatures(date, dbTableName);
			return geoJson;

		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public String getJsonSchemaForDatasetName(String georesourceId) {
		logger.info("Retrieving georesource jsonSchema for datasetId '{}'", georesourceId);

		MetadataGeoresourcesEntity spatialUnitMetadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

		return spatialUnitMetadataEntity.getJsonSchema();
	}

	public String updateFeatures(GeoresourcePUTInputType featureData, String georesourceId)
			throws Exception {
		logger.info("Trying to update georesource features for datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetName(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);
			String datasetName = metadataEntity.getDatasetName();
			String dbTableName = metadataEntity.getDbTableName();
			/*
			 * call DB tool to update features
			 */
			SpatialFeatureDatabaseHandler.updateGeoresourceFeatures(featureData, dbTableName);

			// set lastUpdate in metadata in case of successful update
			metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

			georesourcesMetadataRepo.saveAndFlush(metadataEntity);
			
			// handle OGC web service
			ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, ResourceTypeEnum.GEORESOURCE);

			/*
			 * set wms and wfs urls within metadata
			 */
			updateMetadataWithOgcServiceUrls(metadataEntity.getDatasetId(), dbTableName);
			
			return georesourceId;
		} else {
			logger.error(
					"No georesource dataset with datasetId '{}' was found in database. Update request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public String updateMetadata(GeoresourcePATCHInputType metadata, String georesourceId) throws Exception {
		logger.info("Trying to update georesource metadata for datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetName(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			/*
			 * call DB tool to update features
			 */
			updateMetadata(metadata, metadataEntity);

			georesourcesMetadataRepo.saveAndFlush(metadataEntity);
			return georesourceId;
		} else {
			logger.error(
					"No georesource dataset with datasetId '{}' was found in database. Update request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update georesource metadata, but no dataset existes with datasetId " + georesourceId);
		}
	}

	private void updateMetadata(GeoresourcePATCHInputType metadata, MetadataGeoresourcesEntity entity)
			throws Exception {
		CommonMetadataType genericMetadata = metadata.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());

		/*
		 * add topic to referenced topics, bu only if topic is not yet included!
		 */
		entity.addTopicsIfNotExist(metadata.getApplicableTopics());

		// persist in db
		georesourcesMetadataRepo.saveAndFlush(entity);
	}

	public List<GeoresourceOverviewType> getAllGeoresourcesMetadata(String topic) throws IOException, SQLException {
		/*
		 * topic is an optional parameter and thus might be null! then get all
		 * datasets!
		 */
		logger.info("Retrieving all georesources metadata for optional topic {} from db", topic);

		List<MetadataGeoresourcesEntity> georesourcesMeatadataEntities = georesourcesMetadataRepo.findAll();

		if (topic != null) {
			/*
			 * remove all entities that do not correspond to the topic
			 */
			georesourcesMeatadataEntities = removeEntitiesNotAssociatedToTopic(georesourcesMeatadataEntities, topic);
		}
		List<GeoresourceOverviewType> swaggerGeoresourcesMetadata = GeoresourcesMapper
				.mapToSwaggerGeoresources(georesourcesMeatadataEntities);

		return swaggerGeoresourcesMetadata;
	}

	private List<MetadataGeoresourcesEntity> removeEntitiesNotAssociatedToTopic(
			List<MetadataGeoresourcesEntity> georesourcesMeatadataEntities, String topic) {

		boolean isTopicIncluded = false;

		for (MetadataGeoresourcesEntity metadataGeoresourcesEntity : georesourcesMeatadataEntities) {
			Collection<TopicsEntity> georesourcesTopics = metadataGeoresourcesEntity.getGeoresourcesTopics();

			for (TopicsEntity topicsEntity : georesourcesTopics) {
				if (topicsEntity.getTopicName().equals(topic)) {
					isTopicIncluded = true;
					break;
				}
			}

			if (!isTopicIncluded)
				georesourcesMeatadataEntities.remove(metadataGeoresourcesEntity);

			// reset boolean value for the next iteration / element
			isTopicIncluded = false;
		}

		return georesourcesMeatadataEntities;
	}

}
