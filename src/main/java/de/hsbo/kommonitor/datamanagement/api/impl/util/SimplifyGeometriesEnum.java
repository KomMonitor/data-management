package de.hsbo.kommonitor.datamanagement.api.impl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SimplifyGeometriesEnum {
	
	  ORIGINAL("original"),
	  
	  WEAK("0.0000001"),
	  
	  MEDIUM("0.000001"),
	  
	  STRONG("0.00001");

	  private String value;

	  SimplifyGeometriesEnum(String value) {
	    this.value = value;
	  }

	  public String getValue() {
		return value;
	}

	@Override
	  @JsonValue
	  public String toString() {
	    return String.valueOf(value);
	  }

	  @JsonCreator
	  public static SimplifyGeometriesEnum fromValue(String text) {
	    for (SimplifyGeometriesEnum b : SimplifyGeometriesEnum.values()) {
	      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    Logger logger = LoggerFactory.getLogger(SimplifyGeometriesEnum.class);
	    logger.info("The submitted parameter value for parameter 'simplifiyFeatures' was set to the unrecognizable value of {}. Allowed values are {}. Will use 'ORIGINAL' as default.", text, SimplifyGeometriesEnum.values());
	    return ORIGINAL;
	  }
	}
