package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;


import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsManager;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.OwnerInputType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelInputType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitPUTInputType;
import de.hsbo.kommonitor.datamanagement.msg.MessageResolver;
import jakarta.transaction.Transactional;
import org.geotools.filter.text.cql2.CQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@Repository
@Component
public class SpatialUnitsManager {

    private static Logger logger = LoggerFactory.getLogger(SpatialUnitsManager.class);

    private static final String MSG_SPATIAL_UNIT_EXISTS_ERROR = "spatialunit-exists-error";


    @Autowired
    SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

    @Autowired
    OrganizationalUnitRepository organizationalUnitRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private IndicatorsManager indicatorsManager;

    @Autowired
    OGCWebServiceManager ogcServiceManager;

    @Autowired
    MessageResolver messageResolver;

    public SpatialUnitOverviewType addSpatialUnit(SpatialUnitPOSTInputType featureData) throws Exception {
        String metadataId = null;
        String dbTableName = null;
        MetadataSpatialUnitsEntity metadataEntity = null;
        boolean publishedAsService = false;
        try {
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

            if (spatialUnitsMetadataRepo.existsByDatasetName(datasetName)) {
                MetadataSpatialUnitsEntity existingSpatialUnit = spatialUnitsMetadataRepo.findByDatasetName(datasetName);
                logger.error("The spatialUnit metadataset with datasetName '{}' already exists. Thus aborting add spatial unit request.", datasetName);
                String errMsg = messageResolver.getMessage(MSG_SPATIAL_UNIT_EXISTS_ERROR);
                throw new Exception(String.format(errMsg, datasetName, existingSpatialUnit.getOwner().getMandant().getName()));
            }

            metadataEntity = createMetadata(featureData);
            metadataId = metadataEntity.getDatasetId();

            dbTableName = createFeatureTable(featureData.getGeoJsonString(), featureData.getPeriodOfValidity(), metadataId);

            // handle OGC web service - null parameter is defaultStyle
            publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.SPATIAL_UNIT);

            /*
             * set wms and wfs urls within metadata
             */
            updateMetadataWithOgcServiceUrls(metadataId, dbTableName);

            updateSpatialUnitHierarchy_onAdd(metadataId, featureData);
        } catch (Exception e) {
            /*
             * remove partially created resources and thrwo error
             */
            logger.error("Error while creating spatialUnit. Error message: " + e.getMessage());
            e.printStackTrace();

            logger.info("Deleting partially created resources");

            try {
                logger.info("Delete metadata entry if exists for id '{}'" + metadataId);
                if (metadataId != null) {
                    if (spatialUnitsMetadataRepo.existsByDatasetId(metadataId))
                        spatialUnitsMetadataRepo.deleteByDatasetId(metadataId);
                }

                logger.info("Delete feature table if exists for tableName '{}'" + dbTableName);
                if (dbTableName != null) {
                    SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.SPATIAL_UNIT, dbTableName);
                    ;
                }

                logger.info("Unpublish OGC services if exists");
                if (publishedAsService) {
                    ogcServiceManager.unpublishDbLayer(dbTableName, ResourceTypeEnum.SPATIAL_UNIT);
                }
            } catch (Exception e2) {
                logger.error("Error while deleting partially created georesource. Error message: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            throw e;
        }


        return SpatialUnitsMapper.mapToSwaggerSpatialUnit(spatialUnitsMetadataRepo.findByDatasetId(metadataId));
    }

    private void updateSpatialUnitHierarchy_onAdd(String metadataId, SpatialUnitPOSTInputType featureData) {
        /*
         * automatically update metadata entries with respect to hierarchy
         *
         *
         */

        List<MetadataSpatialUnitsEntity> matchingEntriesForNextLowerHierarchy = spatialUnitsMetadataRepo.findByNextLowerHierarchyLevel(featureData.getNextLowerHierarchyLevel());

        for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : matchingEntriesForNextLowerHierarchy) {
            if (!metadataSpatialUnitsEntity.getDatasetId().equalsIgnoreCase(metadataId)) {
                metadataSpatialUnitsEntity.setNextLowerHierarchyLevel(featureData.getSpatialUnitLevel());
            }
        }

        List<MetadataSpatialUnitsEntity> matchingEntriesForNextUpperHierarchy = spatialUnitsMetadataRepo.findByNextUpperHierarchyLevel(featureData.getNextUpperHierarchyLevel());

