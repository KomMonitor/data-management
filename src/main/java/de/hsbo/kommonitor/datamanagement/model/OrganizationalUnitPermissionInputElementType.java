package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Permissions to set for a single dataset of an organizational unit. The {@code permissions} list
 * holds the ids of the {@code PermissionEntity} grants (belonging to the organizational unit) that
 * the dataset should have after the update.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class OrganizationalUnitPermissionInputElementType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  @Valid
  private List<UUID> permissions = new ArrayList<>();

  public OrganizationalUnitPermissionInputElementType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionInputElementType(UUID id, List<UUID> permissions) {
    this.id = id;
    this.permissions = permissions;
  }

  public OrganizationalUnitPermissionInputElementType id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * business id of the dataset the permissions apply to
   * @return id
   */
  @NotNull @Valid
  @Schema(name = "id", description = "business id of the dataset the permissions apply to", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  public OrganizationalUnitPermissionInputElementType permissions(List<UUID> permissions) {
    this.permissions = permissions;
    return this;
  }

  public OrganizationalUnitPermissionInputElementType addPermissionsItem(UUID permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * ids of the permissions the dataset should have for the organizational unit
   * @return permissions
   */
  @NotNull @Valid
  @Schema(name = "permissions", description = "ids of the permissions the dataset should have for the organizational unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<UUID> getPermissions() {
    return permissions;
  }

  @JsonProperty("permissions")
  public void setPermissions(List<UUID> permissions) {
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
    OrganizationalUnitPermissionInputElementType organizationalUnitPermissionInputElementType = (OrganizationalUnitPermissionInputElementType) o;
    return Objects.equals(this.id, organizationalUnitPermissionInputElementType.id) &&
        Objects.equals(this.permissions, organizationalUnitPermissionInputElementType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionInputElementType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
