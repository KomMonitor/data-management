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
 * Default permissions (template) for resources associated with this topic
 */

@Schema(name = "DefaultResourcePermissionType", description = "Default permissions (template) for resources associated with this topic")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-03-08T11:42:46.348441096+01:00[Europe/Berlin]")
public class DefaultResourcePermissionType implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean isPublic;

  @Valid
  private List<String> permissions = new ArrayList<>();

  public DefaultResourcePermissionType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DefaultResourcePermissionType(Boolean isPublic, List<String> permissions) {
    this.isPublic = isPublic;
    this.permissions = permissions;
  }

  public DefaultResourcePermissionType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * true if resources should be public
   * @return isPublic
  */
  @NotNull 
  @Schema(name = "isPublic", description = "true if resources should be public", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public DefaultResourcePermissionType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public DefaultResourcePermissionType addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * Get permissions
   * @return permissions
  */
  @NotNull 
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
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
    DefaultResourcePermissionType defaultResourcePermissionType = (DefaultResourcePermissionType) o;
    return Objects.equals(this.isPublic, defaultResourcePermissionType.isPublic) &&
        Objects.equals(this.permissions, defaultResourcePermissionType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isPublic, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultResourcePermissionType {\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

