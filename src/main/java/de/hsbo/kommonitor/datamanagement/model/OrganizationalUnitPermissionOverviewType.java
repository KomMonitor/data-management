package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewTypePermissions;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OrganizationalUnitPermissionOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-20T01:40:05.349474681+01:00[Europe/Berlin]")
public class OrganizationalUnitPermissionOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String contact;

  private String description;

  private String name;

  private String organizationalUnitId;

  private OrganizationalUnitPermissionOverviewTypePermissions permissions;

  public OrganizationalUnitPermissionOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitPermissionOverviewType(String contact, String name, String organizationalUnitId, OrganizationalUnitPermissionOverviewTypePermissions permissions) {
    this.contact = contact;
    this.name = name;
    this.organizationalUnitId = organizationalUnitId;
    this.permissions = permissions;
  }

  public OrganizationalUnitPermissionOverviewType contact(String contact) {
    this.contact = contact;
    return this;
  }

  /**
   * contact information of the person responsible for this group
   * @return contact
  */
  @NotNull 
  @Schema(name = "contact", description = "contact information of the person responsible for this group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contact")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public OrganizationalUnitPermissionOverviewType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * additional information
   * @return description
  */
  
  @Schema(name = "description", description = "additional information", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public OrganizationalUnitPermissionOverviewType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of this organizational Unit
   * @return name
  */
  @NotNull 
  @Schema(name = "name", description = "name of this organizational Unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrganizationalUnitPermissionOverviewType organizationalUnitId(String organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
    return this;
  }

  /**
   * unique id of this organizational Unit
   * @return organizationalUnitId
  */
  @NotNull 
  @Schema(name = "organizationalUnitId", description = "unique id of this organizational Unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("organizationalUnitId")
  public String getOrganizationalUnitId() {
    return organizationalUnitId;
  }

  public void setOrganizationalUnitId(String organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
  }

  public OrganizationalUnitPermissionOverviewType permissions(OrganizationalUnitPermissionOverviewTypePermissions permissions) {
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
  public OrganizationalUnitPermissionOverviewTypePermissions getPermissions() {
    return permissions;
  }

  public void setPermissions(OrganizationalUnitPermissionOverviewTypePermissions permissions) {
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
    OrganizationalUnitPermissionOverviewType organizationalUnitPermissionOverviewType = (OrganizationalUnitPermissionOverviewType) o;
    return Objects.equals(this.contact, organizationalUnitPermissionOverviewType.contact) &&
        Objects.equals(this.description, organizationalUnitPermissionOverviewType.description) &&
        Objects.equals(this.name, organizationalUnitPermissionOverviewType.name) &&
        Objects.equals(this.organizationalUnitId, organizationalUnitPermissionOverviewType.organizationalUnitId) &&
        Objects.equals(this.permissions, organizationalUnitPermissionOverviewType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contact, description, name, organizationalUnitId, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitPermissionOverviewType {\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
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

