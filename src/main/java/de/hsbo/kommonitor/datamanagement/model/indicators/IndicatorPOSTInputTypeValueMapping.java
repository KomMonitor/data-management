package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPOSTInputTypeValueMapping
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class IndicatorPOSTInputTypeValueMapping   {
  @JsonProperty("spatialReferenceKey")
  private String spatialReferenceKey = null;

  @JsonProperty("indicatorValue")
  private Float indicatorValue = null;

  public IndicatorPOSTInputTypeValueMapping spatialReferenceKey(String spatialReferenceKey) {
    this.spatialReferenceKey = spatialReferenceKey;
    return this;
  }

   /**
   * identifier (uuid) of the spatial feature to which the value shall be applied
   * @return spatialReferenceKey
  **/
  @ApiModelProperty(value = "identifier (uuid) of the spatial feature to which the value shall be applied")
  public String getSpatialReferenceKey() {
    return spatialReferenceKey;
  }

  public void setSpatialReferenceKey(String spatialReferenceKey) {
    this.spatialReferenceKey = spatialReferenceKey;
  }

  public IndicatorPOSTInputTypeValueMapping indicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
    return this;
  }

   /**
   * the numeric extent of the indicator
   * @return indicatorValue
  **/
  @ApiModelProperty(value = "the numeric extent of the indicator")
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
    return Objects.equals(this.spatialReferenceKey, indicatorPOSTInputTypeValueMapping.spatialReferenceKey) &&
        Objects.equals(this.indicatorValue, indicatorPOSTInputTypeValueMapping.indicatorValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialReferenceKey, indicatorValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeValueMapping {\n");
    
    sb.append("    spatialReferenceKey: ").append(toIndentedString(spatialReferenceKey)).append("\n");
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

