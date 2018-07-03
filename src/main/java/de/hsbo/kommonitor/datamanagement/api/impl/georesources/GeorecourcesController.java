package de.hsbo.kommonitor.datamanagement.api.impl.georesources;

import java.math.BigDecimal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.TopicsApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.util.ApiUtils;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicInputType;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicOverviewType;

import de.hsbo.kommonitor.datamanagement.api.GeoresourcesApi;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePUTInputType;


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
	
	//TODO fill methods
	
	@Override
	public ResponseEntity addGeoresourceAsBody(GeoresourcePOSTInputType featureData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity deleteGeoresourceById(String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity deleteGeoresourceByIdAndYearAndMonth(String georesourceId, BigDecimal year, BigDecimal month,
			BigDecimal day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<GeoresourceOverviewType>> getGeoresource(String topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<GeoresourceOverviewType> getGeoresourceById(String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<byte[]> getGeoresourceByIdAndYearAndMonth(String georesourceId, BigDecimal year,
			BigDecimal month, BigDecimal day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> getGeoresourceSchemaByLevel(String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity updateGeoresourceAsBody(String georesourceId, GeoresourcePUTInputType featureData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity updateGeoresourceMetadataAsBody(String georesourceId, GeoresourcePATCHInputType metadata) {
		// TODO Auto-generated method stub
		return null;
	}

}
