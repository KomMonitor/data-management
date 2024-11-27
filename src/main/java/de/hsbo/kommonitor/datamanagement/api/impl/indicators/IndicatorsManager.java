package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.RegionalReferenceValueEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.scripts.ScriptManager;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.*;
import de.hsbo.kommonitor.datamanagement.msg.MessageResolver;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.geotools.data.DataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Repository
@Component
public class IndicatorsManager {

    private static Logger logger = LoggerFactory.getLogger(IndicatorsManager.class);

    private static final String MSG_INDICATOR_EXISTS_ERROR = "indicator-exists-error";

    @Autowired
    private IndicatorsMetadataRepository indicatorsMetadataRepo;

    @Autowired
    private OrganizationalUnitRepository organizationalUnitRepository;

    @Autowired
    private IndicatorSpatialUnitsRepository indicatorsSpatialUnitsRepo;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    OGCWebServiceManager ogcServiceManager;

    @Autowired
    ScriptManager scriptManager;

    @Autowired
    SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

    @Autowired
    IndicatorsMapper indicatorsMapper;

    @Autowired
    MessageResolver messageResolver;

    @Autowired
    private LastModificationManager lastModManager;
    
    public String updateMetadata(IndicatorMetadataPATCHInputType metadata, String indicatorId) throws Exception {
        logger.info("Trying to update indicator metadata for datasetId '{}'", indicatorId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

            String indicatorName = metadata.getDatasetName();
            String characteristicValue = metadata.getCharacteristicValue();
            IndicatorTypeEnum indicatorType = metadata.getIndicatorType();
            CreationTypeEnum creationType = metadata.getCreationType();

            logger.info("Trying to update indicator using following parameters: name '{}', characteristicValue '{}', indicatorType '{}', creationType '{}'", indicatorName, characteristicValue, indicatorType, creationType.toString());

            /*
             * check if there are changes to key-properties
             *
             * if there are changes then we must check, if the combination of three key properties already exists!
             */
            if (keyPropertiesHaveChanged(metadataEntity, indicatorName, characteristicValue, indicatorType)) {
                if (indicatorsMetadataRepo.existsByDatasetNameAndCharacteristicValueAndIndicatorType(indicatorName, characteristicValue, indicatorType)) {
                    MetadataIndicatorsEntity existingIndicator = indicatorsMetadataRepo.findByDatasetName(indicatorName);
                    logger.error(
                            "The indicator metadataset with datasetName '{}', characteristicValue '{}' and indicatorType '{}' already exists. Thus aborting update indicator request.",
                            indicatorName, characteristicValue, indicatorType);
                    String errMsg = messageResolver.getMessage(MSG_INDICATOR_EXISTS_ERROR);
                    throw new Exception(String.format(errMsg, indicatorName, existingIndicator.getOwner().getMandant().getName()));
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

                try {
                    String datasetTitle = createTitleForWebService(indicatorSpatialUnitJoinEntity.getSpatialUnitName(),
                            indicatorSpatialUnitJoinEntity.getIndicatorName());

                    String styleName;
                    if (metadata.getDefaultClassificationMapping() != null
                            && metadata.getDefaultClassificationMapping().getItems() != null
                            && metadata.getDefaultClassificationMapping().getItems().size() > 0) {
                        styleName = publishDefaultStyleForWebServices(metadata.getDefaultClassificationMapping(),
                                datasetTitle, indicatorSpatialUnitJoinEntity.getIndicatorViewTableName());
                    } else {
                        DefaultClassificationMappingType defaultClassificationMapping = indicatorsMapper
                                .extractDefaultClassificationMappingFromMetadata(metadataEntity);
                        styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle,
                                indicatorSpatialUnitJoinEntity.getIndicatorViewTableName());
                    }

                    ogcServiceManager.publishDbLayerAsOgcService(
                            indicatorSpatialUnitJoinEntity.getIndicatorViewTableName(), datasetTitle, styleName,
                            ResourceTypeEnum.INDICATOR);

                    List<String> allowedRoles = indicatorSpatialUnitJoinEntity.getPermissions().stream()
                            .map(r -> r.getPermissionId()).collect(Collectors.toList());

                    persistNamesOfIndicatorTablesAndServicesInJoinTable(indicatorId,
                            indicatorSpatialUnitJoinEntity.getIndicatorName(),
                            indicatorSpatialUnitJoinEntity.getSpatialUnitName(),
                            indicatorSpatialUnitJoinEntity.getIndicatorViewTableName(),
                            styleName,
                            allowedRoles,
                            indicatorSpatialUnitJoinEntity.getOwner().getOrganizationalUnitId(),
                            indicatorSpatialUnitJoinEntity.isPublic());
                } catch (Exception e) {
                    logger.error("An error ocurred while trying to publish data layer for indicator with id {} and spatial unit with id {}.", indicatorId, indicatorSpatialUnitJoinEntity.getSpatialUnitId());
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

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

    public String updateIndicatorPermissions(PermissionLevelInputType indicatorData, String indicatorId, String spatialUnitId) throws Exception {
        logger.info("Trying to update indicator roles for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
            IndicatorSpatialUnitJoinEntity indicatorEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);

            if (keyPropertiesHaveChanged(indicatorEntity, indicatorData)) {
                indicatorEntity.setPermissions(retrievePermissions(indicatorData.getPermissions()));
                indicatorEntity.setPublic(indicatorData.getIsPublic());

                indicatorsSpatialUnitsRepo.saveAndFlush(indicatorEntity);
                logger.info(
                        "Succesfully updated the roles for indicator dataset with indicatorId '{}' and spatialUnitId '{}'.",
                        indicatorId, spatialUnitId);
                return indicatorEntity.getEntryId();
            } else {
                logger.info(
                        "The roles for indicator dataset with indicatorId '{}' and spatialUnitId '{}' have not changed. Update has no effect.",
                        indicatorId, spatialUnitId);
                return "";
            }


        } else {
            logger.error(
                    "No indicator dataset with indicatorId '{}' and spatialUnitId '{}' was found in database. Update request has no effect.",
                    indicatorId, spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update indicator metadata, but no dataset existes with datasetId " + indicatorId);
        }
    }

    public String updateIndicatorPermissions(PermissionLevelInputType indicatorData, String indicatorId) throws Exception {
        logger.info("Trying to update indicator roles for indicatorId '{}'", indicatorId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            var indicatorEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

            if (keyPropertiesHaveChanged(indicatorEntity, indicatorData)) {
                indicatorEntity.setPermissions(retrievePermissions(indicatorData.getPermissions()));
                indicatorEntity.setPublic(indicatorData.getIsPublic());
                indicatorsMetadataRepo.saveAndFlush(indicatorEntity);
                logger.info(
                        "Successfully updated the roles for indicator dataset with indicatorId '{}'.",
                        indicatorId);
                return indicatorEntity.getDatasetId();
            } else {
                logger.info(
                        "The roles for indicator dataset with indicatorId '{}' have not changed. Update has no effect.",
                        indicatorId);
                return "";
            }


        } else {
            logger.error(
                    "No indicator dataset with indicatorId '{}' was found in database. Update request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update indicator permissions, but no dataset exists with datasetId " + indicatorId);
        }
    }

    public String updateOwnership(OwnerInputType owner, String indicatorId, String spatialUnitId) throws Exception {
        logger.info("Trying to update indicator ownership for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
            IndicatorSpatialUnitJoinEntity indicatorEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
            indicatorEntity.setOwner(getOrganizationalUnitEntity(owner.getOwnerId()));

            indicatorsSpatialUnitsRepo.saveAndFlush(indicatorEntity);
            logger.info("Succesfully updated the ownership for indicator dataset with indicatorId '{}' and spatialUnitId '{}'.",
                        indicatorId, spatialUnitId);
            return indicatorEntity.getEntryId();

        } else {
            logger.error(
                    "No indicator dataset with indicatorId '{}' and spatialUnitId '{}' was found in database. Update request has no effect.",
                    indicatorId, spatialUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update indicator permissions, but no dataset exists with datasetId " + indicatorId);
        }
    }

    public String updateOwnership(OwnerInputType owner, String indicatorId) throws Exception {
        logger.info("Trying to update indicator metadata ownership for datasetId '{}'", indicatorId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);
            metadataEntity.setOwner(getOrganizationalUnitEntity(owner.getOwnerId()));

            indicatorsMetadataRepo.saveAndFlush(metadataEntity);
            logger.info("Successfully updated the ownership for indicator dataset with indicatorId '{}'.", indicatorId);
            return indicatorId;
        } else {
            logger.error("No indicator dataset with datasetId '{}' was found in database. Update request has no effect.", indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update indicator ownership, but no dataset exists with datasetId " + indicatorId);
        }
    }

    private boolean keyPropertiesHaveChanged(RestrictedEntity indicatorEntity, PermissionLevelInputType indicatorData) {
        List<String> oldRoleIds = indicatorEntity.getPermissions().stream().map(PermissionEntity::getPermissionId).collect(Collectors.toList());
        HashSet<String> newRoleIds = new HashSet<String>(indicatorData.getPermissions());

        return !CollectionUtils.isEqualCollection(oldRoleIds, newRoleIds) || indicatorEntity.isPublic() != indicatorData.getIsPublic();
    }

    private boolean keyPropertiesHaveChanged(MetadataIndicatorsEntity metadataEntity, String indicatorName,
                                             String characteristicValue, IndicatorTypeEnum indicatorType) {

        if (!metadataEntity.getDatasetName().equalsIgnoreCase(indicatorName)) {
            return true;
        }
        // characteristic value might be null, so check for that first
        if (metadataEntity.getCharacteristicValue() != null) {
            if (!metadataEntity.getCharacteristicValue().equalsIgnoreCase(characteristicValue)) {
                return true;
            }
        }

        if (!metadataEntity.getIndicatorType().equals(indicatorType)) {
            return true;
        }

        return false;
    }

    private void updateMetadata(IndicatorMetadataPATCHInputType metadata, MetadataIndicatorsEntity entity) throws Exception {
        entity.setDatasetName(metadata.getDatasetName());
        entity.setCharacteristicValue(metadata.getCharacteristicValue());
        entity.setIndicatorType(metadata.getIndicatorType());
        entity.setCreationType(metadata.getCreationType());

        if (metadata.getDisplayOrder() != null) {
            entity.setDisplayOrder(metadata.getDisplayOrder().intValue());
        }
        entity.setReferenceDateNote(metadata.getReferenceDateNote());

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

        if (metadata.getDefaultClassificationMapping() != null) {
            entity.setDefaultClassificationMappingItems(metadata.getDefaultClassificationMapping().getItems());
            entity.setColorBrewerSchemeName(metadata.getDefaultClassificationMapping().getColorBrewerSchemeName());
            @NotNull
    		@Valid
    		@DecimalMin("1")
    		@DecimalMax("9")
    		BigDecimal numClasses = metadata.getDefaultClassificationMapping().getNumClasses();
            if (numClasses == null) {
            	numClasses = new BigDecimal(5);
            }
    		entity.setNumClasses(numClasses.intValue());
    		entity.setClassificationMethod(metadata.getDefaultClassificationMapping().getClassificationMethod());
        }



        /*
         * add topic to referenced topics, bu only if topic is not yet included!
         */
        entity.setTopicReference(metadata.getTopicReference());

        entity.setAbbreviation(metadata.getAbbreviation());
        entity.setHeadlineIndicator(metadata.getIsHeadlineIndicator());
        entity.setInterpretation(metadata.getInterpretation());
        entity.setTags(new HashSet<String>(metadata.getTags()));

        Collection<RegionalReferenceValueEntity> regRefValues = new ArrayList<RegionalReferenceValueEntity>();

        for (RegionalReferenceValueType regionalReferenceValueType : metadata.getRegionalReferenceValues()) {
        	RegionalReferenceValueEntity regRefEntity = new RegionalReferenceValueEntity();

        	regRefEntity.setReferenceDate(regionalReferenceValueType.getReferenceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        	regRefEntity.setRegionalAverage(regionalReferenceValueType.getRegionalAverage());
        	regRefEntity.setRegionalSum(regionalReferenceValueType.getRegionalSum());
        	regRefEntity.setSpatiallyUnassignable(regionalReferenceValueType.getSpatiallyUnassignable());

        	regRefValues.add(regRefEntity);
		}

        entity.setRegionalReferenceValues(regRefValues);

        // persist in db
        indicatorsMetadataRepo.saveAndFlush(entity);
    }

    public void updateJoinedSpatialUnitName(String spatialUnitId, String oldName, String newName) throws Exception {
        List<IndicatorSpatialUnitJoinEntity> affectedIndicatorEntries = indicatorsSpatialUnitsRepo.findBySpatialUnitId(spatialUnitId);

        for (IndicatorSpatialUnitJoinEntity affectedIndicatorEntry : affectedIndicatorEntries) {
            affectedIndicatorEntry.setSpatialUnitName(newName);

        }

        // flush all changes to database
        indicatorsSpatialUnitsRepo.saveAllAndFlush(affectedIndicatorEntries);

        // recreate all views due to changes for spatialUnit
        recreateAllViewsForSpatialUnitById(spatialUnitId);

        lastModManager.updateLastDatabaseModification_indicators();

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

            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName);
                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                /*
                 * call DB tool to update features
                 */
                IndicatorDatabaseHandler.updateIndicatorFeatures(indicatorData, indicatorViewTableName);

                indicatorViewTableName = createOrReplaceIndicatorView_fromViewName(indicatorViewTableName, spatialUnitName, indicatorMetadataEntry.getDatasetId());

                // handle OGC web service
                String styleName;

                DefaultClassificationMappingType defaultClassificationMapping = indicatorsMapper.extractDefaultClassificationMappingFromMetadata(indicatorMetadataEntry);
                styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle, indicatorViewTableName);


                ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, datasetTitle, styleName, ResourceTypeEnum.INDICATOR);

                /*
                 * set wms and wfs urls within metadata
                 */
                persistNamesOfIndicatorTablesAndServicesInJoinTable(indicatorId, indicatorMetadataEntry.getDatasetName(),
                        spatialUnitName, indicatorViewTableName, styleName,
                        indicatorData.getPermissions(), indicatorData.getOwnerId(), indicatorData.getIsPublic());

            } else {
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

                    DefaultClassificationMappingType defaultClassificationMapping = indicatorsMapper.extractDefaultClassificationMappingFromMetadata(indicatorMetadataEntry);
                    styleName = publishDefaultStyleForWebServices(defaultClassificationMapping, datasetTitle, indicatorViewTableName);

                    publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, datasetTitle, styleName, ResourceTypeEnum.INDICATOR);

                    persistNamesOfIndicatorTablesAndServicesInJoinTable(indicatorId, indicatorMetadataEntry.getDatasetName(),
                            spatialUnitName, indicatorViewTableName, styleName,
                            indicatorData.getPermissions(), indicatorData.getOwnerId(), indicatorData.getIsPublic());
                } catch (Exception e) {
                    /*
                     * remove partially created resources and thrwo error
                     */
                    logger.error("Error while creating indicator with id {} for spatialUnit {}. Error message: {}", indicatorId, spatialUnitName, e.getMessage());
                    e.printStackTrace();

                    logger.info("Deleting partially created resources");

                    try {

                        logger.info("Delete indicatorValue table if exists for tableName '{}'", indicatorViewTableName);
                        if (indicatorViewTableName != null) {
                            IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorViewTableName);
                        }

                        logger.info("Unpublish OGC services if exists");
                        if (publishedAsService) {
                            ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
                        }


                        logger.info("Delete indicatorSpatialUnitJoinEntities if exists for metadataId '{}'", indicatorId);
                        if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(indicatorId, spatialUnitName))
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

        if (availableDates != null && availableDates.size() > 0) {
            //pick the most current date and use its property for default style
            String mostCurrentDate = availableDates.get(availableDates.size() - 1);
//			mostCurrentDate = IndicatorDatabaseHandler.DATE_PREFIX + mostCurrentDate;
//
//
//			List<Float> indicatorValues = IndicatorDatabaseHandler.getAllIndicatorValues(indicatorValueTableName, mostCurrentDate);
//
            // year-month-day
            String[] dateComponents = mostCurrentDate.split("-");

            DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();
            FeatureCollection validFeatures = IndicatorDatabaseHandler.getValidFeaturesAsFeatureCollection(dataStore, indicatorViewTableName, new BigDecimal(dateComponents[0]), new BigDecimal(dateComponents[1]), new BigDecimal(dateComponents[2]));

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
                if (indicatorValue == null || Float.isNaN(indicatorValue))
                    throw new Exception("Input contains NULL or NAN values as indicator value. Thus aborting request to update indicator features.");
            }
        }

    }

