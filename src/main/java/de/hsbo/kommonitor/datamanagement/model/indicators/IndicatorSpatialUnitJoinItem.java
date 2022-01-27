package de.hsbo.kommonitor.datamanagement.model.indicators;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IndicatorSpatialUnitJoinItem
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-07-31T11:36:14.910+02:00")

public class IndicatorSpatialUnitJoinItem implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("spatialUnitId")
  private String spatialUnitId = null;

  @JsonProperty("spatialUnitName")
  private String spatialUnitName = null;

  @JsonProperty("allowedRoles")
  @Valid
  private List<String> allowedRoles = null;

  @JsonProperty("userPermissions")
  @Valid
  private List<PermissionLevelType> userPermissions = null;

  public IndicatorSpatialUnitJoinItem spatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * ID of the applicable spatial unit
   * @return spatialUnitId
  **/
  @ApiModelProperty(required = true, value = "ID of the applicable spatial unit")
  @NotNull


  public String getSpatialUnitId() {
    return spatialUnitId;
  }

  public void setSpatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public IndicatorSpatialUnitJoinItem spatialUnitName(String spatialUnitName) {
    this.spatialUnitName = spatialUnitName;
    return this;
  }

  /**
   * name of the applicable spatial unit
   * @return spatialUnitName
  **/
  @ApiModelProperty(required = true, value = "name of the applicable spatial unit")
  @NotNull


  public String getSpatialUnitName() {
    return spatialUnitName;
  }

  public void setSpatialUnitName(String spatialUnitName) {
    this.spatialUnitName = spatialUnitName;
  }

  public IndicatorSpatialUnitJoinItem allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorSpatialUnitJoinItem addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
  **/
  @ApiModelProperty(value = "list of role identifiers that have read access rights for this dataset")


  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  /**
   * list of permissions that are effective on this dataset for the current user
   * @return allowedRoles
   **/
  @ApiModelProperty(value = "list of permissions that are effective on this dataset for the current user")
  public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorSpatialUnitJoinItem indicatorSpatialUnitJoinItem = (IndicatorSpatialUnitJoinItem) o;
    return Objects.equals(this.spatialUnitId, indicatorSpatialUnitJoinItem.spatialUnitId) &&
        Objects.equals(this.spatialUnitName, indicatorSpatialUnitJoinItem.spatialUnitName) &&
        Objects.equals(this.allowedRoles, indicatorSpatialUnitJoinItem.allowedRoles) &&
        Objects.equals(this.userPermissions, indicatorSpatialUnitJoinItem.userPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, spatialUnitName, allowedRoles, userPermissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorSpatialUnitJoinItem {\n");

    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    spatialUnitName: ").append(toIndentedString(spatialUnitName)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
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

