package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

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

import de.hsbo.kommonitor.datamanagement.api.IndicatorsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;

@Controller
public class IndicatorsController extends BasePathController implements IndicatorsApi {

	private static Logger logger = LoggerFactory.getLogger(IndicatorsController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	IndicatorsManager indicatorsManager;

	@org.springframework.beans.factory.annotation.Autowired
	public IndicatorsController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}




	@Override
	public ResponseEntity deleteIndicatorByIdAndYearAndMonth(@PathVariable("indicatorId") String indicatorId, @PathVariable("spatialUnitId") String spatialUnitId,
			@PathVariable("year") BigDecimal year, @PathVariable("month") BigDecimal month,
			@PathVariable("day") BigDecimal day) {
		logger.info("Received request to delete indicator for indicatorId '{}' and Date '{}-{}-{}'", indicatorId, year, month, day);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = indicatorsManager.deleteIndicatorDatasetByIdAndDate(indicatorId, spatialUnitId, year, month, day);

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (ResourceNotFoundException | IOException e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity addIndicatorAsBody(@RequestBody IndicatorPOSTInputType indicatorData) {
		logger.info("Received request to insert new indicator");

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */
		String indicatorMetadataId;
		try {
			indicatorMetadataId = indicatorsManager.addIndicator(indicatorData);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (indicatorMetadataId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = indicatorMetadataId;
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
	public ResponseEntity deleteIndicatorById(@PathVariable("indicatorId") String indicatorId) {
		logger.info("Received request to delete indicator for indicatorId '{}'", indicatorId);

		String accept = request.getHeader("Accept");

		/*
		 * delete topic with the specified id
		 */

		boolean isDeleted;
		try {
			isDeleted = indicatorsManager.deleteIndicatorDatasetById(indicatorId);

			if (isDeleted)
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndId(@PathVariable("indicatorId") String indicatorId,
			@PathVariable("spatialUnitId") String spatialUnitId) {
		logger.info("Received request to get indicators features for spatialUnitId '{}' and Id '{}' ",
				spatialUnitId, indicatorId);
		String accept = request.getHeader("Accept");

		try {
			String geoJsonFeatures = indicatorsManager.getIndicatorFeatures(indicatorId, spatialUnitId);
			String fileName = "IndicatorFeatures_" + spatialUnitId + "_" + indicatorId + ".json";

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", "attachment; filename=" + fileName);
			byte[] JsonBytes = geoJsonFeatures.getBytes();

			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.geo+json"))
					.body(JsonBytes);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<byte[]> getIndicatorBySpatialUnitIdAndIdAndYearAndMonth(@PathVariable("indicatorId") String indicatorId,
			@PathVariable("spatialUnitId") String spatialUnitId, @PathVariable("year") BigDecimal year, @PathVariable("month") BigDecimal month,
			@PathVariable("day") BigDecimal day) {
		logger.info(
				"Received request to get indicators features for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
				spatialUnitId, indicatorId, year, month, day);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */

		try {
			String geoJsonFeatures = indicatorsManager.getValidIndicatorFeatures(indicatorId, spatialUnitId, year,
					month, day);
			String fileName = "IndicatorFeatures_" + spatialUnitId + "_" + indicatorId + "_" + year + "-" + month
					+ "-" + day + ".json";

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-disposition", "attachment; filename=" + fileName);
			byte[] JsonBytes = geoJsonFeatures.getBytes();

			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("application/vnd.geo+json")).body(JsonBytes);

		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<List<IndicatorOverviewType>> getIndicators(@RequestParam(value = "topic", required = false) String topic) {
		logger.info("Received request to get all indicators metadata for topic {}", topic);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve all available users
		 * 
		 * return them to client
		 */
		try {
			
			if (accept != null && accept.contains("application/json")) {

				List<IndicatorOverviewType> spatialunitsMetadata = indicatorsManager.getAllIndicatorsMetadata(topic);

				return new ResponseEntity<>(spatialunitsMetadata, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	@Override
	public ResponseEntity<IndicatorOverviewType> getIndicatorById(@PathVariable("indicatorId") String indicatorId) {
		logger.info("Received request to get indicator metadata for indicatorId '{}'", indicatorId);
		String accept = request.getHeader("Accept");

		/*
		 * retrieve the user for the specified id
		 */
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
	public ResponseEntity updateIndicatorAsBody(@PathVariable("indicatorId") String indicatorId, @RequestBody IndicatorPUTInputType indicatorData) {
		logger.info("Received request to update indicator features for indicator '{}'", indicatorId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			indicatorId = indicatorsManager.updateFeatures(indicatorData, indicatorId);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (indicatorId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = indicatorId;
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
	public ResponseEntity updateIndicatorMetadataAsBody(@PathVariable("indicatorId") String indicatorId, @RequestBody IndicatorPATCHInputType metadata) {
		logger.info("Received request to update indicator metadata for indicatorId '{}'", indicatorId);

		String accept = request.getHeader("Accept");

		/*
		 * analyse input data and save it within database
		 */

		try {
			indicatorId = indicatorsManager.updateMetadata(metadata, indicatorId);
		} catch (Exception e1) {
			return ApiUtils.createResponseEntityFromException(e1);

		}

		if (indicatorId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();

			String location = indicatorId;
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
