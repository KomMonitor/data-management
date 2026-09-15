package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMembershipType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<String> permissions = new ArrayList<>();

  private List<@Valid PeriodOfValidityType> availablePeriodsOfValidity = new ArrayList<>();

  private Boolean isPublic;

  private CommonMetadataType metadata;

  private @Nullable String mandantId;

  private List<@Valid SpatialUnitHierarchyMembershipType> hierarchies = new ArrayList<>();

  private String spatialUnitId;

  private String spatialUnitLevel;

  private List<PermissionLevelType> userPermissions = new ArrayList<>();

  private @Nullable String wfsUrl;

  private @Nullable String wmsUrl;

  private @Nullable Boolean isOutlineLayer;

  private @Nullable String outlineColor;

  private @Nullable BigDecimal outlineWidth;

  private @Nullable String outlineDashArrayString;

  private @Nullable String ownerId;

  public SpatialUnitOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitOverviewType(List<String> permissions, Boolean isPublic, CommonMetadataType metadata, String spatialUnitId, String spatialUnitLevel, List<PermissionLevelType> userPermissions) {
    this.permissions = permissions;
    this.isPublic = isPublic;
    this.metadata = metadata;
    this.spatialUnitId = spatialUnitId;
    this.spatialUnitLevel = spatialUnitLevel;
    this.userPermissions = userPermissions;
  }

  public SpatialUnitOverviewType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public SpatialUnitOverviewType addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * list of permissions on this entity
   * @return permissions
   */
  @NotNull 
  @Schema(name = "permissions", description = "list of permissions on this entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  @JsonProperty("permissions")
  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
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

  @JsonProperty("availablePeriodsOfValidity")
  public void setAvailablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public SpatialUnitOverviewType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether the resource is publicly accessible
   * @return isPublic
   */
  @NotNull 
  @Schema(name = "isPublic", description = "flag whether the resource is publicly accessible", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
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

  @JsonProperty("metadata")
  public void setMetadata(CommonMetadataType metadata) {
    this.metadata = metadata;
  }

  public SpatialUnitOverviewType mandantId(@Nullable String mandantId) {
    this.mandantId = mandantId;
    return this;
  }

  /**
   * identifier of the mandant (organizational unit) this spatial unit belongs to. Its name is unique within this mandant.
   * @return mandantId
   */
  
  @Schema(name = "mandantId", description = "identifier of the mandant (organizational unit) this spatial unit belongs to. Its name is unique within this mandant.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mandantId")
  public @Nullable String getMandantId() {
    return mandantId;
  }

  @JsonProperty("mandantId")
  public void setMandantId(@Nullable String mandantId) {
    this.mandantId = mandantId;
  }

  public SpatialUnitOverviewType hierarchies(List<@Valid SpatialUnitHierarchyMembershipType> hierarchies) {
    this.hierarchies = hierarchies;
    return this;
  }

  public SpatialUnitOverviewType addHierarchiesItem(SpatialUnitHierarchyMembershipType hierarchiesItem) {
    if (this.hierarchies == null) {
      this.hierarchies = new ArrayList<>();
    }
    this.hierarchies.add(hierarchiesItem);
    return this;
  }

  /**
   * the hierarchies this spatial unit is a member of, together with its ordered position (level) in each. May be empty if the spatial unit is not part of any hierarchy.
   * @return hierarchies
   */
  @Valid 
  @Schema(name = "hierarchies", description = "the hierarchies this spatial unit is a member of, together with its ordered position (level) in each. May be empty if the spatial unit is not part of any hierarchy.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hierarchies")
  public List<@Valid SpatialUnitHierarchyMembershipType> getHierarchies() {
    return hierarchies;
  }

  @JsonProperty("hierarchies")
  public void setHierarchies(List<@Valid SpatialUnitHierarchyMembershipType> hierarchies) {
    this.hierarchies = hierarchies;
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

  @JsonProperty("spatialUnitId")
  public void setSpatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public SpatialUnitOverviewType spatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
    return this;
  }

  /**
   * the name of the spatial unit level the features apply to. The name is unique only within a mandant.
   * @return spatialUnitLevel
   */
  @NotNull 
  @Schema(name = "spatialUnitLevel", description = "the name of the spatial unit level the features apply to. The name is unique only within a mandant.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitLevel")
  public String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  @JsonProperty("spatialUnitLevel")
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

  @JsonProperty("userPermissions")
  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public SpatialUnitOverviewType wfsUrl(@Nullable String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

  /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
   */
  
  @Schema(name = "wfsUrl", description = "the URL of a running WFS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wfsUrl")
  public @Nullable String getWfsUrl() {
    return wfsUrl;
  }

  @JsonProperty("wfsUrl")
  public void setWfsUrl(@Nullable String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }

  public SpatialUnitOverviewType wmsUrl(@Nullable String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

  /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
   */
  
  @Schema(name = "wmsUrl", description = "the URL of a running WMS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wmsUrl")
  public @Nullable String getWmsUrl() {
    return wmsUrl;
  }

  @JsonProperty("wmsUrl")
  public void setWmsUrl(@Nullable String wmsUrl) {
    this.wmsUrl = wmsUrl;
  }

  public SpatialUnitOverviewType isOutlineLayer(@Nullable Boolean isOutlineLayer) {
    this.isOutlineLayer = isOutlineLayer;
    return this;
  }

  /**
   * if true, then KomMonitor web client map application will offer this spatial unit as outline layer in legend control
   * @return isOutlineLayer
   */
  
  @Schema(name = "isOutlineLayer", description = "if true, then KomMonitor web client map application will offer this spatial unit as outline layer in legend control", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isOutlineLayer")
  public @Nullable Boolean getIsOutlineLayer() {
    return isOutlineLayer;
  }

  @JsonProperty("isOutlineLayer")
  public void setIsOutlineLayer(@Nullable Boolean isOutlineLayer) {
    this.isOutlineLayer = isOutlineLayer;
  }

  public SpatialUnitOverviewType outlineColor(@Nullable String outlineColor) {
    this.outlineColor = outlineColor;
    return this;
  }

  /**
   * outline color for this layer as hex code
   * @return outlineColor
   */
  
  @Schema(name = "outlineColor", description = "outline color for this layer as hex code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outlineColor")
  public @Nullable String getOutlineColor() {
    return outlineColor;
  }

  @JsonProperty("outlineColor")
  public void setOutlineColor(@Nullable String outlineColor) {
    this.outlineColor = outlineColor;
  }

  public SpatialUnitOverviewType outlineWidth(@Nullable BigDecimal outlineWidth) {
    this.outlineWidth = outlineWidth;
    return this;
  }

  /**
   * outline width as stroke width for outline geometry
   * @return outlineWidth
   */
  @Valid 
  @Schema(name = "outlineWidth", description = "outline width as stroke width for outline geometry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outlineWidth")
  public @Nullable BigDecimal getOutlineWidth() {
    return outlineWidth;
  }

  @JsonProperty("outlineWidth")
  public void setOutlineWidth(@Nullable BigDecimal outlineWidth) {
    this.outlineWidth = outlineWidth;
  }

  public SpatialUnitOverviewType outlineDashArrayString(@Nullable String outlineDashArrayString) {
    this.outlineDashArrayString = outlineDashArrayString;
    return this;
  }

  /**
   * string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return outlineDashArrayString
   */
  
  @Schema(name = "outlineDashArrayString", description = "string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outlineDashArrayString")
  public @Nullable String getOutlineDashArrayString() {
    return outlineDashArrayString;
  }

  @JsonProperty("outlineDashArrayString")
  public void setOutlineDashArrayString(@Nullable String outlineDashArrayString) {
    this.outlineDashArrayString = outlineDashArrayString;
  }

  public SpatialUnitOverviewType ownerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * identifier of the owning group
   * @return ownerId
   */
  
  @Schema(name = "ownerId", description = "identifier of the owning group", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public @Nullable String getOwnerId() {
    return ownerId;
  }

  @JsonProperty("ownerId")
  public void setOwnerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
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
    return Objects.equals(this.permissions, spatialUnitOverviewType.permissions) &&
        Objects.equals(this.availablePeriodsOfValidity, spatialUnitOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.isPublic, spatialUnitOverviewType.isPublic) &&
        Objects.equals(this.metadata, spatialUnitOverviewType.metadata) &&
        Objects.equals(this.mandantId, spatialUnitOverviewType.mandantId) &&
        Objects.equals(this.hierarchies, spatialUnitOverviewType.hierarchies) &&
        Objects.equals(this.spatialUnitId, spatialUnitOverviewType.spatialUnitId) &&
        Objects.equals(this.spatialUnitLevel, spatialUnitOverviewType.spatialUnitLevel) &&
        Objects.equals(this.userPermissions, spatialUnitOverviewType.userPermissions) &&
        Objects.equals(this.wfsUrl, spatialUnitOverviewType.wfsUrl) &&
        Objects.equals(this.wmsUrl, spatialUnitOverviewType.wmsUrl) &&
        Objects.equals(this.isOutlineLayer, spatialUnitOverviewType.isOutlineLayer) &&
        Objects.equals(this.outlineColor, spatialUnitOverviewType.outlineColor) &&
        Objects.equals(this.outlineWidth, spatialUnitOverviewType.outlineWidth) &&
        Objects.equals(this.outlineDashArrayString, spatialUnitOverviewType.outlineDashArrayString) &&
        Objects.equals(this.ownerId, spatialUnitOverviewType.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions, availablePeriodsOfValidity, isPublic, metadata, mandantId, hierarchies, spatialUnitId, spatialUnitLevel, userPermissions, wfsUrl, wmsUrl, isOutlineLayer, outlineColor, outlineWidth, outlineDashArrayString, ownerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitOverviewType {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    mandantId: ").append(toIndentedString(mandantId)).append("\n");
    sb.append("    hierarchies: ").append(toIndentedString(hierarchies)).append("\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
    sb.append("    isOutlineLayer: ").append(toIndentedString(isOutlineLayer)).append("\n");
    sb.append("    outlineColor: ").append(toIndentedString(outlineColor)).append("\n");
    sb.append("    outlineWidth: ").append(toIndentedString(outlineWidth)).append("\n");
    sb.append("    outlineDashArrayString: ").append(toIndentedString(outlineDashArrayString)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

