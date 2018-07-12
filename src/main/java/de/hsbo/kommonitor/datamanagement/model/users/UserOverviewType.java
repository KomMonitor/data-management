package de.hsbo.kommonitor.datamanagement.model.users;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * UserOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class UserOverviewType {
	@JsonProperty("userId")
	private String userId = null;

	@JsonProperty("userName")
	private String userName = null;

	@JsonProperty("roles")

	private List<RoleOverviewType> roles = new ArrayList<>();

	public UserOverviewType(String userId2) {
		this.userId = userId;
	}

	public UserOverviewType userId(String userId) {
		this.userId = userId;
		return this;
	}

	/**
	 * the unique identifier of the user
	 * 
	 * @return userId
	 **/
	@ApiModelProperty(required = true, value = "the unique identifier of the user")
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
	 * 
	 * @return userName
	 **/
	@ApiModelProperty(required = true, value = "the user name from the login credentials")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserOverviewType roles(List<RoleOverviewType> roles) {
		this.roles = roles;
		return this;
	}

	public UserOverviewType addRolesItem(RoleOverviewType rolesItem) {
		this.roles.add(rolesItem);
		return this;
	}

	/**
	 * list of roles the user is associated with
	 * 
	 * @return roles
	 **/
	@ApiModelProperty(required = true, value = "list of roles the user is associated with")
	public List<RoleOverviewType> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleOverviewType> roles) {
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
		UserOverviewType userOverviewType = (UserOverviewType) o;
		return Objects.equals(this.userId, userOverviewType.userId)
				&& Objects.equals(this.userName, userOverviewType.userName)
				&& Objects.equals(this.roles, userOverviewType.roles);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, userName, roles);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserOverviewType {\n");

		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
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
