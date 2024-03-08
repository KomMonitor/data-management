package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Metadata of a permission
 */

@Schema(name = "PermissionOverviewType", description = "Metadata of a permission")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-03-08T11:42:46.348441096+01:00[Europe/Berlin]")
public class PermissionOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private PermissionLevelType permissionLevel;

  private PermissionResourceType permissioneType;

  private String permissionId;

  public PermissionOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PermissionOverviewType(PermissionLevelType permissionLevel, String permissionId) {
    this.permissionLevel = permissionLevel;
    this.permissionId = permissionId;
  }

  public PermissionOverviewType permissionLevel(PermissionLevelType permissionLevel) {
    this.permissionLevel = permissionLevel;
    return this;
  }

  /**
   * Get permissionLevel
   * @return permissionLevel
  */
  @NotNull @Valid 
  @Schema(name = "permissionLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissionLevel")
  public PermissionLevelType getPermissionLevel() {
    return permissionLevel;
  }

  public void setPermissionLevel(PermissionLevelType permissionLevel) {
    this.permissionLevel = permissionLevel;
  }

  public PermissionOverviewType permissioneType(PermissionResourceType permissioneType) {
    this.permissioneType = permissioneType;
    return this;
  }

  /**
   * Get permissioneType
   * @return permissioneType
  */
  @Valid 
  @Schema(name = "permissioneType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissioneType")
  public PermissionResourceType getPermissioneType() {
    return permissioneType;
  }

  public void setPermissioneType(PermissionResourceType permissioneType) {
    this.permissioneType = permissioneType;
  }

  public PermissionOverviewType permissionId(String permissionId) {
    this.permissionId = permissionId;
    return this;
  }

  /**
   * the unique identifier of the permission
   * @return permissionId
  */
  @NotNull 
  @Schema(name = "permissionId", description = "the unique identifier of the permission", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissionId")
  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermissionOverviewType permissionOverviewType = (PermissionOverviewType) o;
    return Objects.equals(this.permissionLevel, permissionOverviewType.permissionLevel) &&
        Objects.equals(this.permissioneType, permissionOverviewType.permissioneType) &&
        Objects.equals(this.permissionId, permissionOverviewType.permissionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissionLevel, permissioneType, permissionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermissionOverviewType {\n");
    sb.append("    permissionLevel: ").append(toIndentedString(permissionLevel)).append("\n");
    sb.append("    permissioneType: ").append(toIndentedString(permissioneType)).append("\n");
    sb.append("    permissionId: ").append(toIndentedString(permissionId)).append("\n");
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

