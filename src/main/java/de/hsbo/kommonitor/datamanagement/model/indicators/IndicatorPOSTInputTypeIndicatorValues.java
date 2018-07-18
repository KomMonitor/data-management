package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPOSTInputTypeIndicatorValues
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-18T20:11:44.438+02:00")

public class IndicatorPOSTInputTypeIndicatorValues   {
  @JsonProperty("spatialReferenceKey")
  private String spatialReferenceKey = null;

  @JsonProperty("valueMapping")
  
  private List<IndicatorPOSTInputTypeValueMapping> valueMapping = null;

  public IndicatorPOSTInputTypeIndicatorValues spatialReferenceKey(String spatialReferenceKey) {
    this.spatialReferenceKey = spatialReferenceKey;
    return this;
  }

   /**
   * identifier (uuid) of the spatial feature to which the values shall be applied
   * @return spatialReferenceKey
  **/
  @ApiModelProperty(value = "identifier (uuid) of the spatial feature to which the values shall be applied")
  public String getSpatialReferenceKey() {
    return spatialReferenceKey;
  }

  public void setSpatialReferenceKey(String spatialReferenceKey) {
    this.spatialReferenceKey = spatialReferenceKey;
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
   * an array of entries mapping an indicator value to a timestamp as mapping key
   * @return valueMapping
  **/
  @ApiModelProperty(value = "an array of entries mapping an indicator value to a timestamp as mapping key")
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
    return Objects.equals(this.spatialReferenceKey, indicatorPOSTInputTypeIndicatorValues.spatialReferenceKey) &&
        Objects.equals(this.valueMapping, indicatorPOSTInputTypeIndicatorValues.valueMapping);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialReferenceKey, valueMapping);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeIndicatorValues {\n");
    
    sb.append("    spatialReferenceKey: ").append(toIndentedString(spatialReferenceKey)).append("\n");
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

