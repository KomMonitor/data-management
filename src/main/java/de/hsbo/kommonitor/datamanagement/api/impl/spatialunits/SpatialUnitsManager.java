package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;



import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
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
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.features.management.GeoJSON2DatabaseTool;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPUTInputType;

@Transactional
@Repository
@Component
public class SpatialUnitsManager {
	
	
	private static Logger logger = LoggerFactory.getLogger(SpatialUnitsManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

	public String addSpatialUnit(SpatialUnitPOSTInputType featureData) throws Exception {
		String datasetName = featureData.getSpatialUnitLevel();
		logger.info("Trying to persist spatialUnit with name '{}'", datasetName);
		
		/*
		 * analyse input type
		 * 
		 * store metadata entry for spatial unit
		 * 
		 * create db table for actual features
		 * 
		 * return metadata id
		 */
		
		if(spatialUnitsMetadataRepo.existsByDatasetName(datasetName)){
			logger.error("The spatialUnit metadataset with datasetName '{}' already exists. Thus aborting add spatial unit request.", datasetName);
			throw new Exception("SpatialUnit already exists. Aborting add spatialUnit request.");
		}
		
		String metadataId = createMetadata(featureData);
		
		boolean isCreated = createFeatureTable(featureData.getGeoJsonString(), featureData.getPeriodOfValidity(), metadataId);
		
		if(!isCreated) {
			/*
			 * TODO FIXME check if any resource has been persisted (metadata or features) --> remove it
			 */
			throw new Exception("An error occured during creation of spatial unit dataset.");
		}
		
		return metadataId;
	}

	private boolean createFeatureTable(String geoJsonString, PeriodOfValidityType periodOfValidityType, String metadataId) throws CQLException, IOException {
		/*
		 * write features to a new unique db table
		 * 
		 * and update metadata entry with the name of that table
		 */
		logger.info("Trying to create unique table for spatialUnit features.");
		
		/*
		 * PERIOD OF VALIDITY:
		 * all features will be enriched with this global setting when they are created for the first time
		 * 
		 * when they will are updated, then each feature might have different validity periods
		 */
		
		String dbTableName = GeoJSON2DatabaseTool.writeGeoJSONFeaturesToDatabase(ResourceTypeEnum.SPATIAL_UNIT, geoJsonString, periodOfValidityType, metadataId);
		
		logger.info("Completed creation of spatialUnit feature table corresponding to datasetId {}. Table name is {}.", metadataId, dbTableName);
		
		updateMetadataWithAssociatedFeatureTable(metadataId, dbTableName);
		
		return true;
	}

	private void updateMetadataWithAssociatedFeatureTable(String metadataId, String dbTableName) {
		logger.info("Updating spatial unit metadataset with datasetId {}. set dbTableName of associated feature table with name {}.", metadataId, dbTableName);
		
		MetadataSpatialUnitsEntity metadataset = spatialUnitsMetadataRepo.findByDatasetId(metadataId);
		
		metadataset.setDbTableName(dbTableName);
		
		spatialUnitsMetadataRepo.save(metadataset);
		
		logger.info("Updating successful");
	}

	private String createMetadata(SpatialUnitPOSTInputType featureData) {
		/*
		 * create instance of MetadataSpatialUnitsEntity
		 * 
		 * persist in db
		 */
		logger.info("Trying to add spatialUnit metadata entry.");
		
		MetadataSpatialUnitsEntity entity = new MetadataSpatialUnitsEntity();
		
		CommonMetadataType genericMetadata = featureData.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDatasetName(featureData.getSpatialUnitLevel());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());
		entity.setJsonSchema(featureData.getJsonSchema());
		
		java.util.Date lastUpdate = java.sql.Date.valueOf(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setNextLowerHierarchyLevel(featureData.getNextLowerHierarchyLevel());
		entity.setNextUpperHierarchyLevel(featureData.getNextUpperHierarchyLevel());
		entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());
		
		/*
		 * the remaining properties cannot be set initially!
		 */
		entity.setDbTableName(null);
		entity.setWfsUrl(null);
		entity.setWmsUrl(null);
		
		// persist in db
		spatialUnitsMetadataRepo.save(entity);
		logger.info("Completed to add spatialUnit metadata entry for spatialUnit dataset with id {}.", entity.getDatasetId());
		
		return entity.getDatasetId();
	}

