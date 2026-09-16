package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import de.hsbo.kommonitor.datamanagement.api.SpatialUnitsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ValidationException;
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
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class SpatialUnitsController extends BasePathController implements SpatialUnitsApi {

	private static Logger logger = LoggerFactory.getLogger(SpatialUnitsController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	SpatialUnitsManager spatialUnitsManager;

	@Autowired
	SpatialUnitHierarchyManager spatialUnitHierarchyManager;

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
	@PreAuthorize("hasRequiredPermissionLevel('creator', 'resources')")
	public ResponseEntity<SpatialUnitOverviewType> addSpatialUnitAsBody(SpatialUnitPOSTInputType featureData) {
		logger.info("Received request to insert new spatial unit");

		SpatialUnitOverviewType spatialUnitMetadata;
		try {
			spatialUnitMetadata = spatialUnitsManager.addSpatialUnit(featureData);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);

		}

		if (spatialUnitMetadata != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitMetadata.getSpatialUnitId();
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
				return ApiUtils.createResponseEntityFromException(e);
			}

			return new ResponseEntity<SpatialUnitOverviewType>(spatialUnitMetadata, responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'editor')")
	public ResponseEntity deleteAllSpatialUnitFeaturesById(@P("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to delete all spatialUnit features for datasetName '{}'", spatialUnitId);

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteAllSpatialUnitFeaturesByDatasetById(spatialUnitId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'creator')")
	public ResponseEntity deleteSpatialUnitById(@P("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to delete spatialUnit for datasetName '{}'", spatialUnitId);

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSpatialUnitDatasetById(spatialUnitId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'editor')")
	public ResponseEntity deleteSpatialUnitByIdAndYearAndMonth(
			@P("spatialUnitId") String spatialUnitId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day) {
		logger.info("Received request to delete spatialUnit for datasetId '{}' and Date '{}-{}-{}'", spatialUnitId, year, month, day);

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSpatialUnitDatasetByIdAndDate(spatialUnitId, year, month, day);
			lastModManager.updateLastDatabaseModificationSpatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception  e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'editor')")
	public ResponseEntity<Void> deleteSingleSpatialUnitFeatureById(
			@P("spatialUnitId") String spatialUnitId,
			String featureId) {
		logger.info("Received request to delete single spatial unit feature databse records for datasetId '{}' and featureId '{}'", spatialUnitId, featureId);

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSingleSpatialUnitFeatureRecordsByFeatureId(spatialUnitId, featureId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'editor')")
	public ResponseEntity<Void> deleteSingleSpatialUnitFeatureRecordById(
			@P("spatialUnitId") String spatialUnitId,
			String featureId,
			String featureRecordId) {
		logger.info("Received request to delete single spatial unit feature databse record for datasetId '{}' and featureId '{}' and recordId '{}'", spatialUnitId, featureId, featureRecordId);

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSingleSpatialUnitFeatureRecordByRecordId(spatialUnitId, featureId, featureRecordId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<List<SpatialUnitOverviewType>> getSpatialUnits() {
		logger.info("Received request to get all spatialUnits metadata");

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			List<SpatialUnitOverviewType> spatialunitsMetadata = spatialUnitsManager.getAllSpatialUnitsMetadata(authInfoProvider);
			return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<SpatialUnitOverviewType> getSpatialUnitsById(@P("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to get spatialUnit metadata for datasetId '{}'", spatialUnitId);

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			SpatialUnitOverviewType spatialUnitMetadata = spatialUnitsManager.getSpatialUnitByDatasetId(spatialUnitId, authInfoProvider);
			return new ResponseEntity<>(spatialUnitMetadata, HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<List<PermissionLevelType>> getSpatialUnitsPermissionsById(String spatialUnitId) {
		logger.info("Received request to list access rights for spatial unit with datasetId '{}'", spatialUnitId);

		AuthInfoProvider provider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			List<PermissionLevelType> permissions =
					spatialUnitsManager.getSpatialUnitsPermissionsByDatasetId(spatialUnitId, provider);

			return new ResponseEntity<>(permissions, HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<byte[]> getAllSpatialUnitFeaturesById(
			@P("spatialUnitId") String spatialUnitId,
			String simplifyGeometries) {
		logger.info("Received request to get spatialUnit features for datasetId '{}' and simplifyGeometries parameter '{}'", spatialUnitId, simplifyGeometries);

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

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
	public ResponseEntity<byte[]> getSingleSpatialUnitFeatureById(
			@P("spatialUnitId") String spatialUnitId,
			String featureId,
			String simplifyGeometries) {
		logger.info("Received request to get single spatial unit feature records for datasetId '{}' and featureId '{}'",
				spatialUnitId, featureId);

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = spatialUnitsManager.getSingleSpatialUnitFeatureRecords(spatialUnitId, featureId,
					simplifyGeometries, authInfoProvider);
			String fileName = "SpatialUnit_" + spatialUnitId + "_featureDatabaseRecords_" + featureId + ".json";

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
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<byte[]> getSingleSpatialUnitFeatureRecordById(
			@P("spatialUnitId") String spatialUnitId,
			String featureId,
			String featureRecordId,
			String simplifyGeometries) {
		logger.info(
				"Received request to get single georesource feature record for datasetId '{}' and featureId '{}' and recordId '{}'",
				spatialUnitId, featureId, featureRecordId);
		
		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

		try {
			String geoJsonFeatures = spatialUnitsManager.getSingleSpatialUnitFeatureRecord(spatialUnitId, featureId,
					featureRecordId, simplifyGeometries, authInfoProvider);
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

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'viewer')")
	public ResponseEntity<byte[]> getSpatialUnitsByIdAndYearAndMonth(
			@P("spatialUnitId") String spatialUnitId,
			BigDecimal year,
			BigDecimal month,
			BigDecimal day,
			String simplifyGeometries) {
		logger.info("Received request to get spatialUnit features for datasetId '{}' and simplifyGeometries parameter '{}'", spatialUnitId, simplifyGeometries);

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

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
	public ResponseEntity<String> getSpatialUnitsSchemaById(@P("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to get spatialUnit metadata for datasetName '{}'", spatialUnitId);

		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();

		String jsonSchema;
		try {
			jsonSchema = spatialUnitsManager.getJsonSchemaForDatasetId(spatialUnitId, authInfoProvider);
		} catch (ResourceNotFoundException e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(jsonSchema, HttpStatus.OK);

	}

	@Override
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'editor')")
	public ResponseEntity updateSpatialUnitAsBody(
			@P("spatialUnitId") String spatialUnitId,
			SpatialUnitPUTInputType featureData) {
		logger.info("Received request to update spatial unit features for datasetName '{}'", spatialUnitId);

		try {
			spatialUnitId = spatialUnitsManager.updateFeatures(featureData, spatialUnitId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
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
	public ResponseEntity updateSpatialUnitMetadataAsBody(
			@P("spatialUnitId") String spatialUnitId,
			SpatialUnitPATCHInputType metadata) {
		logger.info("Received request to update spatial unit metadata for datasetName '{}'", spatialUnitId);

		try {
			spatialUnitId = spatialUnitsManager.updateMetadata(metadata, spatialUnitId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
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
	public ResponseEntity<Void> updateSpatialUnitFeatureRecordAsBody(
			@P("spatialUnitId") String spatialUnitId,
			String featureId,
			String featureRecordId,
			String spatialUnitFeatureRecordData) {
		logger.info("Received request to update single spatial unit feature database record for datasetId '{}' and featureId '{}' and recordId '{}'", spatialUnitId, featureId, featureRecordId);

		try {
			spatialUnitId = spatialUnitsManager.updateFeatureRecordByRecordId(spatialUnitFeatureRecordData, spatialUnitId, featureId, featureRecordId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
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
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'creator')")
	public ResponseEntity<List<PermissionLevelType>> updateSpatialUnitsPermissions(
			@P("spatialUnitId") String spatialUnitId,
			PermissionLevelInputType permissionLevelInputType) {
		 logger.info("Received request to update spatial unit roles for spatialUnitId '{}'", spatialUnitId);
        try {
            spatialUnitId = spatialUnitsManager.updatePermissionLevels(permissionLevelInputType, spatialUnitId);
            lastModManager.updateLastDatabaseModificationSpatialUnits();
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
	@PreAuthorize("isAuthorizedForEntity(#spatialUnitId, 'spatialunit', 'creator')")
	public ResponseEntity<Void> updateSpatialUnitsOwnership(
			@P("spatialUnitId") String spatialUnitId,
			OwnerInputType ownerInputType) {
		logger.info("Received request to update ownership for spatialUnitId '{}'", spatialUnitId);
		try {
			spatialUnitId = spatialUnitsManager.updateOwnership(ownerInputType, spatialUnitId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
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
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<List<SpatialUnitHierarchyOverviewType>> getSpatialUnitHierarchies() {
		logger.info("Received request to get all spatial unit hierarchies");
		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();
		try {
			List<SpatialUnitHierarchyOverviewType> hierarchies = spatialUnitHierarchyManager.getAllHierarchies(authInfoProvider);
			return new ResponseEntity<>(hierarchies, HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("hasRequiredPermissionLevel('viewer')")
	public ResponseEntity<SpatialUnitHierarchyOverviewType> getSpatialUnitHierarchyById(String hierarchyId) {
		logger.info("Received request to get spatial unit hierarchy with id '{}'", hierarchyId);
		AuthInfoProvider authInfoProvider = authInfoProviderFactory.createAuthInfoProvider();
		try {
			SpatialUnitHierarchyOverviewType hierarchy = spatialUnitHierarchyManager.getHierarchy(hierarchyId, authInfoProvider);
			return new ResponseEntity<>(hierarchy, HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForMandant(#hierarchyData.mandantId, 'creator')")
	public ResponseEntity<SpatialUnitHierarchyOverviewType> addSpatialUnitHierarchy(@P("hierarchyData") SpatialUnitHierarchyInputType hierarchyData) {
		logger.info("Received request to create a new spatial unit hierarchy");
		SpatialUnitHierarchyOverviewType hierarchy;
		try {
			hierarchy = spatialUnitHierarchyManager.addHierarchy(hierarchyData);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		if (hierarchy != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			try {
				responseHeaders.setLocation(new URI(hierarchy.getHierarchyId()));
			} catch (URISyntaxException e) {
				// ignore invalid location URI
			}
			return new ResponseEntity<>(hierarchy, responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForSpatialUnitHierarchy(#hierarchyId, 'creator')")
	public ResponseEntity<SpatialUnitHierarchyOverviewType> updateSpatialUnitHierarchy(@P("hierarchyId") String hierarchyId, SpatialUnitHierarchyInputType hierarchyData) {
		logger.info("Received request to update spatial unit hierarchy with id '{}'", hierarchyId);
		try {
			SpatialUnitHierarchyOverviewType hierarchy = spatialUnitHierarchyManager.updateHierarchy(hierarchyId, hierarchyData);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
			return new ResponseEntity<>(hierarchy, HttpStatus.OK);
		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForSpatialUnitHierarchy(#hierarchyId, 'creator')")
	public ResponseEntity<Void> deleteSpatialUnitHierarchyById(@P("hierarchyId") String hierarchyId) {
		logger.info("Received request to delete spatial unit hierarchy with id '{}'", hierarchyId);
		try {
			spatialUnitHierarchyManager.deleteHierarchy(hierarchyId);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForSpatialUnitHierarchy(#hierarchyId, 'creator')")
	public ResponseEntity<SpatialUnitHierarchyOverviewType> updateSpatialUnitHierarchyMembers(@P("hierarchyId") String hierarchyId, List<SpatialUnitHierarchyMemberInputType> members) {
		logger.info("Received request to update members of spatial unit hierarchy with id '{}'", hierarchyId);
		try {
			SpatialUnitHierarchyOverviewType hierarchy = spatialUnitHierarchyManager.updateHierarchyMembers(hierarchyId, members);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
			return new ResponseEntity<>(hierarchy, HttpStatus.OK);
		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	@PreAuthorize("isAuthorizedForSpatialUnitHierarchyMemberships(#spatialUnitId, #hierarchies, 'editor')")
	public ResponseEntity<SpatialUnitOverviewType> updateSpatialUnitHierarchyMemberships(
			@P("spatialUnitId") String spatialUnitId,
			@P("hierarchies") List<SpatialUnitHierarchyMembershipInputType> hierarchies) {
		logger.info("Received request to update hierarchy memberships of spatial unit with id '{}'", spatialUnitId);
		try {
			spatialUnitHierarchyManager.updateSpatialUnitMemberships(spatialUnitId, hierarchies);
			lastModManager.updateLastDatabaseModificationSpatialUnits();
			SpatialUnitOverviewType spatialUnit = spatialUnitsManager.getSpatialUnitByDatasetId(spatialUnitId);
			return new ResponseEntity<>(spatialUnit, HttpStatus.OK);
		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

}
