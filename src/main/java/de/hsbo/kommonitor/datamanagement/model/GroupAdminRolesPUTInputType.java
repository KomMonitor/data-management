package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.AdminRoleType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GroupAdminRolesPUTInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class GroupAdminRolesPUTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String organizationalUnitId;

  private @Nullable String organizationalUnitName;

  private @Nullable String keycloakId;

  @Valid
  private List<AdminRoleType> adminRoles = new ArrayList<>();

  public GroupAdminRolesPUTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GroupAdminRolesPUTInputType(String organizationalUnitId, List<AdminRoleType> adminRoles) {
    this.organizationalUnitId = organizationalUnitId;
    this.adminRoles = adminRoles;
  }

  public GroupAdminRolesPUTInputType organizationalUnitId(String organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
    return this;
  }

  /**
   * unique id of the organizational Unit
   * @return organizationalUnitId
   */
  @NotNull 
  @Schema(name = "organizationalUnitId", description = "unique id of the organizational Unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("organizationalUnitId")
  public String getOrganizationalUnitId() {
    return organizationalUnitId;
  }

  public void setOrganizationalUnitId(String organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
  }

  public GroupAdminRolesPUTInputType organizationalUnitName(String organizationalUnitName) {
    this.organizationalUnitName = organizationalUnitName;
    return this;
  }

  /**
   * unique name of the organizational Unit
   * @return organizationalUnitName
   */
  
  @Schema(name = "organizationalUnitName", description = "unique name of the organizational Unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationalUnitName")
  public String getOrganizationalUnitName() {
    return organizationalUnitName;
  }

  public void setOrganizationalUnitName(String organizationalUnitName) {
    this.organizationalUnitName = organizationalUnitName;
  }

  public GroupAdminRolesPUTInputType keycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
    return this;
  }

  /**
   * unique Keycloak group ID
   * @return keycloakId
   */
  
  @Schema(name = "keycloakId", description = "unique Keycloak group ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keycloakId")
  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public GroupAdminRolesPUTInputType adminRoles(List<AdminRoleType> adminRoles) {
    this.adminRoles = adminRoles;
    return this;
  }

  public GroupAdminRolesPUTInputType addAdminRolesItem(AdminRoleType adminRolesItem) {
    if (this.adminRoles == null) {
      this.adminRoles = new ArrayList<>();
    }
    this.adminRoles.add(adminRolesItem);
    return this;
  }

  /**
   * Admin roles that refer to the organizational unit
   * @return adminRoles
   */
  @NotNull @Valid 
  @Schema(name = "adminRoles", description = "Admin roles that refer to the organizational unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adminRoles")
  public List<AdminRoleType> getAdminRoles() {
    return adminRoles;
  }

  public void setAdminRoles(List<AdminRoleType> adminRoles) {
    this.adminRoles = adminRoles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupAdminRolesPUTInputType groupAdminRolesPUTInputType = (GroupAdminRolesPUTInputType) o;
    return Objects.equals(this.organizationalUnitId, groupAdminRolesPUTInputType.organizationalUnitId) &&
        Objects.equals(this.organizationalUnitName, groupAdminRolesPUTInputType.organizationalUnitName) &&
        Objects.equals(this.keycloakId, groupAdminRolesPUTInputType.keycloakId) &&
        Objects.equals(this.adminRoles, groupAdminRolesPUTInputType.adminRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organizationalUnitId, organizationalUnitName, keycloakId, adminRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupAdminRolesPUTInputType {\n");
    sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
    sb.append("    organizationalUnitName: ").append(toIndentedString(organizationalUnitName)).append("\n");
    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
    sb.append("    adminRoles: ").append(toIndentedString(adminRoles)).append("\n");
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

