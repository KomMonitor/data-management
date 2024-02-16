package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.GeoresourcesApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class GeoresourcesController extends BasePathController implements GeoresourcesApi {

	private static Logger logger = LoggerFactory.getLogger(GeoresourcesController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	private LastModificationManager lastModManager;

	@Autowired
	GeoresourcesManager georesourcesManager;

	@Autowired
	AuthInfoProviderFactory authInfoProviderFactory;

	@org.springframework.beans.factory.annotation.Autowired
	public GeoresourcesController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'resources')")
	public ResponseEntity<GeoresourceOverviewType> addGeoresourceAsBody(GeoresourcePOSTInputType featureData) {
		logger.info("Received request to insert new georesource");

		/*
		 * analyse input data and save it within database
		 */
		GeoresourceOverviewType georesourceMetadata;
		try {
			georesourceMetadata = georesourcesManager.addGeoresource(featureData);
			lastModManager.updateLastDatabaseModification_georesources();
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
		logger.info("Received request to delete all georesource features for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteAllGeoresourceFeaturesById(georesourceId);
			lastModManager.updateLastDatabaseModification_georesources();

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
		logger.info(
				"Received request to delete single georesource feature databse records for datasetId '{}' and featureId '{}'",
				georesourceId, featureId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteSingleGeoresourceFeatureRecordsByFeatureId(georesourceId, featureId);
			lastModManager.updateLastDatabaseModification_georesources();

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
		logger.info(
				"Received request to delete single georesource feature databse record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteSingleGeoresourceFeatureRecordByRecordId(georesourceId, featureId,
					featureRecordId);
			lastModManager.updateLastDatabaseModification_georesources();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'creator')")
	public ResponseEntity<Void> deleteGeoresourceById(@P("georesourceId") String georesourceId) {
		logger.info("Received request to delete georesource for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteGeoresourceDatasetById(georesourceId);
			lastModManager.updateLastDatabaseModification_georesources();

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
		logger.info("Received request to delete georesource for datasetId '{}' and Date '{}-{}-{}'", georesourceId,
				year, month, day);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteGeoresourceFeaturesByIdAndDate(georesourceId, year, month, day);
			lastModManager.updateLastDatabaseModification_georesources();

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
		logger.info("Received request to get all georesources metadata");

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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<GeoresourceOverviewType> getGeoresourceById(@P("georesourceId") String georesourceId) {
		logger.info("Received request to get georesource metadata for datasetId '{}' test", georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();
		/*
		 * retrieve the user for the specified id
		 */
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
		logger.info("Received request to list access rights for georesource with datasetId '{}'", georesourceId);
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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity updateGeoresourcePermissions(
			@P("georesourceId") String georesourceId,
	 		PermissionLevelInputType permissionLevelInputType) {
		logger.info("Received request to update georesource roles for georesourceId '{}'.", georesourceId);
		try {   
			georesourceId = georesourcesManager.updatePermissions(permissionLevelInputType, georesourceId);
			lastModManager.updateLastDatabaseModification_georesources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);
		}
		if (georesourceId != null) {
            HttpHeaders responseHeaders = new HttpHeaders();

            String location = georesourceId;
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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getAllGeoresourceFeaturesById(
			@P("georesourceId") String georesourceId,
			String simplifyGeometries) {
		logger.info(
				"Received request to get all georesource features for datasetId '{}' and simplifyGeometries parameter '{}'",
				georesourceId, simplifyGeometries);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getAllGeoresourceFeatures(georesourceId, simplifyGeometries,
					provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonth(
			@P("georesourceId") String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day,
			String simplifyGeometries) {
		logger.info(
				"Received request to get georesource features for datasetId '{}' and simplifyGeometries parameter '{}'",
				georesourceId, simplifyGeometries);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures(georesourceId, year, month, day,
					simplifyGeometries, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day + ".json";

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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<String> getGeoresourceSchemaByLevel(@P("georesourceId") String georesourceId) {
		logger.info("Received request to get georesource metadata for datasetId '{}'", georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		if (accept != null && accept.contains("application/json")) {

			String jsonSchema = null;
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
		logger.info("Received request to get all georesource features for datasetId '{}' without geometry",
				georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getAllGeoresourceFeatures_withoutGeometry(georesourceId,
					provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all_withoutGeometry.json";

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", "attachment; filename=" + fileName);
			headers.add("Content-Type", "application/json; charset=utf-8");
			byte[] JsonBytes = geoJsonFeatures.getBytes();

			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/json"))
					.body(JsonBytes);

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
		logger.info(
				"Received request to get public single georesource feature records for datasetId '{}' and featureId '{}'",
				georesourceId, featureId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getSingleGeoresourceFeatureRecords(georesourceId, featureId,
					simplifyGeometries, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getSingleGeoresourceFeatureRecordById(
			@P("georesourceId") String georesourceId,
			String featureId,
			String featureRecordId,
			String simplifyGeometries) {
		logger.info(
				"Received request to get public single georesource feature record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getSingleGeoresourceFeatureRecord(georesourceId, featureId,
					featureRecordId, simplifyGeometries, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_all.json";

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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'viewer')")
	public ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonthWithoutGeometry(
			@P("georesourceId") String georesourceId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day
			) {
		logger.info("Received request to get georesource features for datasetId '{}' without geometry", georesourceId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures_withoutGeometry(georesourceId,
					year, month, day, provider);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day
					+ "_withoutGeometry.json";

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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> updateGeoresourceAsBody(
			@P("georesourceId") String georesourceId,
			GeoresourcePUTInputType featureData) {
		logger.info("Received request to update georesource features for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			georesourceId = georesourcesManager.updateFeatures(featureData, georesourceId);
			lastModManager.updateLastDatabaseModification_georesources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (georesourceId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = georesourceId;
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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> updateGeoresourceMetadataAsBody(
			@P("georesourceId") String georesourceId,
			GeoresourcePATCHInputType metadata) {
		logger.info("Received request to update georesource metadata for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			georesourceId = georesourcesManager.updateMetadata(metadata, georesourceId);
			lastModManager.updateLastDatabaseModification_georesources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (georesourceId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = georesourceId;
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
	@PreAuthorize("isAuthorizedForEntity(#georesourceId, 'georesource', 'editor')")
	public ResponseEntity<Void> updateGeoresourceFeatureRecordAsBody(
			@P("georesourceId") String georesourceId,
			String featureId,
			String featureRecordId,
			String georesourceFeatureRecordData) {
		logger.info(
				"Received request to update single georesource feature database record for datasetId '{}' and featureId '{}' and recordId '{}'",
				georesourceId, featureId, featureRecordId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			georesourceId = georesourcesManager.updateFeatureRecordByRecordId(georesourceFeatureRecordData,
					georesourceId, featureId, featureRecordId);
			lastModManager.updateLastDatabaseModification_georesources();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (georesourceId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = georesourceId;
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

}
