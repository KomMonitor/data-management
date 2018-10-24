package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.HashMap;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPropertiesWithoutGeomType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-10-24T11:17:31.441+02:00")

public class IndicatorPropertiesWithoutGeomType extends HashMap<String, String>  {
  @JsonProperty("spatialUnitFeatureId")
  private String spatialUnitFeatureId = null;

  @JsonProperty("spatialUnitFeatureName")
  private String spatialUnitFeatureName = null;

  @JsonProperty("validStartDate")
  private String validStartDate = null;

  @JsonProperty("validEndDate")
  private String validEndDate = null;

  public IndicatorPropertiesWithoutGeomType spatialUnitFeatureId(String spatialUnitFeatureId) {
    this.spatialUnitFeatureId = spatialUnitFeatureId;
    return this;
  }

   /**
   * the id of the spatial feature
   * @return spatialUnitFeatureId
  **/
  @ApiModelProperty(required = true, value = "the id of the spatial feature")
  public String getSpatialUnitFeatureId() {
    return spatialUnitFeatureId;
  }

  public void setSpatialUnitFeatureId(String spatialUnitFeatureId) {
    this.spatialUnitFeatureId = spatialUnitFeatureId;
  }

  public IndicatorPropertiesWithoutGeomType spatialUnitFeatureName(String spatialUnitFeatureName) {
    this.spatialUnitFeatureName = spatialUnitFeatureName;
    return this;
  }

   /**
   * the name of the spatial feature
   * @return spatialUnitFeatureName
  **/
  @ApiModelProperty(required = true, value = "the name of the spatial feature")
  public String getSpatialUnitFeatureName() {
    return spatialUnitFeatureName;
  }

  public void setSpatialUnitFeatureName(String spatialUnitFeatureName) {
    this.spatialUnitFeatureName = spatialUnitFeatureName;
  }

  public IndicatorPropertiesWithoutGeomType validStartDate(String validStartDate) {
    this.validStartDate = validStartDate;
    return this;
  }

   /**
   * the start date from which on the spatial feature is valid
   * @return validStartDate
  **/
  @ApiModelProperty(required = true, value = "the start date from which on the spatial feature is valid")
  public String getValidStartDate() {
    return validStartDate;
  }

  public void setValidStartDate(String validStartDate) {
    this.validStartDate = validStartDate;
  }

  public IndicatorPropertiesWithoutGeomType validEndDate(String validEndDate) {
    this.validEndDate = validEndDate;
    return this;
  }

   /**
   * the end date until the spatial feature is valid - or null if not set
   * @return validEndDate
  **/
  @ApiModelProperty(required = true, value = "the end date until the spatial feature is valid - or null if not set")
  public String getValidEndDate() {
    return validEndDate;
  }

  public void setValidEndDate(String validEndDate) {
    this.validEndDate = validEndDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPropertiesWithoutGeomType indicatorPropertiesWithoutGeomType = (IndicatorPropertiesWithoutGeomType) o;
    return Objects.equals(this.spatialUnitFeatureId, indicatorPropertiesWithoutGeomType.spatialUnitFeatureId) &&
        Objects.equals(this.spatialUnitFeatureName, indicatorPropertiesWithoutGeomType.spatialUnitFeatureName) &&
        Objects.equals(this.validStartDate, indicatorPropertiesWithoutGeomType.validStartDate) &&
        Objects.equals(this.validEndDate, indicatorPropertiesWithoutGeomType.validEndDate) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitFeatureId, spatialUnitFeatureName, validStartDate, validEndDate, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPropertiesWithoutGeomType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    spatialUnitFeatureId: ").append(toIndentedString(spatialUnitFeatureId)).append("\n");
    sb.append("    spatialUnitFeatureName: ").append(toIndentedString(spatialUnitFeatureName)).append("\n");
    sb.append("    validStartDate: ").append(toIndentedString(validStartDate)).append("\n");
    sb.append("    validEndDate: ").append(toIndentedString(validEndDate)).append("\n");
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

