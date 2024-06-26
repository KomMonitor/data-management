package de.hsbo.kommonitor.datamanagement.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * GeoresourcePATCHInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-24T20:35:27.161908166Z[GMT]")


public class GeoresourcePATCHInputType   {
  @JsonProperty("allowedRoles")
  @Valid
  private List<String> allowedRoles = null;

  @JsonProperty("aoiColor")
  private String aoiColor = null;

  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("isAOI")
  private Boolean isAOI = null;

  @JsonProperty("isLOI")
  private Boolean isLOI = null;

  @JsonProperty("isPOI")
  private Boolean isPOI = null;

  @JsonProperty("loiColor")
  private String loiColor = null;

  @JsonProperty("loiDashArrayString")
  private String loiDashArrayString = null;

  @JsonProperty("loiWidth")
  private BigDecimal loiWidth = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("poiMarkerStyle")
  private PoiMarkerStyleEnum poiMarkerStyle = null;

  @JsonProperty("poiMarkerText")
  private String poiMarkerText = null;

  @JsonProperty("poiMarkerColor")
  private ColorType poiMarkerColor = null;

  @JsonProperty("poiSymbolBootstrap3Name")
  private String poiSymbolBootstrap3Name = null;

  @JsonProperty("poiSymbolColor")
  private ColorType poiSymbolColor = null;

  @JsonProperty("topicReference")
  private String topicReference = null;

  public GeoresourcePATCHInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourcePATCHInputType addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<String>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
   **/
  @Schema(description = "list of role identifiers that have read access rights for this dataset")
  
