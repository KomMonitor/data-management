package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * UserInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T15:24:58.815569400+01:00[Europe/Berlin]")
public class UserInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String password;

  @Valid
  private List<String> roles = new ArrayList<>();

  private String userName;

  public UserInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserInputType(String password, List<String> roles, String userName) {
    this.password = password;
    this.roles = roles;
    this.userName = userName;
  }

  public UserInputType password(String password) {
    this.password = password;
    return this;
  }

  /**
   * the user password from the login credentials
   * @return password
  */
  @NotNull 
  @Schema(name = "password", description = "the user password from the login credentials", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserInputType roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public UserInputType addRolesItem(String rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * list of role identifiers; the user is associated with the rights/priviledges of each specififed role.
   * @return roles
  */
  @NotNull 
  @Schema(name = "roles", description = "list of role identifiers; the user is associated with the rights/priviledges of each specififed role.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roles")
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public UserInputType userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * the user name from the login credentials
   * @return userName
  */
  @NotNull 
  @Schema(name = "userName", description = "the user name from the login credentials", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userName")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInputType userInputType = (UserInputType) o;
    return Objects.equals(this.password, userInputType.password) &&
        Objects.equals(this.roles, userInputType.roles) &&
        Objects.equals(this.userName, userInputType.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, roles, userName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInputType {\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
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

