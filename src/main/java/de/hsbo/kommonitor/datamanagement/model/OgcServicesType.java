package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OgcServicesType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-08T15:24:50.334003500+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class OgcServicesType implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String defaultStyleName;

  private @Nullable String spatialUnit;

  private @Nullable String wfsUrl;

  private @Nullable String wmsUrl;

  public OgcServicesType defaultStyleName(String defaultStyleName) {
    this.defaultStyleName = defaultStyleName;
    return this;
  }

  /**
   * the name of the default style (SLD) that is published and applied to the associated dataset
   * @return defaultStyleName
   */
  
  @Schema(name = "defaultStyleName", description = "the name of the default style (SLD) that is published and applied to the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultStyleName")
  public String getDefaultStyleName() {
    return defaultStyleName;
  }

  public void setDefaultStyleName(String defaultStyleName) {
    this.defaultStyleName = defaultStyleName;
  }

  public OgcServicesType spatialUnit(String spatialUnit) {
    this.spatialUnit = spatialUnit;
    return this;
  }

  /**
   * the name of the spatial unit
   * @return spatialUnit
   */
  
  @Schema(name = "spatialUnit", description = "the name of the spatial unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spatialUnit")
  public String getSpatialUnit() {
    return spatialUnit;
  }

  public void setSpatialUnit(String spatialUnit) {
    this.spatialUnit = spatialUnit;
  }

  public OgcServicesType wfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

  /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
   */
  
  @Schema(name = "wfsUrl", description = "the URL of a running WFS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wfsUrl")
  public String getWfsUrl() {
    return wfsUrl;
  }

  public void setWfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }

  public OgcServicesType wmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

  /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
   */
  
  @Schema(name = "wmsUrl", description = "the URL of a running WMS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wmsUrl")
  public String getWmsUrl() {
    return wmsUrl;
  }

  public void setWmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcServicesType ogcServicesType = (OgcServicesType) o;
    return Objects.equals(this.defaultStyleName, ogcServicesType.defaultStyleName) &&
        Objects.equals(this.spatialUnit, ogcServicesType.spatialUnit) &&
        Objects.equals(this.wfsUrl, ogcServicesType.wfsUrl) &&
        Objects.equals(this.wmsUrl, ogcServicesType.wmsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultStyleName, spatialUnit, wfsUrl, wmsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OgcServicesType {\n");
    sb.append("    defaultStyleName: ").append(toIndentedString(defaultStyleName)).append("\n");
    sb.append("    spatialUnit: ").append(toIndentedString(spatialUnit)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
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

