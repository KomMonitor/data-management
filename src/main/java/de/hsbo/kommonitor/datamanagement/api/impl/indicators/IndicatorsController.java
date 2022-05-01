package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.IndicatorsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorMetadataPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPATCHDisplayOrderInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPropertiesWithoutGeomType;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import io.swagger.annotations.ApiParam;

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
    public ResponseEntity deleteIndicatorByIdAndSpatialUnitId(@PathVariable("indicatorId") String indicatorId, @PathVariable("spatialUnitId") String spatialUnitId) throws Exception {
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

        } catch (ResourceNotFoundException | IOException e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'editor')")
    public ResponseEntity deleteIndicatorByIdAndYearAndMonth(@PathVariable("indicatorId") String indicatorId, @PathVariable("spatialUnitId") String spatialUnitId,
                                                             @PathVariable("year") BigDecimal year, @PathVariable("month") BigDecimal month,
                                                             @PathVariable("day") BigDecimal day) throws Exception {
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

        } catch (ResourceNotFoundException | IOException e) {
            return ApiUtils.createResponseEntityFromException(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
	public ResponseEntity<ResponseEntity> deleteSingleIndicatorFeatureById(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset spatial feature", required = true) @PathVariable("featureId") String featureId) {
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

    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
	public ResponseEntity<ResponseEntity> deleteSingleIndicatorFeatureRecordById(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset feature", required = true) @PathVariable("featureId") String featureId,
			@ApiParam(value = "the unique database record identifier of the indicator dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity", required = true) @PathVariable("featureRecordId") String featureRecordId) {
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
    @PreAuthorize("hasRequiredPermissionLevel('publisher')")
    public ResponseEntity<IndicatorOverviewType> addIndicatorAsBody(@RequestBody IndicatorPOSTInputType indicatorData) {
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
    public ResponseEntity deleteIndicatorById(@PathVariable("indicatorId") String indicatorId) {
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
    public ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndId(@PathVariable("indicatorId") String indicatorId,
                                                                   @PathVariable("spatialUnitId") String spatialUnitId,
                                                                   @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries,
                                                                   Principal principal) {
        logger.info("Received request to get indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
    public ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndIdAndYearAndMonth(@PathVariable("indicatorId") String indicatorId,
                                                                                  @PathVariable("spatialUnitId") String spatialUnitId,
                                                                                  @PathVariable("year") BigDecimal year,
                                                                                  @PathVariable("month") BigDecimal month,
                                                                                  @PathVariable("day") BigDecimal day,
                                                                                  @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries,
                                                                                  Principal principal) {
        logger.info(
                "Received request to get indicators features for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
    public ResponseEntity<List<IndicatorOverviewType>> getIndicators(Principal principal) {
        logger.info("Received request to get all indicators metadata");
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
    public ResponseEntity<IndicatorOverviewType> getIndicatorById(@PathVariable("indicatorId") String indicatorId, Principal principal) {
        logger.info("Received request to get indicator metadata for indicatorId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
            @PathVariable("indicatorId") String indicatorId, Principal principal) {
        logger.info("Received request to list permissions for indicator with datasetId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
            @PathVariable("indicatorId") String indicatorId,
            @PathVariable("spatialUnitId") String spatialUnitId, Principal principal) {
        logger.info("Received request to list permissions for spatialUnit Id {} and indicator with datasetId '{}'",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
    public ResponseEntity updateIndicatorAsBody(@PathVariable("indicatorId") String indicatorId, @RequestBody IndicatorPUTInputType indicatorData) {
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
    public ResponseEntity updateIndicatorMetadataAsBody(@PathVariable("indicatorId") String indicatorId, @RequestBody IndicatorMetadataPATCHInputType metadata) {
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

    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
    public ResponseEntity<ResponseEntity> updateIndicatorDisplayOrder(@ApiParam(value = "array of indicator id and displayOrder items" ,required=true )  @RequestBody List<IndicatorPATCHDisplayOrderInputType> indicatorOrderArray) {
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
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
    public ResponseEntity updateIndicatorRoles(@PathVariable("indicatorId") String indicatorId,
                                               @PathVariable("spatialUnitId") String spatialUnitId,
                                               @RequestBody IndicatorPATCHInputType indicatorData) {
        logger.info("Received request to update indicator roles for indicatorId '{}' and spatialUnitId '{}'", indicatorId, spatialUnitId);

        String accept = request.getHeader("Accept");

        try {
            indicatorId = indicatorsManager.updateIndicatorRoles(indicatorData, indicatorId, spatialUnitId);
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
    
    @PreAuthorize("isAuthorizedForEntity(#indicatorId, 'indicator', 'editor')")
	public ResponseEntity<ResponseEntity> updateIndicatorFeatureRecordAsBody(
			@ApiParam(value = "indicator feature record data", required = true) @RequestBody IndicatorPropertiesWithoutGeomType indicatorFeatureRecordData,
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset feature", required = true) @PathVariable("featureId") String featureId,
			@ApiParam(value = "the unique database record identifier of the indicator dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity", required = true) @PathVariable("featureRecordId") String featureRecordId) {
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
            @PathVariable("indicatorId") String indicatorId,
            @PathVariable("spatialUnitId") String spatialUnitId,
            @PathVariable("year") BigDecimal year,
            @PathVariable("month") BigDecimal month,
            @PathVariable("day") BigDecimal day,
            Principal principal) {
        logger.info(
                "Received request to get indicators feature properties without geometries for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
            @PathVariable("indicatorId") String indicatorId,
            @PathVariable("spatialUnitId") String spatialUnitId,
            Principal principal) {
        logger.info("Received request to get indicator feature properties for spatialUnitId '{}' and Id '{}' (without geometries)",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);
        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager.getIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, provider);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }
    
    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getSingleIndicatorFeatureById(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset spatial feature", required = true) @PathVariable("featureId") String featureId,
			@ApiParam(value = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", allowableValues = "original, weak, medium, strong", defaultValue = "original") @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries,
			Principal principal) {

		logger.info("Received request to get single indicator feature database records for indicatorId '{}' and spatialUnitId '{}' and featureId '{}'",
                indicatorId, spatialUnitId, featureId);
        String accept = request.getHeader("Accept");

        AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);
        try {
        	List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecords(indicatorId, spatialUnitId, featureId, provider);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
					HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
	}

    @PreAuthorize("isAuthorizedForJoinedEntity(#indicatorId, #spatialUnitId, 'indicator_spatialunit', 'viewer')")
	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getSingleIndicatorFeatureRecordById(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset spatial feature", required = true) @PathVariable("featureId") String featureId,
			@ApiParam(value = "the unique database record identifier of the indicator dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity", required = true) @PathVariable("featureRecordId") String featureRecordId,
			@ApiParam(value = "Controls simplification of feature geometries. Each option will preserve topology to neighbour features. Simplification increases from 'weak' to 'strong', while 'original' will return original feature geometries without any simplification.", allowableValues = "original, weak, medium, strong", defaultValue = "original") @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries,
			Principal principal) {

		logger.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'",
				indicatorId, spatialUnitId, featureId, featureRecordId);
		String accept = request.getHeader("Accept");
		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

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
