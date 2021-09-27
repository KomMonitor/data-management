package de.hsbo.kommonitor.datamanagement.api.impl.database;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsbo.kommonitor.datamanagement.api.DatabasePublicApi;
import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;
import de.hsbo.kommonitor.datamanagement.model.database.LastModificationOverviewType;

@Controller
public class DatabasePublicController extends BasePathController implements DatabasePublicApi {

	private static Logger logger = LoggerFactory.getLogger(DatabasePublicController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	LastModificationManager lastModManager;

	@org.springframework.beans.factory.annotation.Autowired
	public DatabasePublicController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<LastModificationOverviewType> getLastModification() {
		logger.info("Received request to get last database modification");
		String accept = request.getHeader("Accept");
		try {
			LastModificationOverviewType lastMod = LastModificationMapper.mapToSwaggerModification(lastModManager.getLastModifcationInfo());

			return new ResponseEntity<LastModificationOverviewType>(lastMod, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while fetching response objects", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
