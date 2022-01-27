package de.hsbo.kommonitor.datamanagement.model.spatialunits;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-08-02T08:44:45.917+02:00")

public class SpatialUnitOverviewType   {
  @JsonProperty("spatialUnitId")
  private String spatialUnitId = null;

  @JsonProperty("spatialUnitLevel")
  private String spatialUnitLevel = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("nextLowerHierarchyLevel")
  private String nextLowerHierarchyLevel = null;

  @JsonProperty("nextUpperHierarchyLevel")
  private String nextUpperHierarchyLevel = null;

  @JsonProperty("availablePeriodsOfValidity")
  private AvailablePeriodsOfValidityType availablePeriodsOfValidity = null;

  @JsonProperty("wmsUrl")
  private String wmsUrl = null;

  @JsonProperty("wfsUrl")
  private String wfsUrl = null;

  @JsonProperty("allowedRoles")
  private List<String> allowedRoles = new ArrayList<>();

  @JsonProperty("userPermissions")
  private List<PermissionLevelType> userPermissions = new ArrayList<>();

  public SpatialUnitOverviewType spatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

   /**
   * the unique identifier of the spatial unit level the features apply to
   * @return spatialUnitId
  **/
  @ApiModelProperty(required = true, value = "the unique identifier of the spatial unit level the features apply to")
  public String getSpatialUnitId() {
    return spatialUnitId;
  }

  public void setSpatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public SpatialUnitOverviewType spatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
    return this;
  }

   /**
   * the name of the spatial unit level the features apply to
   * @return spatialUnitLevel
  **/
  @ApiModelProperty(required = true, value = "the name of the spatial unit level the features apply to")
  public String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  public void setSpatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitOverviewType metadata(CommonMetadataType metadata) {
    this.metadata = metadata;
    return this;
  }

   /**
   * Get metadata
   * @return metadata
  **/
  @ApiModelProperty(required = true, value = "")
  public CommonMetadataType getMetadata() {
    return metadata;
  }

  public void setMetadata(CommonMetadataType metadata) {
    this.metadata = metadata;
  }

  public SpatialUnitOverviewType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
    return this;
  }

   /**
   * the identifier/name of the spatial unit level that contains the features of the nearest lower hierarchy level
   * @return nextLowerHierarchyLevel
  **/
  @ApiModelProperty(required = true, value = "the identifier/name of the spatial unit level that contains the features of the nearest lower hierarchy level")
  public String getNextLowerHierarchyLevel() {
    return nextLowerHierarchyLevel;
  }

  public void setNextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
  }

  public SpatialUnitOverviewType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
    return this;
  }

   /**
   * the identifier/name of the spatial unit level that contains the features of the nearest upper hierarchy level
   * @return nextUpperHierarchyLevel
  **/
  @ApiModelProperty(required = true, value = "the identifier/name of the spatial unit level that contains the features of the nearest upper hierarchy level")
  public String getNextUpperHierarchyLevel() {
    return nextUpperHierarchyLevel;
  }

  public void setNextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
  }

  public SpatialUnitOverviewType availablePeriodOfValidity(AvailablePeriodsOfValidityType availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    return this;
  }

   /**
   * Get availablePeriodOfValidity
   * @return availablePeriodOfValidity
  **/
  @ApiModelProperty(value = "")
  public AvailablePeriodsOfValidityType getAvailablePeriodsOfValidity() {
    return availablePeriodsOfValidity;
  }

  public void setAvailablePeriodsOfValidity(AvailablePeriodsOfValidityType availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public SpatialUnitOverviewType wmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

   /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
  **/
  @ApiModelProperty(required = true, value = "the URL of a running WMS instance serving the spatial features of the associated dataset")
  public String getWmsUrl() {
    return wmsUrl;
  }

  public void setWmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
  }

  public SpatialUnitOverviewType wfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

   /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
  **/
  @ApiModelProperty(required = true, value = "the URL of a running WFS instance serving the spatial features of the associated dataset")
  public String getWfsUrl() {
    return wfsUrl;
  }

  public void setWfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }

  public SpatialUnitOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public SpatialUnitOverviewType addAllowedRolesItem(String allowedRolesItem) {
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
   **/
  @ApiModelProperty(required = true, value = "list of role identifiers that have read access rights for this dataset")
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
  @ApiModelProperty(required = true, value = "list of permissions that are effective on this dataset for the current user")
  public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitOverviewType spatialUnitOverviewType = (SpatialUnitOverviewType) o;
    return Objects.equals(this.spatialUnitId, spatialUnitOverviewType.spatialUnitId) &&
        Objects.equals(this.spatialUnitLevel, spatialUnitOverviewType.spatialUnitLevel) &&
        Objects.equals(this.metadata, spatialUnitOverviewType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitOverviewType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitOverviewType.nextUpperHierarchyLevel) &&
        Objects.equals(this.availablePeriodsOfValidity, spatialUnitOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.wmsUrl, spatialUnitOverviewType.wmsUrl) &&
        Objects.equals(this.wfsUrl, spatialUnitOverviewType.wfsUrl) &&
        Objects.equals(this.allowedRoles, spatialUnitOverviewType.allowedRoles) &&
        Objects.equals(this.userPermissions, spatialUnitOverviewType.userPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, spatialUnitLevel, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel,
            availablePeriodsOfValidity, wmsUrl, wfsUrl, allowedRoles, userPermissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitOverviewType {\n");
    
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
    sb.append("    availablePeriodOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
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

