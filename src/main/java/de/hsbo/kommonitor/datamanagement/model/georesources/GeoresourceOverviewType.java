package de.hsbo.kommonitor.datamanagement.model.georesources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * GeoresourceOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-02-12T21:13:46.924+01:00")

public class GeoresourceOverviewType   {
  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("georesourceId")
  private String georesourceId = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("availablePeriodOfValidity")
  private AvailablePeriodOfValidityType availablePeriodOfValidity = null;

  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = null;

  @JsonProperty("isPOI")
  private Boolean isPOI = null;

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

  public GeoresourceOverviewType availablePeriodOfValidity(AvailablePeriodOfValidityType availablePeriodOfValidity) {
    this.availablePeriodOfValidity = availablePeriodOfValidity;
    return this;
  }

   /**
   * Get availablePeriodOfValidity
   * @return availablePeriodOfValidity
  **/
  @ApiModelProperty(value = "")
  public AvailablePeriodOfValidityType getAvailablePeriodOfValidity() {
    return availablePeriodOfValidity;
  }

  public void setAvailablePeriodOfValidity(AvailablePeriodOfValidityType availablePeriodOfValidity) {
    this.availablePeriodOfValidity = availablePeriodOfValidity;
  }

  public GeoresourceOverviewType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public GeoresourceOverviewType addApplicableTopicsItem(String applicableTopicsItem) {
    this.applicableTopics.add(applicableTopicsItem);
    return this;
  }

   /**
   * array of thematic categories for which the dataset is applicable. Note that the used topicName has to be defined under /topics
   * @return applicableTopics
  **/
  @ApiModelProperty(required = true, value = "array of thematic categories for which the dataset is applicable. Note that the used topicName has to be defined under /topics")
  public List<String> getApplicableTopics() {
    return applicableTopics;
  }

  public void setApplicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
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
        Objects.equals(this.availablePeriodOfValidity, georesourceOverviewType.availablePeriodOfValidity) &&
        Objects.equals(this.applicableTopics, georesourceOverviewType.applicableTopics) &&
        Objects.equals(this.allowedRoles, georesourceOverviewType.allowedRoles) &&
        Objects.equals(this.isPOI, georesourceOverviewType.isPOI) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourceOverviewType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiMarkerColor, georesourceOverviewType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolColor, georesourceOverviewType.poiSymbolColor) &&
        Objects.equals(this.wmsUrl, georesourceOverviewType.wmsUrl) &&
        Objects.equals(this.wfsUrl, georesourceOverviewType.wfsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasetName, georesourceId, metadata, availablePeriodOfValidity, applicableTopics, allowedRoles, isPOI, poiSymbolBootstrap3Name, poiMarkerColor, poiSymbolColor, wmsUrl, wfsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourceOverviewType {\n");
    
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    georesourceId: ").append(toIndentedString(georesourceId)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    availablePeriodOfValidity: ").append(toIndentedString(availablePeriodOfValidity)).append("\n");
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
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