    private String createTitleForWebService(String spatialUnitName, String indicatorName) {
        return indicatorName + "_" + spatialUnitName;
    }

    public IndicatorOverviewType getIndicatorById(String indicatorId) throws Exception {
        return getIndicatorById(indicatorId, null);
    }

    public IndicatorOverviewType getIndicatorById(String indicatorId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving indicator metadata for datasetId '{}'", indicatorId);
        MetadataIndicatorsEntity indicatorsMetadataEntity = fetchMetadataIndicatorsEntity(provider, indicatorId);

        List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(indicatorsMetadataEntity.getDatasetId());
        List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(indicatorsMetadataEntity.getDatasetId());

        List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits = indicatorsSpatialUnitsRepo.findByIndicatorMetadataId(indicatorId);

        IndicatorOverviewType swaggerIndicatorMetadata = indicatorsMapper
                .mapToSwaggerIndicator(indicatorsMetadataEntity, indicatorReferences, georesourcesReferences, indicatorSpatialUnits);

        return swaggerIndicatorMetadata;
    }

    public List<IndicatorOverviewType> getAllIndicatorsMetadata() throws Exception {
        return getAllIndicatorsMetadata(null);
    }

    public List<IndicatorOverviewType> getAllIndicatorsMetadata(AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving all indicators metadata from db");

        List<MetadataIndicatorsEntity> indicatorsMeatadataEntities = fetchIndicatorMetadataEntities(provider);

        List<IndicatorOverviewType> swaggerIndicatorsMetadata = indicatorsMapper.mapToSwaggerIndicators(indicatorsMeatadataEntities);

        swaggerIndicatorsMetadata.sort(Comparator.comparing(IndicatorOverviewType::getIndicatorName));

        return swaggerIndicatorsMetadata;
    }

