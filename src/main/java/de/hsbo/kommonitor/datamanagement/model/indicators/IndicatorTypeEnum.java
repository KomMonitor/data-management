package de.hsbo.kommonitor.datamanagement.model.indicators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * indicates whether the indicator is a status indicator (values represent the extent of the watched phenomenon for a certain point in time) or a dynamic indicator (values represent the change of extent of the watched phenomenon within a certain period of time)
 */
public enum IndicatorTypeEnum {
  STATUS_ABSOLUTE("STATUS_ABSOLUTE"),
  
  DYNAMIC_ABSOLUTE("DYNAMIC_ABSOLUTE"),
	
  STATUS_RELATIVE("STATUS_RELATIVE"),
	  
  DYNAMIC_RELATIVE("DYNAMIC_RELATIVE"),
	
  STATUS_STANDARDIZED("STATUS_STANDARDIZED"),
	  
  DYNAMIC_STANDARDIZED("DYNAMIC_STANDARDIZED");	

  private String value;

  IndicatorTypeEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static IndicatorTypeEnum fromValue(String text) {
    for (IndicatorTypeEnum b : IndicatorTypeEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
