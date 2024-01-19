package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IndicatorPUTInputTypeValueMapping
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T14:41:06.171227400+01:00[Europe/Berlin]")
public class IndicatorPUTInputTypeValueMapping implements Serializable {

  private static final long serialVersionUID = 1L;

  private Float indicatorValue;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate timestamp;

  public IndicatorPUTInputTypeValueMapping indicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
    return this;
  }

  /**
   * the numeric extent of the indicator for the timestamp
   * @return indicatorValue
  */
  
  @Schema(name = "indicatorValue", description = "the numeric extent of the indicator for the timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorValue")
  public Float getIndicatorValue() {
    return indicatorValue;
  }

  public void setIndicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
  }

  public IndicatorPUTInputTypeValueMapping timestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * timestamp consisting of year, month and day according to ISO 8601 (e.g. 2018-01-30)
   * @return timestamp
  */
  @Valid 
  @Schema(name = "timestamp", description = "timestamp consisting of year, month and day according to ISO 8601 (e.g. 2018-01-30)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPUTInputTypeValueMapping indicatorPUTInputTypeValueMapping = (IndicatorPUTInputTypeValueMapping) o;
    return Objects.equals(this.indicatorValue, indicatorPUTInputTypeValueMapping.indicatorValue) &&
        Objects.equals(this.timestamp, indicatorPUTInputTypeValueMapping.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorValue, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPUTInputTypeValueMapping {\n");
    sb.append("    indicatorValue: ").append(toIndentedString(indicatorValue)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

