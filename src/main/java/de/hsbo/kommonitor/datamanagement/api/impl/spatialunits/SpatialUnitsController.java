package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.SpatialUnitsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
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
	
	//TODO fill methods

	@Override
	public ResponseEntity addSpatialUnitAsBody(SpatialUnitPOSTInputType featureData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity deleteSpatialUnitByUnit(String spatialUnitLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity deleteSpatialUnitByUnitAndYearAndMonth(String spatialUnitLevel, BigDecimal year,
			BigDecimal month, BigDecimal day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<SpatialUnitOverviewType>> getSpatialUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<SpatialUnitOverviewType> getSpatialUnitsByLevel(String spatialUnitLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<byte[]> getSpatialUnitsByLevelAndYearAndMonth(String spatialUnitLevel, BigDecimal year,
			BigDecimal month, BigDecimal day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> getSpatialUnitsSchemaByLevel(String spatialUnitLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity updateSpatialUnitAsBody(String spatialUnitLevel, SpatialUnitPUTInputType featureData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity updateSpatialUnitMetadataAsBody(String spatialUnitLevel, SpatialUnitPATCHInputType metadata) {
		// TODO Auto-generated method stub
		return null;
	}

}
