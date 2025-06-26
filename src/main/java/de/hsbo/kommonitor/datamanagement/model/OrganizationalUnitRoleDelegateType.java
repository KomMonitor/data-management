package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.GroupAdminRolesType;
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
 * OrganizationalUnitRoleDelegateType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class OrganizationalUnitRoleDelegateType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid GroupAdminRolesType> roleDelegates = new ArrayList<>();

  public OrganizationalUnitRoleDelegateType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitRoleDelegateType(List<@Valid GroupAdminRolesType> roleDelegates) {
    this.roleDelegates = roleDelegates;
  }

  public OrganizationalUnitRoleDelegateType roleDelegates(List<@Valid GroupAdminRolesType> roleDelegates) {
    this.roleDelegates = roleDelegates;
    return this;
  }

  public OrganizationalUnitRoleDelegateType addRoleDelegatesItem(GroupAdminRolesType roleDelegatesItem) {
    if (this.roleDelegates == null) {
      this.roleDelegates = new ArrayList<>();
    }
    this.roleDelegates.add(roleDelegatesItem);
    return this;
  }

  /**
   * list of admin roles for this organizational units that have been assigned to other organizational units for delegation purpose
   * @return roleDelegates
   */
  @NotNull @Valid 
  @Schema(name = "roleDelegates", description = "list of admin roles for this organizational units that have been assigned to other organizational units for delegation purpose", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roleDelegates")
  public List<@Valid GroupAdminRolesType> getRoleDelegates() {
    return roleDelegates;
  }

  public void setRoleDelegates(List<@Valid GroupAdminRolesType> roleDelegates) {
    this.roleDelegates = roleDelegates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitRoleDelegateType organizationalUnitRoleDelegateType = (OrganizationalUnitRoleDelegateType) o;
    return Objects.equals(this.roleDelegates, organizationalUnitRoleDelegateType.roleDelegates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleDelegates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitRoleDelegateType {\n");
    sb.append("    roleDelegates: ").append(toIndentedString(roleDelegates)).append("\n");
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

