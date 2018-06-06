package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeValueMapping;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * IndicatorPOSTInputTypeIndicatorValues
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class IndicatorPOSTInputTypeIndicatorValues   {
  @JsonProperty("timestamp")
  private LocalDate timestamp = null;

  @JsonProperty("valueMapping")
  
  private List<IndicatorPOSTInputTypeValueMapping> valueMapping = null;

  public IndicatorPOSTInputTypeIndicatorValues timestamp(LocalDate timestamp) {
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

  public IndicatorPOSTInputTypeIndicatorValues valueMapping(List<IndicatorPOSTInputTypeValueMapping> valueMapping) {
    this.valueMapping = valueMapping;
    return this;
  }

  public IndicatorPOSTInputTypeIndicatorValues addValueMappingItem(IndicatorPOSTInputTypeValueMapping valueMappingItem) {
    if (this.valueMapping == null) {
      this.valueMapping = new ArrayList<>();
    }
    this.valueMapping.add(valueMappingItem);
    return this;
  }

   /**
   * an array of entries mapping an indicator value to a spatial feature via its unique uuid as mapping key
   * @return valueMapping
  **/
  @ApiModelProperty(value = "an array of entries mapping an indicator value to a spatial feature via its unique uuid as mapping key")
  public List<IndicatorPOSTInputTypeValueMapping> getValueMapping() {
    return valueMapping;
  }

  public void setValueMapping(List<IndicatorPOSTInputTypeValueMapping> valueMapping) {
    this.valueMapping = valueMapping;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputTypeIndicatorValues indicatorPOSTInputTypeIndicatorValues = (IndicatorPOSTInputTypeIndicatorValues) o;
    return Objects.equals(this.timestamp, indicatorPOSTInputTypeIndicatorValues.timestamp) &&
        Objects.equals(this.valueMapping, indicatorPOSTInputTypeIndicatorValues.valueMapping);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, valueMapping);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeIndicatorValues {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    valueMapping: ").append(toIndentedString(valueMapping)).append("\n");
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

