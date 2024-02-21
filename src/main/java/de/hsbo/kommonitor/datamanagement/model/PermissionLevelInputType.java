package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PermissionLevelInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:09.865691461+01:00[Europe/Berlin]")
public class PermissionLevelInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> permissions = new ArrayList<>();

  private Boolean isPublic;

  public PermissionLevelInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PermissionLevelInputType(List<String> permissions, Boolean isPublic) {
    this.permissions = permissions;
    this.isPublic = isPublic;
  }

  public PermissionLevelInputType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public PermissionLevelInputType addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * list of permissions on this entity
   * @return permissions
  */
  @NotNull 
  @Schema(name = "permissions", description = "list of permissions on this entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public PermissionLevelInputType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether this resource is publicly viewable
   * @return isPublic
  */
  @NotNull 
  @Schema(name = "isPublic", description = "flag whether this resource is publicly viewable", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermissionLevelInputType permissionLevelInputType = (PermissionLevelInputType) o;
    return Objects.equals(this.permissions, permissionLevelInputType.permissions) &&
        Objects.equals(this.isPublic, permissionLevelInputType.isPublic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions, isPublic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermissionLevelInputType {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
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

