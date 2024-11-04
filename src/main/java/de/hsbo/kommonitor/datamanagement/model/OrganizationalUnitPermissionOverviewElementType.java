package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * OrganizationalUnitPermissionOverviewElementType
 */

@Schema(name = "RoleOverviewType", description = "Combination of organizationalUnit and permissionLevel to control access to a resource")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class OrganizationalUnitPermissionOverviewElementType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  private String permissionLevel;

  private UUID roleId;

  public OrganizationalUnitPermissionOverviewElementType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionOverviewElementType(UUID id, String permissionLevel, UUID roleId) {
    this.id = id;
    this.permissionLevel = permissionLevel;
    this.roleId = roleId;
  }

  public OrganizationalUnitPermissionOverviewElementType id(UUID id) {
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

  public OrganizationalUnitPermissionOverviewElementType permissionLevel(String permissionLevel) {
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

  public OrganizationalUnitPermissionOverviewElementType roleId(UUID roleId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitPermissionOverviewElementType organizationalUnitPermissionOverviewElementType = (OrganizationalUnitPermissionOverviewElementType) o;
    return Objects.equals(this.id, organizationalUnitPermissionOverviewElementType.id) &&
        Objects.equals(this.permissionLevel, organizationalUnitPermissionOverviewElementType.permissionLevel) &&
        Objects.equals(this.roleId, organizationalUnitPermissionOverviewElementType.roleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, permissionLevel, roleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionOverviewElementType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

