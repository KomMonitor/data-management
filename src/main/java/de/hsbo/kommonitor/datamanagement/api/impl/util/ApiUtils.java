package de.hsbo.kommonitor.datamanagement.api.impl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import de.hsbo.kommonitor.datamanagement.model.legacy.ErrorType;

public class ApiUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ApiUtils.class);
	
	/**
     * Creates a 'ResponseEntity' containing 'ErrorType' using a passed
     * 'Exeption' for exception handling.
     *
     * @param exception
     * @return A 'ResponseEntity' containing an 'ErrorType' object.
     */
    public static ResponseEntity createResponseEntityFromException(Exception exception) {
        exception.printStackTrace();
        logger.error(exception.getMessage());
    	
    	ErrorType er = new ErrorType();
        er.setLabel(exception.getClass().getName());
        er.setMessage(exception.getMessage());
        er.setType(exception.getClass().getSimpleName());

        BodyBuilder bb = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity respEn = bb.contentType(MediaType.parseMediaType("application/json"))
                .body(er);

        return respEn;
    }

}
