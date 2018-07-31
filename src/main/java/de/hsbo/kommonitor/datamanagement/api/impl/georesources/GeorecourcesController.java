package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.GeoresourcesApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePUTInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitOverviewType;


@Controller
public class GeorecourcesController extends BasePathController implements GeoresourcesApi {

	
	private static Logger logger = LoggerFactory.getLogger(GeorecourcesController.class);

	private final ObjectMapper objectMapper;
	
	private final HttpServletRequest request;
	
	@Autowired
	GeoresourcesManager georesourcesManager;
	
	@org.springframework.beans.factory.annotation.Autowired
	public GeorecourcesController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}
	
	
	
	@Override
	public ResponseEntity addGeoresourceAsBody(@RequestBody GeoresourcePOSTInputType featureData) {
		logger.info("Received request to insert new georesource");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		String georesourceMetadataId;
		try {
			georesourceMetadataId = georesourcesManager.addGeoresource(featureData);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (georesourceMetadataId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = georesourceMetadataId;
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
	public ResponseEntity deleteGeoresourceById(@PathVariable("georesourceId") String georesourceId) {
		logger.info("Received request to delete georesource for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteGeoresourceDatasetById(georesourceId);

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (ResourceNotFoundException | IOException e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity deleteGeoresourceByIdAndYearAndMonth(@PathVariable("georesourceId") String georesourceId, @PathVariable("year") BigDecimal year, @PathVariable("month") BigDecimal month,
			@PathVariable("day") BigDecimal day) {
		logger.info("Received request to delete georesource for datasetId '{}' and Date '{}-{}-{}'", georesourceId, year, month, day);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = georesourcesManager.deleteGeoresourceFeaturesByIdAndDate(georesourceId, year, month, day);

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (ResourceNotFoundException | IOException e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<GeoresourceOverviewType>> getGeoresources(@RequestParam(value = "topic", required = false) String topic) {
		logger.info("Received request to get all georesources metadata for topic {}", topic);
		/*
		 * topic is an optional parameter and thus may be null!
		 */
		String accept = request.getHeader("Accept");

		/*
		 * retrieve all available users
		 * 
		 * return them to client
		 */
		try {
			
			if (accept != null && accept.contains("application/json")) {

				List<GeoresourceOverviewType> georesourcesMetadata = georesourcesManager.getAllGeoresourcesMetadata(topic);

				return new ResponseEntity<>(georesourcesMetadata, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<GeoresourceOverviewType> getGeoresourceById(@PathVariable("georesourceId") String georesourceId) {
		logger.info("Received request to get georesource metadata for datasetId '{}'", georesourceId);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */
		try {
			if (accept != null && accept.contains("application/json")) {

				
				GeoresourceOverviewType georesourceMetadata = georesourcesManager.getGeoresourceByDatasetId(georesourceId);

				return new ResponseEntity<>(georesourceMetadata, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonth(@PathVariable("georesourceId") String georesourceId, @PathVariable("year") BigDecimal year, @PathVariable("month") BigDecimal month,
			@PathVariable("day") BigDecimal day) {
		logger.info("Received request to get georesource features for datasetId '{}'", georesourceId);
		String accept = request.getHeader("Accept");

		try {
			String geoJsonFeatures = georesourcesManager.getValidGeoresourceFeatures(georesourceId, year, month, day);
			String fileName = "GeoresourceFeatures_" + georesourceId + "_" + year + "-" + month + "-" + day + ".json";

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
	public ResponseEntity<String> getGeoresourceSchemaByLevel(@PathVariable("georesourceId") String georesourceId) {
		logger.info("Received request to get georesource metadata for datasetId '{}'", georesourceId);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */

		if (accept != null && accept.contains("application/json")) {

			String jsonSchema = georesourcesManager.getJsonSchemaForDatasetName(georesourceId);

			return new ResponseEntity<>(jsonSchema, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity updateGeoresourceAsBody(@PathVariable("georesourceId") String georesourceId, @RequestBody GeoresourcePUTInputType featureData) {
		logger.info("Received request to update georesource features for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			georesourceId = georesourcesManager.updateFeatures(featureData, georesourceId);
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
	public ResponseEntity updateGeoresourceMetadataAsBody(@PathVariable("georesourceId") String georesourceId, @RequestBody GeoresourcePATCHInputType metadata) {
		logger.info("Received request to update georesource metadata for datasetId '{}'", georesourceId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			georesourceId = georesourcesManager.updateMetadata(metadata, georesourceId);
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
