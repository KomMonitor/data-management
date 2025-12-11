package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.api.IndicatorsPublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.api.impl.util.SimplifyGeometriesEnum;
import de.hsbo.kommonitor.datamanagement.export.ExportManager;
import de.hsbo.kommonitor.datamanagement.export.TempFileInputStream;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.model.IndicatorOverviewType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPropertiesWithoutGeomType;
import de.hsbo.kommonitor.datamanagement.model.ResourceFilterType;
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
import org.springframework.stereotype.Controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class IndicatorsPublicController extends BasePathController implements IndicatorsPublicApi {

    private static final Logger LOG = LoggerFactory.getLogger(IndicatorsPublicController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    IndicatorsManager indicatorsManager;

    @Autowired
    private ExportManager exportManager;

    @Autowired
    public IndicatorsPublicController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<byte[]> getPublicIndicatorBySpatialUnitIdAndId(
            String indicatorId,
            String spatialUnitId,
            String simplifyGeometries) {
        LOG.info("Received request to get public indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        try {
            String geoJsonFeatures = indicatorsManager.getIndicatorFeatures(indicatorId, spatialUnitId, simplifyGeometries);
            String fileName = "IndicatorFeatures_" + spatialUnitId + "_" + indicatorId + ".json";

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
    public ResponseEntity<Resource> exportPublicIndicatorBySpatialUnitIdAndId(String indicatorId, String spatialUnitId, String format) {
        LOG.info("Received request to export indicators features for spatialUnitId '{}' and Id '{}' ",
                spatialUnitId, indicatorId);
        DataStore dataStore = null;
        try {
            dataStore = DatabaseHelperUtil.getPostGisDataStore();

            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) indicatorsManager
                    .getIndicatorFeatureCollection(
                            indicatorId,
                            spatialUnitId,
                            SimplifyGeometriesEnum.ORIGINAL.toString(),
                            dataStore);
            if (featureCollection.isEmpty()) {
                throw new Exception(String.format("No valid features could be retrieved for indicator %s and spatial unit %s.", indicatorId, spatialUnitId));
            }

            File exportFile = exportManager.exportFeatureCollection(featureCollection, format);

            dataStore.dispose();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + indicatorId + "." + format);
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
    public ResponseEntity<Resource> exportPublicIndicatorBySpatialUnitIdAndIdAndYearAndMonth(String indicatorId, String spatialUnitId, BigDecimal year, BigDecimal month, BigDecimal day, String format) {
        DataStore dataStore = null;
        try {
            dataStore = DatabaseHelperUtil.getPostGisDataStore();

            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) indicatorsManager
                    .getValidIndicatorFeatureCollection(
                            indicatorId,
                            spatialUnitId,
                            year,
                            month,
                            day,
                            SimplifyGeometriesEnum.ORIGINAL.toString(),
                            dataStore
                    );

            if (featureCollection.isEmpty()) {
                throw new Exception(String.format("No valid features could be retrieved for indicator %s and spatial unit %s. for date %s-%s-%s", indicatorId, spatialUnitId, year, month, day));
            }

            File exportFile = exportManager.exportFeatureCollection(featureCollection, format);

            dataStore.dispose();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kommonitor-export-" + indicatorId + "." + format);
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
    public ResponseEntity<byte[]> getPublicIndicatorBySpatialUnitIdAndIdAndYearAndMonth(
            String indicatorId,
            String spatialUnitId,
            BigDecimal year,
            BigDecimal month,
            BigDecimal day,
            String simplifyGeometries) {
        LOG.info(
                "Received request to get public indicators features for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        try {
            String geoJsonFeatures = indicatorsManager.getValidIndicatorFeatures(indicatorId, spatialUnitId, year,
                    month, day, simplifyGeometries);
            String fileName = "IndicatorFeatures_" + spatialUnitId + "_" + indicatorId + "_" + year + "-" + month
                    + "-" + day + ".json";

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
    public ResponseEntity<List<IndicatorOverviewType>> getPublicIndicators() {
        LOG.info("Received request to get all public indicators metadata");
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                List<IndicatorOverviewType> indicatorsMetadata = indicatorsManager.getAllIndicatorsMetadata();
                return new ResponseEntity<>(indicatorsMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<IndicatorOverviewType>> filterPublicIndicators(ResourceFilterType resourceFilterType) {
        LOG.info("Received request to get filtered public indicators metadata");
        String accept = request.getHeader("Accept");

        try {
            if (accept != null && accept.contains("application/json")) {
                List<IndicatorOverviewType> indicatorsMetadata = indicatorsManager.filterIndicatorsMetadata(null, resourceFilterType);
                return new ResponseEntity<>(indicatorsMetadata, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<IndicatorOverviewType> getPublicIndicatorById(String indicatorId) {
        LOG.info("Received request to get public indicator metadata for indicatorId '{}'", indicatorId);
        String accept = request.getHeader("Accept");

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
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicIndicatorBySpatialUnitIdAndIdAndYearAndMonthWithoutGeometry(
            String indicatorId,
            String spatialUnitId,
            BigDecimal year,
            BigDecimal month,
            BigDecimal day) {
        LOG.info(
                "Received request to get public indicators feature properties without geometries for spatialUnitId '{}' and Id '{}' and Date '{}-{}-{}' ",
                spatialUnitId, indicatorId, year, month, day);
        String accept = request.getHeader("Accept");

        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties =
                    indicatorsManager.getValidIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId, year,
                            month, day);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }

    @Override
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicIndicatorBySpatialUnitIdAndIdWithoutGeometry(
            String indicatorId,
            String spatialUnitId) {
        LOG.info("Received request to get public indicator feature properties for spatialUnitId '{}' and Id '{}' (without geometries)",
                spatialUnitId, indicatorId);
        String accept = request.getHeader("Accept");

        try {
            List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager.getIndicatorFeaturePropertiesWithoutGeometry(indicatorId, spatialUnitId);
            return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties, HttpStatus.OK);
        } catch (Exception e) {
            return ApiUtils.createResponseEntityFromException(e);
        }
    }
    
	@Override
    public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicSingleIndicatorFeatureById(
			String indicatorId,
			String spatialUnitId,
			String featureId) {

		LOG.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}'",
				indicatorId, spatialUnitId, featureId);
		String accept = request.getHeader("Accept");

		try {
			List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecords(indicatorId, spatialUnitId, featureId);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
					HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

	public ResponseEntity<List<IndicatorPropertiesWithoutGeomType>> getPublicSingleIndicatorFeatureRecordById(
			String indicatorId,
			String spatialUnitId,
			String featureId,
			String featureRecordId) {

		LOG.info(
				"Received request to get public single indicator feature records for datasetId '{}' and spatialUnitId '{}' and featureId '{}' and recordId '{}'",
				indicatorId, spatialUnitId, featureId, featureRecordId);
		String accept = request.getHeader("Accept");

		try {
			List<IndicatorPropertiesWithoutGeomType> indicatorFeatureProperties = indicatorsManager
					.getSingleIndicatorFeatureRecord(indicatorId, spatialUnitId, featureId, featureRecordId);
			return new ResponseEntity<List<IndicatorPropertiesWithoutGeomType>>(indicatorFeatureProperties,
					HttpStatus.OK);
		} catch (Exception e) {
			return ApiUtils.createResponseEntityFromException(e);
		}
	}

}
