package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * OgcServicesType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-08-29T11:30:05.521+02:00")

public class OgcServicesType   {
  @JsonProperty("spatialUnit")
  private String spatialUnit = null;

  @JsonProperty("wmsUrl")
  private String wmsUrl = null;

  @JsonProperty("wfsUrl")
  private String wfsUrl = null;

  public OgcServicesType spatialUnit(String spatialUnit) {
    this.spatialUnit = spatialUnit;
    return this;
  }

   /**
   * the name of the spatial unit
   * @return spatialUnit
  **/
  @ApiModelProperty(value = "the name of the spatial unit")
  public String getSpatialUnit() {
    return spatialUnit;
  }

  public void setSpatialUnit(String spatialUnit) {
    this.spatialUnit = spatialUnit;
  }

  public OgcServicesType wmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

   /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
  **/
  @ApiModelProperty(required = true, value = "the URL of a running WMS instance serving the spatial features of the associated dataset")
  public String getWmsUrl() {
    return wmsUrl;
  }

  public void setWmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
  }

  public OgcServicesType wfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

   /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
  **/
  @ApiModelProperty(required = true, value = "the URL of a running WFS instance serving the spatial features of the associated dataset")
  public String getWfsUrl() {
    return wfsUrl;
  }

  public void setWfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcServicesType ogcServicesType = (OgcServicesType) o;
    return Objects.equals(this.spatialUnit, ogcServicesType.spatialUnit) &&
        Objects.equals(this.wmsUrl, ogcServicesType.wmsUrl) &&
        Objects.equals(this.wfsUrl, ogcServicesType.wfsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnit, wmsUrl, wfsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OgcServicesType {\n");
    
    sb.append("    spatialUnit: ").append(toIndentedString(spatialUnit)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
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

