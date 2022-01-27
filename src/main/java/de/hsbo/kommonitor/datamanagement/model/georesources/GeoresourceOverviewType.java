package de.hsbo.kommonitor.datamanagement.model.georesources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import io.swagger.annotations.ApiModelProperty;

/**
 * GeoresourceOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2020-03-08T20:17:51.649+01:00")

public class GeoresourceOverviewType   {
  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("georesourceId")
  private String georesourceId = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("availablePeriodsOfValidity")
  private AvailablePeriodsOfValidityType availablePeriodsOfValidity = null;

  @JsonProperty("topicReference")
  private String topicReference = null;

  @JsonProperty("allowedRoles")
  private List<String> allowedRoles = null;

  @JsonProperty("userPermissions")
  private List<PermissionLevelType> userPermissions = null;

  @JsonProperty("isPOI")
  private Boolean isPOI = null;

  @JsonProperty("isLOI")
  private Boolean isLOI = false;

  @JsonProperty("isAOI")
  private Boolean isAOI = false;

  @JsonProperty("loiColor")
  private String loiColor = null;

  @JsonProperty("loiWidth")
  private BigDecimal loiWidth = null;

  @JsonProperty("loiDashArrayString")
  private String loiDashArrayString = null;

  @JsonProperty("aoiColor")
  private String aoiColor = null;

  @JsonProperty("poiSymbolBootstrap3Name")
  private String poiSymbolBootstrap3Name = null;

  @JsonProperty("poiMarkerColor")
  private PoiMarkerColorEnum poiMarkerColor = null;

  @JsonProperty("poiSymbolColor")
  private PoiSymbolColorEnum poiSymbolColor = null;

  @JsonProperty("wmsUrl")
  private String wmsUrl = null;

  @JsonProperty("wfsUrl")
  private String wfsUrl = null;

  public GeoresourceOverviewType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

   /**
   * the meaningful name of the dataset
   * @return datasetName
  **/
  @ApiModelProperty(required = true, value = "the meaningful name of the dataset")
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
  **/
  @ApiModelProperty(required = true, value = "the unique identifier of the dataset")
  public String getGeoresourceId() {
    return georesourceId;
  }

  public void setGeoresourceId(String georesourceId) {
    this.georesourceId = georesourceId;
  }

  public GeoresourceOverviewType metadata(CommonMetadataType metadata) {
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

  public GeoresourceOverviewType availablePeriodsOfValidity(AvailablePeriodsOfValidityType availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    return this;
  }

   /**
   * Get availablePeriodsOfValidity
   * @return availablePeriodsOfValidity
  **/
  @ApiModelProperty(required = true, value = "")
  public AvailablePeriodsOfValidityType getAvailablePeriodsOfValidity() {
    return availablePeriodsOfValidity;
  }

  public void setAvailablePeriodsOfValidity(AvailablePeriodsOfValidityType availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public GeoresourceOverviewType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

   /**
   * id of the last topic hierarchy entity 
   * @return topicReference
  **/
  @ApiModelProperty(required = true, value = "id of the last topic hierarchy entity ")
  public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  public GeoresourceOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourceOverviewType addAllowedRolesItem(String allowedRolesItem) {
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

  public GeoresourceOverviewType isPOI(Boolean isPOI) {
    this.isPOI = isPOI;
    return this;
  }

  /**
   * list of permissions that are effective on this dataset for the current user
   * @return userPermissions
   **/
  @ApiModelProperty(value = "list of permissions that are effective on this dataset for the current user ")
  public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }


   /**
   * boolean value indicating if the dataset contains points of interest
   * @return isPOI
  **/
  @ApiModelProperty(required = true, value = "boolean value indicating if the dataset contains points of interest")
  public Boolean isIsPOI() {
    return isPOI;
  }

  public void setIsPOI(Boolean isPOI) {
    this.isPOI = isPOI;
  }

  public GeoresourceOverviewType isLOI(Boolean isLOI) {
    this.isLOI = isLOI;
    return this;
  }

   /**
   * boolean value indicating if the dataset contains lines of interest
   * @return isLOI
  **/
  @ApiModelProperty(required = true, value = "boolean value indicating if the dataset contains lines of interest")
  public Boolean isIsLOI() {
    return isLOI;
  }

  public void setIsLOI(Boolean isLOI) {
    this.isLOI = isLOI;
  }

  public GeoresourceOverviewType isAOI(Boolean isAOI) {
    this.isAOI = isAOI;
    return this;
  }

   /**
   * boolean value indicating if the dataset contains areas of interest
   * @return isAOI
  **/
  @ApiModelProperty(required = true, value = "boolean value indicating if the dataset contains areas of interest")
  public Boolean isIsAOI() {
    return isAOI;
  }

  public void setIsAOI(Boolean isAOI) {
    this.isAOI = isAOI;
  }

  public GeoresourceOverviewType loiColor(String loiColor) {
    this.loiColor = loiColor;
    return this;
  }

   /**
   * color name or color code (i.e. hex number) for lines of interest
   * @return loiColor
  **/
  @ApiModelProperty(value = "color name or color code (i.e. hex number) for lines of interest")
  public String getLoiColor() {
    return loiColor;
  }

  public void setLoiColor(String loiColor) {
    this.loiColor = loiColor;
  }

  public GeoresourceOverviewType loiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
    return this;
  }

   /**
   * display width for lines of interest (number of pixels in leaflet)
   * @return loiWidth
  **/
  @ApiModelProperty(value = "display width for lines of interest (number of pixels in leaflet)", example = "0")
  public BigDecimal getLoiWidth() {
    return loiWidth;
  }

  public void setLoiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
  }

  public GeoresourceOverviewType loiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
    return this;
  }

   /**
   * sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return loiDashArrayString
  **/
  @ApiModelProperty(value = "sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)")
  public String getLoiDashArrayString() {
    return loiDashArrayString;
  }

  public void setLoiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
  }

  public GeoresourceOverviewType aoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
    return this;
  }

   /**
   * color name or color code (i.e. hex number) for areas of interest
   * @return aoiColor
  **/
  @ApiModelProperty(value = "color name or color code (i.e. hex number) for areas of interest")
  public String getAoiColor() {
    return aoiColor;
  }

  public void setAoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
  }

  public GeoresourceOverviewType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
    return this;
  }

   /**
   * If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)
   * @return poiSymbolBootstrap3Name
  **/
  @ApiModelProperty(value = "If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)")
  public String getPoiSymbolBootstrap3Name() {
    return poiSymbolBootstrap3Name;
  }

  public void setPoiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
  }

  public GeoresourceOverviewType poiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
    return this;
  }

   /**
   * If georesource is a POI then custom POI marker color can be set by specifying one of the following color names
   * @return poiMarkerColor
  **/
  @ApiModelProperty(value = "If georesource is a POI then custom POI marker color can be set by specifying one of the following color names")
  public PoiMarkerColorEnum getPoiMarkerColor() {
    return poiMarkerColor;
  }

  public void setPoiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
  }

  public GeoresourceOverviewType poiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
    return this;
  }

   /**
   * If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names
   * @return poiSymbolColor
  **/
  @ApiModelProperty(value = "If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names")
  public PoiSymbolColorEnum getPoiSymbolColor() {
    return poiSymbolColor;
  }

  public void setPoiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
  }

  public GeoresourceOverviewType wmsUrl(String wmsUrl) {
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

  public GeoresourceOverviewType wfsUrl(String wfsUrl) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourceOverviewType georesourceOverviewType = (GeoresourceOverviewType) o;
    return Objects.equals(this.datasetName, georesourceOverviewType.datasetName) &&
        Objects.equals(this.georesourceId, georesourceOverviewType.georesourceId) &&
        Objects.equals(this.metadata, georesourceOverviewType.metadata) &&
        Objects.equals(this.availablePeriodsOfValidity, georesourceOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.topicReference, georesourceOverviewType.topicReference) &&
        Objects.equals(this.allowedRoles, georesourceOverviewType.allowedRoles) &&
        Objects.equals(this.userPermissions, georesourceOverviewType.userPermissions) &&
        Objects.equals(this.isPOI, georesourceOverviewType.isPOI) &&
        Objects.equals(this.isLOI, georesourceOverviewType.isLOI) &&
        Objects.equals(this.isAOI, georesourceOverviewType.isAOI) &&
        Objects.equals(this.loiColor, georesourceOverviewType.loiColor) &&
        Objects.equals(this.loiWidth, georesourceOverviewType.loiWidth) &&
        Objects.equals(this.loiDashArrayString, georesourceOverviewType.loiDashArrayString) &&
        Objects.equals(this.aoiColor, georesourceOverviewType.aoiColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourceOverviewType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiMarkerColor, georesourceOverviewType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolColor, georesourceOverviewType.poiSymbolColor) &&
        Objects.equals(this.wmsUrl, georesourceOverviewType.wmsUrl) &&
        Objects.equals(this.wfsUrl, georesourceOverviewType.wfsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasetName, georesourceId, metadata, availablePeriodsOfValidity, topicReference, allowedRoles, userPermissions, isPOI, isLOI, isAOI, loiColor, loiWidth, loiDashArrayString, aoiColor, poiSymbolBootstrap3Name, poiMarkerColor, poiSymbolColor, wmsUrl, wfsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourceOverviewType {\n");
    
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    georesourceId: ").append(toIndentedString(georesourceId)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
    sb.append("    isLOI: ").append(toIndentedString(isLOI)).append("\n");
    sb.append("    isAOI: ").append(toIndentedString(isAOI)).append("\n");
    sb.append("    loiColor: ").append(toIndentedString(loiColor)).append("\n");
    sb.append("    loiWidth: ").append(toIndentedString(loiWidth)).append("\n");
    sb.append("    loiDashArrayString: ").append(toIndentedString(loiDashArrayString)).append("\n");
    sb.append("    aoiColor: ").append(toIndentedString(aoiColor)).append("\n");
    sb.append("    poiSymbolBootstrap3Name: ").append(toIndentedString(poiSymbolBootstrap3Name)).append("\n");
    sb.append("    poiMarkerColor: ").append(toIndentedString(poiMarkerColor)).append("\n");
    sb.append("    poiSymbolColor: ").append(toIndentedString(poiSymbolColor)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
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

