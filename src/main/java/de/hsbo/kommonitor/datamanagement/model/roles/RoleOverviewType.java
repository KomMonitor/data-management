package de.hsbo.kommonitor.datamanagement.model.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * RoleOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class RoleOverviewType   {
  @JsonProperty("roleId")
  private String roleId = null;

  @JsonProperty("roleName")
  private String roleName = null;

  @JsonProperty("privileges")
  
  private List<String> privileges = new ArrayList<>();

  public RoleOverviewType(String roleId) {
	  this.roleId = roleId;
}

  public RoleOverviewType() {}
  
public RoleOverviewType roleId(String roleId) {
    this.roleId = roleId;
    return this;
  }

   /**
   * the unique role identifier
   * @return roleId
  **/
  @ApiModelProperty(required = true, value = "the unique role identifier")
  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public RoleOverviewType roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

   /**
   * the role name
   * @return roleName
  **/
  @ApiModelProperty(required = true, value = "the role name")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public RoleOverviewType privileges(List<String> privileges) {
    this.privileges = privileges;
    return this;
  }

  public RoleOverviewType addPrivilegesItem(String privilegesItem) {
    this.privileges.add(privilegesItem);
    return this;
  }

   /**
   * list of privileges that are associated to this role. They indicate, what operations and data can be accessed by users.
   * @return privileges
  **/
  @ApiModelProperty(required = true, value = "list of privileges that are associated to this role. They indicate, what operations and data can be accessed by users.")
  public List<String> getPrivileges() {
    return privileges;
  }

  public void setPrivileges(List<String> privileges) {
    this.privileges = privileges;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleOverviewType roleOverviewType = (RoleOverviewType) o;
    return Objects.equals(this.roleId, roleOverviewType.roleId) &&
        Objects.equals(this.roleName, roleOverviewType.roleName) &&
        Objects.equals(this.privileges, roleOverviewType.privileges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleId, roleName, privileges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleOverviewType {\n");
    
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
    sb.append("    privileges: ").append(toIndentedString(privileges)).append("\n");
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

