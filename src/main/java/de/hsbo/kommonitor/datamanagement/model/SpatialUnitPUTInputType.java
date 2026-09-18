package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
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
 * SpatialUnitPUTInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitPUTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String geoJsonString;

  private @Nullable Boolean isPartialUpdate;

  private PeriodOfValidityType periodOfValidity;

  public SpatialUnitPUTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitPUTInputType(String geoJsonString, PeriodOfValidityType periodOfValidity) {
    this.geoJsonString = geoJsonString;
    this.periodOfValidity = periodOfValidity;
  }

  public SpatialUnitPUTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

  /**
   * a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'
   * @return geoJsonString
   */
  @NotNull 
  @Schema(name = "geoJsonString", example = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[7.0,51.4],[7.1,51.4],[7.1,51.5],[7.0,51.5],[7.0,51.4]]]},\"properties\":{\"uuid\":\"8f14e45f-ceea-467d-9a1b-000000000101\",\"name\":\"District Centre\"}}]}", description = "a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("geoJsonString")
  public String getGeoJsonString() {
    return geoJsonString;
  }

  @JsonProperty("geoJsonString")
  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }

  public SpatialUnitPUTInputType isPartialUpdate(@Nullable Boolean isPartialUpdate) {
    this.isPartialUpdate = isPartialUpdate;
    return this;
  }

  /**
   * if set to TRUE, then a partial upload of geometries is possible. Missing features that are already in the database will then not be deleted
   * @return isPartialUpdate
   */
  
  @Schema(name = "isPartialUpdate", example = "false", description = "if set to TRUE, then a partial upload of geometries is possible. Missing features that are already in the database will then not be deleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPartialUpdate")
  public @Nullable Boolean getIsPartialUpdate() {
    return isPartialUpdate;
  }

  @JsonProperty("isPartialUpdate")
  public void setIsPartialUpdate(@Nullable Boolean isPartialUpdate) {
    this.isPartialUpdate = isPartialUpdate;
  }

  public SpatialUnitPUTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

  /**
   * Get periodOfValidity
   * @return periodOfValidity
   */
  @NotNull @Valid 
  @Schema(name = "periodOfValidity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("periodOfValidity")
  public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  @JsonProperty("periodOfValidity")
  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitPUTInputType spatialUnitPUTInputType = (SpatialUnitPUTInputType) o;
    return Objects.equals(this.geoJsonString, spatialUnitPUTInputType.geoJsonString) &&
        Objects.equals(this.isPartialUpdate, spatialUnitPUTInputType.isPartialUpdate) &&
        Objects.equals(this.periodOfValidity, spatialUnitPUTInputType.periodOfValidity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(geoJsonString, isPartialUpdate, periodOfValidity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitPUTInputType {\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
    sb.append("    isPartialUpdate: ").append(toIndentedString(isPartialUpdate)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

