package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.geotools.filter.text.cql2.CQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.SpatialUnitsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
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

	@org.springframework.beans.factory.annotation.Autowired
	public SpatialUnitsController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public ResponseEntity addSpatialUnitAsBody(@RequestBody SpatialUnitPOSTInputType featureData) {
		logger.info("Received request to insert new spatial unit");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		String spatialUnitMetadataId;
		try {
			spatialUnitMetadataId = spatialUnitsManager.addSpatialUnit(featureData);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (spatialUnitMetadataId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitMetadataId;
			try {
				responseHeaders.setLocation(new URI(location));
			} catch (URISyntaxException e) {
				// return ApiResponseUtil.createResponseEntityFromException(e);
			}

			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity deleteSpatialUnitByUnit(@PathVariable("spatialUnitLevel") String spatialUnitLevel) {
		logger.info("Received request to delete spatialUnit for datasetName '{}'", spatialUnitLevel);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSpatialUnitDatasetByName(spatialUnitLevel);

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (ResourceNotFoundException | IOException e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity deleteSpatialUnitByUnitAndYearAndMonth(@PathVariable("spatialUnitLevel") String spatialUnitLevel, @PathVariable("year") BigDecimal year,
			@PathVariable("month") BigDecimal month, @PathVariable("day") BigDecimal day) {
		logger.info("Received request to delete spatialUnit for datasetName '{}' and Date '{}-{}-{}'", spatialUnitLevel, year, month, day);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = spatialUnitsManager.deleteSpatialUnitDatasetByNameAndDate(spatialUnitLevel, year, month, day);

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (ResourceNotFoundException | IOException e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<SpatialUnitOverviewType>> getSpatialUnits() {
		logger.info("Received request to get all spatialUnits metadata");
		String accept = request.getHeader("Accept");

		/*
		 * retrieve all available users
		 * 
		 * return them to client
		 */
		try {
			
			if (accept != null && accept.contains("application/json")) {

				List<SpatialUnitOverviewType> spatialunitsMetadata = spatialUnitsManager.getAllSpatialUnitsMetadata();

				return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<SpatialUnitOverviewType> getSpatialUnitsByLevel(@PathVariable("spatialUnitLevel") String spatialUnitLevel) {
		logger.info("Received request to get spatialUnit metadata for datasetName '{}'", spatialUnitLevel);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */
		try {
			if (accept != null && accept.contains("application/json")) {

				
				SpatialUnitOverviewType spatialUnitMetadata = spatialUnitsManager.getSpatialUnitByDatasetName(spatialUnitLevel);

				return new ResponseEntity<>(spatialUnitMetadata, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		
	}

	@Override
	public ResponseEntity<byte[]> getSpatialUnitsByLevelAndYearAndMonth(@PathVariable("spatialUnitLevel") String spatialUnitLevel, @PathVariable("year") BigDecimal year,
			@PathVariable("month") BigDecimal month, @PathVariable("day") BigDecimal day) {
		logger.info("Received request to get spatialUnit features for datasetName '{}'", spatialUnitLevel);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */

		try {
			String geoJsonFeatures = spatialUnitsManager.getValidSpatialUnitFeatures(spatialUnitLevel, year, month,
					day);
			String fileName = "SpatialUnitFeatures_" + spatialUnitLevel + "_" + year + "-" + month + "-" + day
					+ ".json";

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", "attachment; filename=" + fileName);
			byte[] JsonBytes = geoJsonFeatures.getBytes();

			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("image/tiff"))
					.body(JsonBytes);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<String> getSpatialUnitsSchemaByLevel(@PathVariable("spatialUnitLevel") String spatialUnitLevel) {
		logger.info("Received request to get spatialUnit metadata for datasetName '{}'", spatialUnitLevel);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */

		if (accept != null && accept.contains("application/json")) {

			String jsonSchema = spatialUnitsManager.getJsonSchemaForDatasetName(spatialUnitLevel);

			return new ResponseEntity<>(jsonSchema, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity updateSpatialUnitAsBody(@PathVariable("spatialUnitLevel") String spatialUnitLevel, @RequestBody SpatialUnitPUTInputType featureData) {
		logger.info("Received request to update spatial unit features for datasetName '{}'", spatialUnitLevel);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			spatialUnitLevel = spatialUnitsManager.updateFeatures(featureData, spatialUnitLevel);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (spatialUnitLevel != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitLevel;
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
	public ResponseEntity updateSpatialUnitMetadataAsBody(@PathVariable("spatialUnitLevel") String spatialUnitLevel, @RequestBody SpatialUnitPATCHInputType metadata) {
		logger.info("Received request to update spatial unit metadata for datasetName '{}'", spatialUnitLevel);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			spatialUnitLevel = spatialUnitsManager.updateMetadata(metadata, spatialUnitLevel);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (spatialUnitLevel != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = spatialUnitLevel;
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
