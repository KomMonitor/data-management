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
 * the poi marker type, either text or symbol
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public enum PoiMarkerStyleEnum implements Serializable {
  
  TEXT("text"),
  
  SYMBOL("symbol");

  private final String value;

  PoiMarkerStyleEnum(String value) {
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
  public static PoiMarkerStyleEnum fromValue(String value) {
    for (PoiMarkerStyleEnum b : PoiMarkerStyleEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

