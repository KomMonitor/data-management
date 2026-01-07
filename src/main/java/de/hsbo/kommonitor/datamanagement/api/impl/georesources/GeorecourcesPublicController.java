package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.GeoresourcesPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.api.impl.util.SimplifyGeometriesEnum;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.export.ExportManager;
import de.hsbo.kommonitor.datamanagement.export.TempFileInputStream;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.model.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.ResourceFilterType;
import jakarta.servlet.http.HttpServletRequest;
import org.geotools.api.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class GeorecourcesPublicController extends BasePathController implements GeoresourcesPublicApi {

	private static final Logger LOG = LoggerFactory.getLogger(GeorecourcesPublicController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	GeoresourcesManager georesourcesManager;

	@Autowired
	private ExportManager exportManager;

	@Autowired
	public GeorecourcesPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity<List<GeoresourceOverviewType>> getPublicGeoresources() {
		LOG.info("Received request to get all public georesources metadata");

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
			String georesourceId) {
		LOG.info("Received request to get public georesource metadata for datasetId '{}'", georesourceId);

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
	public ResponseEntity<List<GeoresourceOverviewType>> filterPublicGeoresources(ResourceFilterType resourceFilterType) {
		LOG.info("Received request to get filtered public georesources metadata");

		String accept = request.getHeader("Accept");
		try {
			if (accept != null && accept.contains("application/json")) {
				List<GeoresourceOverviewType> georesourcesMetadata = georesourcesManager.filterGeoresourcesMetadata(resourceFilterType);
				return new ResponseEntity<>(georesourcesMetadata, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getAllPublicGeoresourceFeaturesById(
			String georesourceId,
			String simplifyGeometries) {
		LOG.info(
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
	public ResponseEntity<Resource> exportAllPublicGeoresourceFeaturesById(String georesourceId, String format) {
		LOG.info(
				"Received request to export all public georesource features for datasetId '{}' in format '{}'",
				georesourceId, format);
		DataStore dataStore = null;
		try {
			dataStore = DatabaseHelperUtil.getPostGisDataStore();

			SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) georesourcesManager
					.getGeoresourceFeatureCollection(
							georesourceId,
							SimplifyGeometriesEnum.ORIGINAL.toString(),
							dataStore
					);

			if (featureCollection.isEmpty()) {
				throw new Exception(String.format("No valid features could be retrieved for georesource %s.", georesourceId));
			}

			File exportFile = exportManager.exportFeatureCollection(featureCollection, format);

			dataStore.dispose();

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + georesourceId + "." + format);
			headers.add("Content-Type", "application/json; charset=utf-8");

			TempFileInputStream resourceStream = new TempFileInputStream(exportFile);
			InputStreamResource resource = new InputStreamResource(resourceStream);
			return ResponseEntity.ok()
					.headers(headers)
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(resource);

		} catch (Exception e) {
			if (dataStore != null) {
				dataStore.dispose();
			}
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<Resource> exportPublicGeoresourceByIdAndYearAndMonth(String georesourceId, BigDecimal year, BigDecimal month, BigDecimal day, String format) {
		LOG.info(
				"Received request to export public georesource features for datasetId '{}', date '{}-{}-{}' and format '{}'", georesourceId, year, month, day, format);
		DataStore dataStore = null;
		try {
			dataStore = DatabaseHelperUtil.getPostGisDataStore();

			SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) georesourcesManager.getValidGeoresourceFeatureCollection(
					georesourceId,
					year,
					month,
					day,
					SimplifyGeometriesEnum.ORIGINAL.toString(),
					dataStore);
			if (featureCollection.isEmpty()) {
				throw new Exception(String.format("No valid features could be retrieved for georesource %s for date %s-%s-%s..", georesourceId, year, month, day));
			}
			File exportFile = exportManager.exportFeatureCollection(featureCollection, format);

			dataStore.dispose();

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + georesourceId + "." + format);
			headers.add("Content-Type", "application/json; charset=utf-8");

			TempFileInputStream resourceStream = new TempFileInputStream(exportFile);
			InputStreamResource resource = new InputStreamResource(resourceStream);
			return ResponseEntity.ok()
					.headers(headers)
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(resource);

		} catch (Exception e) {
			if (dataStore != null) {
				dataStore.dispose();
			}
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getPublicGeoresourceByIdAndYearAndMonth(
			String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day,
			String simplifyGeometries) {
		LOG.info(
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
			String georesourceId) {
		LOG.info("Received request to get public georesource metadata for datasetId '{}'", georesourceId);


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
			String georesourceId) {
		LOG.info("Received request to get all public georesource features for datasetId '{}' without geometry",
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
			String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day
			) {
		LOG.info("Received request to get public georesource features for datasetId '{}' without geometry",
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
			String georesourceId,
			String featureId,
			String simplifyGeometries) {
		LOG.info(
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
			String georesourceId,
			String featureId,
			String featureRecordId,
			String simplifyGeometries) {
		LOG.info(
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
