package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.IndicatorsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.api.impl.util.SimplifyGeometriesEnum;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.export.ExportManager;
import de.hsbo.kommonitor.datamanagement.export.TempFileInputStream;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.geotools.api.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class IndicatorsController extends BasePathController implements IndicatorsApi {

    private static final Logger LOG = LoggerFactory.getLogger(IndicatorsController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    IndicatorsManager indicatorsManager;

    @Autowired
    private ExportManager exportManager;
    
    @Autowired
    private LastModificationManager lastModManager;

    @Autowired
    private AuthInfoProviderFactory authInfoProviderFactory;

    @org.springframework.beans.factory.annotation.Autowired
    public IndicatorsController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'editor')")
    public ResponseEntity deleteIndicatorByIdAndSpatialUnitId(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId) {
        LOG.info("Received request to delete indicator for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);

        boolean isDeleted;
        try {
            isDeleted = indicatorsManager.deleteIndicatorDatasetByIdAndSpatialUnitId(indicatorId, spatialUnitId);
            lastModManager.updateLastDatabaseModification_indicators();

            if (isDeleted)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'editor')")
    public ResponseEntity deleteIndicatorByIdAndYearAndMonth(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            BigDecimal year,
            BigDecimal month,
            BigDecimal day) {
        LOG.info("Received request to delete indicator for indicatorId '{}' and Date '{}-{}-{}'", indicatorId, year, month, day);

        boolean isDeleted;
        try {
            isDeleted = indicatorsManager.deleteIndicatorDatasetByIdAndDate(indicatorId, spatialUnitId, year, month, day);
            lastModManager.updateLastDatabaseModification_indicators();

            if (isDeleted)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
	public ResponseEntity<Void> deleteSingleIndicatorFeatureById(
			@P("indicatorId") String indicatorId,
			String spatialUnitId,
			String featureId) {
		LOG.info("Received request to delete single indicator feature databse records for indicatorId '{}' and spatialUnitId '{}' and featureId '{}'", indicatorId, spatialUnitId, featureId);

        boolean isDeleted;
        try {
            isDeleted = indicatorsManager.deleteSingleIndicatorFeatureRecordsByFeatureId(indicatorId, spatialUnitId, featureId);
            lastModManager.updateLastDatabaseModification_indicators();

            if (isDeleted)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
	public ResponseEntity<Void> deleteSingleIndicatorFeatureRecordById(
			@P("indicatorId") String indicatorId,
			String spatialUnitId,
			String featureId,
			String featureRecordId) {
		LOG.info("Received request to delete single indicator feature databse record for indicatorId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId, featureId, featureRecordId);

        boolean isDeleted;
        try {
            isDeleted = indicatorsManager.deleteSingleIndicatorFeatureRecordByFeatureId(indicatorId, spatialUnitId, featureId, featureRecordId);
            lastModManager.updateLastDatabaseModification_indicators();

            if (isDeleted)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
    public ResponseEntity<Resource> exportIndicatorBySpatialUnitIdAndId(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            String format) {
        LOG.info("Received request to export indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();

            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) indicatorsManager
                    .getIndicatorFeatureCollection(
                            indicatorId,
                            spatialUnitId,
                            SimplifyGeometriesEnum.ORIGINAL.toString(),
                            provider,
                            dataStore);
            File exportFile = exportManager.exportFeatureCollection(featureCollection, format);

            dataStore.dispose();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + indicatorId + "." + format);
            headers.add("Content-Type", "application/json; charset=utf-8");

            TempFileInputStream resourceStream = new TempFileInputStream(exportFile);
            InputStreamResource resource = new InputStreamResource(resourceStream);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<Resource> exportIndicatorBySpatialUnitIdAndIdAndYearAndMonth(String indicatorId, String spatialUnitId, BigDecimal year, BigDecimal month, BigDecimal day, String format) {
        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();

            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) indicatorsManager
                    .getValidIndicatorFeatureCollection(
                            indicatorId,
                            spatialUnitId,
                            year,
                            month,
                            day,
                            SimplifyGeometriesEnum.ORIGINAL.toString(),
                            dataStore,
                            provider
                    );
            File exportFile = exportManager.exportFeatureCollection(featureCollection, format);

            dataStore.dispose();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + indicatorId + "." + format);
            headers.add("Content-Type", "application/json; charset=utf-8");

            TempFileInputStream resourceStream = new TempFileInputStream(exportFile);
            InputStreamResource resource = new InputStreamResource(resourceStream);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    // TODO Check if to use this refactored method
//    public ResponseEntity<Resource> createExportResponse(DataStore dataStore, SimpleFeatureCollection featureCollection, String format, String indicatorId) throws IOException, ResourceNotFoundException {
//        File exportFile = exportManager.exportFeatureCollection(featureCollection, format);
//
//        dataStore.dispose();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + indicatorId + "." + format);
//        headers.add("Content-Type", "application/json; charset=utf-8");
//
//        TempFileInputStream resourceStream = new TempFileInputStream(exportFile);
//        InputStreamResource resource = new InputStreamResource(resourceStream);
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('creator', 'resources')")
    public ResponseEntity<IndicatorOverviewType> addIndicatorAsBody(IndicatorPOSTInputType indicatorData) {
        LOG.info("Received request to insert new indicator");

        IndicatorOverviewType indicatorMetadata;
        try {
            indicatorMetadata = indicatorsManager.addIndicator(indicatorData);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorMetadata != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorMetadata.getIndicatorId();
            try {
                responseHeaders.setLocation(new URI(location));
            } catch (URISyntaxException e) {
                // return ApiResponseUtil.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(indicatorMetadata, responseHeaders, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'creator')")
    public ResponseEntity deleteIndicatorById(@P("indicatorId") String indicatorId) {
        LOG.info("Received request to delete indicator for indicatorId '{}'", indicatorId);

        boolean isDeleted;
        try {
            isDeleted = indicatorsManager.deleteIndicatorDatasetById(indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();

            if (isDeleted)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
    public ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndId(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            String simplifyGeometries) {
        LOG.info("Received request to get indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            String geoJsonFeatures = indicatorsManager.getIndicatorFeatures(indicatorId, spatialUnitId, simplifyGeometries, provider);
            String fileName = "IndicatorFeatures_" + spatialUnitId + "_" + indicatorId + ".json";

            HttpHeaders headers = new HttpHeaders();
            headers.add("content-disposition", "attachment; filename=" + fileName);
            headers.add("Content-Type", "application/json; charset=utf-8");
            byte[] JsonBytes = geoJsonFeatures.getBytes();

            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.geo+json"))
                    .body(JsonBytes);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
    public ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndIdAndYearAndMonth(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            BigDecimal year,
            BigDecimal month,
            BigDecimal day,
            String simplifyGeometries) {
        LOG.info(
                "Received request to get indicators features for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            String geoJsonFeatures = indicatorsManager.getValidIndicatorFeatures(indicatorId, spatialUnitId, year,
                    month, day, simplifyGeometries, provider);
            String fileName = "IndicatorFeatures_" + spatialUnitId + "_" + indicatorId + "_" + year + "-" + month
                    + "-" + day + ".json";

            HttpHeaders headers = new HttpHeaders();
            headers.add("content-disposition", "attachment; filename=" + fileName);
            headers.add("Content-Type", "application/json; charset=utf-8");
            byte[] JsonBytes = geoJsonFeatures.getBytes();

            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.geo+json")).body(JsonBytes);

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("hasRequiredPermissionLevel('viewer')")
    public ResponseEntity<List<IndicatorOverviewType>> getIndicators() {
        LOG.info("Received request to get all indicators metadata");
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {

            if (accept != null && accept.contains("application/json")) {

                List<IndicatorOverviewType> spatialunitsMetadata = indicatorsManager.getAllIndicatorsMetadata(provider);

                return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<IndicatorOverviewType>> filterIndicators(ResourceFilterType resourceFilterType) {
        LOG.info("Received request to get all indicators metadata filtered by the resource type");
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            if (accept != null && accept.contains("application/json")) {
                List<IndicatorOverviewType> spatialunitsMetadata = indicatorsManager.filterIndicatorsMetadata(provider, resourceFilterType);
                return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'viewer')")
    public ResponseEntity<IndicatorOverviewType> getIndicatorById(@P("indicatorId") String indicatorId) {
        LOG.info("Received request to get indicator metadata for indicatorId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            if (accept != null && accept.contains("application/json")) {
                IndicatorOverviewType indicatorMetadata = indicatorsManager.getIndicatorById(indicatorId, provider);
                return new ResponseEntity<>(indicatorMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<PermissionLevelType>> getIndicatorPermissionsById(
            String indicatorId) {
        LOG.info("Received request to list permissions for indicator with datasetId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            if (accept != null && accept.contains("application/json")) {
                List<PermissionLevelType> permissions =
                        indicatorsManager.getIndicatortPermissionsByDatasetId(indicatorId, provider);

                return new ResponseEntity<>(permissions, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<PermissionLevelType>> getIndicatorPermissionsBySpatialUnitIdAndId(
            String indicatorId,
            String spatialUnitId) {
        LOG.info("Received request to list permissions for spatialUnit Id {} and indicator with datasetId '{}'",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            if (accept != null && accept.contains("application/json")) {
                List<PermissionLevelType> permissions =
                        indicatorsManager.getIndicatortPermissionsBySpatialUnitIdAndId(indicatorId, spatialUnitId, provider);

                return new ResponseEntity<>(permissions, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
    public ResponseEntity updateIndicatorAsBody(
            @P("indicatorId") String indicatorId,
            IndicatorPUTInputType indicatorData) {
        LOG.info("Received request to update indicator features for indicator '{}'", indicatorId);

        try {
            indicatorId = indicatorsManager.updateFeatures(indicatorData, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
    public ResponseEntity updateIndicatorMetadataAsBody(
            @P("indicatorId") String indicatorId,
            IndicatorMetadataPATCHInputType metadata) {
        LOG.info("Received request to update indicator metadata for indicatorId '{}'", indicatorId);

        try {
            indicatorId = indicatorsManager.updateMetadata(metadata, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorOrderArray, 'indicator', 'editor')")
    public ResponseEntity<Void> updateIndicatorDisplayOrder(@P("indicatorOrderArray") List<IndicatorPATCHDisplayOrderInputType> indicatorOrderArray) {
    	LOG.info("Received request to update indicator display order ");

        boolean update;

        try {
            update = indicatorsManager.updateIndicatorOrder(indicatorOrderArray);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (update) {

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'creator')")
    public ResponseEntity updateIndicatorPermissionsBySpatialUnit(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            PermissionLevelInputType indicatorData) {
        LOG.info("Received request to update indicator roles for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        try {
            indicatorId = indicatorsManager.updateIndicatorPermissions(indicatorData, indicatorId, spatialUnitId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'creator')")
    public ResponseEntity updateIndicatorPermissions(
            @P("indicatorId") String indicatorId,
            PermissionLevelInputType indicatorData) {
        LOG.info("Received request to update indicator roles for indicatorId '{}'", indicatorId);
        try {
            indicatorId = indicatorsManager.updateIndicatorPermissions(indicatorData, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'creator')")
    public ResponseEntity<Void> updateIndicatorOwnership(
            @P("indicatorId")String indicatorId,
            OwnerInputType indicatorData) {
        try {
            indicatorId = indicatorsManager.updateOwnership(indicatorData, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'creator')")
    public ResponseEntity<Void> updateIndicatorOwnershipBySpatialUnit(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            OwnerInputType indicatorData) {
        LOG.info("Received request to update indicator ownership for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        try {
            indicatorId = indicatorsManager.updateOwnership(indicatorData, indicatorId, spatialUnitId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
	public ResponseEntity<Void> updateIndicatorFeatureRecordAsBody(
            @P("indicatorId") String indicatorId,
            String spatialUnitId,
            String featureId,
            String featureRecordId,
            IndicatorPropertiesWithoutGeomType indicatorFeatureRecordData) {
		LOG.info("Received request to update single indicator feature database record for indicatorId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId, featureId, featureRecordId);

        try {
            indicatorId = indicatorsManager.updateFeatureRecordByRecordId(indicatorFeatureRecordData, indicatorId, spatialUnitId, featureId, featureRecordId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            try {
                responseHeaders.setLocation(new URI(indicatorId));
            } catch (URISyntaxException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getIndicatorBySpatialUnitIdAndIdAndYearAndMonthWithoutGeometry(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId,
            BigDecimal year,
            BigDecimal month,
            BigDecimal day) {
        LOG.info(
                "Received request to get indicators feature properties without geometries for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties =
                    indicatorsManager.getValidIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, year,
                            month, day, provider);
            return new ResponseEntity<>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getIndicatorBySpatialUnitIdAndIdWithoutGeometry(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId) {
        LOG.info("Received request to get indicator feature properties for spatialUnitId '{}' and Id '{}' (without geometries)",
                spatialUnitId, indicatorId);

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager.getIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, provider);
            return new ResponseEntity<>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }
    
    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getSingleIndicatorFeatureById(
			@P("indicatorId") String indicatorId,
			@P("spatialUnitId") String spatialUnitId,
			String featureId,
			String simplifyGeometries) {

		LOG.info("Received request to get single indicator feature database records for indicatorId '{}' and spatialUnitId '{}' and featureId '{}'",
                indicatorId, spatialUnitId, featureId);

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        try {
        	List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecords(indicatorId, spatialUnitId, featureId, provider);
			return new ResponseEntity<>(indicatorFeatureProperties,
                    HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
	}

    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getSingleIndicatorFeatureRecordById(
			@P("indicatorId") String indicatorId,
			@P("spatialUnitId") String spatialUnitId,
			String featureId,
			String featureRecordId,
			String simplifyGeometries) {

		LOG.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'",
				indicatorId, spatialUnitId, featureId, featureRecordId);
		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecord(indicatorId, spatialUnitId, featureId, featureRecordId, provider);
			return new ResponseEntity<>(indicatorFeatureProperties,
                    HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

}
