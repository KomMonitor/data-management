package de.hsbo.kommonitor.datamanagement.model.legacy.roles;

import java.util.Objects;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionLevelType;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Combination of organizationalUnit and permissionLevel to control access to a resource
 */
@ApiModel(description = "Combination of organizationalUnit and permissionLevel to control access to a resource")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-01-04T19:28:20.077Z")
public class RoleOverviewType {

    @JsonProperty("roleId")
    private String roleId = null;

    @JsonProperty("permissionLevel")
    private PermissionLevelType permissionLevel = null;

    public RoleOverviewType roleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    /**
     * the unique role identifier
     *
     * @return roleId
     **/
    @ApiModelProperty(required = true, value = "the unique role identifier")

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public RoleOverviewType permissionLevel(PermissionLevelType permissionLevel) {
        this.permissionLevel = permissionLevel;
        return this;
    }

    /**
     * Get permissionLevel
     *
     * @return permissionLevel
     **/
    @ApiModelProperty(required = true, value = "")

    public PermissionLevelType getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevelType permissionLevel) {
        this.permissionLevel = permissionLevel;
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
            Objects.equals(this.permissionLevel, roleOverviewType.permissionLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionLevel);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RoleOverviewType {\n");

        sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
        sb.append("    permissionLevel: ").append(toIndentedString(permissionLevel)).append("\n");
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

