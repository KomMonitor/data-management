package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionInputElementType;
import java.util.ArrayList;
import java.util.List;
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
 * Permissions to set for a single indicator / spatial-unit combination of an organizational unit.
 * Identified by the indicator id ({@code id}) together with the {@code spatialUnitId}.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class OrganizationalUnitPermissionInputSpatialUnitElementType extends OrganizationalUnitPermissionInputElementType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID spatialUnitId;

  public OrganizationalUnitPermissionInputSpatialUnitElementType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionInputSpatialUnitElementType(UUID spatialUnitId, UUID id, List<UUID> permissions) {
    super(id, permissions);
    this.spatialUnitId = spatialUnitId;
  }

  public OrganizationalUnitPermissionInputSpatialUnitElementType spatialUnitId(UUID spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * business id of the spatial unit the indicator is joined with
   * @return spatialUnitId
   */
  @NotNull @Valid
  @Schema(name = "spatialUnitId", description = "business id of the spatial unit the indicator is joined with", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitId")
  public UUID getSpatialUnitId() {
    return spatialUnitId;
  }

  @JsonProperty("spatialUnitId")
  public void setSpatialUnitId(UUID spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }


  public OrganizationalUnitPermissionInputSpatialUnitElementType id(UUID id) {
    super.id(id);
    return this;
  }

  public OrganizationalUnitPermissionInputSpatialUnitElementType permissions(List<UUID> permissions) {
    super.permissions(permissions);
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
    OrganizationalUnitPermissionInputSpatialUnitElementType organizationalUnitPermissionInputSpatialUnitElementType = (OrganizationalUnitPermissionInputSpatialUnitElementType) o;
    return Objects.equals(this.spatialUnitId, organizationalUnitPermissionInputSpatialUnitElementType.spatialUnitId) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionInputSpatialUnitElementType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
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