    public List<IndicatorOverviewType> filterIndicatorsMetadata(AuthInfoProvider provider, ResourceFilterType resourceFilterType) throws Exception {
        List<MetadataIndicatorsEntity> indicatorsMeatadataEntities = fetchIndicatorMetadataEntities(provider);

        List<MetadataIndicatorsEntity> idFilteredList = indicatorsMeatadataEntities.stream()
                .filter(i -> resourceFilterType.getIds().stream()
                        .anyMatch(r -> r.equals(i.getDatasetId()))).toList();

        List<MetadataIndicatorsEntity> topicFilteredList = indicatorsMeatadataEntities.stream()
                .filter(i -> resourceFilterType.getTopicIds().stream()
                        .anyMatch(r -> r.equals(i.getTopicReference()))).toList();

        List<MetadataIndicatorsEntity> filterResults = Stream.concat(idFilteredList.stream(), topicFilteredList.stream()).distinct().toList();

        List<IndicatorOverviewType> swaggerIndicatorsMetadata = indicatorsMapper.mapToSwaggerIndicators(filterResults);

        swaggerIndicatorsMetadata.sort(Comparator.comparing(IndicatorOverviewType::getIndicatorName));

        return swaggerIndicatorsMetadata;
    }

    private List<MetadataIndicatorsEntity> fetchIndicatorMetadataEntities(AuthInfoProvider provider) {
        List<MetadataIndicatorsEntity> indicatorsMeatadataEntities;

        if (provider == null) {
            indicatorsMeatadataEntities = indicatorsMetadataRepo.findAll().stream()
                    .filter(MetadataIndicatorsEntity::isPublic)
                    .collect(Collectors.toList());
        } else {
            indicatorsMeatadataEntities = indicatorsMetadataRepo.findAll().stream()
                    .filter(entity -> provider.checkPermissions(entity, PermissionLevelType.VIEWER))
                    .collect(Collectors.toList());

            // Iterate over the indicators and add the current user permissions. Iterator is used here in order to
            // safely remove an entity form the collection if no permissions have been found. Actually, this should
            // never happen, however, it is meant as an additional security check.
            Iterator<MetadataIndicatorsEntity> iter = indicatorsMeatadataEntities.iterator();
            while (iter.hasNext()) {
                MetadataIndicatorsEntity i = iter.next();
                try {
                    i.setUserPermissions(provider.getPermissions(i));
                } catch (NoSuchElementException ex) {
                    logger.error("No permissions found for indicator '{}'. Entity will be removed" +
                            " from resulting list.", i.getDatasetId());
                    iter.remove();
                }
            }
        }
        return indicatorsMeatadataEntities;
    }

