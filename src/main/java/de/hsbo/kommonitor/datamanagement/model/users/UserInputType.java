package de.hsbo.kommonitor.datamanagement.model.users;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * UserInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class UserInputType   {
  @JsonProperty("userName")
  private String userName = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("roles")
  
  private List<String> roles = new ArrayList<>();

  public UserInputType userName(String userName) {
    this.userName = userName;
    return this;
  }

   /**
   * the user name from the login credentials
   * @return userName
  **/
  @ApiModelProperty(required = true, value = "the user name from the login credentials")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public UserInputType password(String password) {
    this.password = password;
    return this;
  }

   /**
   * the user password from the login credentials
   * @return password
  **/
  @ApiModelProperty(required = true, value = "the user password from the login credentials")
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
    this.roles.add(rolesItem);
    return this;
  }

   /**
   * list of role identifiers; the user is associated with the rights/priviledges of each specififed role.
   * @return roles
  **/
  @ApiModelProperty(required = true, value = "list of role identifiers; the user is associated with the rights/priviledges of each specififed role.")
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInputType userInputType = (UserInputType) o;
    return Objects.equals(this.userName, userInputType.userName) &&
        Objects.equals(this.password, userInputType.password) &&
        Objects.equals(this.roles, userInputType.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, password, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInputType {\n");
    
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

