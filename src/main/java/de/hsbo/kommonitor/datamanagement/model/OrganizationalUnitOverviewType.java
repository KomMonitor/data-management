package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.PermissionOverviewType;
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
 * organizational unit (group)
 */

@Schema(name = "OrganizationalUnitOverviewType", description = "organizational unit (group)")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:09.865691461+01:00[Europe/Berlin]")
public class OrganizationalUnitOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String organizationalUnitId;

  private String name;

  private Boolean mandant;

  private String keycloakId;

  private String contact;

  private String description;

  @Valid
  private List<@Valid PermissionOverviewType> permissions = new ArrayList<>();

  private String parentId;

  @Valid
  private List<String> children = new ArrayList<>();

  public OrganizationalUnitOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitOverviewType(String organizationalUnitId, String name, Boolean mandant, String keycloakId, String contact, List<@Valid PermissionOverviewType> permissions, List<String> children) {
    this.organizationalUnitId = organizationalUnitId;
    this.name = name;
    this.mandant = mandant;
    this.keycloakId = keycloakId;
    this.contact = contact;
    this.permissions = permissions;
    this.children = children;
  }

  public OrganizationalUnitOverviewType organizationalUnitId(String organizationalUnitId) {
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

  public OrganizationalUnitOverviewType name(String name) {
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

  public OrganizationalUnitOverviewType mandant(Boolean mandant) {
    this.mandant = mandant;
    return this;
  }

  /**
   * flag whether this unit is an autonomous mandant
   * @return mandant
  */
  @NotNull 
  @Schema(name = "mandant", description = "flag whether this unit is an autonomous mandant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mandant")
  public Boolean getMandant() {
    return mandant;
  }

  public void setMandant(Boolean mandant) {
    this.mandant = mandant;
  }

  public OrganizationalUnitOverviewType keycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
    return this;
  }

  /**
   * uuid of the corresponding Keycloak group
   * @return keycloakId
  */
  @NotNull 
  @Schema(name = "keycloakId", description = "uuid of the corresponding Keycloak group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("keycloakId")
  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public OrganizationalUnitOverviewType contact(String contact) {
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

  public OrganizationalUnitOverviewType description(String description) {
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

  public OrganizationalUnitOverviewType permissions(List<@Valid PermissionOverviewType> permissions) {
    this.permissions = permissions;
    return this;
  }

  public OrganizationalUnitOverviewType addPermissionsItem(PermissionOverviewType permissionsItem) {
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
  public List<@Valid PermissionOverviewType> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<@Valid PermissionOverviewType> permissions) {
    this.permissions = permissions;
  }

  public OrganizationalUnitOverviewType parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * uuid of the parent group
   * @return parentId
  */
  
  @Schema(name = "parentId", description = "uuid of the parent group", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parentId")
  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public OrganizationalUnitOverviewType children(List<String> children) {
    this.children = children;
    return this;
  }

  public OrganizationalUnitOverviewType addChildrenItem(String childrenItem) {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(childrenItem);
    return this;
  }

  /**
   * uuids of the first-level children
   * @return children
  */
  @NotNull 
  @Schema(name = "children", description = "uuids of the first-level children", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("children")
  public List<String> getChildren() {
    return children;
  }

  public void setChildren(List<String> children) {
    this.children = children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitOverviewType organizationalUnitOverviewType = (OrganizationalUnitOverviewType) o;
    return Objects.equals(this.organizationalUnitId, organizationalUnitOverviewType.organizationalUnitId) &&
        Objects.equals(this.name, organizationalUnitOverviewType.name) &&
        Objects.equals(this.mandant, organizationalUnitOverviewType.mandant) &&
        Objects.equals(this.keycloakId, organizationalUnitOverviewType.keycloakId) &&
        Objects.equals(this.contact, organizationalUnitOverviewType.contact) &&
        Objects.equals(this.description, organizationalUnitOverviewType.description) &&
        Objects.equals(this.permissions, organizationalUnitOverviewType.permissions) &&
        Objects.equals(this.parentId, organizationalUnitOverviewType.parentId) &&
        Objects.equals(this.children, organizationalUnitOverviewType.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organizationalUnitId, name, mandant, keycloakId, contact, description, permissions, parentId, children);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitOverviewType {\n");
    sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mandant: ").append(toIndentedString(mandant)).append("\n");
    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    children: ").append(toIndentedString(children)).append("\n");
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

