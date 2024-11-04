package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:09.865691461+01:00[Europe/Berlin]")
public class OrganizationalUnitPermissionOverviewSpatialUnitElementType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  private String permissionLevel;

  private UUID roleId;

  private UUID spatialUnitId;

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionOverviewSpatialUnitElementType(UUID id, String permissionLevel, UUID roleId, UUID spatialUnitId) {
    this.id = id;
    this.permissionLevel = permissionLevel;
    this.roleId = roleId;
    this.spatialUnitId = spatialUnitId;
  }

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType permissionLevel(String permissionLevel) {
    this.permissionLevel = permissionLevel;
    return this;
  }

  /**
   * Get permissionLevel
   * @return permissionLevel
  */
  @NotNull 
  @Schema(name = "permissionLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissionLevel")
  public String getPermissionLevel() {
    return permissionLevel;
  }

  public void setPermissionLevel(String permissionLevel) {
    this.permissionLevel = permissionLevel;
  }

  public OrganizationalUnitPermissionOverviewSpatialUnitElementType roleId(UUID roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * Get roleId
   * @return roleId
  */
  @NotNull @Valid 
  @Schema(name = "roleId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roleId")
  public UUID getRoleId() {
    return roleId;
  }

  public void setRoleId(UUID roleId) {
    this.roleId = roleId;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitPermissionOverviewSpatialUnitElementType organizationalUnitPermissionOverviewSpatialUnitElementType = (OrganizationalUnitPermissionOverviewSpatialUnitElementType) o;
    return Objects.equals(this.id, organizationalUnitPermissionOverviewSpatialUnitElementType.id) &&
        Objects.equals(this.permissionLevel, organizationalUnitPermissionOverviewSpatialUnitElementType.permissionLevel) &&
        Objects.equals(this.roleId, organizationalUnitPermissionOverviewSpatialUnitElementType.roleId) &&
        Objects.equals(this.spatialUnitId, organizationalUnitPermissionOverviewSpatialUnitElementType.spatialUnitId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, permissionLevel, roleId, spatialUnitId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionOverviewSpatialUnitElementType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    permissionLevel: ").append(toIndentedString(permissionLevel)).append("\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
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

