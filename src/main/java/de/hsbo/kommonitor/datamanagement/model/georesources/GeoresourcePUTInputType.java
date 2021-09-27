package de.hsbo.kommonitor.datamanagement.model.georesources;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModelProperty;

/**
 * GeoresourcePUTInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-31T18:56:11.978Z")


public class GeoresourcePUTInputType   {
  @JsonProperty("geoJsonString")
  private String geoJsonString = null;

  @JsonProperty("periodOfValidity")
  private PeriodOfValidityType periodOfValidity = null;

  @JsonProperty("isPartialUpdate")
  private Boolean isPartialUpdate = false;

  public GeoresourcePUTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

  /**
   * a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset
   * @return geoJsonString
  **/
  @ApiModelProperty(required = true, value = "a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset")
  @NotNull


  public String getGeoJsonString() {
    return geoJsonString;
  }

  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }

  public GeoresourcePUTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

  /**
   * Get periodOfValidity
   * @return periodOfValidity
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  public GeoresourcePUTInputType isPartialUpdate(Boolean isPartialUpdate) {
    this.isPartialUpdate = isPartialUpdate;
    return this;
  }

  /**
   * if set to TRUE, then a partial upload of geometries is possible. Missing features that are already in the database will then not be deleted
   * @return isPartialUpdate
  **/
  @ApiModelProperty(value = "if set to TRUE, then a partial upload of geometries is possible. Missing features that are already in the database will then not be deleted")


  public Boolean isIsPartialUpdate() {
    return isPartialUpdate;
  }

  public void setIsPartialUpdate(Boolean isPartialUpdate) {
    this.isPartialUpdate = isPartialUpdate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourcePUTInputType georesourcePUTInputType = (GeoresourcePUTInputType) o;
    return Objects.equals(this.geoJsonString, georesourcePUTInputType.geoJsonString) &&
        Objects.equals(this.periodOfValidity, georesourcePUTInputType.periodOfValidity) &&
        Objects.equals(this.isPartialUpdate, georesourcePUTInputType.isPartialUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(geoJsonString, periodOfValidity, isPartialUpdate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePUTInputType {\n");
    
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    isPartialUpdate: ").append(toIndentedString(isPartialUpdate)).append("\n");
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

