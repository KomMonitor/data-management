package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewSpatialUnitElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OrganizationalUnitPermissionOverviewTypePermissions
 */

@JsonTypeName("OrganizationalUnitPermissionOverviewType_permissions")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-03-08T11:42:46.348441096+01:00[Europe/Berlin]")
public class OrganizationalUnitPermissionOverviewTypePermissions implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid OrganizationalUnitPermissionOverviewElementType> georesources;

  @Valid
  private List<@Valid OrganizationalUnitPermissionOverviewElementType> spatialunits;

  @Valid
  private List<@Valid OrganizationalUnitPermissionOverviewElementType> indicators;

  @Valid
  private List<@Valid OrganizationalUnitPermissionOverviewSpatialUnitElementType> indicatorspatialunits;

  public OrganizationalUnitPermissionOverviewTypePermissions georesources(List<@Valid OrganizationalUnitPermissionOverviewElementType> georesources) {
    this.georesources = georesources;
    return this;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions addGeoresourcesItem(OrganizationalUnitPermissionOverviewElementType georesourcesItem) {
    if (this.georesources == null) {
      this.georesources = new ArrayList<>();
    }
    this.georesources.add(georesourcesItem);
    return this;
  }

  /**
   * Get georesources
   * @return georesources
  */
  @Valid 
  @Schema(name = "georesources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("georesources")
  public List<@Valid OrganizationalUnitPermissionOverviewElementType> getGeoresources() {
    return georesources;
  }

  public void setGeoresources(List<@Valid OrganizationalUnitPermissionOverviewElementType> georesources) {
    this.georesources = georesources;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions spatialunits(List<@Valid OrganizationalUnitPermissionOverviewElementType> spatialunits) {
    this.spatialunits = spatialunits;
    return this;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions addSpatialunitsItem(OrganizationalUnitPermissionOverviewElementType spatialunitsItem) {
    if (this.spatialunits == null) {
      this.spatialunits = new ArrayList<>();
    }
    this.spatialunits.add(spatialunitsItem);
    return this;
  }

  /**
   * Get spatialunits
   * @return spatialunits
  */
  @Valid 
  @Schema(name = "spatialunits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spatialunits")
  public List<@Valid OrganizationalUnitPermissionOverviewElementType> getSpatialunits() {
    return spatialunits;
  }

  public void setSpatialunits(List<@Valid OrganizationalUnitPermissionOverviewElementType> spatialunits) {
    this.spatialunits = spatialunits;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions indicators(List<@Valid OrganizationalUnitPermissionOverviewElementType> indicators) {
    this.indicators = indicators;
    return this;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions addIndicatorsItem(OrganizationalUnitPermissionOverviewElementType indicatorsItem) {
    if (this.indicators == null) {
      this.indicators = new ArrayList<>();
    }
    this.indicators.add(indicatorsItem);
    return this;
  }

  /**
   * Get indicators
   * @return indicators
  */
  @Valid 
  @Schema(name = "indicators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicators")
  public List<@Valid OrganizationalUnitPermissionOverviewElementType> getIndicators() {
    return indicators;
  }

  public void setIndicators(List<@Valid OrganizationalUnitPermissionOverviewElementType> indicators) {
    this.indicators = indicators;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions indicatorspatialunits(List<@Valid OrganizationalUnitPermissionOverviewSpatialUnitElementType> indicatorspatialunits) {
    this.indicatorspatialunits = indicatorspatialunits;
    return this;
  }

  public OrganizationalUnitPermissionOverviewTypePermissions addIndicatorspatialunitsItem(OrganizationalUnitPermissionOverviewSpatialUnitElementType indicatorspatialunitsItem) {
    if (this.indicatorspatialunits == null) {
      this.indicatorspatialunits = new ArrayList<>();
    }
    this.indicatorspatialunits.add(indicatorspatialunitsItem);
    return this;
  }

  /**
   * Get indicatorspatialunits
   * @return indicatorspatialunits
  */
  @Valid 
  @Schema(name = "indicatorspatialunits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorspatialunits")
  public List<@Valid OrganizationalUnitPermissionOverviewSpatialUnitElementType> getIndicatorspatialunits() {
    return indicatorspatialunits;
  }

  public void setIndicatorspatialunits(List<@Valid OrganizationalUnitPermissionOverviewSpatialUnitElementType> indicatorspatialunits) {
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
    OrganizationalUnitPermissionOverviewTypePermissions organizationalUnitPermissionOverviewTypePermissions = (OrganizationalUnitPermissionOverviewTypePermissions) o;
    return Objects.equals(this.georesources, organizationalUnitPermissionOverviewTypePermissions.georesources) &&
        Objects.equals(this.spatialunits, organizationalUnitPermissionOverviewTypePermissions.spatialunits) &&
        Objects.equals(this.indicators, organizationalUnitPermissionOverviewTypePermissions.indicators) &&
        Objects.equals(this.indicatorspatialunits, organizationalUnitPermissionOverviewTypePermissions.indicatorspatialunits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(georesources, spatialunits, indicators, indicatorspatialunits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionOverviewTypePermissions {\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

