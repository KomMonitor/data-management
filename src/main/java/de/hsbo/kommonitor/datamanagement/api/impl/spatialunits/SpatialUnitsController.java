package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.auth.AuthInfoProviderFactory;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
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

import de.hsbo.kommonitor.datamanagement.api.SpatialUnitsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPUTInputType;

@Controller
public class SpatialUnitsController extends BasePathController implements SpatialUnitsApi {

	private static Logger logger = LoggerFactory.getLogger(SpatialUnitsController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	SpatialUnitsManager spatialUnitsManager;

	@Autowired
	AuthInfoProviderFactory authInfoProviderFactory;
	
	@Autowired
    private LastModificationManager lastModManager;

	@org.springframework.beans.factory.annotation.Autowired
	public SpatialUnitsController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('publisher')")
	public ResponseEntity<SpatialUnitOverviewType> addSpatialUnitAsBody(@RequestBody SpatialUnitPOSTInputType featureData) {
		logger.info("Received request to insert new spatial unit");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		SpatialUnitOverviewType spatialUnitMetadata;
		try {
			spatialUnitMetadata = spatialUnitsManager.addSpatialUnit(featureData);
			lastModManager.updateLastDatabaseModification_spatialUnits();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (spatialUnitMetadata != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitMetadata.getSpatialUnitId();
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
				// return ApiResponseUtil.createResponseEntityFromException(e);
			}

			return new ResponseEntity<SpatialUnitOverviewType>(spatialUnitMetadata, responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'creator')")
	public ResponseEntity deleteAllSpatialUnitFeaturesById(@PathVariable("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to delete all spatialUnit features for datasetName '{}'", spatialUnitId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteAllSpatialUnitFeaturesByDatasetById(spatialUnitId);
			lastModManager.updateLastDatabaseModification_spatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'creator')")
	public ResponseEntity deleteSpatialUnitById(@PathVariable("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to delete spatialUnit for datasetName '{}'", spatialUnitId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSpatialUnitDatasetById(spatialUnitId);
			lastModManager.updateLastDatabaseModification_spatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'creator')")
	public ResponseEntity deleteSpatialUnitByIdAndYearAndMonth(@PathVariable("spatialUnitId") String spatialUnitId, @PathVariable("year") BigDecimal year,
			@PathVariable("month") BigDecimal month, @PathVariable("day") BigDecimal day) {
		logger.info("Received request to delete spatialUnit for datasetId '{}' and Date '{}-{}-{}'", spatialUnitId, year, month, day);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSpatialUnitDatasetByIdAndDate(spatialUnitId, year, month, day);
			lastModManager.updateLastDatabaseModification_spatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception  e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<List<SpatialUnitOverviewType>> getSpatialUnits(Principal principal) {
		logger.info("Received request to get all spatialUnits metadata");
		String accept = request.getHeader("Accept");

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider(principal);
		/*
		 * retrieve all available users
		 * 
		 * return them to client
		 */
		try {
			
//			if (accept != null && accept.contains("application/json")) {

				List<SpatialUnitOverviewType> spatialunitsMetadata = spatialUnitsManager.getAllSpatialUnitsMetadata(authInfoProvider);

				return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);
//
//			} else {
//				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
			
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<SpatialUnitOverviewType> getSpatialUnitsById(@PathVariable("spatialUnitId") String spatialUnitId, Principal principal) {
		logger.info("Received request to get spatialUnit metadata for datasetId '{}'", spatialUnitId);
		String accept = request.getHeader("Accept");
		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider(principal);

		/*
		 * retrieve the user for the specified id
		 */
		try {
			if (accept != null && accept.contains("application/json")) {

				
				SpatialUnitOverviewType spatialUnitMetadata = spatialUnitsManager.getSpatialUnitByDatasetId(spatialUnitId, authInfoProvider);

				return new ResponseEntity<>(spatialUnitMetadata, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		
	}

	@Override
	public ResponseEntity<List<PermissionLevelType>> getSpatialUnitsPermissionsById(
			@PathVariable("spatialUnitId") String spatialUnitId, Principal principal) {
		logger.info("Received request to list access rights for spatial unit with datasetId '{}'", spatialUnitId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider(principal);

		try {
			if (accept != null && accept.contains("application/json")) {
				List<PermissionLevelType> permissions =
						spatialUnitsManager.getSpatialUnitsPermissionsByDatasetId(spatialUnitId, provider);

				return new ResponseEntity<>(permissions, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<byte[]> getAllSpatialUnitFeaturesById(@PathVariable("spatialUnitId") String spatialUnitId, 
			@RequestParam(value = "simplifyGeometries", required = false, defaultValue="original") String simplifyGeometries, Principal principal) {
		logger.info("Received request to get spatialUnit features for datasetId '{}' and simplifyGeometries parameter '{}'", spatialUnitId, simplifyGeometries);
		String accept = request.getHeader("Accept");

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider(principal);

		/*
		 * retrieve the user for the specified id
		 */

		try {
			String geoJsonFeatures = spatialUnitsManager.getAllSpatialUnitFeatures(spatialUnitId, simplifyGeometries, authInfoProvider);
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
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<byte[]> getSpatialUnitsByIdAndYearAndMonth(@PathVariable("spatialUnitId") String spatialUnitId, @PathVariable("year") BigDecimal year,
			@PathVariable("month") BigDecimal month, @PathVariable("day") BigDecimal day,
			@RequestParam(value = "simplifyGeometries", required = false, defaultValue="original") String simplifyGeometries, Principal principal) {
		logger.info("Received request to get spatialUnit features for datasetId '{}' and simplifyGeometries parameter '{}'", spatialUnitId, simplifyGeometries);
		String accept = request.getHeader("Accept");

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider(principal);

		/*
		 * retrieve the user for the specified id
		 */

		try {
			String geoJsonFeatures = spatialUnitsManager.getValidSpatialUnitFeatures(spatialUnitId, year, month,
					day, simplifyGeometries, authInfoProvider);
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

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<String> getSpatialUnitsSchemaById(@PathVariable("spatialUnitId") String spatialUnitId, Principal principal) {
		logger.info("Received request to get spatialUnit metadata for datasetName '{}'", spatialUnitId);
		String accept = request.getHeader("Accept");

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider(principal);

		/*
		 * retrieve the user for the specified id
		 */

		if (accept != null && accept.contains("application/json")) {

			String jsonSchema = null;
			try {
				jsonSchema = spatialUnitsManager.getJsonSchemaForDatasetId(spatialUnitId, authInfoProvider);
			} catch (ResourceNotFoundException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(jsonSchema, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'publisher')")
	public ResponseEntity updateSpatialUnitAsBody(@PathVariable("spatialUnitId") String spatialUnitId, @RequestBody SpatialUnitPUTInputType featureData) {
		logger.info("Received request to update spatial unit features for datasetName '{}'", spatialUnitId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			spatialUnitId = spatialUnitsManager.updateFeatures(featureData, spatialUnitId);
			lastModManager.updateLastDatabaseModification_spatialUnits();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (spatialUnitId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitId;
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
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'editor')")
	public ResponseEntity updateSpatialUnitMetadataAsBody(@PathVariable("spatialUnitId") String spatialUnitId, @RequestBody SpatialUnitPATCHInputType metadata) {
		logger.info("Received request to update spatial unit metadata for datasetName '{}'", spatialUnitId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			spatialUnitId = spatialUnitsManager.updateMetadata(metadata, spatialUnitId);
			lastModManager.updateLastDatabaseModification_spatialUnits();
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (spatialUnitId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitId;
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
