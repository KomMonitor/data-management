package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.IndicatorsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class IndicatorsController extends BasePathController implements IndicatorsApi {

    private static Logger logger = LoggerFactory.getLogger(IndicatorsController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    IndicatorsManager indicatorsManager;
    
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
        logger.info("Received request to delete indicator for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);

        String accept = request.getHeader("Accept");

        /*
         * delete topic with the specified id
         */

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
        logger.info("Received request to delete indicator for indicatorId '{}' and Date '{}-{}-{}'", indicatorId, year, month, day);

        String accept = request.getHeader("Accept");

        /*
         * delete topic with the specified id
         */

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
		logger.info("Received request to delete single indicator feature databse records for indicatorId '{}' and spatialUnitId '{}' and featureId '{}'", indicatorId, spatialUnitId, featureId);

        String accept = request.getHeader("Accept");

        /*
         * delete topic with the specified id
         */

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
		logger.info("Received request to delete single indicator feature databse record for indicatorId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId, featureId, featureRecordId);

        String accept = request.getHeader("Accept");

        /*
         * delete topic with the specified id
         */

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
    @PreAuthorize("hasRequiredPermissionLevel('creator', 'resources')")
    public ResponseEntity<IndicatorOverviewType> addIndicatorAsBody(IndicatorPOSTInputType indicatorData) {
        logger.info("Received request to insert new indicator");

        String accept = request.getHeader("Accept");

        /*
         * analyse input data and save it within database
         */
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

            return new ResponseEntity<IndicatorOverviewType>(indicatorMetadata, responseHeaders, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'creator')")
    public ResponseEntity deleteIndicatorById(@P("indicatorId") String indicatorId) {
        logger.info("Received request to delete indicator for indicatorId '{}'", indicatorId);

        String accept = request.getHeader("Accept");

        /*
         * delete topic with the specified id
         */

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
            String spatialUnitId,
            String simplifyGeometries) {
        logger.info("Received request to get indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

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
        logger.info(
                "Received request to get indicators features for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

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
        logger.info("Received request to get all indicators metadata");
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
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'viewer')")
    public ResponseEntity<IndicatorOverviewType> getIndicatorById(@P("indicatorId") String indicatorId) {
        logger.info("Received request to get indicator metadata for indicatorId '{}'", indicatorId);
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
        logger.info("Received request to list permissions for indicator with datasetId '{}'", indicatorId);
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
        logger.info("Received request to list permissions for spatialUnit Id {} and indicator with datasetId '{}'",
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
        logger.info("Received request to update indicator features for indicator '{}'", indicatorId);

        String accept = request.getHeader("Accept");

        /*
         * analyse input data and save it within database
         */

        try {
            indicatorId = indicatorsManager.updateFeatures(indicatorData, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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
        logger.info("Received request to update indicator metadata for indicatorId '{}'", indicatorId);

        String accept = request.getHeader("Accept");

        /*
         * analyse input data and save it within database
         */

        try {
            indicatorId = indicatorsManager.updateMetadata(metadata, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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
    	logger.info("Received request to update indicator display order ");

        String accept = request.getHeader("Accept");

        /*
         * analyse input data and save it within database
         */
        
        boolean update = false;

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
        logger.info("Received request to update indicator roles for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        try {
            indicatorId = indicatorsManager.updateIndicatorPermissions(indicatorData, indicatorId, spatialUnitId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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
        logger.info("Received request to update indicator roles for indicatorId '{}'", indicatorId);
        try {
            indicatorId = indicatorsManager.updateIndicatorPermissions(indicatorData, indicatorId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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
        logger.info("Received request to update indicator ownership for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);
        try {
            indicatorId = indicatorsManager.updateOwnership(indicatorData, indicatorId, spatialUnitId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);
        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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
		logger.info("Received request to update single indicator feature database record for indicatorId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'", indicatorId, spatialUnitId, featureId, featureRecordId);

        String accept = request.getHeader("Accept");

        /*
         * analyse input data and save it within database
         */

        try {
            indicatorId = indicatorsManager.updateFeatureRecordByRecordId(indicatorFeatureRecordData, indicatorId, spatialUnitId, featureId, featureRecordId);
            lastModManager.updateLastDatabaseModification_indicators();
        } catch (Exception e1) {
            return ApiUtils.createResponseEntityFromException(e1);

        }

        if (indicatorId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = indicatorId;
            try {
                responseHeaders.setLocation(new URI(location));
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
        logger.info(
                "Received request to get indicators feature properties without geometries for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties =
                    indicatorsManager.getValidIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, year,
                            month, day, provider);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getIndicatorBySpatialUnitIdAndIdWithoutGeometry(
            @P("indicatorId") String indicatorId,
            @P("spatialUnitId") String spatialUnitId) {
        logger.info("Received request to get indicator feature properties for spatialUnitId '{}' and Id '{}' (without geometries)",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager.getIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, provider);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
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

		logger.info("Received request to get single indicator feature database records for indicatorId '{}' and spatialUnitId '{}' and featureId '{}'",
                indicatorId, spatialUnitId, featureId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
        try {
        	List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecords(indicatorId, spatialUnitId, featureId, provider);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
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

		logger.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'",
				indicatorId, spatialUnitId, featureId, featureRecordId);
		String accept = request.getHeader("Accept");
		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecord(indicatorId, spatialUnitId, featureId, featureRecordId, provider);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
					HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

}
