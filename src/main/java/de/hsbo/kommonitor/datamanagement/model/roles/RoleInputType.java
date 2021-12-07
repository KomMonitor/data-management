package de.hsbo.kommonitor.datamanagement.model.roles;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Combination of organizationalUnit and permissionLevel to control access to a resource
 */
@ApiModel(description = "Combination of organizationalUnit and permissionLevel to control access to a resource")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-12-06T22:18:33.128Z")
public class RoleInputType   {

  @JsonProperty("organizationalUnit")
  private String organizationalUnit = null;

  @JsonProperty("permissionLevel")
  private PermissionLevelType permissionLevel = null;

  public RoleInputType organizationalUnit(String organizationalUnit) {
    this.organizationalUnit = organizationalUnit;
    return this;
  }

  /**
   * Get organizationalUnit
   * @return organizationalUnit
   **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public String getOrganizationalUnit() {
    return organizationalUnit;
  }

  public void setOrganizationalUnit(String organizationalUnit) {
    this.organizationalUnit = organizationalUnit;
  }

  public RoleInputType permissionLevel(PermissionLevelType permissionLevel) {
    this.permissionLevel = permissionLevel;
    return this;
  }

  /**
   * Get permissionLevel
   * @return permissionLevel
   **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
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
    RoleInputType roleInputType = (RoleInputType) o;
    return Objects.equals(this.organizationalUnit, roleInputType.organizationalUnit) &&
        Objects.equals(this.permissionLevel, roleInputType.permissionLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organizationalUnit, permissionLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleInputType {\n");

    sb.append("    organizationalUnit: ").append(toIndentedString(organizationalUnit)).append("\n");
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