    public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public GeoresourcePATCHInputType aoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
    return this;
  }

  /**
   * color name or color code (i.e. hex number) for areas of interest
   * @return aoiColor
   **/
  @Schema(description = "color name or color code (i.e. hex number) for areas of interest")
  
    public String getAoiColor() {
    return aoiColor;
  }

  public void setAoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
  }

  public GeoresourcePATCHInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the dataset
   * @return datasetName
   **/
  @Schema(description = "the meaningful name of the dataset")
  
    public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public GeoresourcePATCHInputType isAOI(Boolean isAOI) {
    this.isAOI = isAOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains areas of interest
   * @return isAOI
   **/
  @Schema(description = "boolean value indicating if the dataset contains areas of interest")
  
    public Boolean isIsAOI() {
    return isAOI;
  }

  public void setIsAOI(Boolean isAOI) {
    this.isAOI = isAOI;
  }

  public GeoresourcePATCHInputType isLOI(Boolean isLOI) {
    this.isLOI = isLOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains lines of interest
   * @return isLOI
   **/
  @Schema(description = "boolean value indicating if the dataset contains lines of interest")
  
    public Boolean isIsLOI() {
    return isLOI;
  }

  public void setIsLOI(Boolean isLOI) {
    this.isLOI = isLOI;
  }

  public GeoresourcePATCHInputType isPOI(Boolean isPOI) {
    this.isPOI = isPOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains points of interest
   * @return isPOI
   **/
  @Schema(description = "boolean value indicating if the dataset contains points of interest")
  
    public Boolean isIsPOI() {
    return isPOI;
  }

  public void setIsPOI(Boolean isPOI) {
    this.isPOI = isPOI;
  }

  public GeoresourcePATCHInputType loiColor(String loiColor) {
    this.loiColor = loiColor;
    return this;
  }

  /**
   * color name or color code (i.e. hex number) for lines of interest
   * @return loiColor
   **/
  @Schema(description = "color name or color code (i.e. hex number) for lines of interest")
  
    public String getLoiColor() {
    return loiColor;
  }

  public void setLoiColor(String loiColor) {
    this.loiColor = loiColor;
  }

  public GeoresourcePATCHInputType loiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
    return this;
  }

  /**
   * string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return loiDashArrayString
   **/
  @Schema(description = "string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)")
  
    public String getLoiDashArrayString() {
    return loiDashArrayString;
  }

  public void setLoiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
  }

  public GeoresourcePATCHInputType loiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
    return this;
  }

  /**
   * display width for lines of interest (number of pixels in leaflet)
   * @return loiWidth
   **/
  @Schema(example = "0", description = "display width for lines of interest (number of pixels in leaflet)")
  
    @Valid
    public BigDecimal getLoiWidth() {
    return loiWidth;
  }

  public void setLoiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
  }

  public GeoresourcePATCHInputType metadata(CommonMetadataType metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public CommonMetadataType getMetadata() {
    return metadata;
  }

  public void setMetadata(CommonMetadataType metadata) {
    this.metadata = metadata;
  }

  public GeoresourcePATCHInputType poiMarkerStyle(PoiMarkerStyleEnum poiMarkerStyle) {
    this.poiMarkerStyle = poiMarkerStyle;
    return this;
  }

  /**
   * the poi marker type, either text or symbol
   * @return poiMarkerStyle
   **/
  @Schema(description = "the poi marker type, either text or symbol")
  
    public PoiMarkerStyleEnum getPoiMarkerStyle() {
    return poiMarkerStyle;
  }

  public void setPoiMarkerStyle(PoiMarkerStyleEnum poiMarkerStyle) {
    this.poiMarkerStyle = poiMarkerStyle;
  }

  public GeoresourcePATCHInputType poiMarkerText(String poiMarkerText) {
    this.poiMarkerText = poiMarkerText;
    return this;
  }

  /**
   * the poi marker text string to be used if poiMarkerStyle is set to text
   * @return poiMarkerText
   **/
  @Schema(description = "the poi marker text string to be used if poiMarkerStyle is set to text")
  
  @Size(max=3)   public String getPoiMarkerText() {
    return poiMarkerText;
  }

  public void setPoiMarkerText(String poiMarkerText) {
    this.poiMarkerText = poiMarkerText;
  }

  public GeoresourcePATCHInputType poiMarkerColor(ColorType poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
    return this;
  }

  /**
   * Get poiMarkerColor
   * @return poiMarkerColor
   **/
  @Schema(description = "")
  
    @Valid
    public ColorType getPoiMarkerColor() {
    return poiMarkerColor;
  }

  public void setPoiMarkerColor(ColorType poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
  }

  public GeoresourcePATCHInputType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
    return this;
  }

  /**
   * If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)
   * @return poiSymbolBootstrap3Name
   **/
  @Schema(description = "If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)")
  
    public String getPoiSymbolBootstrap3Name() {
    return poiSymbolBootstrap3Name;
  }

  public void setPoiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
  }

  public GeoresourcePATCHInputType poiSymbolColor(ColorType poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
    return this;
  }

  /**
   * Get poiSymbolColor
   * @return poiSymbolColor
   **/
  @Schema(description = "")
  
    @Valid
    public ColorType getPoiSymbolColor() {
    return poiSymbolColor;
  }

  public void setPoiSymbolColor(ColorType poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
  }

  public GeoresourcePATCHInputType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity 
   * @return topicReference
   **/
  @Schema(description = "id of the last topic hierarchy entity ")
  
    public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourcePATCHInputType georesourcePATCHInputType = (GeoresourcePATCHInputType) o;
    return Objects.equals(this.allowedRoles, georesourcePATCHInputType.allowedRoles) &&
        Objects.equals(this.aoiColor, georesourcePATCHInputType.aoiColor) &&
        Objects.equals(this.datasetName, georesourcePATCHInputType.datasetName) &&
        Objects.equals(this.isAOI, georesourcePATCHInputType.isAOI) &&
        Objects.equals(this.isLOI, georesourcePATCHInputType.isLOI) &&
        Objects.equals(this.isPOI, georesourcePATCHInputType.isPOI) &&
        Objects.equals(this.loiColor, georesourcePATCHInputType.loiColor) &&
        Objects.equals(this.loiDashArrayString, georesourcePATCHInputType.loiDashArrayString) &&
        Objects.equals(this.loiWidth, georesourcePATCHInputType.loiWidth) &&
        Objects.equals(this.metadata, georesourcePATCHInputType.metadata) &&
        Objects.equals(this.poiMarkerStyle, georesourcePATCHInputType.poiMarkerStyle) &&
        Objects.equals(this.poiMarkerText, georesourcePATCHInputType.poiMarkerText) &&
        Objects.equals(this.poiMarkerColor, georesourcePATCHInputType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourcePATCHInputType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourcePATCHInputType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourcePATCHInputType.topicReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, aoiColor, datasetName, isAOI, isLOI, isPOI, loiColor, loiDashArrayString, loiWidth, metadata, poiMarkerStyle, poiMarkerText, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePATCHInputType {\n");
    
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    aoiColor: ").append(toIndentedString(aoiColor)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    isAOI: ").append(toIndentedString(isAOI)).append("\n");
    sb.append("    isLOI: ").append(toIndentedString(isLOI)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
    sb.append("    loiColor: ").append(toIndentedString(loiColor)).append("\n");
    sb.append("    loiDashArrayString: ").append(toIndentedString(loiDashArrayString)).append("\n");
    sb.append("    loiWidth: ").append(toIndentedString(loiWidth)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    poiMarkerStyle: ").append(toIndentedString(poiMarkerStyle)).append("\n");
    sb.append("    poiMarkerText: ").append(toIndentedString(poiMarkerText)).append("\n");
    sb.append("    poiMarkerColor: ").append(toIndentedString(poiMarkerColor)).append("\n");
    sb.append("    poiSymbolBootstrap3Name: ").append(toIndentedString(poiSymbolBootstrap3Name)).append("\n");
    sb.append("    poiSymbolColor: ").append(toIndentedString(poiSymbolColor)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
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
