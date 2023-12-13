package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
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
 * SpatialUnitOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-13T09:18:57.441387500+01:00[Europe/Berlin]")
public class SpatialUnitOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  @Valid
  private List<@Valid PeriodOfValidityType> availablePeriodsOfValidity;

  private CommonMetadataType metadata;

  private String nextLowerHierarchyLevel;

  private String nextUpperHierarchyLevel;

  private String spatialUnitId;

  private String spatialUnitLevel;

  @Valid
  private List<PermissionLevelType> userPermissions = new ArrayList<>();

  private String wfsUrl;

  private String wmsUrl;

  public SpatialUnitOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitOverviewType(List<String> allowedRoles, CommonMetadataType metadata, String nextLowerHierarchyLevel, String nextUpperHierarchyLevel, String spatialUnitId, String spatialUnitLevel, List<PermissionLevelType> userPermissions, String wfsUrl, String wmsUrl) {
    this.allowedRoles = allowedRoles;
    this.metadata = metadata;
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
    this.spatialUnitId = spatialUnitId;
    this.spatialUnitLevel = spatialUnitLevel;
    this.userPermissions = userPermissions;
    this.wfsUrl = wfsUrl;
    this.wmsUrl = wmsUrl;
  }

  public SpatialUnitOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public SpatialUnitOverviewType addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
  */
  @NotNull 
  @Schema(name = "allowedRoles", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("allowedRoles")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public SpatialUnitOverviewType availablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    return this;
  }

  public SpatialUnitOverviewType addAvailablePeriodsOfValidityItem(PeriodOfValidityType availablePeriodsOfValidityItem) {
    if (this.availablePeriodsOfValidity == null) {
      this.availablePeriodsOfValidity = new ArrayList<>();
    }
    this.availablePeriodsOfValidity.add(availablePeriodsOfValidityItem);
    return this;
  }

  /**
   * Get availablePeriodsOfValidity
   * @return availablePeriodsOfValidity
  */
  @Valid 
  @Schema(name = "availablePeriodsOfValidity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availablePeriodsOfValidity")
  public List<@Valid PeriodOfValidityType> getAvailablePeriodsOfValidity() {
    return availablePeriodsOfValidity;
  }

  public void setAvailablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public SpatialUnitOverviewType metadata(CommonMetadataType metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
  */
  @NotNull @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metadata")
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
  */
  @NotNull 
  @Schema(name = "nextLowerHierarchyLevel", description = "the identifier/name of the spatial unit level that contains the features of the nearest lower hierarchy level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nextLowerHierarchyLevel")
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
  */
  @NotNull 
  @Schema(name = "nextUpperHierarchyLevel", description = "the identifier/name of the spatial unit level that contains the features of the nearest upper hierarchy level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nextUpperHierarchyLevel")
  public String getNextUpperHierarchyLevel() {
    return nextUpperHierarchyLevel;
  }

  public void setNextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
  }

  public SpatialUnitOverviewType spatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * the unique identifier of the spatial unit level the features apply to
   * @return spatialUnitId
  */
  @NotNull 
  @Schema(name = "spatialUnitId", description = "the unique identifier of the spatial unit level the features apply to", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitId")
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
  */
  @NotNull 
  @Schema(name = "spatialUnitLevel", description = "the name of the spatial unit level the features apply to", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitLevel")
  public String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  public void setSpatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitOverviewType userPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public SpatialUnitOverviewType addUserPermissionsItem(PermissionLevelType userPermissionsItem) {
    if (this.userPermissions == null) {
      this.userPermissions = new ArrayList<>();
    }
    this.userPermissions.add(userPermissionsItem);
    return this;
  }

  /**
   * list of permissions that are effective on this dataset for the current user
   * @return userPermissions
  */
  @NotNull @Valid 
  @Schema(name = "userPermissions", description = "list of permissions that are effective on this dataset for the current user", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userPermissions")
  public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public SpatialUnitOverviewType wfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

  /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
  */
  @NotNull 
  @Schema(name = "wfsUrl", description = "the URL of a running WFS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("wfsUrl")
  public String getWfsUrl() {
    return wfsUrl;
  }

  public void setWfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }

  public SpatialUnitOverviewType wmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

  /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
  */
  @NotNull 
  @Schema(name = "wmsUrl", description = "the URL of a running WMS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("wmsUrl")
  public String getWmsUrl() {
    return wmsUrl;
  }

  public void setWmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitOverviewType spatialUnitOverviewType = (SpatialUnitOverviewType) o;
    return Objects.equals(this.allowedRoles, spatialUnitOverviewType.allowedRoles) &&
        Objects.equals(this.availablePeriodsOfValidity, spatialUnitOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.metadata, spatialUnitOverviewType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitOverviewType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitOverviewType.nextUpperHierarchyLevel) &&
        Objects.equals(this.spatialUnitId, spatialUnitOverviewType.spatialUnitId) &&
        Objects.equals(this.spatialUnitLevel, spatialUnitOverviewType.spatialUnitLevel) &&
        Objects.equals(this.userPermissions, spatialUnitOverviewType.userPermissions) &&
        Objects.equals(this.wfsUrl, spatialUnitOverviewType.wfsUrl) &&
        Objects.equals(this.wmsUrl, spatialUnitOverviewType.wmsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, availablePeriodsOfValidity, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel, spatialUnitId, spatialUnitLevel, userPermissions, wfsUrl, wmsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitOverviewType {\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
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

