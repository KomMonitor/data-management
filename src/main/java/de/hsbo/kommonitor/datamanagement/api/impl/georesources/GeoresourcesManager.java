package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import jakarta.transaction.Transactional;

import de.hsbo.kommonitor.datamanagement.model.*;
import org.geotools.filter.text.cql2.CQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsManager;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.scripts.ScriptManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.util.SimplifyGeometriesEnum;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;

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
	OrganizationalUnitRepository organizationalUnitRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	OGCWebServiceManager ogcServiceManager;

	@Autowired
	private IndicatorsManager indicatorsManager;

	@Autowired
	private ScriptManager scriptManager;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;

	public GeoresourceOverviewType addGeoresource(GeoresourcePOSTInputType featureData) throws Exception {
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

			MetadataGeoresourcesEntity georesourceMetadataEntity = createMetadata(featureData);
			metadataId = georesourceMetadataEntity.getDatasetId();

			dbTableName = createFeatureTable(featureData.getGeoJsonString(), featureData.getPeriodOfValidity(),
					metadataId);

			// handle OGC web service - null parameter is defaultStyle
			publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null,
					ResourceTypeEnum.GEORESOURCE);

			/*
			 * set wms and wfs urls within metadata
			 */
			updateMetadataWithOgcServiceUrls(metadataId, dbTableName);

			return GeoresourcesMapper.mapToSwaggerGeoresource(georesourcesMetadataRepo.findByDatasetId(metadataId));
		} catch (Exception e) {
			/*
			 * remove partially created resources and thrwo error
			 */
			logger.error("Error while creating georesource. Error message: " + e.getMessage());
			e.printStackTrace();

			logger.info("Deleting partially created resources");

			try {
				logger.info("Delete metadata entry if exists for id '{}'" + metadataId);
				if (metadataId != null) {
					if (georesourcesMetadataRepo.existsByDatasetId(metadataId))
						georesourcesMetadataRepo.deleteByDatasetId(metadataId);
				}

				logger.info("Delete feature table if exists for tableName '{}'" + dbTableName);
				if (dbTableName != null) {
					SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.GEORESOURCE, dbTableName);
					;
				}

				logger.info("Unpublish OGC services if exists");
				if (publishedAsService) {
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

	private Collection<PermissionEntity> retrievePermissions(List<String> permissionIds) throws ResourceNotFoundException {
		Collection<PermissionEntity> allowedRoles = new ArrayList<>();
		for (String id : permissionIds) {
			PermissionEntity role = permissionRepository.findByPermissionId(id);
			if (role == null) {
				throw new ResourceNotFoundException(400, String.format("The requested role %s does not exist.", id));
			}
			if (!allowedRoles.contains(role)) {
				allowedRoles.add(role);
			}
		}
		return allowedRoles;
	}

	private OrganizationalUnitEntity getOrganizationalUnitEntity(String id) throws ResourceNotFoundException {
		OrganizationalUnitEntity entity = organizationalUnitRepository.findByOrganizationalUnitId(id);
		if (entity == null) {
			throw new ResourceNotFoundException(400, String.format("The requested organizationalUnit does not exist.", id));
		}
		return entity;
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
		 * PERIOD OF VALIDITY: all features will be enriched with this global setting
		 * when they are created for the first time
		 *
		 * when they will are updated, then each feature might have different validity
		 * periods
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

	private MetadataGeoresourcesEntity createMetadata(GeoresourcePOSTInputType featureData) throws Exception {
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
		entity.setDataBasis(genericMetadata.getDatabasis());
		entity.setNote(genericMetadata.getNote());
		entity.setLiterature(genericMetadata.getLiterature());
		entity.setJsonSchema(featureData.getJsonSchema());

		java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
		if (lastUpdate == null)
			lastUpdate = java.util.Calendar.getInstance().getTime();
		entity.setLastUpdate(lastUpdate);
		entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());
		entity.setPOI(featureData.getIsPOI());
		entity.setLOI(featureData.getIsLOI());
		entity.setAOI(featureData.getIsAOI());
		entity.setPoiSymbolBootstrap3Name(featureData.getPoiSymbolBootstrap3Name());
		entity.setPoiMarkerColor(featureData.getPoiMarkerColor());
		entity.setPoiSymbolColor(featureData.getPoiSymbolColor());
		entity.setLoiColor(featureData.getLoiColor());
		if (featureData.getLoiWidth() != null) {
			entity.setLoiWidth(featureData.getLoiWidth().intValue());
		} else {
			entity.setLoiWidth(3);
		}
		entity.setLoiDashArrayString(featureData.getLoiDashArrayString());
		entity.setAoiColor(featureData.getAoiColor());

		entity.setTopicReference(featureData.getTopicReference());

		/*
		 * the remaining properties cannot be set initially!
		 */
		entity.setDbTableName(null);
		entity.setWfsUrl(null);
		entity.setWmsUrl(null);

		entity.setPermissions(retrievePermissions(featureData.getAllowedRoles()));
		entity.setOwner(getOrganizationalUnitEntity(featureData.getOwnerId()));

		// persist in db
		georesourcesMetadataRepo.saveAndFlush(entity);
		logger.info("Completed to add georesource metadata entry for georesource dataset with id {}.",
				entity.getDatasetId());

		return entity;
	}

	public boolean deleteAllGeoresourceFeaturesById(String georesourceId) throws Exception {
		logger.info("Trying to delete all georesource features for dataset with datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {

			MetadataGeoresourcesEntity georesourceEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			String dbTableName = georesourceEntity.getDbTableName();
			/*
			 * delete features and their properties
			 */
			SpatialFeatureDatabaseHandler.deleteAllFeaturesFromFeatureTable(ResourceTypeEnum.GEORESOURCE, dbTableName);

			georesourceEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

			georesourcesMetadataRepo.saveAndFlush(georesourceEntity);

			// handle OGC web service - null parameter is defaultStyle
			ogcServiceManager.publishDbLayerAsOgcService(dbTableName, georesourceEntity.getDatasetName(), null,
					ResourceTypeEnum.GEORESOURCE);

			/*
			 * set wms and wfs urls within metadata
			 */
			updateMetadataWithOgcServiceUrls(georesourceEntity.getDatasetId(), dbTableName);

			return true;
		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Delete request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete georesource features, but no dataset existes with datasetId " + georesourceId);
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

	public boolean deleteGeoresourceDatasetById(String georesourceId) throws Exception {
		logger.info("Trying to delete georesource dataset with datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {

			boolean success = true;

			try {
				boolean deletedReferences = indicatorsManager.deleteIndicatorReferencesByGeoresource(georesourceId);
			} catch (Exception e) {
				logger.error("Error while deleting indicator references for georesource with id {}", georesourceId);
				logger.error("Error was: {}", e.getMessage());
				e.printStackTrace();
			}

			try {
				boolean deleteScriptsForGeoresource = scriptManager.deleteScriptsByGeoresourceId(georesourceId);
			} catch (Exception e) {
				logger.error("Error while deleting scripts for georesource with id {}", georesourceId);
				logger.error("Error was: {}", e.getMessage());
				e.printStackTrace();
			}

			MetadataGeoresourcesEntity georesourceEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			// delete any linked roles first
			try {
				georesourceEntity = removeAnyLinkedRoles(georesourceEntity);
			} catch (Exception e) {
				logger.error("Error while deleting roles for georesource with id {}", georesourceId);
				logger.error("Error was: {}", e.getMessage());
				e.printStackTrace();
			}
			// now remove feature data and remaining

			String dbTableName = georesourceEntity.getDbTableName();

			try {
				/*
				 * delete featureTable
				 */
				SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.GEORESOURCE, dbTableName);
			} catch (Exception e) {
				logger.error("Error while deleting feature table for georesource with id {}", georesourceId);
				logger.error("Error was: {}", e.getMessage());
				e.printStackTrace();
			}

			try {
				/*
				 * delete metadata entry
				 */
				georesourcesMetadataRepo.deleteByDatasetId(georesourceId);
			} catch (Exception e) {
				logger.error("Error while deleting metadata entry for georesource with id {}", georesourceId);
				logger.error("Error was: {}", e.getMessage());
				e.printStackTrace();
				success = false;
			}

			try {
				// handle OGC web service
				ogcServiceManager.unpublishDbLayer(dbTableName, ResourceTypeEnum.GEORESOURCE);
			} catch (Exception e) {
				logger.error("Error while unbublishing OGC service layer for georesource with id {}", georesourceId);
				logger.error("Error was: {}", e.getMessage());
				e.printStackTrace();
			}

			return success;
		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Delete request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete georesource dataset, but no dataset existes with datasetId " + georesourceId);
		}
	}

	private MetadataGeoresourcesEntity removeAnyLinkedRoles(MetadataGeoresourcesEntity georesourceEntity) {
		georesourceEntity.setPermissions(new ArrayList<>());

		georesourcesMetadataRepo.saveAndFlush(georesourceEntity);

		georesourceEntity = georesourcesMetadataRepo.findByDatasetId(georesourceEntity.getDatasetId());

		return georesourceEntity;
	}

	public GeoresourceOverviewType getGeoresourceByDatasetId(String georesourceId) throws Exception {
		return getGeoresourceByDatasetId(georesourceId, null);
	}

	public GeoresourceOverviewType getGeoresourceByDatasetId(String georesourceId, AuthInfoProvider authInfoProvider)
			throws Exception {
		logger.info("Retrieving georesources metadata for datasetId '{}'", georesourceId);

		MetadataGeoresourcesEntity georesourceMetadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

        if (authInfoProvider == null) {
            if (georesourceMetadataEntity == null || !georesourceMetadataEntity.isPublic()) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
            }
        } else {
            if (georesourceMetadataEntity == null || !authInfoProvider.checkPermissions(georesourceMetadataEntity, PermissionLevelType.VIEWER)) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
            }
            try {
                georesourceMetadataEntity.setUserPermissions(authInfoProvider.getPermissions(georesourceMetadataEntity));
            } catch (NoSuchElementException ex) {
                logger.error("No permissions found for georesource '{}'", georesourceMetadataEntity.getDatasetId());
            }

        }
        GeoresourceOverviewType swaggerGeoresourceMetadata = GeoresourcesMapper
                .mapToSwaggerGeoresource(georesourceMetadataEntity);

		return swaggerGeoresourceMetadata;
	}

	public String getAllGeoresourceFeatures(String georesourceId, String simplifyGeometries) throws Exception {
		return getAllGeoresourceFeatures(georesourceId, simplifyGeometries, null);
	}

	public String getAllGeoresourceFeatures(String georesourceId, String simplifyGeometries,
			AuthInfoProvider authInfoProvider) throws Exception {

		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

            if (authInfoProvider == null) {
                if (metadataEntity == null || !metadataEntity.isPublic()) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            } else {
                if (metadataEntity == null || !authInfoProvider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            }

			String dbTableName = metadataEntity.getDbTableName();

			String geoJson = SpatialFeatureDatabaseHandler.getAllFeatures(dbTableName, simplifyGeometries);
			return geoJson;

        } else {
            logger.error(
                    "No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
                    georesourceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
        }
    }

    public String getAllGeoresourceFeatures_withoutGeometry(String georesourceId) throws Exception {
		return getAllGeoresourceFeatures_withoutGeometry(georesourceId, null);
	}
    
	public String getAllGeoresourceFeatures_withoutGeometry(String georesourceId, AuthInfoProvider authInfoProvider)
			throws Exception {
            
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);
			
			if (authInfoProvider == null) {
                if (metadataEntity == null || !metadataEntity.isPublic()) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            } else {
                if (metadataEntity == null || !authInfoProvider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            }

			String dbTableName = metadataEntity.getDbTableName();

			String json = SpatialFeatureDatabaseHandler.getAllFeatures_withoutGeometry(dbTableName);
			return json;
		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

    public String getValidGeoresourceFeatures(String georesourceId, BigDecimal year, BigDecimal month, BigDecimal day, String simplifyGeometries)
            throws Exception {
        return getValidGeoresourceFeatures(georesourceId, year, month, day, simplifyGeometries, null);
    }

    public String getValidGeoresourceFeatures(String georesourceId, BigDecimal year, BigDecimal month, BigDecimal day, String simplifyGeometries, AuthInfoProvider provider)
            throws Exception {
        Calendar calender = Calendar.getInstance();
        calender.set(year.intValue(), month.intValueExact() - 1, day.intValue());
        java.util.Date date = calender.getTime();
        logger.info("Retrieving valid georesource features from Dataset with id '{}' for date '{}'", georesourceId,
                date);

        if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
            MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

            if (provider == null) {
                if (metadataEntity == null || !metadataEntity.isPublic()) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            } else {
                if (metadataEntity == null || !provider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            }

            String dbTableName = metadataEntity.getDbTableName();

            String geoJson = SpatialFeatureDatabaseHandler.getValidFeatures(date, dbTableName, simplifyGeometries);
            return geoJson;

        } else {
            logger.error(
                    "No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
                    georesourceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
        }
    }

    public String getValidGeoresourceFeatures_withoutGeometry(String georesourceId, BigDecimal year, BigDecimal month,
			BigDecimal day, AuthInfoProvider provider) throws Exception {
    	Calendar calender = Calendar.getInstance();
        calender.set(year.intValue(), month.intValueExact() - 1, day.intValue());
        java.util.Date date = calender.getTime();
        logger.info("Retrieving valid georesource features from Dataset with id '{}' for date '{}'", georesourceId,
                date);

        if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
            MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

            if (provider == null) {
                if (metadataEntity == null || !metadataEntity.isPublic()) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            } else {
                if (metadataEntity == null || !provider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            }

            String dbTableName = metadataEntity.getDbTableName();

            String json = SpatialFeatureDatabaseHandler.getValidFeatures_withoutGeometry(date, dbTableName);
            return json;

        } else {
            logger.error(
                    "No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
                    georesourceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
        }
	}

	public String getValidGeoresourceFeatures_withoutGeometry(String georesourceId, BigDecimal year, BigDecimal month,
			BigDecimal day) throws Exception {
		return getValidGeoresourceFeatures_withoutGeometry(georesourceId, year, month, day, null);
	}

	public String getJsonSchemaForDatasetName(String georesourceId) throws Exception {
		logger.info("Retrieving georesource jsonSchema for datasetId '{}'", georesourceId);

		MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

        if (metadataEntity == null || !metadataEntity.isPublic()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
        }

		return retrieveJsonSchema_georesource(georesourceId, metadataEntity);
	}

	public String getJsonSchemaForDatasetName(String georesourceId, AuthInfoProvider authInfoProvider)
			throws Exception {
		logger.info("Retrieving georesource jsonSchema for datasetId '{}'", georesourceId);

		MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

        if (metadataEntity == null || !authInfoProvider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
        }

        return retrieveJsonSchema_georesource(georesourceId, metadataEntity);
    }

	private String retrieveJsonSchema_georesource(String georesourceId, MetadataGeoresourcesEntity metadataEntity)
			throws Exception {
		String jsonSchema = metadataEntity.getJsonSchema();
//		if(jsonSchema == null) {
//        	jsonSchema = parseSchemaFromGeoresource(georesourceId);
//        	metadataEntity.setJsonSchema(jsonSchema);
//        	georesourcesMetadataRepo.saveAndFlush(metadataEntity);
//        }

		jsonSchema = parseSchemaFromGeoresource(georesourceId);
		metadataEntity.setJsonSchema(jsonSchema);
		georesourcesMetadataRepo.saveAndFlush(metadataEntity);

		return jsonSchema;
	}

	private String parseSchemaFromGeoresource(String georesourceId) throws Exception {

		MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

        if (metadataEntity == null ) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
        }

        SortedMap<String, String> elements = new TreeMap();
        
        String dbTableName = metadataEntity.getDbTableName();

		String allGeoresourceFeatures = SpatialFeatureDatabaseHandler.getAllFeatures(dbTableName, SimplifyGeometriesEnum.ORIGINAL.toString());

		SortedMap<String, String> properties = SpatialFeatureDatabaseHandler
				.guessSchemaFromFeatureValues(allGeoresourceFeatures);

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			String json = objectMapper.writeValueAsString(properties);
			System.out.println(json);

			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String updateFeatures(GeoresourcePUTInputType featureData, String georesourceId) throws Exception {
		logger.info("Trying to update georesource features for datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
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

			// handle OGC web service - null parameter is defaultStyle
			ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.GEORESOURCE);

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
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
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

	public String updatePermissions(PermissionLevelInputType permissionLevelInput, String georesourceId) throws Exception {
		logger.info("Trying to update georesource permissions for datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			metadataEntity.setPermissions(retrievePermissions(permissionLevelInput.getAllowedRoles()));

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

	public String updateOwnership(OwnerInputType owner, String georesourceId) throws Exception {
		logger.info("Trying to update georesource ownership for datasetId '{}'", georesourceId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);
			metadataEntity.setOwner(getOrganizationalUnitEntity(owner.getOwnerId()));

			georesourcesMetadataRepo.saveAndFlush(metadataEntity);
			return georesourceId;
		} else {
			logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Update request has no effect.", georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update spatialUnit metadata, but no dataset existes with datasetId " + georesourceId);
		}
	}

	private void updateMetadata(GeoresourcePATCHInputType metadata, MetadataGeoresourcesEntity entity)
			throws Exception {
		entity.setDatasetName(metadata.getDatasetName());

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
		entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
		entity.setUpdateIntervall(genericMetadata.getUpdateInterval());
		entity.setPOI(metadata.getIsPOI());
		entity.setLOI(metadata.getIsLOI());
		entity.setAOI(metadata.getIsAOI());
		entity.setPoiSymbolBootstrap3Name(metadata.getPoiSymbolBootstrap3Name());
		entity.setPoiMarkerColor(metadata.getPoiMarkerColor());
		entity.setPoiSymbolColor(metadata.getPoiSymbolColor());
		entity.setLoiColor(metadata.getLoiColor());
		if (metadata.getLoiWidth() != null) {
			entity.setLoiWidth(metadata.getLoiWidth().intValue());
		} else {
			entity.setLoiWidth(3);
		}

		entity.setLoiDashArrayString(metadata.getLoiDashArrayString());
		entity.setAoiColor(metadata.getAoiColor());

		entity.setTopicReference(metadata.getTopicReference());

		// persist in db
		georesourcesMetadataRepo.saveAndFlush(entity);
	}

	public List<GeoresourceOverviewType> getAllGeoresourcesMetadata() throws Exception {
		/*
		 * topic is an optional parameter and thus might be null! then get all datasets!
		 */
		logger.info("Retrieving all public georesources metadata from db");

        List<MetadataGeoresourcesEntity> georesourcesMeatadataEntities = georesourcesMetadataRepo.findAll().stream()
                .filter(MetadataGeoresourcesEntity::isPublic)
                .collect(Collectors.toList());

		return generateSwaggerGeoresourcesMetadata(georesourcesMeatadataEntities);
	}

	public List<GeoresourceOverviewType> getAllGeoresourcesMetadata(AuthInfoProvider provider) throws Exception {
		logger.info("Retrieving secured georesources metadata from db");

        List<MetadataGeoresourcesEntity> georesourcesMeatadataEntities = georesourcesMetadataRepo.findAll().stream()
                .filter(g -> provider.checkPermissions(g, PermissionLevelType.VIEWER))
                .collect(Collectors.toList());

        // Iterate over the georesources and add the current user permissions. Iterator is used here in order to safely
        // remove an entity form the collection if no permissions have been found. Actually, this should never happen,
        // however, it is meant as an additional security check.
        Iterator<MetadataGeoresourcesEntity> iter = georesourcesMeatadataEntities.iterator();
        while(iter.hasNext()) {
            MetadataGeoresourcesEntity g = iter.next();
            try {
                g.setUserPermissions(provider.getPermissions(g));
            } catch(NoSuchElementException ex) {
                logger.error("No permissions found for georesource '{}'. Entity will be removed from resulting list.",
                        g.getDatasetId());
                iter.remove();
            }
        }

		return generateSwaggerGeoresourcesMetadata(georesourcesMeatadataEntities);
	}

	private List<GeoresourceOverviewType> generateSwaggerGeoresourcesMetadata(
			List<MetadataGeoresourcesEntity> georesourcesMeatadataEntities) throws Exception {
		List<GeoresourceOverviewType> swaggerGeoresourcesMetadata = GeoresourcesMapper
				.mapToSwaggerGeoresources(georesourcesMeatadataEntities);

		swaggerGeoresourcesMetadata.sort(Comparator.comparing(GeoresourceOverviewType::getDatasetName));

		return swaggerGeoresourcesMetadata;

	}

    public List<PermissionLevelType> getGeoresourcePermissionsByDatasetId(String georesourceId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving georesources permissions for datasetId '{}'", georesourceId);

        MetadataGeoresourcesEntity georesourceMetadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

        if (georesourceMetadataEntity == null || !provider.checkPermissions(georesourceMetadataEntity, PermissionLevelType.VIEWER)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
        }

        List<PermissionLevelType> permissions = provider.getPermissions(georesourceMetadataEntity);
        return permissions;
    }

//	private boolean hasAllowedRole(AuthInfoProvider authInfoProvider,
//			MetadataGeoresourcesEntity georesourceMetadataEntity) {
//		return georesourceMetadataEntity.getRoles() == null || georesourceMetadataEntity.getRoles().isEmpty()
//				|| authInfoProvider.hasRealmAdminRole() || georesourceMetadataEntity.getRoles().stream()
//						.anyMatch(r -> authInfoProvider.hasRealmRole(r.getRoleName()));
//	}

	public String getSingleGeoresourceFeatureRecord(String georesourceId, String featureId, String featureRecordId,
			String simplifyGeometries) throws Exception {
		return getSingleGeoresourceFeatureRecord(georesourceId, featureId, featureRecordId, simplifyGeometries, null);
	}

	public String getSingleGeoresourceFeatureRecords(String georesourceId, String featureId, String simplifyGeometries)
			throws Exception {
		return getSingleGeoresourceFeatureRecords(georesourceId, featureId, simplifyGeometries, null);
	}

	public String getSingleGeoresourceFeatureRecord(String georesourceId, String featureId, String featureRecordId,
			String simplifyGeometries, AuthInfoProvider authInfoProvider) throws Exception {
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			if (authInfoProvider == null) {
                if (metadataEntity == null || !metadataEntity.isPublic()) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            } else {
                if (metadataEntity == null || !authInfoProvider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            }

			String dbTableName = metadataEntity.getDbTableName();

			String geoJson = SpatialFeatureDatabaseHandler.getSingleFeatureRecordByRecordId(dbTableName, featureId,
					featureRecordId, simplifyGeometries);
			return geoJson;

		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public String getSingleGeoresourceFeatureRecords(String georesourceId, String featureId, String simplifyGeometries,
			AuthInfoProvider authInfoProvider) throws Exception {
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			if (authInfoProvider == null) {
                if (metadataEntity == null || !metadataEntity.isPublic()) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            } else {
                if (metadataEntity == null || !authInfoProvider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                    throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", georesourceId));
                }
            }

			String dbTableName = metadataEntity.getDbTableName();

			String geoJson = SpatialFeatureDatabaseHandler.getSingleFeatureRecordsByFeatureId(dbTableName, featureId,
					simplifyGeometries);
			return geoJson;

		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Get request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public boolean deleteSingleGeoresourceFeatureRecordsByFeatureId(String georesourceId, String featureId) throws Exception {
		logger.info("Trying to delete single georesource feature records for dataset with datasetId '{}' and featureId '{}'", georesourceId, featureId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {

			MetadataGeoresourcesEntity georesourceEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			String dbTableName = georesourceEntity.getDbTableName();
			/*
			 * delete features and their properties
			 */
			SpatialFeatureDatabaseHandler.deleteSingleFeatureRecordsForFeatureId(ResourceTypeEnum.GEORESOURCE, dbTableName, featureId);

			georesourceEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

			georesourcesMetadataRepo.saveAndFlush(georesourceEntity);

			// handle OGC web service - null parameter is defaultStyle
			ogcServiceManager.publishDbLayerAsOgcService(dbTableName, georesourceEntity.getDatasetName(), null,
					ResourceTypeEnum.GEORESOURCE);

			/*
			 * set wms and wfs urls within metadata
			 */
			updateMetadataWithOgcServiceUrls(georesourceEntity.getDatasetId(), dbTableName);

			return true;
		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Delete request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public boolean deleteSingleGeoresourceFeatureRecordByRecordId(String georesourceId, String featureId,
			String featureRecordId) throws Exception {
		logger.info("Trying to delete single georesource feature record for dataset with datasetId '{}' and featureId '{}' and recordId '{}'", georesourceId, featureId, featureRecordId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {

			MetadataGeoresourcesEntity georesourceEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);

			String dbTableName = georesourceEntity.getDbTableName();
			/*
			 * delete features and their properties
			 */
			SpatialFeatureDatabaseHandler.deleteSingleFeatureRecordForRecordId(ResourceTypeEnum.GEORESOURCE, dbTableName, featureId, featureRecordId);

			georesourceEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

			georesourcesMetadataRepo.saveAndFlush(georesourceEntity);

			// handle OGC web service - null parameter is defaultStyle
			ogcServiceManager.publishDbLayerAsOgcService(dbTableName, georesourceEntity.getDatasetName(), null,
					ResourceTypeEnum.GEORESOURCE);

			/*
			 * set wms and wfs urls within metadata
			 */
			updateMetadataWithOgcServiceUrls(georesourceEntity.getDatasetId(), dbTableName);

			return true;
		} else {
			logger.error(
					"No georesource dataset with datasetName '{}' was found in database. Delete request has no effect.",
					georesourceId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete georesource features, but no dataset existes with datasetId " + georesourceId);
		}
	}

	public String updateFeatureRecordByRecordId(String georesourceFeatureRecordData, String georesourceId,
			String featureId, String featureRecordId) throws IOException, Exception {
		logger.info("Trying to update georesource single feature record for datasetId '{}' and featureId '{}' and recordId '{}'", georesourceId, featureId, featureRecordId);
		if (georesourcesMetadataRepo.existsByDatasetId(georesourceId)) {
			MetadataGeoresourcesEntity metadataEntity = georesourcesMetadataRepo.findByDatasetId(georesourceId);
			String datasetName = metadataEntity.getDatasetName();
			String dbTableName = metadataEntity.getDbTableName();
			/*
			 * call DB tool to update features
			 */
			SpatialFeatureDatabaseHandler.updateSpatialResourceFeatureRecordByRecordId(georesourceFeatureRecordData, dbTableName, featureId, featureRecordId);

			// set lastUpdate in metadata in case of successful update
			metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

			georesourcesMetadataRepo.saveAndFlush(metadataEntity);

			// handle OGC web service - null parameter is defaultStyle
			ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.GEORESOURCE);

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

}
