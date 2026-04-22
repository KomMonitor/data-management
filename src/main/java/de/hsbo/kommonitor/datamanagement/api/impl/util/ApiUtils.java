package de.hsbo.kommonitor.datamanagement.api.impl.util;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ApiException;
import de.hsbo.kommonitor.datamanagement.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;


public class ApiUtils {
	
	private final static Logger LOG = LoggerFactory.getLogger(ApiUtils.class);
	
	/**
     * Creates a 'ResponseEntity' containing 'ErrorType' using a passed
     * 'Exception' for exception handling.
     *
     * @param exception
     * @return A 'ResponseEntity' containing an 'ErrorType' object.
     */
    public static ResponseEntity createResponseEntityFromException(Exception exception) {
        LOG.error(exception.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(exception instanceof ApiException apiEx) {
            status = HttpStatus.resolve(apiEx.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    	
    	ErrorType er = new ErrorType();
        er.setLabel(exception.getClass().getName());
        er.setMessage(exception.getMessage());
        er.setType(exception.getClass().getSimpleName());

        BodyBuilder bb = ResponseEntity.status(status);
        ResponseEntity respEn = bb
                .contentType(MediaType.parseMediaType("application/json"))
                .body(er);

        return respEn;
    }

}
