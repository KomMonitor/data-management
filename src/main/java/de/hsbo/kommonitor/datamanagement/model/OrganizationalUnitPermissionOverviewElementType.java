package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionDetailType;
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
 * OrganizationalUnitPermissionOverviewElementType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class OrganizationalUnitPermissionOverviewElementType implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  private String name;

  @Valid
  private List<@Valid OrganizationalUnitPermissionDetailType> permissions = new ArrayList<>();

  public OrganizationalUnitPermissionOverviewElementType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionOverviewElementType(UUID id, String name, List<@Valid OrganizationalUnitPermissionDetailType> permissions) {
    this.id = id;
    this.name = name;
    this.permissions = permissions;
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

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  public OrganizationalUnitPermissionOverviewElementType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public OrganizationalUnitPermissionOverviewElementType permissions(List<@Valid OrganizationalUnitPermissionDetailType> permissions) {
    this.permissions = permissions;
    return this;
  }

  public OrganizationalUnitPermissionOverviewElementType addPermissionsItem(OrganizationalUnitPermissionDetailType permissionsItem) {
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
  @NotNull @Valid
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<@Valid OrganizationalUnitPermissionDetailType> getPermissions() {
    return permissions;
  }

  @JsonProperty("permissions")
  public void setPermissions(List<@Valid OrganizationalUnitPermissionDetailType> permissions) {
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
    OrganizationalUnitPermissionOverviewElementType organizationalUnitPermissionOverviewElementType = (OrganizationalUnitPermissionOverviewElementType) o;
    return Objects.equals(this.id, organizationalUnitPermissionOverviewElementType.id) &&
        Objects.equals(this.name, organizationalUnitPermissionOverviewElementType.name) &&
        Objects.equals(this.permissions, organizationalUnitPermissionOverviewElementType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionOverviewElementType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

