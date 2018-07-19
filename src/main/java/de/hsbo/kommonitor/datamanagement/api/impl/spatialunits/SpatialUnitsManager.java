package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;



import java.io.IOException;
import java.sql.Date;

import javax.transaction.Transactional;

import org.geotools.filter.text.cql2.CQLException;
import org.hibernate.annotations.GenerationTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.features.management.GeoJSON2DatabaseTool;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPOSTInputType;

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
	
	
	//TODO: Methoden zum handling der SpatialUnits
	
}
