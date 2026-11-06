package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionInputTypePermissions;
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
 * Batch element describing the permissions to set for one organizational unit across all resource
 * types. Only the listed datasets of the listed resource types are updated; everything else stays
 * untouched (PATCH semantics).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class OrganizationalUnitPermissionInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID organizationalUnitId;

  private OrganizationalUnitPermissionInputTypePermissions permissions;

  public OrganizationalUnitPermissionInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionInputType(UUID organizationalUnitId, OrganizationalUnitPermissionInputTypePermissions permissions) {
    this.organizationalUnitId = organizationalUnitId;
    this.permissions = permissions;
  }

  public OrganizationalUnitPermissionInputType organizationalUnitId(UUID organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
    return this;
  }

  /**
   * id of the organizational unit whose permissions are updated
   * @return organizationalUnitId
   */
  @NotNull @Valid
  @Schema(name = "organizationalUnitId", description = "id of the organizational unit whose permissions are updated", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("organizationalUnitId")
  public UUID getOrganizationalUnitId() {
    return organizationalUnitId;
  }

  @JsonProperty("organizationalUnitId")
  public void setOrganizationalUnitId(UUID organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
  }

  public OrganizationalUnitPermissionInputType permissions(OrganizationalUnitPermissionInputTypePermissions permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * Get permissions
   * @return permissions
   */
  @NotNull @Valid
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public OrganizationalUnitPermissionInputTypePermissions getPermissions() {
    return permissions;
  }

  @JsonProperty("permissions")
  public void setPermissions(OrganizationalUnitPermissionInputTypePermissions permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitPermissionInputType organizationalUnitPermissionInputType = (OrganizationalUnitPermissionInputType) o;
    return Objects.equals(this.organizationalUnitId, organizationalUnitPermissionInputType.organizationalUnitId) &&
        Objects.equals(this.permissions, organizationalUnitPermissionInputType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organizationalUnitId, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionInputType {\n");
    sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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
