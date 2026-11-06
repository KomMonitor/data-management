package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionInputElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionInputSpatialUnitElementType;
import java.util.ArrayList;
import java.util.List;
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
 * Per-resource-type permissions to set for an organizational unit. Each list is optional; an omitted
 * (or null) list leaves that resource type untouched.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class OrganizationalUnitPermissionInputTypePermissions implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid OrganizationalUnitPermissionInputElementType> georesources = new ArrayList<>();

  @Valid
  private List<@Valid OrganizationalUnitPermissionInputElementType> spatialunits = new ArrayList<>();

  @Valid
  private List<@Valid OrganizationalUnitPermissionInputElementType> indicators = new ArrayList<>();

  @Valid
  private List<@Valid OrganizationalUnitPermissionInputSpatialUnitElementType> indicatorspatialunits = new ArrayList<>();

  public OrganizationalUnitPermissionInputTypePermissions georesources(List<@Valid OrganizationalUnitPermissionInputElementType> georesources) {
    this.georesources = georesources;
    return this;
  }

  public OrganizationalUnitPermissionInputTypePermissions addGeoresourcesItem(OrganizationalUnitPermissionInputElementType georesourcesItem) {
    if (this.georesources == null) {
      this.georesources = new ArrayList<>();
    }
    this.georesources.add(georesourcesItem);
    return this;
  }

  @Valid
  @Schema(name = "georesources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("georesources")
  public List<@Valid OrganizationalUnitPermissionInputElementType> getGeoresources() {
    return georesources;
  }

  @JsonProperty("georesources")
  public void setGeoresources(List<@Valid OrganizationalUnitPermissionInputElementType> georesources) {
    this.georesources = georesources;
  }

  public OrganizationalUnitPermissionInputTypePermissions spatialunits(List<@Valid OrganizationalUnitPermissionInputElementType> spatialunits) {
    this.spatialunits = spatialunits;
    return this;
  }

  public OrganizationalUnitPermissionInputTypePermissions addSpatialunitsItem(OrganizationalUnitPermissionInputElementType spatialunitsItem) {
    if (this.spatialunits == null) {
      this.spatialunits = new ArrayList<>();
    }
    this.spatialunits.add(spatialunitsItem);
    return this;
  }

  @Valid
  @Schema(name = "spatialunits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spatialunits")
  public List<@Valid OrganizationalUnitPermissionInputElementType> getSpatialunits() {
    return spatialunits;
  }

  @JsonProperty("spatialunits")
  public void setSpatialunits(List<@Valid OrganizationalUnitPermissionInputElementType> spatialunits) {
    this.spatialunits = spatialunits;
  }

  public OrganizationalUnitPermissionInputTypePermissions indicators(List<@Valid OrganizationalUnitPermissionInputElementType> indicators) {
    this.indicators = indicators;
    return this;
  }

  public OrganizationalUnitPermissionInputTypePermissions addIndicatorsItem(OrganizationalUnitPermissionInputElementType indicatorsItem) {
    if (this.indicators == null) {
      this.indicators = new ArrayList<>();
    }
    this.indicators.add(indicatorsItem);
    return this;
  }

  @Valid
  @Schema(name = "indicators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicators")
  public List<@Valid OrganizationalUnitPermissionInputElementType> getIndicators() {
    return indicators;
  }

  @JsonProperty("indicators")
  public void setIndicators(List<@Valid OrganizationalUnitPermissionInputElementType> indicators) {
    this.indicators = indicators;
  }

  public OrganizationalUnitPermissionInputTypePermissions indicatorspatialunits(List<@Valid OrganizationalUnitPermissionInputSpatialUnitElementType> indicatorspatialunits) {
    this.indicatorspatialunits = indicatorspatialunits;
    return this;
  }

  public OrganizationalUnitPermissionInputTypePermissions addIndicatorspatialunitsItem(OrganizationalUnitPermissionInputSpatialUnitElementType indicatorspatialunitsItem) {
    if (this.indicatorspatialunits == null) {
      this.indicatorspatialunits = new ArrayList<>();
    }
    this.indicatorspatialunits.add(indicatorspatialunitsItem);
    return this;
  }

  @Valid
  @Schema(name = "indicatorspatialunits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorspatialunits")
  public List<@Valid OrganizationalUnitPermissionInputSpatialUnitElementType> getIndicatorspatialunits() {
    return indicatorspatialunits;
  }

  @JsonProperty("indicatorspatialunits")
  public void setIndicatorspatialunits(List<@Valid OrganizationalUnitPermissionInputSpatialUnitElementType> indicatorspatialunits) {
    this.indicatorspatialunits = indicatorspatialunits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitPermissionInputTypePermissions organizationalUnitPermissionInputTypePermissions = (OrganizationalUnitPermissionInputTypePermissions) o;
    return Objects.equals(this.georesources, organizationalUnitPermissionInputTypePermissions.georesources) &&
        Objects.equals(this.spatialunits, organizationalUnitPermissionInputTypePermissions.spatialunits) &&
        Objects.equals(this.indicators, organizationalUnitPermissionInputTypePermissions.indicators) &&
        Objects.equals(this.indicatorspatialunits, organizationalUnitPermissionInputTypePermissions.indicatorspatialunits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(georesources, spatialunits, indicators, indicatorspatialunits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionInputTypePermissions {\n");
    sb.append("    georesources: ").append(toIndentedString(georesources)).append("\n");
    sb.append("    spatialunits: ").append(toIndentedString(spatialunits)).append("\n");
    sb.append("    indicators: ").append(toIndentedString(indicators)).append("\n");
    sb.append("    indicatorspatialunits: ").append(toIndentedString(indicatorspatialunits)).append("\n");
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
