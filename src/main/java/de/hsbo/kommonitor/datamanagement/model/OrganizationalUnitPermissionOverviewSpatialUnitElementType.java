package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewElementType;
import java.util.UUID;
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
 * OrganizationalUnitPermissionOverviewSpatialUnitElementType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class OrganizationalUnitPermissionOverviewSpatialUnitElementType extends OrganizationalUnitPermissionOverviewElementType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID spatialUnitId;

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionOverviewSpatialUnitElementType(UUID spatialUnitId, UUID id, String permissionLevel, UUID roleId) {
    super(id, permissionLevel, roleId);
    this.spatialUnitId = spatialUnitId;
  }

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType spatialUnitId(UUID spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * Get spatialUnitId
   * @return spatialUnitId
   */
  @NotNull @Valid 
  @Schema(name = "spatialUnitId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitId")
  public UUID getSpatialUnitId() {
    return spatialUnitId;
  }

  public void setSpatialUnitId(UUID spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }


  public OrganizationalUnitPermissionOverviewSpatialUnitElementType id(UUID id) {
    super.id(id);
    return this;
  }

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType permissionLevel(String permissionLevel) {
    super.permissionLevel(permissionLevel);
    return this;
  }

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType roleId(UUID roleId) {
    super.roleId(roleId);
    return this;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitPermissionOverviewSpatialUnitElementType organizationalUnitPermissionOverviewSpatialUnitElementType = (OrganizationalUnitPermissionOverviewSpatialUnitElementType) o;
    return Objects.equals(this.spatialUnitId, organizationalUnitPermissionOverviewSpatialUnitElementType.spatialUnitId) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionOverviewSpatialUnitElementType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
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

