package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.RoleOverviewType;
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
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-13T09:18:57.441387500+01:00[Europe/Berlin]")
public class OrganizationalUnitOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String contact;

  private String description;

  private String name;

  private String organizationalUnitId;

  @Valid
  private List<@Valid RoleOverviewType> roles = new ArrayList<>();

  public OrganizationalUnitOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitOverviewType(String contact, String name, String organizationalUnitId, List<@Valid RoleOverviewType> roles) {
    this.contact = contact;
    this.name = name;
    this.organizationalUnitId = organizationalUnitId;
    this.roles = roles;
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

  public OrganizationalUnitOverviewType roles(List<@Valid RoleOverviewType> roles) {
    this.roles = roles;
    return this;
  }

  public OrganizationalUnitOverviewType addRolesItem(RoleOverviewType rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
  */
  @NotNull @Valid 
  @Schema(name = "roles", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roles")
  public List<@Valid RoleOverviewType> getRoles() {
    return roles;
  }

  public void setRoles(List<@Valid RoleOverviewType> roles) {
    this.roles = roles;
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
    return Objects.equals(this.contact, organizationalUnitOverviewType.contact) &&
        Objects.equals(this.description, organizationalUnitOverviewType.description) &&
        Objects.equals(this.name, organizationalUnitOverviewType.name) &&
        Objects.equals(this.organizationalUnitId, organizationalUnitOverviewType.organizationalUnitId) &&
        Objects.equals(this.roles, organizationalUnitOverviewType.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contact, description, name, organizationalUnitId, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitOverviewType {\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

