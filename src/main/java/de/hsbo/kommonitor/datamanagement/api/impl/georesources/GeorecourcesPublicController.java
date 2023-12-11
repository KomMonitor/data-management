package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
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

import de.hsbo.kommonitor.datamanagement.api.GeoresourcesPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathPublicController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.GeoresourceOverviewType;

@Controller
public class GeorecourcesPublicController extends BasePathPublicController implements GeoresourcesPublicApi {

	private static Logger logger = LoggerFactory.getLogger(GeorecourcesPublicController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	GeoresourcesManager georesourcesManager;

	@Autowired
	public GeorecourcesPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity<List<GeoresourceOverviewType>> getPublicGeoresources() {
		logger.info("Received request to get all public georesources metadata");

		String accept = request.getHeader("Accept");
		try {
			if (accept != null && accept.contains("application/json")) {
				List<GeoresourceOverviewType> georesourcesMetadata = georesourcesManager.getAllGeoresourcesMetadata();
				return new ResponseEntity<>(georesourcesMetadata, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<GeoresourceOverviewType> getPublicGeoresourceById(
			@PathVariable("georesourceId") String georesourceId) {
		logger.info("Received request to get public georesource metadata for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");
		try {
			if (accept != null && accept.contains("application/json")) {
				GeoresourceOverviewType georesourceMetadata = georesourcesManager
						.getGeoresourceByDatasetId(georesourceId);
				return new ResponseEntity<>(georesourceMetadata, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getAllPublicGeoresourceFeaturesById(
			@PathVariable("georesourceId") String georesourceId,
			@RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries) {
		logger.info(
				"Received request to get all public georesource features for datasetId '{}' and simplifyGeometries parameter '{}'",
				georesourceId, simplifyGeometries);

		try {
			String geoJsonFeatures = georesourcesManager.getAllGeoresourceFeatures(georesourceId, simplifyGeometries);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

			return createGeoresourceFeatureResponse(fileName, geoJsonFeatures);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getPublicGeoresourceByIdAndYearAndMonth(
			@PathVariable("georesourceId") String georesourceId, @PathVariable("year") BigDecimal year,
			@PathVariable("month") BigDecimal month, @PathVariable("day") BigDecimal day,
			@RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries) {
		logger.info(
				"Received request to get public georesource features for datasetId '{}' and simplifyGeometries parameter '{}'",
				georesourceId, simplifyGeometries);

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures(georesourceId, year, month, day,
					simplifyGeometries);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day + ".json";

			return createGeoresourceFeatureResponse(fileName, geoJsonFeatures);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<String> getPublicGeoresourceSchemaByLevel(
			@PathVariable("georesourceId") String georesourceId) {
		logger.info("Received request to get public georesource metadata for datasetId '{}'", georesourceId);


		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			String jsonSchema;
			try {
				jsonSchema = georesourcesManager.getJsonSchemaForDatasetName(georesourceId);
			} catch (Exception e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
			return new ResponseEntity<>(jsonSchema, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<byte[]> getAllPublicGeoresourceFeaturesByIdWithoutGeometry(
			@PathVariable("georesourceId") String georesourceId) {
		logger.info("Received request to get all public georesource features for datasetId '{}' without geometry",
				georesourceId);

		try {
			String geoJsonFeatures = georesourcesManager.getAllGeoresourceFeatures_withoutGeometry(georesourceId);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all_withoutGeometry.json";

			return createGeoresourceFeatureResponse(fileName, geoJsonFeatures);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getPublicGeoresourceByIdAndYearAndMonthWithoutGeometry(
			@PathVariable("georesourceId") String georesourceId,
			@PathVariable("year") BigDecimal year,
			@PathVariable("month") BigDecimal month,
			@PathVariable("day") BigDecimal day
			) {
		logger.info("Received request to get public georesource features for datasetId '{}' without geometry",
				georesourceId);

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures_withoutGeometry(georesourceId,
					year, month, day);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day
					+ "_withoutGeometry.json";

			return createGeoresourceFeatureResponse(fileName, geoJsonFeatures);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	public ResponseEntity<byte[]> getPublicSingleGeoresourceFeatureById(
			@PathVariable("georesourceId") String georesourceId,
			@PathVariable("featureId") String featureId,
			@RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries) {
		logger.info(
				"Received request to get public single georesource feature records for datasetId '{}' and featureId '{}'",
				georesourceId, featureId);

		try {
			String geoJsonFeatures = georesourcesManager.getSingleGeoresourceFeatureRecords(georesourceId, featureId,
					simplifyGeometries);
			String fileName = "Georesource_" + georesourceId + "_featureDatabaseRecords_" + featureId + ".json";

			return createGeoresourceFeatureResponse(fileName, geoJsonFeatures);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	public ResponseEntity<byte[]> getPublicSingleGeoresourceFeatureRecordById(
			@PathVariable("georesourceId") String georesourceId,
			@PathVariable("featureId") String featureId,
			@PathVariable("featureRecordId") String featureRecordId,
			@RequestParam(value = "simplifyGeometries", required = false, defaultValue = "original") String simplifyGeometries) {
		logger.info(
				"Received request to get public single georesource feature record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		try {
			String geoJsonFeatures = georesourcesManager.getSingleGeoresourceFeatureRecord(georesourceId, featureId,
					featureRecordId, simplifyGeometries);
			String fileName = "Georesource_" + georesourceId + "_featureDatabaseRecord_" + featureRecordId + ".json";

			return createGeoresourceFeatureResponse(fileName, geoJsonFeatures);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	private ResponseEntity<byte[]> createGeoresourceFeatureResponse(String fileName, String geoJsonFeatures) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "attachment; filename=" + fileName);
		headers.add("Content-Type", "application/json; charset=utf-8");
		byte[] JsonBytes = geoJsonFeatures.getBytes();

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.geo+json"))
				.body(JsonBytes);
	}
}