        for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : matchingEntriesForNextUpperHierarchy) {
            if (!metadataSpatialUnitsEntity.getDatasetId().equalsIgnoreCase(metadataId)) {
                metadataSpatialUnitsEntity.setNextUpperHierarchyLevel(featureData.getSpatialUnitLevel());
            }
        }

    }

    private void updateSpatialUnitHierarchy_onUpdate(String metadataId, String oldName, String newName) {

        /*
         * automatically update metadata entries with respect to hierarchy
         *
         *
         */

        List<MetadataSpatialUnitsEntity> matchingEntriesForNextLowerHierarchy = spatialUnitsMetadataRepo.findByNextLowerHierarchyLevel(oldName);

        for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : matchingEntriesForNextLowerHierarchy) {
            if (!metadataSpatialUnitsEntity.getDatasetId().equalsIgnoreCase(metadataId)) {
                metadataSpatialUnitsEntity.setNextLowerHierarchyLevel(newName);
            }
        }

        List<MetadataSpatialUnitsEntity> matchingEntriesForNextUpperHierarchy = spatialUnitsMetadataRepo.findByNextUpperHierarchyLevel(oldName);

        for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : matchingEntriesForNextUpperHierarchy) {
            if (!metadataSpatialUnitsEntity.getDatasetId().equalsIgnoreCase(metadataId)) {
                metadataSpatialUnitsEntity.setNextUpperHierarchyLevel(newName);
            }
        }

    }

    private void updateSpatialUnitHierarchy_onDelete(String metadataId) {
        /*
         * automatically update metadata entries with respect to hierarchy
         *
         *
         */

        MetadataSpatialUnitsEntity deleteEntry = spatialUnitsMetadataRepo.findByDatasetId(metadataId);

        List<MetadataSpatialUnitsEntity> matchingEntriesForNextLowerHierarchy = spatialUnitsMetadataRepo.findByNextLowerHierarchyLevel(deleteEntry.getDatasetName());

        for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : matchingEntriesForNextLowerHierarchy) {
            if (!metadataSpatialUnitsEntity.getDatasetId().equalsIgnoreCase(metadataId)) {
                metadataSpatialUnitsEntity.setNextLowerHierarchyLevel(deleteEntry.getNextLowerHierarchyLevel());
            }
        }

        List<MetadataSpatialUnitsEntity> matchingEntriesForNextUpperHierarchy = spatialUnitsMetadataRepo.findByNextUpperHierarchyLevel(deleteEntry.getDatasetName());

        for (MetadataSpatialUnitsEntity metadataSpatialUnitsEntity : matchingEntriesForNextUpperHierarchy) {
            if (!metadataSpatialUnitsEntity.getDatasetId().equalsIgnoreCase(metadataId)) {
                metadataSpatialUnitsEntity.setNextUpperHierarchyLevel(deleteEntry.getNextUpperHierarchyLevel());
            }
        }
    }

    private void updateMetadataWithOgcServiceUrls(String metadataId, String dbTableName) {
        MetadataSpatialUnitsEntity metadata = spatialUnitsMetadataRepo.findByDatasetId(metadataId);

        metadata.setWmsUrl(ogcServiceManager.getWmsUrl(dbTableName));
        metadata.setWfsUrl(ogcServiceManager.getWfsUrl(dbTableName));

        spatialUnitsMetadataRepo.saveAndFlush(metadata);
    }

    private String createFeatureTable(String geoJsonString, PeriodOfValidityType periodOfValidityType, String metadataId) throws CQLException, IOException {
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

        String dbTableName = SpatialFeatureDatabaseHandler.writeGeoJSONFeaturesToDatabase(ResourceTypeEnum.SPATIAL_UNIT, geoJsonString, periodOfValidityType, metadataId);

        logger.info("Completed creation of spatialUnit feature table corresponding to datasetId {}. Table name is {}.", metadataId, dbTableName);

        updateMetadataWithAssociatedFeatureTable(metadataId, dbTableName);

        return dbTableName;
    }

    private void updateMetadataWithAssociatedFeatureTable(String metadataId, String dbTableName) {
        logger.info("Updating spatial unit metadataset with datasetId {}. set dbTableName of associated feature table with name {}.", metadataId, dbTableName);

        MetadataSpatialUnitsEntity metadataset = spatialUnitsMetadataRepo.findByDatasetId(metadataId);

        metadataset.setDbTableName(dbTableName);

        spatialUnitsMetadataRepo.saveAndFlush(metadataset);

        logger.info("Updating successful");
    }

    private MetadataSpatialUnitsEntity createMetadata(SpatialUnitPOSTInputType featureData) throws ResourceNotFoundException {
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
        entity.setDataBasis(genericMetadata.getDatabasis());
        entity.setNote(genericMetadata.getNote());
        entity.setLiterature(genericMetadata.getLiterature());
        entity.setJsonSchema(featureData.getJsonSchema());

        java.util.Date lastUpdate = DateTimeUtil.fromLocalDate(genericMetadata.getLastUpdate());
        if (lastUpdate == null)
            lastUpdate = java.util.Calendar.getInstance().getTime();
        entity.setLastUpdate(lastUpdate);
        entity.setNextLowerHierarchyLevel(featureData.getNextLowerHierarchyLevel());
        entity.setNextUpperHierarchyLevel(featureData.getNextUpperHierarchyLevel());
        entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
        entity.setUpdateIntervall(genericMetadata.getUpdateInterval());

        entity.setOutlineLayer(featureData.getIsOutlineLayer());
        entity.setOutlineColor(featureData.getOutlineColor());

        BigDecimal outlineWidth = featureData.getOutlineWidth();
        if (outlineWidth != null) {
            entity.setOutlineWidth(outlineWidth.intValue());
        }

        entity.setOutlineDashArrayString(featureData.getOutlineDashArrayString());

        /*
         * the remaining properties cannot be set initially!
         */
        entity.setDbTableName(null);
        entity.setWfsUrl(null);
        entity.setWmsUrl(null);

        //
        entity.setPermissions(getPermissionEntities(featureData.getPermissions()));
        entity.setOwner(getOrganizationalUnitEntity(featureData.getOwnerId()));
        entity.setPublic(featureData.getIsPublic());

        // persist in db
        spatialUnitsMetadataRepo.saveAndFlush(entity);

        logger.info("Completed to add spatialUnit metadata entry for spatialUnit dataset with id {}.", entity.getDatasetId());

        return entity;
    }

    private Collection<PermissionEntity> getPermissionEntities(List<String> ids) throws ResourceNotFoundException {
        Collection<PermissionEntity> allowedRoles = new ArrayList<>();
        for (String id : ids) {
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

    public boolean deleteAllSpatialUnitFeaturesByDatasetById(String spatialUnitId) throws Exception {
        logger.info("Trying to delete all spatialUnit features from dataset with datasetId '{}'", spatialUnitId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            String datasetName = metadataEntity.getDatasetName();
            String dbTableName = metadataEntity.getDbTableName();
            /*
             * call DB tool to delete features
             */
            SpatialFeatureDatabaseHandler.deleteAllFeaturesFromFeatureTable(ResourceTypeEnum.SPATIAL_UNIT, dbTableName);

            // set lastUpdate in metadata in case of successful update
            metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);

            // handle OGC web service - null parameter is defaultStyle
            ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.SPATIAL_UNIT);

            // as a new spatial unit feature table was generated, we must regenerate all views for all indicators that have 
            // the modified spatial unit
            indicatorsManager.recreateAllViewsForSpatialUnitById(spatialUnitId);

            /*
             * set wms and wfs urls within metadata
             */
            updateMetadataWithOgcServiceUrls(metadataEntity.getDatasetId(), dbTableName);

            return true;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Delete request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete all spatialUnit features for dataset, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public boolean deleteSpatialUnitDatasetById(String spatialUnitId) throws Exception {
        logger.info("Trying to delete spatialUnit dataset with datasetId '{}'", spatialUnitId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            boolean success = true;
            MetadataSpatialUnitsEntity spatialUnitEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            String dbTableName = spatialUnitEntity.getDbTableName();

            try {
                /*
                 * delete all associated indicator layers
                 */
                deleteAssociatedIndicatorLayers(spatialUnitId);
            } catch (Exception e) {
                logger.error("Error while deleting associated indicator layer entries for spatial unit with id {}",
                        spatialUnitId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            // delete any linked roles first
            try {
                spatialUnitEntity = removeAnyLinkedRoles(spatialUnitEntity);
            } catch (Exception e) {
                logger.error("Error while deleting roles for spatial unit with id {}", spatialUnitEntity);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
                /*
                 * delete featureTable
                 */
                SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.SPATIAL_UNIT, dbTableName);
            } catch (Exception e) {
                logger.error("Error while deleting feature table for spatial unit with id {}", spatialUnitId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {

                // update spatial unit hierarchy and make it consistent again
                updateSpatialUnitHierarchy_onDelete(spatialUnitId);
            } catch (Exception e) {
                logger.error("Error while updating spatial unit hierarchy due to deletion of spatial unit with id {}", spatialUnitId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
                /*
                 * delete metadata entry
                 */
                spatialUnitsMetadataRepo.deleteByDatasetId(spatialUnitId);
            } catch (Exception e) {
                logger.error("Error while deleting metadata entry for spatial unit with id {}", spatialUnitId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
                success = false;
            }

            try {
                // handle OGC web service
                ogcServiceManager.unpublishDbLayer(dbTableName, ResourceTypeEnum.SPATIAL_UNIT);
            } catch (Exception e) {
                logger.error("Error while unpublishing OGC service layer for spatial unit with id {}", spatialUnitId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            return success;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Delete request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), "Tried to delete spatialUnit dataset, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    private MetadataSpatialUnitsEntity removeAnyLinkedRoles(MetadataSpatialUnitsEntity spatialUnitEntity) {
        spatialUnitEntity.setPermissions(new ArrayList<>());

        spatialUnitsMetadataRepo.saveAndFlush(spatialUnitEntity);

        spatialUnitEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitEntity.getDatasetId());

        return spatialUnitEntity;
    }

    private boolean deleteAssociatedIndicatorLayers(String spatialUnitId) throws Exception {
        logger.info("Must delete and unpublish all indicator data tables and views that are associated to submitted spatial unit with id '{}'", spatialUnitId);
        /*
         * identify all indicator layers for the spatialUnitId and delete/unpublish them
         */

        return indicatorsManager.deleteIndicatorLayersForSpatialUnitId(spatialUnitId);

    }

    public boolean deleteSpatialUnitDatasetByIdAndDate(String spatialUnitId, BigDecimal year, BigDecimal month, BigDecimal day) throws ResourceNotFoundException, IOException {
        // TODO fill method
        return false;
    }

    public String updateFeatures(SpatialUnitPUTInputType featureData, String spatialUnitId) throws Exception {
        logger.info("Trying to update spatialUnit features for datasetId '{}'", spatialUnitId);
        logger.info("isPartialUpdate is set to " + featureData.getIsPartialUpdate());
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            String datasetName = metadataEntity.getDatasetName();
            String dbTableName = metadataEntity.getDbTableName();
            /*
             * call DB tool to update features
             */
            SpatialFeatureDatabaseHandler.updateSpatialUnitFeatures(featureData, dbTableName);

            // set lastUpdate in metadata in case of successful update
            metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);

            // handle OGC web service - null parameter is defaultStyle
            ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.SPATIAL_UNIT);


            indicatorsManager.recreateAllViewsForSpatialUnitById(spatialUnitId);

            /*
             * set wms and wfs urls within metadata
             */
            updateMetadataWithOgcServiceUrls(metadataEntity.getDatasetId(), dbTableName);

            return spatialUnitId;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Update request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update spatialUnit features, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public String updateMetadata(SpatialUnitPATCHInputType metadata, String spatialUnitId) throws Exception {
        logger.info("Trying to update spatialUnit metadata for datasetId '{}'", spatialUnitId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);

            /*
             * call DB tool to update features
             */
            updateMetadata(metadata, metadataEntity);

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);
            return spatialUnitId;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Update request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update spatialUnit metadata, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public String updatePermissionLevels(PermissionLevelInputType permissionLevels, String spatialUnitId) throws Exception {
        logger.info("Trying to update spatialUnit permissions for datasetId '{}'", spatialUnitId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);

            metadataEntity.setPermissions(getPermissionEntities(permissionLevels.getPermissions()));
            metadataEntity.setPublic(permissionLevels.getIsPublic());

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);
            return spatialUnitId;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Update request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update spatialUnit metadata, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public String updateOwnership(OwnerInputType owner, String spatialUnitId) throws Exception {
        logger.info("Trying to update spatialUnit ownership for datasetId '{}'", spatialUnitId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            metadataEntity.setOwner(getOrganizationalUnitEntity(owner.getOwnerId()));

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);
            return spatialUnitId;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Update request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update spatialUnit metadata, but no dataset existes with datasetId " + spatialUnitId);
        }
    }


    private void updateMetadata(SpatialUnitPATCHInputType metadata, MetadataSpatialUnitsEntity entity) throws Exception {

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
        entity.setNextLowerHierarchyLevel(metadata.getNextLowerHierarchyLevel());
        entity.setNextUpperHierarchyLevel(metadata.getNextUpperHierarchyLevel());
        entity.setSridEpsg(genericMetadata.getSridEPSG().intValue());
        entity.setUpdateIntervall(genericMetadata.getUpdateInterval());

        entity.setOutlineLayer(metadata.getIsOutlineLayer());
        entity.setOutlineColor(metadata.getOutlineColor());

        BigDecimal outlineWidth = metadata.getOutlineWidth();
        if (outlineWidth != null) {
            entity.setOutlineWidth(outlineWidth.intValue());
        }

        entity.setOutlineDashArrayString(metadata.getOutlineDashArrayString());

        /*
         * UPDATE DATASETNAME and adjust hierarchy order if needed
         * also adjust spatialUnitName in all affected indicatorSpatialUnitJoinEntities
         */
        String oldName = entity.getDatasetName();
        String newName = metadata.getDatasetName();
        if (!newName.equals(oldName) && newName != null && newName != "") {
            // update datasetName
            entity.setDatasetName(newName);
            updateSpatialUnitHierarchy_onUpdate(entity.getDatasetId(), oldName, newName);
            indicatorsManager.updateJoinedSpatialUnitName(entity.getDatasetId(), newName);
        }

        /*
         * dbTable name and OGC service urls may not be set here!
         * they are set automatically by other components
         */
    }

    public List<SpatialUnitOverviewType> getAllSpatialUnitsMetadata() throws Exception {
        return getAllSpatialUnitsMetadata(null);
    }

    public List<SpatialUnitOverviewType> getAllSpatialUnitsMetadata(AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving all spatialUnits metadata from db");

        List<MetadataSpatialUnitsEntity> spatialUnitMeatadataEntities;

        if (provider == null) {
            spatialUnitMeatadataEntities = spatialUnitsMetadataRepo.findAll().stream()
                    .filter(MetadataSpatialUnitsEntity::isPublic)
                    .collect(Collectors.toList());
        } else {
            spatialUnitMeatadataEntities = spatialUnitsMetadataRepo.findAll().stream()
                    .filter(s -> provider.checkPermissions(s, PermissionLevelType.VIEWER))
                    .collect(Collectors.toList());

            // Iterate over the spatial units and add the current user permissions. Iterator is used here in order to
            // safely remove an entity form the collection if no permissions have been found. Actually, this should
            // never happen, however, it is meant as an additional security check.
            Iterator<MetadataSpatialUnitsEntity> iter = spatialUnitMeatadataEntities.iterator();
            while (iter.hasNext()) {
                MetadataSpatialUnitsEntity s = iter.next();
                try {
                    s.setUserPermissions(provider.getPermissions(s));
                } catch (NoSuchElementException ex) {
                    logger.error("No permissions found for spatial unit '{}'. Entity will be removed" +
                            " from resulting list.", s.getDatasetId());
                    iter.remove();
                }
            }

        }

        logger.info("Retrieved a total number of {} entries for spatialUnits metadata from db. Convert them to JSON Output structure and return.", spatialUnitMeatadataEntities.size());
        List<SpatialUnitOverviewType> swaggerSpatialUnitsMetadata = SpatialUnitsMapper.mapToSwaggerSpatialUnits(spatialUnitMeatadataEntities);

        swaggerSpatialUnitsMetadata = sortSpatialUnitsHierarchically(swaggerSpatialUnitsMetadata);

        return swaggerSpatialUnitsMetadata;
    }

    public static List<SpatialUnitOverviewType> sortSpatialUnitsHierarchically(
            List<SpatialUnitOverviewType> swaggerSpatialUnitsMetadata) {

        List<SpatialUnitOverviewType> backupCopy = new ArrayList<SpatialUnitOverviewType>(swaggerSpatialUnitsMetadata.size());
        backupCopy.addAll(swaggerSpatialUnitsMetadata);


        try {
            List<SpatialUnitOverviewType> newOrder = new ArrayList<SpatialUnitOverviewType>();
            for (SpatialUnitOverviewType spatialUnitOverviewType : swaggerSpatialUnitsMetadata) {
                if (spatialUnitOverviewType.getNextUpperHierarchyLevel() == null) {
                    newOrder.add(spatialUnitOverviewType);
                    swaggerSpatialUnitsMetadata.remove(spatialUnitOverviewType);
                    break;
                }
            }

            int loopFinisher = 100;
            int counter = 0;

            while (swaggerSpatialUnitsMetadata.size() > 0) {
                /*
                 * find next lower hierarchyElement
                 */
                SpatialUnitOverviewType lastIndexElement = newOrder.get(newOrder.size() - 1);
                for (SpatialUnitOverviewType spatialUnitOverviewType : swaggerSpatialUnitsMetadata) {
                    if (lastIndexElement.getNextLowerHierarchyLevel() == null) {
                        newOrder.add(spatialUnitOverviewType);
                        swaggerSpatialUnitsMetadata.remove(spatialUnitOverviewType);
                        break;
                    }
                    // compare nextLowerHierarchyLevel of lastIndexElement to spatialUnitName of current element
                    else if (lastIndexElement.getNextLowerHierarchyLevel().equalsIgnoreCase(spatialUnitOverviewType.getSpatialUnitLevel())) {
                        newOrder.add(spatialUnitOverviewType);
                        swaggerSpatialUnitsMetadata.remove(spatialUnitOverviewType);
                        break;
                    } else if (counter >= loopFinisher) {
                        newOrder.addAll(swaggerSpatialUnitsMetadata);
                        swaggerSpatialUnitsMetadata.removeAll(swaggerSpatialUnitsMetadata);
                        break;
                    }
                }

                counter++;
            }

            return newOrder;
        } catch (Exception e) {
            // log error and return unsorted list
            logger.error(e.getMessage());
            e.printStackTrace();
            return backupCopy;
        }

    }

    public SpatialUnitOverviewType getSpatialUnitByDatasetId(String spatialUnitId) throws Exception {
        return getSpatialUnitByDatasetId(spatialUnitId, null);
    }

    public SpatialUnitOverviewType getSpatialUnitByDatasetId(String spatialUnitId, AuthInfoProvider provider)
            throws Exception {
        logger.info("Retrieving spatialUnit metadata for datasetId '{}'", spatialUnitId);

        MetadataSpatialUnitsEntity spatialUnitMetadataEntity = fetchEntity(provider, spatialUnitId);

        SpatialUnitOverviewType swaggerSpatialUnitMetadata = SpatialUnitsMapper.mapToSwaggerSpatialUnit(spatialUnitMetadataEntity);

        return swaggerSpatialUnitMetadata;
    }

    public String getJsonSchemaForDatasetId(String spatialUnitId) throws ResourceNotFoundException {
        return getJsonSchemaForDatasetId(spatialUnitId, null);
    }

    public String getJsonSchemaForDatasetId(String spatialUnitId, AuthInfoProvider provider) throws ResourceNotFoundException {
        logger.info("Retrieving spatialUnit jsonSchema for datasetId '{}'", spatialUnitId);

        MetadataSpatialUnitsEntity spatialUnitMetadataEntity = fetchEntity(provider, spatialUnitId);

        return spatialUnitMetadataEntity.getJsonSchema();
    }

    public String getAllSpatialUnitFeatures(String spatialUnitId, String simplifyGeometries) throws Exception {
        return getAllSpatialUnitFeatures(spatialUnitId, simplifyGeometries, null);
    }

    public String getAllSpatialUnitFeatures(String spatialUnitId, String simplifyGeometries, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving all spatialUnit Features from Dataset '{}'", spatialUnitId);

        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = fetchEntity(provider, spatialUnitId);

            String dbTableName = metadataEntity.getDbTableName();

            String geoJson = SpatialFeatureDatabaseHandler.getAllFeatures(dbTableName, simplifyGeometries);
            return geoJson;

        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Get request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get spatialUnit features, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public String getValidSpatialUnitFeatures(String spatialUnitId, BigDecimal year, BigDecimal month,
                                              BigDecimal day, String simplifyGeometries) throws Exception {
        return getValidSpatialUnitFeatures(spatialUnitId, year, month, day, simplifyGeometries, null);
    }


    public String getValidSpatialUnitFeatures(String spatialUnitId, BigDecimal year, BigDecimal month,
                                              BigDecimal day, String simplifyGeometries, AuthInfoProvider provider) throws Exception {
        Calendar calender = Calendar.getInstance();
        calender.set(year.intValue(), month.intValueExact() - 1, day.intValue());
        java.util.Date date = calender.getTime();
        logger.info("Retrieving valid spatialUnit Features from Dataset '{}' for date '{}'", spatialUnitId, date);

        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = fetchEntity(provider, spatialUnitId);

            String dbTableName = metadataEntity.getDbTableName();

            String geoJson = SpatialFeatureDatabaseHandler.getValidFeatures(date, dbTableName, simplifyGeometries);
            return geoJson;

        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Get request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get spatialUnit features, but no dataset existes with datasetId " + spatialUnitId);
        }

    }

    private MetadataSpatialUnitsEntity fetchEntity(AuthInfoProvider provider, String spatialUnitId) throws ResourceNotFoundException {
        MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
        if (provider == null) {
            if (metadataEntity == null || !metadataEntity.isPublic()) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' " +
                        "was not found.", spatialUnitId));
            }
        } else {
            if (metadataEntity == null || !provider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' " +
                        "was not found.", spatialUnitId));
            }
            try {
                metadataEntity.setUserPermissions(provider.getPermissions(metadataEntity));
            } catch (NoSuchElementException ex) {
                logger.error("No permissions found for spatial unit '{}'", metadataEntity.getDatasetId());
            }

        }
        return metadataEntity;
    }

    public List<PermissionLevelType> getSpatialUnitsPermissionsByDatasetId(String spatialUnitId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving spatial unit permissions for datasetId '{}'", spatialUnitId);

        MetadataSpatialUnitsEntity spatialUnitsMetadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);

        if (spatialUnitsMetadataEntity == null || !provider.checkPermissions(spatialUnitsMetadataEntity, PermissionLevelType.VIEWER)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", spatialUnitId));
        }

        List<PermissionLevelType> permissions = provider.getPermissions(spatialUnitsMetadataEntity);
        return permissions;
    }

    public String getSingleSpatialUnitFeatureRecords(String spatialUnitId, String featureId,
                                                     String simplifyGeometries) throws Exception {
        return getSingleSpatialUnitFeatureRecords(spatialUnitId, featureId, simplifyGeometries, null);
    }

    public String getSingleSpatialUnitFeatureRecords(String spatialUnitId, String featureId, String simplifyGeometries,
                                                     AuthInfoProvider authInfoProvider) throws Exception {
        logger.info("Retrieving single spatialUnit Feature records for Dataset '{}' and featureId '{}'", spatialUnitId, featureId);

        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = fetchEntity(authInfoProvider, spatialUnitId);

            String dbTableName = metadataEntity.getDbTableName();

            String geoJson = SpatialFeatureDatabaseHandler.getSingleFeatureRecordsByFeatureId(dbTableName, featureId,
                    simplifyGeometries);
            return geoJson;

        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Get request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get spatialUnit features, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public String getSingleSpatialUnitFeatureRecord(String spatialUnitId, String featureId, String featureRecordId,
                                                    String simplifyGeometries) throws Exception {
        return getSingleSpatialUnitFeatureRecord(spatialUnitId, featureId, featureRecordId, simplifyGeometries, null);
    }

    public String getSingleSpatialUnitFeatureRecord(String spatialUnitId, String featureId, String featureRecordId,
                                                    String simplifyGeometries, AuthInfoProvider authInfoProvider) throws Exception {
        logger.info(
                "Retrieving single spatialUnit Feature record for Dataset '{}' and featureId '{}' and recordId '{}'",
                spatialUnitId, featureId, featureRecordId);

        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = fetchEntity(authInfoProvider, spatialUnitId);

            String dbTableName = metadataEntity.getDbTableName();

            String geoJson = SpatialFeatureDatabaseHandler.getSingleFeatureRecordByRecordId(dbTableName, featureId,
                    featureRecordId, simplifyGeometries);
            return geoJson;

        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Get request has no effect.",
                    spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to get spatialUnit features, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public boolean deleteSingleSpatialUnitFeatureRecordsByFeatureId(String spatialUnitId, String featureId) throws Exception {
        logger.info("Trying to delete single spatialUnit feature database records for dataset with datasetId '{}' and featureId '{}'", spatialUnitId, featureId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            String datasetName = metadataEntity.getDatasetName();
            String dbTableName = metadataEntity.getDbTableName();
            /*
             * call DB tool to delete features
             */
            SpatialFeatureDatabaseHandler.deleteSingleFeatureRecordsForFeatureId(ResourceTypeEnum.SPATIAL_UNIT, dbTableName, featureId);

            // set lastUpdate in metadata in case of successful update
            metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);

            // handle OGC web service - null parameter is defaultStyle
            ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.SPATIAL_UNIT);

            // as a new spatial unit feature table was generated, we must regenerate all views for all indicators that have 
            // the modified spatial unit
            indicatorsManager.recreateAllViewsForSpatialUnitById(spatialUnitId);

            /*
             * set wms and wfs urls within metadata
             */
            updateMetadataWithOgcServiceUrls(metadataEntity.getDatasetId(), dbTableName);

            return true;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Delete request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete all spatialUnit features for dataset, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public boolean deleteSingleSpatialUnitFeatureRecordByRecordId(String spatialUnitId, String featureId,
                                                                  String featureRecordId) throws Exception {
        logger.info("Trying to delete single spatialUnit feature database record for dataset with datasetId '{}' and featureId '{}' and recordId '{}'", spatialUnitId, featureId, featureRecordId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            String datasetName = metadataEntity.getDatasetName();
            String dbTableName = metadataEntity.getDbTableName();
            /*
             * call DB tool to delete features
             */
            SpatialFeatureDatabaseHandler.deleteSingleFeatureRecordForRecordId(ResourceTypeEnum.SPATIAL_UNIT, dbTableName, featureId, featureRecordId);;

            // set lastUpdate in metadata in case of successful update
            metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);

            // handle OGC web service - null parameter is defaultStyle
            ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.SPATIAL_UNIT);

            // as a new spatial unit feature table was generated, we must regenerate all views for all indicators that have 
            // the modified spatial unit
            indicatorsManager.recreateAllViewsForSpatialUnitById(spatialUnitId);

            /*
             * set wms and wfs urls within metadata
             */
            updateMetadataWithOgcServiceUrls(metadataEntity.getDatasetId(), dbTableName);

            return true;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Delete request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete all spatialUnit features for dataset, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

    public String updateFeatureRecordByRecordId(String spatialUnitFeatureRecordData, String spatialUnitId,
                                                String featureId, String featureRecordId) throws IOException, Exception {
        logger.info("Trying to update single spatialUnit feature database record for dataset with datasetId '{}' and featureId '{}' and recordId '{}'", spatialUnitId, featureId, featureRecordId);
        if (spatialUnitsMetadataRepo.existsByDatasetId(spatialUnitId)) {
            MetadataSpatialUnitsEntity metadataEntity = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
            String datasetName = metadataEntity.getDatasetName();
            String dbTableName = metadataEntity.getDbTableName();
            /*
             * call DB tool to delete features
             */
            SpatialFeatureDatabaseHandler.updateSpatialResourceFeatureRecordByRecordId(spatialUnitFeatureRecordData, dbTableName, featureId, featureRecordId);

            // set lastUpdate in metadata in case of successful update
            metadataEntity.setLastUpdate(java.util.Calendar.getInstance().getTime());

            spatialUnitsMetadataRepo.saveAndFlush(metadataEntity);

            // handle OGC web service - null parameter is defaultStyle
            ogcServiceManager.publishDbLayerAsOgcService(dbTableName, datasetName, null, ResourceTypeEnum.SPATIAL_UNIT);

            // as a new spatial unit feature table was generated, we must regenerate all views for all indicators that have 
            // the modified spatial unit
            indicatorsManager.recreateAllViewsForSpatialUnitById(spatialUnitId);

            /*
             * set wms and wfs urls within metadata
             */
            updateMetadataWithOgcServiceUrls(metadataEntity.getDatasetId(), dbTableName);

            return spatialUnitId;
        } else {
            logger.error("No spatialUnit dataset with datasetId '{}' was found in database. Delete request has no effect.", spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete all spatialUnit features for dataset, but no dataset existes with datasetId " + spatialUnitId);
        }
    }

}
