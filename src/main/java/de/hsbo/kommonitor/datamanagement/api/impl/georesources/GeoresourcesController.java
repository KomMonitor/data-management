package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.GeoresourcesApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
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
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class GeoresourcesController extends BasePathController implements GeoresourcesApi {

	private static Logger LOG = LoggerFactory.getLogger(GeoresourcesController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	private LastModificationManager lastModManager;

	@Autowired
	GeoresourcesManager georesourcesManager;

	@Autowired
	AuthInfoProviderFactory authInfoProviderFactory;

	@Autowired
	private ExportManager exportManager;

	@org.springframework.beans.factory.annotation.Autowired
	public GeoresourcesController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'resources')")
	public ResponseEntity<GeoresourceOverviewType> addGeoresourceAsBody(GeoresourcePOSTInputType featureData) {
		LOG.info("Received request to insert new georesource");

		/*
		 * analyse input data and save it within database
		 */
		GeoresourceOverviewType georesourceMetadata;
		try {
			georesourceMetadata = georesourcesManager.addGeoresource(featureData);
			lastModManager.updateLastDatabaseModificationGeoresources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (georesourceMetadata != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = georesourceMetadata.getGeoresourceId();
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<GeoresourceOverviewType>(georesourceMetadata, responseHeaders,
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> deleteAllGeoresourceFeaturesById(@P("georesourceId") String georesourceId) {
		LOG.info("Received request to delete all georesource features for datasetId '{}'", georesourceId);

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteAllGeoresourceFeaturesById(georesourceId);
			lastModManager.updateLastDatabaseModificationGeoresources();

			if (isDeleted) {
				return new ResponseEntity<>(HttpStatus.OK);
			}

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> deleteSingleGeoresourceFeatureById(
			@P("georesourceId") String georesourceId,
			String featureId) {
		LOG.info(
				"Received request to delete single georesource feature databse records for datasetId '{}' and featureId '{}'",
				georesourceId, featureId);

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteSingleGeoresourceFeatureRecordsByFeatureId(georesourceId, featureId);
			lastModManager.updateLastDatabaseModificationGeoresources();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> deleteSingleGeoresourceFeatureRecordById(
			@P("georesourceId") String georesourceId,
			String featureId,
			String featureRecordId) {
		LOG.info(
				"Received request to delete single georesource feature databse record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteSingleGeoresourceFeatureRecordByRecordId(georesourceId, featureId,
					featureRecordId);
			lastModManager.updateLastDatabaseModificationGeoresources();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<Resource> exportAllGeoresourceFeaturesById(@P("georesourceId") String georesourceId, String format) {
		LOG.info(
				"Received request to export all georesource features for datasetId '{}' in format '{}'",
				georesourceId, format);
		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
		DataStore dataStore = null;
		try {
			dataStore = DatabaseHelperUtil.getPostGisDataStore();

			SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) georesourcesManager
					.getGeoresourceFeatureCollection(
							georesourceId,
							SimplifyGeometriesEnum.ORIGINAL.toString(),
							provider,
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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<Resource> exportGeoresourceByIdAndYearAndMonth(@P("georesourceId") String georesourceId, BigDecimal year, BigDecimal month, BigDecimal day, String format) {
		LOG.info(
				"Received request to export georesource features for datasetId '{}', date '{}-{}-{}' and format '{}'", georesourceId, year, month, day, format);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
		DataStore dataStore = null;

		try {
			dataStore = DatabaseHelperUtil.getPostGisDataStore();

			SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) georesourcesManager.getValidGeoresourceFeatureCollection(
					georesourceId,
					year,
					month,
					day,
					SimplifyGeometriesEnum.ORIGINAL.toString(),
					provider,
					dataStore);
			if (featureCollection.isEmpty()) {
				throw new Exception(String.format("No valid features could be retrieved for georesource %s for date %s-%s-%s.", georesourceId, year, month, day));
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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'creator')")
	public ResponseEntity<Void> deleteGeoresourceById(@P("georesourceId") String georesourceId) {
		LOG.info("Received request to delete georesource for datasetId '{}'", georesourceId);

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteGeoresourceDatasetById(georesourceId);
			lastModManager.updateLastDatabaseModificationGeoresources();

			if (isDeleted) {
				return new ResponseEntity<>(HttpStatus.OK);
			}

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> deleteGeoresourceByIdAndYearAndMonth(
			@P("georesourceId") String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day) {
		LOG.info("Received request to delete georesource for datasetId '{}' and Date '{}-{}-{}'", georesourceId,
				year, month, day);

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteGeoresourceFeaturesByIdAndDate(georesourceId, year, month, day);
			lastModManager.updateLastDatabaseModificationGeoresources();

			if (isDeleted) {
				return new ResponseEntity<>(HttpStatus.OK);
			}

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<List<GeoresourceOverviewType>> getGeoresources() {
		LOG.info("Received request to get all georesources metadata");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
		String accept = request.getHeader("Accept");
		try {
			if (accept != null && accept.contains("application/json")) {
				List<GeoresourceOverviewType> georesourcesMetadata = georesourcesManager
						.getAllGeoresourcesMetadata(provider);
				return new ResponseEntity<>(georesourcesMetadata, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<List<GeoresourceOverviewType>> filterGeoresources(ResourceFilterType resourceFilterType) {
		LOG.info("Received request to get filtered georesources metadata");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
		String accept = request.getHeader("Accept");
		try {
			if (accept != null && accept.contains("application/json")) {
				List<GeoresourceOverviewType> georesourcesMetadata = georesourcesManager
						.filterGeoresourcesMetadata(provider, resourceFilterType);
				return new ResponseEntity<>(georesourcesMetadata, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<GeoresourceOverviewType> getGeoresourceById(@P("georesourceId") String georesourceId) {
		LOG.info("Received request to get georesource metadata for datasetId '{}' test", georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			if (accept != null && accept.contains("application/json")) {
				GeoresourceOverviewType georesourceMetadata = georesourcesManager
						.getGeoresourceByDatasetId(georesourceId, provider);

				return new ResponseEntity<>(georesourceMetadata, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<List<PermissionLevelType>> getGeoresourcePermissionsById(String georesourceId) {
		LOG.info("Received request to list access rights for georesource with datasetId '{}'", georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			if (accept != null && accept.contains("application/json")) {
				List<PermissionLevelType> permissions = georesourcesManager
						.getGeoresourcePermissionsByDatasetId(georesourceId, provider);

				return new ResponseEntity<>(permissions, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'creator')")
	public ResponseEntity updateGeoresourcePermissions(
			@P("georesourceId") String georesourceId,
	 		PermissionLevelInputType permissionLevelInputType) {
		LOG.info("Received request to update georesource permissions for georesourceId '{}'.", georesourceId);
		try {   
			georesourceId = georesourcesManager.updatePermissions(permissionLevelInputType, georesourceId);
			lastModManager.updateLastDatabaseModificationGeoresources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}
		return createUpdateSuccessResponse(georesourceId);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'creator')")
	public ResponseEntity<Void> updateGeoresourceOwnership(
			@P("georesourceId") String georesourceId,
			OwnerInputType ownerInputType) {
		LOG.info("Received request to update georesource ownership for georesourceId '{}'.", georesourceId);
		try {
			georesourceId = georesourcesManager.updateOwnership(ownerInputType, georesourceId);
			lastModManager.updateLastDatabaseModificationGeoresources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}
		return createUpdateSuccessResponse(georesourceId);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getAllGeoresourceFeaturesById(
			@P("georesourceId") String georesourceId,
			String simplifyGeometries) {
		LOG.info(
				"Received request to get all georesource features for datasetId '{}' and simplifyGeometries parameter '{}'",
				georesourceId, simplifyGeometries);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getAllGeoresourceFeatures(georesourceId, simplifyGeometries,
					provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

			return createFeatureResponse(geoJsonFeatures, fileName);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonth(
			@P("georesourceId") String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day,
			String simplifyGeometries) {
		LOG.info(
				"Received request to get georesource features for datasetId '{}' and simplifyGeometries parameter '{}'",
				georesourceId, simplifyGeometries);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures(georesourceId, year, month, day,
					simplifyGeometries, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day + ".json";

			return createFeatureResponse(geoJsonFeatures, fileName);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<String> getGeoresourceSchemaByLevel(@P("georesourceId") String georesourceId) {
		LOG.info("Received request to get georesource metadata for datasetId '{}'", georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		if (accept != null && accept.contains("application/json")) {

			String jsonSchema;
			try {
				jsonSchema = georesourcesManager.getJsonSchemaForDatasetName(georesourceId, provider);
			} catch (Exception e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(jsonSchema, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getAllGeoresourceFeaturesByIdWithoutGeometry(@P("georesourceId") String georesourceId) {
		LOG.info("Received request to get all georesource features for datasetId '{}' without geometry",
				georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getAllGeoresourceFeatures_withoutGeometry(georesourceId,
					provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all_withoutGeometry.json";

			return createFeatureResponse(geoJsonFeatures, fileName);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getSingleGeoresourceFeatureById(
			@P("georesourceId") String georesourceId,
			String featureId,
			String simplifyGeometries) {
		LOG.info(
				"Received request to get public single georesource feature records for datasetId '{}' and featureId '{}'",
				georesourceId, featureId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getSingleGeoresourceFeatureRecords(georesourceId, featureId,
					simplifyGeometries, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

			return createFeatureResponse(geoJsonFeatures, fileName);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getSingleGeoresourceFeatureRecordById(
			@P("georesourceId") String georesourceId,
			String featureId,
			String featureRecordId,
			String simplifyGeometries) {
		LOG.info(
				"Received request to get public single georesource feature record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getSingleGeoresourceFeatureRecord(georesourceId, featureId,
					featureRecordId, simplifyGeometries, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

			return createFeatureResponse(geoJsonFeatures, fileName);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonthWithoutGeometry(
			@P("georesourceId") String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day
			) {
		LOG.info("Received request to get georesource features for datasetId '{}' without geometry", georesourceId);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures_withoutGeometry(georesourceId,
					year, month, day, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day
					+ "_withoutGeometry.json";

			return createFeatureResponse(geoJsonFeatures, fileName);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> updateGeoresourceAsBody(
			@P("georesourceId") String georesourceId,
			GeoresourcePUTInputType featureData) {
		LOG.info("Received request to update georesource features for datasetId '{}'", georesourceId);

		try {
			georesourceId = georesourcesManager.updateFeatures(featureData, georesourceId);
			lastModManager.updateLastDatabaseModificationGeoresources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}

		return createUpdateSuccessResponse(georesourceId);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> updateGeoresourceMetadataAsBody(
			@P("georesourceId") String georesourceId,
			GeoresourcePATCHInputType metadata) {
		LOG.info("Received request to update georesource metadata for datasetId '{}'", georesourceId);

		try {
			georesourceId = georesourcesManager.updateMetadata(metadata, georesourceId);
			lastModManager.updateLastDatabaseModificationGeoresources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}

		return createUpdateSuccessResponse(georesourceId);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> updateGeoresourceFeatureRecordAsBody(
			@P("georesourceId") String georesourceId,
			String featureId,
			String featureRecordId,
			String georesourceFeatureRecordData) {
		LOG.info(
				"Received request to update single georesource feature database record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		try {
			georesourceId = georesourcesManager.updateFeatureRecordByRecordId(georesourceFeatureRecordData,
					georesourceId, featureId, featureRecordId);
			lastModManager.updateLastDatabaseModificationGeoresources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}

		return createUpdateSuccessResponse(georesourceId);
	}

	private ResponseEntity<Void> createUpdateSuccessResponse(String georesourceId) {
		if (georesourceId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			try {
				responseHeaders.setLocation(new URI(georesourceId));
			} catch (URISyntaxException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}
			return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<byte[]> createFeatureResponse(String geoJsonFeatures, String fileName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-disposition", "attachment; filename=" + fileName);
		headers.add("Content-Type", "application/json; charset=utf-8");
		byte[] JsonBytes = geoJsonFeatures.getBytes();

		return ResponseEntity.ok().headers(headers)
				.contentType(MediaType.parseMediaType("application/vnd.geo+json")).body(JsonBytes);
	}

}
