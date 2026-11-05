package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * OrganizationalUnitPermissionDetailType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class OrganizationalUnitPermissionDetailType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID permissionId;

  private String permissionLevel;

  public OrganizationalUnitPermissionDetailType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionDetailType(UUID permissionId, String permissionLevel) {
    this.permissionId = permissionId;
    this.permissionLevel = permissionLevel;
  }

  public OrganizationalUnitPermissionDetailType permissionId(UUID permissionId) {
    this.permissionId = permissionId;
    return this;
  }

  /**
   * Get permissionId
   * @return permissionId
   */
  @NotNull @Valid
  @Schema(name = "permissionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissionId")
  public UUID getPermissionId() {
    return permissionId;
  }

  @JsonProperty("permissionId")
  public void setPermissionId(UUID permissionId) {
    this.permissionId = permissionId;
  }

  public OrganizationalUnitPermissionDetailType permissionLevel(String permissionLevel) {
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

  @JsonProperty("permissionLevel")
  public void setPermissionLevel(String permissionLevel) {
    this.permissionLevel = permissionLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitPermissionDetailType organizationalUnitPermissionDetailType = (OrganizationalUnitPermissionDetailType) o;
    return Objects.equals(this.permissionId, organizationalUnitPermissionDetailType.permissionId) &&
        Objects.equals(this.permissionLevel, organizationalUnitPermissionDetailType.permissionLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissionId, permissionLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionDetailType {\n");
    sb.append("    permissionId: ").append(toIndentedString(permissionId)).append("\n");
    sb.append("    permissionLevel: ").append(toIndentedString(permissionLevel)).append("\n");
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

