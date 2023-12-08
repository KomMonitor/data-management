package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RoleInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T14:41:06.171227400+01:00[Europe/Berlin]")
public class RoleInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String roleName;

  public RoleInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleInputType(String roleName) {
    this.roleName = roleName;
  }

  public RoleInputType roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  /**
   * the role name
   * @return roleName
  */
  @NotNull 
  @Schema(name = "roleName", description = "the role name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roleName")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleInputType roleInputType = (RoleInputType) o;
    return Objects.equals(this.roleName, roleInputType.roleName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleInputType {\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
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