	public boolean deleteSpatialUnitDatasetByName(String spatialUnitLevel) throws ResourceNotFoundException, IOException {
		logger.info("Trying to delete spatialUnit dataset with datasetName '{}'", spatialUnitLevel);
		if (spatialUnitsMetadataRepo.existsByDatasetName(spatialUnitLevel)){
			String dbTableName = spatialUnitsMetadataRepo.findByDatasetName(spatialUnitLevel).getDbTableName();
			/*
			 * delete featureTable
			 */
			GeoJSON2DatabaseTool.deleteFeatureTable(ResourceTypeEnum.SPATIAL_UNIT, dbTableName);
			/*
			 * delete metadata entry
			 */
			spatialUnitsMetadataRepo.deleteByDatasetName(spatialUnitLevel);
			
			return true;
		}else{
			logger.error("No spatialUnit dataset with datasetName '{}' was found in database. Delete request has no effect.", spatialUnitLevel);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), "Tried to delete spatialUnit dataset, but no dataset existes with datasetName " + spatialUnitLevel);
		}
	}

	public String updateFeatures(SpatialUnitPUTInputType featureData, String spatialUnitLevel) throws ResourceNotFoundException {
		logger.info("Trying to update spatialUnit features for datasetName '{}'", spatialUnitLevel);
		if (spatialUnitsMetadataRepo.existsByDatasetName(spatialUnitLevel)) {
			MetadataSpatialUnitsEntity metadataEntity= spatialUnitsMetadataRepo.findByDatasetName(spatialUnitLevel);
			String dbTableName = metadataEntity.getDbTableName();
			/*
			 * call DB tool to update features
			 */
			GeoJSON2DatabaseTool.updateFeatures(featureData, dbTableName);
			
			// set lastUpdate in metadata in case of successful update
			metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());
			
			spatialUnitsMetadataRepo.save(metadataEntity);
			return spatialUnitLevel;
		} else {
			logger.error("No spatialUnit dataset with datasetName '{}' was found in database. Update request has no effect.", spatialUnitLevel);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update spatialUnit features, but no dataset existes with datasetName " + spatialUnitLevel);
		}
	}

	public String updateMetadata(SpatialUnitPATCHInputType metadata, String spatialUnitLevel) throws ResourceNotFoundException {
		logger.info("Trying to update spatialUnit metadata for datasetName '{}'", spatialUnitLevel);
		if (spatialUnitsMetadataRepo.existsByDatasetName(spatialUnitLevel)) {
			MetadataSpatialUnitsEntity metadataEntity= spatialUnitsMetadataRepo.findByDatasetName(spatialUnitLevel);

			/*
			 * call DB tool to update features
			 */
			updateMetadata(metadata, metadataEntity);
			
			spatialUnitsMetadataRepo.save(metadataEntity);
			return spatialUnitLevel;
		} else {
			logger.error("No spatialUnit dataset with datasetName '{}' was found in database. Update request has no effect.", spatialUnitLevel);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update spatialUnit metadata, but no dataset existes with datasetName " + spatialUnitLevel);
		}
	}

	private void updateMetadata(SpatialUnitPATCHInputType metadata, MetadataSpatialUnitsEntity entity) {
		
		CommonMetadataType genericMetadata = metadata.getMetadata();
		entity.setContact(genericMetadata.getContact());
		entity.setDataSource(genericMetadata.getDatasource());
		entity.setDescription(genericMetadata.getDescription());
		
		java.util.Date lastUpdate = java.sql.Date.valueOf(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setNextLowerHierarchyLevel(metadata.getNextLowerHierarchyLevel());
		entity.setNextUpperHierarchyLevel(metadata.getNextUpperHierarchyLevel());
		entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());
		
		/*
		 * dbTanle name and OGC service urls may not be set here!
		 * they are set automatically by other components
		 */
	}

	public List<SpatialUnitOverviewType> getAllSpatialUnitsMetadata() throws IOException, SQLException {
		logger.info("Retrieving all spatialUnits metadata from db");

		List<MetadataSpatialUnitsEntity> spatialUnitMeatadataEntities = spatialUnitsMetadataRepo.findAll();
		List<SpatialUnitOverviewType> swaggerSpatialUnitsMetadata = SpatialUnitsMapper.mapToSwaggerSpatialUnits(spatialUnitMeatadataEntities);

		return swaggerSpatialUnitsMetadata;
	}

	public SpatialUnitOverviewType getSpatialUnitByDatasetName(String spatialUnitLevel)
			throws IOException, SQLException {
		logger.info("Retrieving spatialUnit metadata for datasetName '{}'", spatialUnitLevel);

		MetadataSpatialUnitsEntity spatialUnitMetadataEntity = spatialUnitsMetadataRepo.findByDatasetName(spatialUnitLevel);
		SpatialUnitOverviewType swaggerSpatialUnitMetadata = SpatialUnitsMapper.mapToSwaggerSpatialUnit(spatialUnitMetadataEntity);

		return swaggerSpatialUnitMetadata;
	}

	public String getJsonSchemaForDatasetName(String spatialUnitLevel) {
		logger.info("Retrieving spatialUnit jsonSchema for datasetName '{}'", spatialUnitLevel);

		MetadataSpatialUnitsEntity spatialUnitMetadataEntity = spatialUnitsMetadataRepo.findByDatasetName(spatialUnitLevel);
		
		return spatialUnitMetadataEntity.getJsonSchema();
	}

	public String getValidSpatialUnitFeatures(String spatialUnitLevel, BigDecimal year, BigDecimal month,
			BigDecimal day) throws Exception {
		Calendar calender = Calendar.getInstance();
		calender.set(year.intValue(), month.intValueExact()-1, day.intValue());
		java.util.Date date = calender.getTime();
		logger.info("Retrieving valid spatialUnit Features from Dataset '{}' for date '{}'", spatialUnitLevel, date);
		
		if (spatialUnitsMetadataRepo.existsByDatasetName(spatialUnitLevel)) {
			MetadataSpatialUnitsEntity metadataEntity= spatialUnitsMetadataRepo.findByDatasetName(spatialUnitLevel);
	
		String dbTableName = metadataEntity.getDbTableName();
			
		String geoJson = GeoJSON2DatabaseTool.getValidFeatures(date, dbTableName);
		return geoJson;
		
		}else {
			logger.error("No spatialUnit dataset with datasetName '{}' was found in database. Get request has no effect.", spatialUnitLevel);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get spatialUnit features, but no dataset existes with datasetName " + spatialUnitLevel);
		}
		
	}
	
	
	//TODO: Methoden zum handling der SpatialUnits
	
}
