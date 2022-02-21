package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.IndicatorsPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathPublicController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPropertiesWithoutGeomType;
import io.swagger.annotations.ApiParam;

@Controller
public class IndicatorsPublicController extends BasePathPublicController implements IndicatorsPublicApi {

    private static Logger logger = LoggerFactory.getLogger(IndicatorsPublicController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    IndicatorsManager indicatorsManager;

    @Autowired
    public IndicatorsPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<byte[]> getPublicIndicatorBySpatialUnitIdAndId(@PathVariable("indicatorId") String indicatorId,
                                                                         @PathVariable("spatialUnitId") String spatialUnitId,
                                                                         @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries) {
        logger.info("Received request to get public indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        try {
            String geoJsonFeatures = indicatorsManager.getIndicatorFeatures(indicatorId, spatialUnitId, simplifyGeometries);
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
    public ResponseEntity<byte[]> getPublicIndicatorBySpatialUnitIdAndIdAndYearAndMonth(@PathVariable("indicatorId") String indicatorId,
                                                                                        @PathVariable("spatialUnitId") String spatialUnitId,
                                                                                        @PathVariable("year") BigDecimal year,
                                                                                        @PathVariable("month") BigDecimal month,
                                                                                        @PathVariable("day") BigDecimal day,
                                                                                        @RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries) {
        logger.info(
                "Received request to get public indicators features for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        try {
            String geoJsonFeatures = indicatorsManager.getValidIndicatorFeatures(indicatorId, spatialUnitId, year,
                    month, day, simplifyGeometries);
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
    public ResponseEntity<List<IndicatorOverviewType>> getPublicIndicators() {
        logger.info("Received request to get all public indicators metadata");
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                List<IndicatorOverviewType> spatialunitsMetadata = indicatorsManager.getAllIndicatorsMetadata();
                return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<IndicatorOverviewType> getPublicIndicatorById(@PathVariable("indicatorId") String indicatorId) {
        logger.info("Received request to get public indicator metadata for indicatorId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                IndicatorOverviewType indicatorMetadata = indicatorsManager.getIndicatorById(indicatorId);
                return new ResponseEntity<>(indicatorMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicIndicatorBySpatialUnitIdAndIdAndYearAndMonthWithoutGeometry(
            @PathVariable("indicatorId") String indicatorId,
            @PathVariable("spatialUnitId") String spatialUnitId,
            @PathVariable("year") BigDecimal year,
            @PathVariable("month") BigDecimal month,
            @PathVariable("day") BigDecimal day) {
        logger.info(
                "Received request to get public indicators feature properties without geometries for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties =
                    indicatorsManager.getValidIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, year,
                            month, day);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicIndicatorBySpatialUnitIdAndIdWithoutGeometry(
            @PathVariable("indicatorId") String indicatorId,
            @PathVariable("spatialUnitId") String spatialUnitId) {
        logger.info("Received request to get public indicator feature properties for spatialUnitId '{}' and Id '{}' (without geometries)",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager.getIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }
    
	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicSingleIndicatorFeatureById(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset spatial feature", required = true) @PathVariable("featureId") String featureId) {

		logger.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}'",
				indicatorId, spatialUnitId, featureId);
		String accept = request.getHeader("Accept");

		try {
			List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecords(indicatorId, spatialUnitId, featureId);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
					HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicSingleIndicatorFeatureRecordById(
			@ApiParam(value = "unique identifier of the selected indicator dataset", required = true) @PathVariable("indicatorId") String indicatorId,
			@ApiParam(value = "the unique identifier of the spatial level", required = true) @PathVariable("spatialUnitId") String spatialUnitId,
			@ApiParam(value = "the identifier of the indicator dataset spatial feature", required = true) @PathVariable("featureId") String featureId,
			@ApiParam(value = "the unique database record identifier of the indicator dataset feature - multiple records may exist for the same real world object if they apply to different periods of validity", required = true) @PathVariable("featureRecordId") String featureRecordId) {

		logger.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'",
				indicatorId, spatialUnitId, featureId, featureRecordId);
		String accept = request.getHeader("Accept");

		try {
			List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecord(indicatorId, spatialUnitId, featureId, featureRecordId);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
					HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

}
