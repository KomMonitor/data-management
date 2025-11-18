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
 * Gets or Sets TopicOrderModeEnum
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-18T12:07:10.385885300+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public enum TopicOrderModeEnum implements Serializable {
  
  ALPHABETICAL("alphabetical"),
  
  CUSTOM("custom");

  private final String value;

  TopicOrderModeEnum(String value) {
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
  public static TopicOrderModeEnum fromValue(String value) {
    for (TopicOrderModeEnum b : TopicOrderModeEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

