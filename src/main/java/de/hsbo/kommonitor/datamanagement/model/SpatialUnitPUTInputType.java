package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitPUTInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class SpatialUnitPUTInputType   {
  @JsonProperty("periodOfValidity")
  private PeriodOfValidityType periodOfValidity = null;

  @JsonProperty("geoJsonString")
  private String geoJsonString = null;

  public SpatialUnitPUTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

   /**
   * Get periodOfValidity
   * @return periodOfValidity
  **/
  @ApiModelProperty(required = true, value = "")
  public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  public SpatialUnitPUTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

   /**
   * a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'
   * @return geoJsonString
  **/
  @ApiModelProperty(required = true, value = "a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'")
  public String getGeoJsonString() {
    return geoJsonString;
  }

  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitPUTInputType spatialUnitPUTInputType = (SpatialUnitPUTInputType) o;
    return Objects.equals(this.periodOfValidity, spatialUnitPUTInputType.periodOfValidity) &&
        Objects.equals(this.geoJsonString, spatialUnitPUTInputType.geoJsonString);
  }

  @Override
  public int hashCode() {
    return Objects.hash(periodOfValidity, geoJsonString);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitPUTInputType {\n");
    
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
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

