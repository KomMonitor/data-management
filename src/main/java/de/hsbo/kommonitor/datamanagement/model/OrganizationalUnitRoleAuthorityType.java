package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.GroupAdminRolesType;
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
 * OrganizationalUnitRoleAuthorityType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-10T13:40:31.582152900+02:00[Europe/Berlin]")
public class OrganizationalUnitRoleAuthorityType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid GroupAdminRolesType> authorityRoles = new ArrayList<>();

  public OrganizationalUnitRoleAuthorityType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitRoleAuthorityType(List<@Valid GroupAdminRolesType> authorityRoles) {
    this.authorityRoles = authorityRoles;
  }

  public OrganizationalUnitRoleAuthorityType authorityRoles(List<@Valid GroupAdminRolesType> authorityRoles) {
    this.authorityRoles = authorityRoles;
    return this;
  }

  public OrganizationalUnitRoleAuthorityType addAuthorityRolesItem(GroupAdminRolesType authorityRolesItem) {
    if (this.authorityRoles == null) {
      this.authorityRoles = new ArrayList<>();
    }
    this.authorityRoles.add(authorityRolesItem);
    return this;
  }

  /**
   * list of Keycloak group based admin roles that have been assigned to this organizational unit
   * @return authorityRoles
  */
  @NotNull @Valid 
  @Schema(name = "authorityRoles", description = "list of Keycloak group based admin roles that have been assigned to this organizational unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("authorityRoles")
  public List<@Valid GroupAdminRolesType> getAuthorityRoles() {
    return authorityRoles;
  }

  public void setAuthorityRoles(List<@Valid GroupAdminRolesType> authorityRoles) {
    this.authorityRoles = authorityRoles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitRoleAuthorityType organizationalUnitRoleAuthorityType = (OrganizationalUnitRoleAuthorityType) o;
    return Objects.equals(this.authorityRoles, organizationalUnitRoleAuthorityType.authorityRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorityRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitRoleAuthorityType {\n");
    sb.append("    authorityRoles: ").append(toIndentedString(authorityRoles)).append("\n");
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

