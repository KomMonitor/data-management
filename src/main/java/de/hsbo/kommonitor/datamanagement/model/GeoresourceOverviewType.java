package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ColorType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PoiMarkerStyleEnum;
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
 * GeoresourceOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class GeoresourceOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> permissions = new ArrayList<>();

  private @Nullable String aoiColor;

  @Valid
  private List<@Valid PeriodOfValidityType> availablePeriodsOfValidity = new ArrayList<>();

  private String datasetName;

  private String georesourceId;

  private Boolean isAOI;

  private Boolean isLOI;

  private Boolean isPOI;

  private Boolean isPublic;

  private @Nullable String loiColor;

  private @Nullable String loiDashArrayString;

  private @Nullable BigDecimal loiWidth;

  private CommonMetadataType metadata;

  private String ownerId;

  private @Nullable PoiMarkerStyleEnum poiMarkerStyle;

  private @Nullable String poiMarkerText;

  private @Nullable ColorType poiMarkerColor;

  private @Nullable String poiSymbolBootstrap3Name;

  private @Nullable ColorType poiSymbolColor;

  private String topicReference;

  @Valid
  private List<PermissionLevelType> userPermissions = new ArrayList<>();

  private @Nullable String wfsUrl;

  private @Nullable String wmsUrl;

  public GeoresourceOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GeoresourceOverviewType(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity, String datasetName, String georesourceId, Boolean isAOI, Boolean isLOI, Boolean isPOI, Boolean isPublic, CommonMetadataType metadata, String ownerId, String topicReference) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    this.datasetName = datasetName;
    this.georesourceId = georesourceId;
    this.isAOI = isAOI;
    this.isLOI = isLOI;
    this.isPOI = isPOI;
    this.isPublic = isPublic;
    this.metadata = metadata;
    this.ownerId = ownerId;
    this.topicReference = topicReference;
  }

  public GeoresourceOverviewType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public GeoresourceOverviewType addPermissionsItem(String permissionsItem) {
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
  
  @Schema(name = "permissions", description = "list of permissions on this entity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public GeoresourceOverviewType aoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
    return this;
  }

  /**
   * color name or color code (i.e. hex number) for areas of interest
   * @return aoiColor
   */
  
  @Schema(name = "aoiColor", description = "color name or color code (i.e. hex number) for areas of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aoiColor")
  public String getAoiColor() {
    return aoiColor;
  }

  public void setAoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
  }

  public GeoresourceOverviewType availablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    return this;
  }

  public GeoresourceOverviewType addAvailablePeriodsOfValidityItem(PeriodOfValidityType availablePeriodsOfValidityItem) {
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
  @NotNull @Valid 
  @Schema(name = "availablePeriodsOfValidity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("availablePeriodsOfValidity")
  public List<@Valid PeriodOfValidityType> getAvailablePeriodsOfValidity() {
    return availablePeriodsOfValidity;
  }

  public void setAvailablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public GeoresourceOverviewType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the dataset
   * @return datasetName
   */
  @NotNull 
  @Schema(name = "datasetName", description = "the meaningful name of the dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datasetName")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public GeoresourceOverviewType georesourceId(String georesourceId) {
    this.georesourceId = georesourceId;
    return this;
  }

  /**
   * the unique identifier of the dataset
   * @return georesourceId
   */
  @NotNull 
  @Schema(name = "georesourceId", description = "the unique identifier of the dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("georesourceId")
  public String getGeoresourceId() {
    return georesourceId;
  }

  public void setGeoresourceId(String georesourceId) {
    this.georesourceId = georesourceId;
  }

  public GeoresourceOverviewType isAOI(Boolean isAOI) {
    this.isAOI = isAOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains areas of interest
   * @return isAOI
   */
  @NotNull 
  @Schema(name = "isAOI", description = "boolean value indicating if the dataset contains areas of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isAOI")
  public Boolean getIsAOI() {
    return isAOI;
  }

  public void setIsAOI(Boolean isAOI) {
    this.isAOI = isAOI;
  }

  public GeoresourceOverviewType isLOI(Boolean isLOI) {
    this.isLOI = isLOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains lines of interest
   * @return isLOI
   */
  @NotNull 
  @Schema(name = "isLOI", description = "boolean value indicating if the dataset contains lines of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isLOI")
  public Boolean getIsLOI() {
    return isLOI;
  }

  public void setIsLOI(Boolean isLOI) {
    this.isLOI = isLOI;
  }

  public GeoresourceOverviewType isPOI(Boolean isPOI) {
    this.isPOI = isPOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains points of interest
   * @return isPOI
   */
  @NotNull 
  @Schema(name = "isPOI", description = "boolean value indicating if the dataset contains points of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPOI")
  public Boolean getIsPOI() {
    return isPOI;
  }

  public void setIsPOI(Boolean isPOI) {
    this.isPOI = isPOI;
  }

  public GeoresourceOverviewType isPublic(Boolean isPublic) {
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

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public GeoresourceOverviewType loiColor(String loiColor) {
    this.loiColor = loiColor;
    return this;
  }

  /**
   * color name or color code (i.e. hex number) for lines of interest
   * @return loiColor
   */
  
  @Schema(name = "loiColor", description = "color name or color code (i.e. hex number) for lines of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiColor")
  public String getLoiColor() {
    return loiColor;
  }

  public void setLoiColor(String loiColor) {
    this.loiColor = loiColor;
  }

  public GeoresourceOverviewType loiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
    return this;
  }

  /**
   * sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return loiDashArrayString
   */
  
  @Schema(name = "loiDashArrayString", description = "sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiDashArrayString")
  public String getLoiDashArrayString() {
    return loiDashArrayString;
  }

  public void setLoiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
  }

  public GeoresourceOverviewType loiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
    return this;
  }

  /**
   * display width for lines of interest (number of pixels in leaflet)
   * @return loiWidth
   */
  @Valid 
  @Schema(name = "loiWidth", example = "0.0", description = "display width for lines of interest (number of pixels in leaflet)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiWidth")
  public BigDecimal getLoiWidth() {
    return loiWidth;
  }

  public void setLoiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
  }

  public GeoresourceOverviewType metadata(CommonMetadataType metadata) {
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

  public GeoresourceOverviewType ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * identifier of the owning group
   * @return ownerId
   */
  @NotNull 
  @Schema(name = "ownerId", description = "identifier of the owning group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerId")
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public GeoresourceOverviewType poiMarkerStyle(PoiMarkerStyleEnum poiMarkerStyle) {
    this.poiMarkerStyle = poiMarkerStyle;
    return this;
  }

  /**
   * Get poiMarkerStyle
   * @return poiMarkerStyle
   */
  @Valid 
  @Schema(name = "poiMarkerStyle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiMarkerStyle")
  public PoiMarkerStyleEnum getPoiMarkerStyle() {
    return poiMarkerStyle;
  }

  public void setPoiMarkerStyle(PoiMarkerStyleEnum poiMarkerStyle) {
    this.poiMarkerStyle = poiMarkerStyle;
  }

  public GeoresourceOverviewType poiMarkerText(String poiMarkerText) {
    this.poiMarkerText = poiMarkerText;
    return this;
  }

  /**
   * the poi marker text string to be used if poiMarkerStyle is set to text
   * @return poiMarkerText
   */
  @Size(max = 3) 
  @Schema(name = "poiMarkerText", description = "the poi marker text string to be used if poiMarkerStyle is set to text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiMarkerText")
  public String getPoiMarkerText() {
    return poiMarkerText;
  }

  public void setPoiMarkerText(String poiMarkerText) {
    this.poiMarkerText = poiMarkerText;
  }

  public GeoresourceOverviewType poiMarkerColor(ColorType poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
    return this;
  }

  /**
   * Get poiMarkerColor
   * @return poiMarkerColor
   */
  @Valid 
  @Schema(name = "poiMarkerColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiMarkerColor")
  public ColorType getPoiMarkerColor() {
    return poiMarkerColor;
  }

  public void setPoiMarkerColor(ColorType poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
  }

  public GeoresourceOverviewType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
    return this;
  }

  /**
   * If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)
   * @return poiSymbolBootstrap3Name
   */
  
  @Schema(name = "poiSymbolBootstrap3Name", description = "If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiSymbolBootstrap3Name")
  public String getPoiSymbolBootstrap3Name() {
    return poiSymbolBootstrap3Name;
  }

  public void setPoiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
  }

  public GeoresourceOverviewType poiSymbolColor(ColorType poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
    return this;
  }

  /**
   * Get poiSymbolColor
   * @return poiSymbolColor
   */
  @Valid 
  @Schema(name = "poiSymbolColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiSymbolColor")
  public ColorType getPoiSymbolColor() {
    return poiSymbolColor;
  }

  public void setPoiSymbolColor(ColorType poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
  }

  public GeoresourceOverviewType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity 
   * @return topicReference
   */
  @NotNull 
  @Schema(name = "topicReference", description = "id of the last topic hierarchy entity ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicReference")
  public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  public GeoresourceOverviewType userPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public GeoresourceOverviewType addUserPermissionsItem(PermissionLevelType userPermissionsItem) {
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
  @Valid 
  @Schema(name = "userPermissions", description = "list of permissions that are effective on this dataset for the current user ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userPermissions")
  public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public GeoresourceOverviewType wfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

  /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
   */
  
  @Schema(name = "wfsUrl", description = "the URL of a running WFS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wfsUrl")
  public String getWfsUrl() {
    return wfsUrl;
  }

  public void setWfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }

  public GeoresourceOverviewType wmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

  /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
   */
  
  @Schema(name = "wmsUrl", description = "the URL of a running WMS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    GeoresourceOverviewType georesourceOverviewType = (GeoresourceOverviewType) o;
    return Objects.equals(this.permissions, georesourceOverviewType.permissions) &&
        Objects.equals(this.aoiColor, georesourceOverviewType.aoiColor) &&
        Objects.equals(this.availablePeriodsOfValidity, georesourceOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.datasetName, georesourceOverviewType.datasetName) &&
        Objects.equals(this.georesourceId, georesourceOverviewType.georesourceId) &&
        Objects.equals(this.isAOI, georesourceOverviewType.isAOI) &&
        Objects.equals(this.isLOI, georesourceOverviewType.isLOI) &&
        Objects.equals(this.isPOI, georesourceOverviewType.isPOI) &&
        Objects.equals(this.isPublic, georesourceOverviewType.isPublic) &&
        Objects.equals(this.loiColor, georesourceOverviewType.loiColor) &&
        Objects.equals(this.loiDashArrayString, georesourceOverviewType.loiDashArrayString) &&
        Objects.equals(this.loiWidth, georesourceOverviewType.loiWidth) &&
        Objects.equals(this.metadata, georesourceOverviewType.metadata) &&
        Objects.equals(this.ownerId, georesourceOverviewType.ownerId) &&
        Objects.equals(this.poiMarkerStyle, georesourceOverviewType.poiMarkerStyle) &&
        Objects.equals(this.poiMarkerText, georesourceOverviewType.poiMarkerText) &&
        Objects.equals(this.poiMarkerColor, georesourceOverviewType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourceOverviewType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourceOverviewType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourceOverviewType.topicReference) &&
        Objects.equals(this.userPermissions, georesourceOverviewType.userPermissions) &&
        Objects.equals(this.wfsUrl, georesourceOverviewType.wfsUrl) &&
        Objects.equals(this.wmsUrl, georesourceOverviewType.wmsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions, aoiColor, availablePeriodsOfValidity, datasetName, georesourceId, isAOI, isLOI, isPOI, isPublic, loiColor, loiDashArrayString, loiWidth, metadata, ownerId, poiMarkerStyle, poiMarkerText, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference, userPermissions, wfsUrl, wmsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourceOverviewType {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    aoiColor: ").append(toIndentedString(aoiColor)).append("\n");
    sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    georesourceId: ").append(toIndentedString(georesourceId)).append("\n");
    sb.append("    isAOI: ").append(toIndentedString(isAOI)).append("\n");
    sb.append("    isLOI: ").append(toIndentedString(isLOI)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    loiColor: ").append(toIndentedString(loiColor)).append("\n");
    sb.append("    loiDashArrayString: ").append(toIndentedString(loiDashArrayString)).append("\n");
    sb.append("    loiWidth: ").append(toIndentedString(loiWidth)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    poiMarkerStyle: ").append(toIndentedString(poiMarkerStyle)).append("\n");
    sb.append("    poiMarkerText: ").append(toIndentedString(poiMarkerText)).append("\n");
    sb.append("    poiMarkerColor: ").append(toIndentedString(poiMarkerColor)).append("\n");
    sb.append("    poiSymbolBootstrap3Name: ").append(toIndentedString(poiSymbolBootstrap3Name)).append("\n");
    sb.append("    poiSymbolColor: ").append(toIndentedString(poiSymbolColor)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
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

