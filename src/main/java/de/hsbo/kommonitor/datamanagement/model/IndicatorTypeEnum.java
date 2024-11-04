package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * indicates whether the indicator is a status indicator (values represent the extent of the watched phenomenon for a certain point in time) or a dynamic indicator (values represent the change of extent of the watched phenomenon within a certain period of time)
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-04T15:13:26.315379200+01:00[Europe/Berlin]")
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

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static IndicatorTypeEnum fromValue(String value) {
    for (IndicatorTypeEnum b : IndicatorTypeEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

