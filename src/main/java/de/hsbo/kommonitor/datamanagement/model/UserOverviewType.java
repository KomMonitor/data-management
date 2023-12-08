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
 * UserOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T15:24:58.815569400+01:00[Europe/Berlin]")
public class UserOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid RoleOverviewType> roles = new ArrayList<>();

  private String userId;

  private String userName;

  public UserOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserOverviewType(List<@Valid RoleOverviewType> roles, String userId, String userName) {
    this.roles = roles;
    this.userId = userId;
    this.userName = userName;
  }

  public UserOverviewType roles(List<@Valid RoleOverviewType> roles) {
    this.roles = roles;
    return this;
  }

  public UserOverviewType addRolesItem(RoleOverviewType rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * list of roles the user is associated with
   * @return roles
  */
  @NotNull @Valid 
  @Schema(name = "roles", description = "list of roles the user is associated with", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roles")
  public List<@Valid RoleOverviewType> getRoles() {
    return roles;
  }

  public void setRoles(List<@Valid RoleOverviewType> roles) {
    this.roles = roles;
  }

  public UserOverviewType userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * the unique identifier of the user
   * @return userId
  */
  @NotNull 
  @Schema(name = "userId", description = "the unique identifier of the user", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public UserOverviewType userName(String userName) {
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
    UserOverviewType userOverviewType = (UserOverviewType) o;
    return Objects.equals(this.roles, userOverviewType.roles) &&
        Objects.equals(this.userId, userOverviewType.userId) &&
        Objects.equals(this.userName, userOverviewType.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roles, userId, userName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserOverviewType {\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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

