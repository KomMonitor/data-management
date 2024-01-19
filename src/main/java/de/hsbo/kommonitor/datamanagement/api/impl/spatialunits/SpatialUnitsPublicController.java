package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.SpatialUnitsPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitOverviewType;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SpatialUnitsPublicController extends BasePathController implements SpatialUnitsPublicApi {

    private static Logger logger = LoggerFactory.getLogger(SpatialUnitsPublicController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    SpatialUnitsManager spatialUnitsManager;

    @Autowired
    AuthInfoProviderFactory authInfoProviderFactory;

    @Autowired
    public SpatialUnitsPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<SpatialUnitOverviewType>> getPublicSpatialUnits() {
        logger.info("Received request to get all public spatialUnits metadata");
        String accept = request.getHeader("Accept");

        try {
            List<SpatialUnitOverviewType> spatialunitsMetadata = spatialUnitsManager.getAllSpatialUnitsMetadata();

            return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<SpatialUnitOverviewType> getPublicSpatialUnitsById(String spatialUnitId) {
        logger.info("Received request to get public spatialUnit metadata for datasetId '{}'", spatialUnitId);
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                SpatialUnitOverviewType spatialUnitMetadata = spatialUnitsManager.getSpatialUnitByDatasetId(spatialUnitId);

                return new ResponseEntity<>(spatialUnitMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<byte[]> getAllPublicSpatialUnitFeaturesById
            (String spatialUnitId,
             String simplifyGeometries) {
        logger.info("Received request to get public spatialUnit features for datasetId '{}' and simplifyGeometries parameter '{}'", spatialUnitId, simplifyGeometries);
        String accept = request.getHeader("Accept");

        try {
            String geoJsonFeatures = spatialUnitsManager.getAllSpatialUnitFeatures(spatialUnitId, simplifyGeometries);
            String fileName = "SpatialUnitFeatures_" + spatialUnitId + "_all.json";

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
    public ResponseEntity<byte[]> getPublicSpatialUnitsByIdAndYearAndMonth(
            String spatialUnitId,
            BigDecimal year,
            BigDecimal month,
            BigDecimal day,
            String simplifyGeometries) {
        logger.info("Received request to get public spatialUnit features for datasetId '{}' and simplifyGeometries parameter '{}'", spatialUnitId, simplifyGeometries);
        String accept = request.getHeader("Accept");

        try {
            String geoJsonFeatures = spatialUnitsManager.getValidSpatialUnitFeatures(spatialUnitId, year, month,
                    day, simplifyGeometries);
            String fileName = "SpatialUnitFeatures_" + spatialUnitId + "_" + year + "-" + month + "-" + day
                    + ".json";

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

    public ResponseEntity<String> getPublicSpatialUnitsSchemaById(String spatialUnitId) {
        logger.info("Received request to get public spatialUnit metadata for datasetName '{}'", spatialUnitId);
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {

			String jsonSchema = null;
            try {
                jsonSchema = spatialUnitsManager.getJsonSchemaForDatasetId(spatialUnitId);
            } catch (ResourceNotFoundException e) {
                return ApiUtils.createResponseEntityFromException(e);
            }

            return new ResponseEntity<>(jsonSchema, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
	public ResponseEntity<byte[]> getPublicSingleSpatialUnitFeatureById(
            String spatialUnitId,
			String featureId,
			String simplifyGeometries) {
		logger.info(
				"Received request to get public single spatial unit feature records for datasetId '{}' and featureId '{}'",
				spatialUnitId, featureId);

		try {
			String geoJsonFeatures = spatialUnitsManager.getSingleSpatialUnitFeatureRecords(spatialUnitId, featureId,
					simplifyGeometries);
			String fileName = "SpatialUnit_" + spatialUnitId + "_featureDatabaseRecords_" + featureId + ".json";
			
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

	public ResponseEntity<byte[]> getPublicSingleSpatialUnitFeatureRecordById(
			String spatialUnitId,
			String featureId,
			String featureRecordId,
			String simplifyGeometries) {
		logger.info(
				"Received request to get public single georesource feature record for datasetId '{}' and featureId '{}' and recordId '{}'",
				spatialUnitId, featureId, featureRecordId);

		try {
			String geoJsonFeatures = spatialUnitsManager.getSingleSpatialUnitFeatureRecord(spatialUnitId, featureId,
					featureRecordId, simplifyGeometries);
			String fileName = "SpatialUnit_" + spatialUnitId + "_featureDatabaseRecord_" + featureRecordId + ".json";

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
}
