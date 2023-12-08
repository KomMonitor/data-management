package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Combination of organizationalUnit and permissionLevel to control access to a resource
 */

@Schema(name = "RoleOverviewType", description = "Combination of organizationalUnit and permissionLevel to control access to a resource")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T15:24:58.815569400+01:00[Europe/Berlin]")
public class RoleOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Gets or Sets permissionLevel
   */
  public enum PermissionLevelEnum {
    CREATOR("creator"),
    
    PUBLISHER("publisher"),
    
    EDITOR("editor"),
    
    VIEWER("viewer");

    private String value;

    PermissionLevelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PermissionLevelEnum fromValue(String value) {
      for (PermissionLevelEnum b : PermissionLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PermissionLevelEnum permissionLevel;

  private String roleId;

  public RoleOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleOverviewType(PermissionLevelEnum permissionLevel, String roleId) {
    this.permissionLevel = permissionLevel;
    this.roleId = roleId;
  }

  public RoleOverviewType permissionLevel(PermissionLevelEnum permissionLevel) {
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
  public PermissionLevelEnum getPermissionLevel() {
    return permissionLevel;
  }

  public void setPermissionLevel(PermissionLevelEnum permissionLevel) {
    this.permissionLevel = permissionLevel;
  }

  public RoleOverviewType roleId(String roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * the unique role identifier
   * @return roleId
  */
  @NotNull 
  @Schema(name = "roleId", description = "the unique role identifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roleId")
  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleOverviewType roleOverviewType = (RoleOverviewType) o;
    return Objects.equals(this.permissionLevel, roleOverviewType.permissionLevel) &&
        Objects.equals(this.roleId, roleOverviewType.roleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissionLevel, roleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleOverviewType {\n");
    sb.append("    permissionLevel: ").append(toIndentedString(permissionLevel)).append("\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
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

