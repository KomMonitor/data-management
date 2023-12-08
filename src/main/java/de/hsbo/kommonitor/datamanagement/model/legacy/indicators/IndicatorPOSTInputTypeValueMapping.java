package de.hsbo.kommonitor.datamanagement.model.legacy.indicators;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPOSTInputTypeValueMapping
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-18T20:11:44.438+02:00")

public class IndicatorPOSTInputTypeValueMapping   {
  @JsonProperty("timestamp")
  private LocalDate timestamp = null;

  @JsonProperty("indicatorValue")
  private Float indicatorValue = null;

  public IndicatorPOSTInputTypeValueMapping timestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
    return this;
  }

   /**
   * timestamp consisting of year, month and day according to ISO 8601 (e.g. 2018-01-30)
   * @return timestamp
  **/
  @ApiModelProperty(value = "timestamp consisting of year, month and day according to ISO 8601 (e.g. 2018-01-30)")
  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public IndicatorPOSTInputTypeValueMapping indicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
    return this;
  }

   /**
   * the numeric extent of the indicator for the timestamp
   * @return indicatorValue
  **/
  @ApiModelProperty(value = "the numeric extent of the indicator for the timestamp", example = "0")
  public Float getIndicatorValue() {
    return indicatorValue;
  }

  public void setIndicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputTypeValueMapping indicatorPOSTInputTypeValueMapping = (IndicatorPOSTInputTypeValueMapping) o;
    return Objects.equals(this.timestamp, indicatorPOSTInputTypeValueMapping.timestamp) &&
        Objects.equals(this.indicatorValue, indicatorPOSTInputTypeValueMapping.indicatorValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, indicatorValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeValueMapping {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    indicatorValue: ").append(toIndentedString(indicatorValue)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