    public String getValidIndicatorFeatures(String indicatorId, String spatialUnitId, BigDecimal year,
                                            BigDecimal month, BigDecimal day, String simplifyGeometries) throws Exception {
        return getValidIndicatorFeatures(indicatorId, spatialUnitId, year, month, day, simplifyGeometries, null);
    }

    public String getValidIndicatorFeatures(String indicatorId, String spatialUnitId, BigDecimal year,
                                            BigDecimal month, BigDecimal day, String simplifyGeometries, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving valid indicator features from Dataset with id '{}'for spatialUnit '{}' for date '{}-{}-{}'", indicatorId, spatialUnitId,
                year, month, day);

        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = fetchIndicatorSpatialUnitJoinEntity(provider, indicatorId, spatialUnitId);

                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                String json = IndicatorDatabaseHandler.getValidFeatures(indicatorViewTableName, year, month, day, simplifyGeometries);
                return json;

            } else {
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

    public String getIndicatorFeatures(String indicatorId, String spatialUnitId, String simplifyGeometries) throws Exception {
        return getIndicatorFeatures(indicatorId, spatialUnitId, simplifyGeometries, null);
    }

    public String getIndicatorFeatures(String indicatorId, String spatialUnitId, String simplifyGeometries, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving all indicator features from Dataset with id '{}'for spatialUnitId '{}' ", indicatorId, spatialUnitId);

        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = fetchIndicatorSpatialUnitJoinEntity(provider, indicatorId, spatialUnitId);

                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                String json = IndicatorDatabaseHandler.getIndicatorFeatures(indicatorViewTableName, simplifyGeometries);
                return json;

            } else {
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
        boolean success = true;
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {

            ReferenceManager.removeReferences(indicatorId);

            try {
                boolean deleteScriptsForIndicators = scriptManager.deleteScriptsByIndicatorsId(indicatorId);
            } catch (Exception e) {
                logger.error("Error while deleting scripts for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits = indicatorsSpatialUnitsRepo.findByIndicatorMetadataId(indicatorId);



            /*
             * delete featureTables and views for each spatial unit
             */
            for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorSpatialUnits) {
                String indicatorViewTableName = indicatorSpatialUnitJoinEntity.getIndicatorViewTableName();
                // delete any linked roles first
                try {
                    indicatorSpatialUnitJoinEntity = removeAnyLinkedRoles_indicatorSpatialUnit(indicatorSpatialUnitJoinEntity);
                } catch (Exception e) {
                    logger.error("Error while deleting roles for indicator spatial unit");
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

                try {
//					IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);

                    IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorSpatialUnitJoinEntity.getIndicatorViewTableName());
                } catch (Exception e) {
                    logger.error("Error while deleting spatialUnitLayers for indicator with id {}", indicatorId);
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

                try {
                    // handle OGC web service
                    ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
                } catch (Exception e) {
                    logger.error("Error while unpublishing spatialUnitLayers in OGSService for indicator with id {}", indicatorId);
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }
            }

            try {
                /*
                 * delete entries from indicatorsMetadataRepo
                 */
                indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataId(indicatorId);
            } catch (Exception e) {
                logger.error("Error while deleting entries from indicatorSpatialUnitsRepo for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
                success = false;
            }

            // delete any linked roles first
            try {
                removeAnyLinkedRoles_indicator(indicatorsMetadataRepo.findByDatasetId(indicatorId));
            } catch (Exception e) {
                logger.error("Error while deleting roles for indicator spatial unit");
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
                /*
                 * delete metadata entry
                 */
                indicatorsMetadataRepo.deleteByDatasetId(indicatorId);
            } catch (Exception e) {
                logger.error("Error while deleting metadata entry for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
                success = false;
            }

            return success;
        } else {
            logger.error(
                    "No indicator dataset with datasetName '{}' was found in database. Delete request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
        }
    }

    private void removeAnyLinkedRoles_indicator(MetadataIndicatorsEntity indicatorEntity) {
        indicatorEntity.setPermissions(new ArrayList<>());

        indicatorsMetadataRepo.saveAndFlush(indicatorEntity);

    }

    private IndicatorSpatialUnitJoinEntity removeAnyLinkedRoles_indicatorSpatialUnit(
            IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity) {

        indicatorSpatialUnitJoinEntity.setPermissions(new ArrayList<>());

        indicatorsSpatialUnitsRepo.saveAndFlush(indicatorSpatialUnitJoinEntity);

        return indicatorSpatialUnitJoinEntity;
    }

    public boolean deleteIndicatorDatasetByIdAndSpatialUnitId(String indicatorId, String spatialUnitId) throws Exception {
        logger.info("Trying to delete indicator dataset with datasetId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            boolean success = true;
            IndicatorSpatialUnitJoinEntity indicatorForSpatialUnit = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
            String indicatorViewTableName = indicatorForSpatialUnit.getIndicatorViewTableName();

            // delete any linked roles first
            try {
                indicatorForSpatialUnit = removeAnyLinkedRoles_indicatorSpatialUnit(indicatorForSpatialUnit);
            } catch (Exception e) {
                logger.error("Error while deleting roles for indicator spatial unit");
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
				/*
				 * delete featureTable and views for each spatial unit
				 */

                IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorForSpatialUnit.getIndicatorViewTableName());

            } catch (Exception e) {
                logger.error("Error while deleting spatialUnitLayer for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
                // handle OGC web service
                ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
            } catch (Exception e) {
                logger.error("Error while unpublishing spatialUnitLayer as OGC service for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
                /*
                 * delete entry from indicatorsMetadataRepo
                 */
                indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
            } catch (Exception e) {
                logger.error("Error while deleting entry from indicatorSpatialUnitsRepo for indicator with id {} and spatialUnit with id {}", indicatorId, spatialUnitId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
                success = false;
            }

            return success;
        } else {
            logger.error(
                    "No indicator dataset with datasetId '{}' was found in database. Delete request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
        }
    }

    public boolean deleteIndicatorLayersForSpatialUnitId(String spatialUnitId) throws Exception {
        logger.info("Trying to delete all indicator layers associated with the spatialUnitId '{}'", spatialUnitId);
        if (indicatorsSpatialUnitsRepo.existsBySpatialUnitId(spatialUnitId)) {
            List<IndicatorSpatialUnitJoinEntity> indicatorDatasetsForSpatialUnit = indicatorsSpatialUnitsRepo.findBySpatialUnitId(spatialUnitId);
            int numberOfIndicatorLayersToDelete = indicatorDatasetsForSpatialUnit.size();

            List<String> indicatorNames = new ArrayList<String>();

            /*
             * delete featureTables and views for each indicator dataset
             */
            for (IndicatorSpatialUnitJoinEntity indicatorSpatialUnitJoinEntity : indicatorDatasetsForSpatialUnit) {
                String indicatorViewTableName = indicatorSpatialUnitJoinEntity.getIndicatorViewTableName();
//				IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);

                // delete any linked roles first
                try {
                    indicatorSpatialUnitJoinEntity = removeAnyLinkedRoles_indicatorSpatialUnit(indicatorSpatialUnitJoinEntity);
                } catch (Exception e) {
                    logger.error("Error while deleting roles for indicator spatial unit");
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

                try {
					/*
					 * delete featureTable and views for each spatial unit
					 */

                    IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorSpatialUnitJoinEntity.getIndicatorViewTableName());
                } catch (Exception e) {
                    logger.error("Error while deleting spatialUnitLayer for indicator with id {}", indicatorSpatialUnitJoinEntity.getIndicatorMetadataId());
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

                try {
                    // handle OGC web service
                    ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
                } catch (Exception e) {
                    logger.error("Error while unpublishing spatialUnitLayer as OGC service for indicator with id {}", indicatorSpatialUnitJoinEntity.getIndicatorMetadataId());
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

                try {
                    /*
                     * delete entry from indicatorsMetadataRepo
                     */
                    indicatorsSpatialUnitsRepo.deleteByIndicatorMetadataIdAndSpatialUnitId(indicatorSpatialUnitJoinEntity.getIndicatorMetadataId(), spatialUnitId);

                } catch (Exception e) {
                    logger.error("Error while deleting entry from indicatorSpatialUnitsRepo for indicator with id {} and spatialUnit with id {}", indicatorSpatialUnitJoinEntity.getIndicatorMetadataId(), spatialUnitId);
                    logger.error("Error was: {}", e.getMessage());
                    e.printStackTrace();
                }

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

            boolean success = true;
            /*
             * delete featureTable and views for each spatial unit
             */
            String indicatorViewTableName = indicatorForSpatialUnit.getIndicatorViewTableName();
//			IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);


            try {
                /*
                 * delete timestamp for indicator and spatial unit
                 */
                IndicatorDatabaseHandler.deleteIndicatorTimeStamp(indicatorForSpatialUnit.getIndicatorViewTableName(), year, month, day);

            } catch (Exception e) {
                logger.error("Error while deleting timestamp in value table for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

            try {
                indicatorMetadataEntry = deleteTimestampInMetadataEntry(year, month, day, indicatorMetadataEntry);
                indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);
            } catch (Exception e) {
                logger.error("Error while deleting timestamp in metadata entry for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
                success = false;
            }

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
            return success;
        } else {
            logger.error(
                    "No indicator dataset with datasetId '{}' was found in database. Delete request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
        }
    }

    public IndicatorOverviewType addIndicator(IndicatorPOSTInputType indicatorData) throws Exception {
        String spatialUnitName = null;
        String indicatorViewTableName = null;
        boolean publishedAsService = false;
        MetadataIndicatorsEntity indicatorMetadataEntity = null;
        String metadataId = null;
        try {
            /*
             * addIndicator can be called multiple times, i.e. for each spatialUnitName!
             * (there is only 1 metadata entry for the indicator, but for each spatial unit there is one indicator value table
             * and one feature view. Thus add will be called for each combination of indicator and spatial unit)
             */
            String indicatorName = indicatorData.getDatasetName();
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
                MetadataIndicatorsEntity existingIndicator = indicatorsMetadataRepo.findByDatasetName(indicatorName);
                logger.error(
                        "The indicator metadataset with datasetName '{}', characteristicValue '{}' and indicatorType '{}' already exists. Thus aborting add indicator request.",
                        indicatorName, characteristicValue, indicatorType);

                String errMsg = messageResolver.getMessage(MSG_INDICATOR_EXISTS_ERROR);
                throw new Exception(String.format(errMsg, indicatorName, existingIndicator.getOwner().getMandant().getName()));
            }

            indicatorMetadataEntity = createMetadata(indicatorData);

            metadataId = indicatorMetadataEntity.getDatasetId();
            ReferenceManager.createReferences(indicatorData.getRefrencesToGeoresources(),
                    indicatorData.getRefrencesToOtherIndicators(), metadataId);

            /*
             * only if creationType == INSERTION then create table and view
             */

//            if (creationType.equals(CreationTypeEnum.INSERTION)) {
//
//                logger.info("As creationType is set to '{}', a featureTable and featureView will be created from indicator values. Also OGC publishing will be done.", creationType.toString());
//                String indicatorValueTableName = createIndicatorValueTable(indicatorData.getIndicatorValues(), metadataId);
//                indicatorViewTableName = createOrReplaceIndicatorView_fromValueTableName(indicatorValueTableName, spatialUnitName, metadataId);
////				deleteIndicatorValueTable(indicatorTempTableName);
//
//                // handle OGC web service
//                String styleName = publishDefaultStyleForWebServices(indicatorData.getDefaultClassificationMapping(), createTitleForWebService(spatialUnitName, indicatorName), indicatorViewTableName);
//                publishedAsService = ogcServiceManager.publishDbLayerAsOgcService(indicatorViewTableName, createTitleForWebService(spatialUnitName, indicatorName), styleName, ResourceTypeEnum.INDICATOR);
//
//                persistNamesOfIndicatorTablesAndServicesInJoinTable(metadataId, indicatorName, spatialUnitName, indicatorViewTableName, styleName, indicatorData.getPermissions());
//
//            } else {
//                logger.info("As creationType is set to '{}', Only the metadata entry was created. No featureTable and view have been created..", creationType.toString());
//            }
        } catch (Exception e) {
            /*
             * remove partially created resources and thrwo error
             */
            logger.error("Error while creating indicator. Error message: " + e.getMessage());
            e.printStackTrace();

            logger.info("Deleting partially created resources");

            try {
                logger.info("Delete metadata entry if exists for id '{}'" + metadataId);
                if (metadataId != null) {
                    if (indicatorsMetadataRepo.existsByDatasetId(metadataId))
                        indicatorsMetadataRepo.deleteByDatasetId(metadataId);
                }

                logger.info("Delete indicatorValue table if exists for tableName '{}'" + indicatorViewTableName);
                if (indicatorViewTableName != null) {
                    IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorViewTableName);
                }

                logger.info("Unpublish OGC services if exists");
                if (publishedAsService) {
                    ogcServiceManager.unpublishDbLayer(indicatorViewTableName, ResourceTypeEnum.INDICATOR);
                }


                logger.info("Delete indicatorSpatialUnitJoinEntities if exists for metadataId '{}'" + metadataId);
                if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitName(metadataId, spatialUnitName))
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

        List<IndicatorReferenceType> indicatorReferences = ReferenceManager.getIndicatorReferences(metadataId);
        List<GeoresourceReferenceType> georesourcesReferences = ReferenceManager.getGeoresourcesReferences(metadataId);

        List<IndicatorSpatialUnitJoinEntity> indicatorSpatialUnits = indicatorsSpatialUnitsRepo.findByIndicatorMetadataId(metadataId);

        return indicatorsMapper
                .mapToSwaggerIndicator(indicatorsMetadataRepo.findByDatasetId(metadataId), indicatorReferences, georesourcesReferences, indicatorSpatialUnits);

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

        if (indicatorValues != null && indicatorValues.size() > 0) {
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
        logger.info("parsing date from submitted date components. Submitted components were 'year: {}, month: {}, day: {}'. As Java time treats month 0-based, the follwing date will be used: 'year-month(-1)-day {}-{}-{}'", year, month, day, year, month.intValue() - 1, day);
        String datePropertyName = IndicatorDatabaseHandler.createDateStringForDbProperty(date);
        datePropertyName = datePropertyName.replace(IndicatorDatabaseHandler.DATE_PREFIX, "");

        indicatorMetadataEntry.removeTimestampIfExists(datePropertyName);

        return indicatorMetadataEntry;
    }

    private void deleteIndicatorValueTable(String indicatorTempTableName) throws IOException, SQLException {
        logger.info("Deleting indicator table with name {}.", indicatorTempTableName);

        IndicatorDatabaseHandler.deleteIndicatorValueTable(indicatorTempTableName);
        ;

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

    private void persistNamesOfIndicatorTablesAndServicesInJoinTable(String indicatorMetadataId, String indicatorName,
                                                                     String spatialUnitName, String indicatorViewTableName,
                                                                     String styleName, List<String> permissions, String ownerId,
                                                                     boolean istPublic) throws ResourceNotFoundException {
        logger.info(
                "Create or modify entry in indicator spatial units join table for indicatorId '{}', and spatialUnitName '{}'. Set indicatorValueTable with name '{}'.",
                indicatorMetadataId, spatialUnitName, indicatorViewTableName);

        MetadataSpatialUnitsEntity spatialUnitMetadataEntity = DatabaseHelperUtil.getSpatialUnitMetadataEntityByName(spatialUnitName);
        String spatialUnitId = spatialUnitMetadataEntity.getDatasetId();

        IndicatorSpatialUnitJoinEntity entity = new IndicatorSpatialUnitJoinEntity();

        /*
         * if an entity already exists for this combination of indicator and spatial unti then only modify those values
         */
        if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorMetadataId, spatialUnitId))
            entity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorMetadataId, spatialUnitId);

        entity.setIndicatorMetadataId(indicatorMetadataId);
        entity.setIndicatorName(indicatorName);
        entity.setIndicatorValueTableName(indicatorViewTableName);
        entity.setSpatialUnitId(spatialUnitId);
        entity.setSpatialUnitName(spatialUnitName);
        entity.setWmsUrl(ogcServiceManager.getWmsUrl(indicatorViewTableName));
        entity.setWfsUrl(ogcServiceManager.getWfsUrl(indicatorViewTableName));
        entity.setDefaultStyleName(styleName);

        entity.setPermissions(retrievePermissions(permissions));
        entity.setOwner(getOrganizationalUnitEntity(ownerId));
        entity.setPublic(istPublic);

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

        if (indicatorData.getDisplayOrder() != null) {
            entity.setDisplayOrder(indicatorData.getDisplayOrder().intValue());
        }
        entity.setReferenceDateNote(indicatorData.getReferenceDateNote());

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
        @NotNull
		@Valid
		@DecimalMin("1")
		@DecimalMax("9")
		BigDecimal numClasses = indicatorData.getDefaultClassificationMapping().getNumClasses();
        if (numClasses == null) {
        	numClasses = new BigDecimal(5);
        }
		entity.setNumClasses(numClasses.intValue());
		entity.setClassificationMethod(indicatorData.getDefaultClassificationMapping().getClassificationMethod());

        entity.setAbbreviation(indicatorData.getAbbreviation());
        entity.setHeadlineIndicator(indicatorData.getIsHeadlineIndicator());
        entity.setInterpretation(indicatorData.getInterpretation());
        entity.setTags(new HashSet<String>(indicatorData.getTags()));


        /*
         * the remaining properties cannot be set initially!
         */
        entity.setDbTableName(null);
        entity.setWfsUrl(null);
        entity.setWmsUrl(null);

        entity.setPermissions(retrievePermissions(indicatorData.getPermissions()));
        entity.setOwner(getOrganizationalUnitEntity(indicatorData.getOwnerId()));
        entity.setPublic(indicatorData.getIsPublic());

        entity.setRegionalReferenceValues(new ArrayList<RegionalReferenceValueEntity>());

        /*
         * process availableTimestamps property for indicator metadata entity
         */
//        entity = addNewTimestampsToMetadataEntry(indicatorData.getIndicatorValues(), entity);

        // persist in db
        indicatorsMetadataRepo.saveAndFlush(entity);
        logger.info("Completed to add indicator metadata entry for indicator dataset with id {}.",
                entity.getDatasetId());

        return entity;
    }

    public List<IndicatorPropertiesWithoutGeomType> getIndicatorFeaturePropertiesWithoutGeometry(String indicatorId,
                                                                                                 String spatialUnitId) throws SQLException, IOException, ResourceNotFoundException {
        return getIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, null);
    }

    public List<IndicatorPropertiesWithoutGeomType> getIndicatorFeaturePropertiesWithoutGeometry(String indicatorId,
                                                                                                 String spatialUnitId,
                                                                                                 AuthInfoProvider provider) throws SQLException, IOException, ResourceNotFoundException {
        logger.info("Retrieving all indicator feature properties without geometries from dataset with id '{}'for spatialUnitId '{}' ", indicatorId, spatialUnitId);

        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = fetchIndicatorSpatialUnitJoinEntity(provider, indicatorId, spatialUnitId);

                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = IndicatorDatabaseHandler.getIndicatorFeaturePropertiesWithoutGeometries(indicatorViewTableName);
                return indicatorFeaturePropertiesWithoutGeom;

            } else {
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
        return getValidIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, year, month, day, null);
    }

    public List<IndicatorPropertiesWithoutGeomType> getValidIndicatorFeaturePropertiesWithoutGeometry(
            String indicatorId, String spatialUnitId, BigDecimal year, BigDecimal month, BigDecimal day, AuthInfoProvider provider) throws ResourceNotFoundException, IOException, SQLException {
        logger.info("Retrieving valid indicator feature properties without geometries from dataset with id '{}'for spatialUnit '{}' for date '{}-{}-{}'", indicatorId, spatialUnitId,
                year, month, day);

        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = fetchIndicatorSpatialUnitJoinEntity(provider, indicatorId, spatialUnitId);

                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                List<IndicatorPropertiesWithoutGeomType> validIndicatorFeaturePropertiesWithoutGeom =
                        IndicatorDatabaseHandler.getValidFeaturePropertiesWithoutGeometries(indicatorViewTableName, year, month, day);
                return validIndicatorFeaturePropertiesWithoutGeom;

            } else {
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

    private MetadataIndicatorsEntity fetchMetadataIndicatorsEntity(AuthInfoProvider provider, String indicatorsId) throws ResourceNotFoundException {
        MetadataIndicatorsEntity metadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorsId);
        if (provider == null) {
            if (metadataEntity == null || !metadataEntity.isPublic()) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' " +
                        "was not found.", indicatorsId));
            }
        } else {
            if (metadataEntity == null || !provider.checkPermissions(metadataEntity, PermissionLevelType.VIEWER)) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' " +
                        "was not found.", indicatorsId));
            }
            try {
                metadataEntity.setUserPermissions(provider.getPermissions(metadataEntity));
            } catch (NoSuchElementException ex) {
                logger.error("No permissions found for indicator '{}'", metadataEntity.getDatasetId());
            }
        }
        return metadataEntity;
    }

    private IndicatorSpatialUnitJoinEntity fetchIndicatorSpatialUnitJoinEntity(AuthInfoProvider provider, String indicatorId, String spatialUnitId) throws ResourceNotFoundException {
        IndicatorSpatialUnitJoinEntity entity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
        if (provider == null) {
            if (entity == null || !entity.isPublic()) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource " +
                        "for indicator '%s' and spatial unit '%s' was not found.", indicatorId, spatialUnitId));
            }
        } else {
            if (entity == null || !provider.checkPermissions(entity, PermissionLevelType.VIEWER)) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource " +
                        "for indicator '%s' and spatial unit '%s' was not found.", indicatorId, spatialUnitId));
            }
            try {
                entity.setUserPermissions(provider.getPermissions(entity));
            } catch (NoSuchElementException ex) {
                logger.error("No permissions found for indicator '{}' and spatial unit '{}'",
                        entity.getIndicatorMetadataId(), entity.getSpatialUnitId());
            }
        }
        return entity;
    }

    public List<PermissionLevelType> getIndicatortPermissionsByDatasetId(String indicatorId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving indicator permissions for datasetId '{}'", indicatorId);

        MetadataIndicatorsEntity indicatorMetadataEntity = indicatorsMetadataRepo.findByDatasetId(indicatorId);

        if (indicatorMetadataEntity == null || !provider.checkPermissions(indicatorMetadataEntity, PermissionLevelType.VIEWER)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", indicatorId));
        }

        List<PermissionLevelType> permissions = provider.getPermissions(indicatorMetadataEntity);
        return permissions;
    }

    public List<PermissionLevelType> getIndicatortPermissionsBySpatialUnitIdAndId(String indicatorId, String spatialUnitId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving indicator permissions for datasetId '{}'", indicatorId);

        IndicatorSpatialUnitJoinEntity entity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);

        if (entity == null || !provider.checkPermissions(entity, PermissionLevelType.VIEWER)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", indicatorId));
        }

        List<PermissionLevelType> permissions = provider.getPermissions(entity);
        return permissions;
    }

    //TODO: should strict be the default mode?
    /*
    private boolean hasAllowedRoleStrict(AuthInfoProvider authInfoProvider, IndicatorSpatialUnitJoinEntity entity) {
        return (entity.getRoles() == null ||
                entity.getRoles().isEmpty() ||
                authInfoProvider.hasRealmAdminRole() ||
                entity.getRoles().stream()
                        .anyMatch(r -> authInfoProvider.hasRealmRole(r.getOrganizationalUnit()))) &&
                (entity.getMetadataIndicatorsEntity().getRoles() == null ||
                        entity.getMetadataIndicatorsEntity().getRoles().isEmpty() ||
                        entity.getMetadataIndicatorsEntity().getRoles().stream()
                                .anyMatch(r -> authInfoProvider.hasRealmRole(r.getOrganizationalUnit()))) &&
                (entity.getMetadataSpatialUnitsEntity().getRoles() == null ||
                        entity.getMetadataSpatialUnitsEntity().getRoles().isEmpty() ||
                        entity.getMetadataSpatialUnitsEntity().getRoles().stream()
                                .anyMatch(r -> authInfoProvider.hasRealmRole(r.getOrganizationalUnit())));
    }
    */

    public boolean updateIndicatorOrder(List<IndicatorPATCHDisplayOrderInputType> indicatorOrderArray) {
        for (IndicatorPATCHDisplayOrderInputType indicatorPATCHDisplayOrderInputType : indicatorOrderArray) {
            if (this.indicatorsMetadataRepo.existsByDatasetId(indicatorPATCHDisplayOrderInputType.getIndicatorId())) {
                MetadataIndicatorsEntity indicatorMetadataEntity = this.indicatorsMetadataRepo.findByDatasetId(indicatorPATCHDisplayOrderInputType.getIndicatorId());
                indicatorMetadataEntity.setDisplayOrder(indicatorPATCHDisplayOrderInputType.getDisplayOrder().intValue());

                this.indicatorsMetadataRepo.save(indicatorMetadataEntity);
            }
        }
        this.indicatorsMetadataRepo.flush();
        return true;
    }

    public void recreateAllViewsForSpatialUnitById(String spatialUnitId) {

        List<IndicatorSpatialUnitJoinEntity> affectedIndicatorEntries = indicatorsSpatialUnitsRepo.findBySpatialUnitId(spatialUnitId);

        for (IndicatorSpatialUnitJoinEntity affectedIndicatorEntry : affectedIndicatorEntries) {
            String indicatorViewTableName = affectedIndicatorEntry.getIndicatorViewTableName();

            try {
                indicatorViewTableName = createOrReplaceIndicatorView_fromViewName(indicatorViewTableName, affectedIndicatorEntry.getSpatialUnitName(), affectedIndicatorEntry.getIndicatorMetadataId());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void recreateAllViews() {

        List<IndicatorSpatialUnitJoinEntity> affectedIndicatorEntries = indicatorsSpatialUnitsRepo.findAll();

        for (IndicatorSpatialUnitJoinEntity affectedIndicatorEntry : affectedIndicatorEntries) {
            String indicatorViewTableName = affectedIndicatorEntry.getIndicatorViewTableName();

            try {
                indicatorViewTableName = createOrReplaceIndicatorView_fromViewName(indicatorViewTableName, affectedIndicatorEntry.getSpatialUnitName(), affectedIndicatorEntry.getIndicatorMetadataId());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecords(String indicatorId,
                                                                                     String spatialUnitId, String featureId) throws Exception {
        return getSingleIndicatorFeatureRecords(indicatorId,
                spatialUnitId, featureId, null);
    }

    public List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecord(String indicatorId,
                                                                                    String spatialUnitId, String featureId, String featureRecordId) throws Exception {
        // TODO Auto-generated method stub
        return getSingleIndicatorFeatureRecord(indicatorId, spatialUnitId, featureId, featureRecordId, null);
    }

    public List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecords(String indicatorId,
                                                                                     String spatialUnitId, String featureId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving single indicator feature database records for dataset with id '{}' and spatialUnitId '{}' and featureId '{}'", indicatorId, spatialUnitId,
                featureId);

        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = fetchIndicatorSpatialUnitJoinEntity(provider, indicatorId, spatialUnitId);

                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom =
                        IndicatorDatabaseHandler.getSingleIndicatorFeatureRecords(indicatorViewTableName, featureId);
                return indicatorFeaturePropertiesWithoutGeom;

            } else {
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

    public List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecord(String indicatorId,
                                                                                    String spatialUnitId, String featureId, String featureRecordId, AuthInfoProvider provider) throws Exception {
        logger.info("Retrieving single indicator feature database record for dataset with id '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId,
                featureId, featureRecordId);

        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            if (indicatorsSpatialUnitsRepo.existsByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId)) {
                IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = fetchIndicatorSpatialUnitJoinEntity(provider, indicatorId, spatialUnitId);

                String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

                List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom =
                        IndicatorDatabaseHandler.getSingleIndicatorFeatureRecord(indicatorViewTableName, featureId, featureRecordId);
                return indicatorFeaturePropertiesWithoutGeom;

            } else {
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

    public boolean deleteSingleIndicatorFeatureRecordsByFeatureId(String indicatorId, String spatialUnitId,
                                                                  String featureId) throws Exception {
        logger.info("Trying to delete single indicator feature records for dataset with indicatorId '{}' and spatialUnitId '{}' and featureId '{}'", indicatorId, spatialUnitId, featureId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            MetadataIndicatorsEntity indicatorMetadataEntry = indicatorsMetadataRepo.findByDatasetId(indicatorId);
            IndicatorSpatialUnitJoinEntity indicatorForSpatialUnit = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);

            boolean success = true;
            /*
             * delete featureTable and views for each spatial unit
             */
            String indicatorViewTableName = indicatorForSpatialUnit.getIndicatorViewTableName();
//			IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);


            try {
                /*
                 * delete timestamp for indicator and spatial unit
                 */
                IndicatorDatabaseHandler.deleteSingleFeatureRecordsForFeatureId(indicatorViewTableName, featureId);
                indicatorMetadataEntry.setLastUpdate(java.util.Calendar.getInstance().getTime());
                indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);

            } catch (Exception e) {
                logger.error("Error while deleting features in value table for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

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
            return success;
        } else {
            logger.error(
                    "No indicator dataset with datasetId '{}' was found in database. Delete request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
        }
    }

    public boolean deleteSingleIndicatorFeatureRecordByFeatureId(String indicatorId, String spatialUnitId,
                                                                 String featureId, String featureRecordId) throws Exception {
        logger.info("Trying to delete single indicator feature record for dataset with indicatorId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId, featureId, featureRecordId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            MetadataIndicatorsEntity indicatorMetadataEntry = indicatorsMetadataRepo.findByDatasetId(indicatorId);
            IndicatorSpatialUnitJoinEntity indicatorForSpatialUnit = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);

            boolean success = true;
            /*
             * delete featureTable and views for each spatial unit
             */
            String indicatorViewTableName = indicatorForSpatialUnit.getIndicatorViewTableName();
//			IndicatorDatabaseHandler.deleteIndicatorFeatureView(featureViewTableName);


            try {
                /*
                 * delete timestamp for indicator and spatial unit
                 */
                IndicatorDatabaseHandler.deleteSingleFeatureRecordForFeatureId(indicatorViewTableName, featureId, featureRecordId);
                indicatorMetadataEntry.setLastUpdate(java.util.Calendar.getInstance().getTime());
                indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);

            } catch (Exception e) {
                logger.error("Error while deleting features in value table for indicator with id {}", indicatorId);
                logger.error("Error was: {}", e.getMessage());
                e.printStackTrace();
            }

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
            return success;
        } else {
            logger.error(
                    "No indicator dataset with datasetId '{}' was found in database. Delete request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete indicator dataset, but no dataset existes with datasetId " + indicatorId);
        }
    }

    public String updateFeatureRecordByRecordId(IndicatorPropertiesWithoutGeomType indicatorFeatureRecordData,
                                                String indicatorId, String spatialUnitId, String featureId, String featureRecordId) throws Exception {
        logger.info("Trying to update indicator single feature record for indicatorId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId, featureId, featureRecordId);
        if (indicatorsMetadataRepo.existsByDatasetId(indicatorId)) {
            MetadataIndicatorsEntity indicatorMetadataEntry = indicatorsMetadataRepo.findByDatasetId(indicatorId);

            IndicatorSpatialUnitJoinEntity indicatorSpatialsUnitsEntity = indicatorsSpatialUnitsRepo.findByIndicatorMetadataIdAndSpatialUnitId(indicatorId, spatialUnitId);
            String indicatorViewTableName = indicatorSpatialsUnitsEntity.getIndicatorViewTableName();

            /*
             * call DB tool to update features
             */
            IndicatorDatabaseHandler.updateSpatialResourceFeatureRecordByRecordId(indicatorFeatureRecordData, indicatorViewTableName, featureId, featureRecordId);
            indicatorMetadataEntry.setLastUpdate(java.util.Calendar.getInstance().getTime());
            indicatorsMetadataRepo.saveAndFlush(indicatorMetadataEntry);

            indicatorViewTableName = IndicatorDatabaseHandler.createOrReplaceIndicatorView_fromViewTableName(indicatorViewTableName, indicatorSpatialsUnitsEntity.getSpatialUnitName());

            /*
             * republish indicator layer as OGC service
             */
            String spatialUnitName = indicatorSpatialsUnitsEntity.getSpatialUnitName();

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

            return indicatorId;

        } else {
            logger.error(
                    "No indicator dataset with datasetId '{}' was found in database. Update request has no effect.",
                    indicatorId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update indicator feature record, but no dataset existes with datasetId " + indicatorId);
        }
    }
}
