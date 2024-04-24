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
 * GeoresourcePOSTInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-24T20:35:27.161908166Z[GMT]")


public class GeoresourcePOSTInputType   {
  @JsonProperty("allowedRoles")
  @Valid
  private List<String> allowedRoles = null;

  @JsonProperty("aoiColor")
  private String aoiColor = null;

  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("geoJsonString")
  private String geoJsonString = null;

  @JsonProperty("isAOI")
  private Boolean isAOI = null;

  @JsonProperty("isLOI")
  private Boolean isLOI = null;

  @JsonProperty("isPOI")
  private Boolean isPOI = null;

  @JsonProperty("jsonSchema")
  private String jsonSchema = null;

  @JsonProperty("loiColor")
  private String loiColor = null;

  @JsonProperty("loiDashArrayString")
  private String loiDashArrayString = null;

  @JsonProperty("loiWidth")
  private BigDecimal loiWidth = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("periodOfValidity")
  private PeriodOfValidityType periodOfValidity = null;

  
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

  public GeoresourcePOSTInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourcePOSTInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public GeoresourcePOSTInputType aoiColor(String aoiColor) {
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

  public GeoresourcePOSTInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the dataset
   * @return datasetName
   **/
  @Schema(required = true, description = "the meaningful name of the dataset")
      @NotNull

    public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public GeoresourcePOSTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

  /**
   * a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset
   * @return geoJsonString
   **/
  @Schema(description = "a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset")
  
    public String getGeoJsonString() {
    return geoJsonString;
  }

  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }

  public GeoresourcePOSTInputType isAOI(Boolean isAOI) {
    this.isAOI = isAOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains areas of interest
   * @return isAOI
   **/
  @Schema(required = true, description = "boolean value indicating if the dataset contains areas of interest")
      @NotNull

    public Boolean isIsAOI() {
    return isAOI;
  }

  public void setIsAOI(Boolean isAOI) {
    this.isAOI = isAOI;
  }

  public GeoresourcePOSTInputType isLOI(Boolean isLOI) {
    this.isLOI = isLOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains lines of interest
   * @return isLOI
   **/
  @Schema(required = true, description = "boolean value indicating if the dataset contains lines of interest")
      @NotNull

    public Boolean isIsLOI() {
    return isLOI;
  }

  public void setIsLOI(Boolean isLOI) {
    this.isLOI = isLOI;
  }

  public GeoresourcePOSTInputType isPOI(Boolean isPOI) {
    this.isPOI = isPOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains points of interest
   * @return isPOI
   **/
  @Schema(required = true, description = "boolean value indicating if the dataset contains points of interest")
      @NotNull

    public Boolean isIsPOI() {
    return isPOI;
  }

  public void setIsPOI(Boolean isPOI) {
    this.isPOI = isPOI;
  }

  public GeoresourcePOSTInputType jsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
    return this;
  }

  /**
   * a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.
   * @return jsonSchema
   **/
  @Schema(description = "a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.")
  
    public String getJsonSchema() {
    return jsonSchema;
  }

  public void setJsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
  }

  public GeoresourcePOSTInputType loiColor(String loiColor) {
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

  public GeoresourcePOSTInputType loiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
    return this;
  }

  /**
   * sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return loiDashArrayString
   **/
  @Schema(description = "sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)")
  
    public String getLoiDashArrayString() {
    return loiDashArrayString;
  }

  public void setLoiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
  }

  public GeoresourcePOSTInputType loiWidth(BigDecimal loiWidth) {
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

  public GeoresourcePOSTInputType metadata(CommonMetadataType metadata) {
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

  public GeoresourcePOSTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

  /**
   * Get periodOfValidity
   * @return periodOfValidity
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  public GeoresourcePOSTInputType poiMarkerStyle(PoiMarkerStyleEnum poiMarkerStyle) {
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

  public GeoresourcePOSTInputType poiMarkerText(String poiMarkerText) {
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

  public GeoresourcePOSTInputType poiMarkerColor(ColorType poiMarkerColor) {
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

  public GeoresourcePOSTInputType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
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

  public GeoresourcePOSTInputType poiSymbolColor(ColorType poiSymbolColor) {
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

  public GeoresourcePOSTInputType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity
   * @return topicReference
   **/
  @Schema(description = "id of the last topic hierarchy entity")
  
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
    GeoresourcePOSTInputType georesourcePOSTInputType = (GeoresourcePOSTInputType) o;
    return Objects.equals(this.allowedRoles, georesourcePOSTInputType.allowedRoles) &&
        Objects.equals(this.aoiColor, georesourcePOSTInputType.aoiColor) &&
        Objects.equals(this.datasetName, georesourcePOSTInputType.datasetName) &&
        Objects.equals(this.geoJsonString, georesourcePOSTInputType.geoJsonString) &&
        Objects.equals(this.isAOI, georesourcePOSTInputType.isAOI) &&
        Objects.equals(this.isLOI, georesourcePOSTInputType.isLOI) &&
        Objects.equals(this.isPOI, georesourcePOSTInputType.isPOI) &&
        Objects.equals(this.jsonSchema, georesourcePOSTInputType.jsonSchema) &&
        Objects.equals(this.loiColor, georesourcePOSTInputType.loiColor) &&
        Objects.equals(this.loiDashArrayString, georesourcePOSTInputType.loiDashArrayString) &&
        Objects.equals(this.loiWidth, georesourcePOSTInputType.loiWidth) &&
        Objects.equals(this.metadata, georesourcePOSTInputType.metadata) &&
        Objects.equals(this.periodOfValidity, georesourcePOSTInputType.periodOfValidity) &&
        Objects.equals(this.poiMarkerStyle, georesourcePOSTInputType.poiMarkerStyle) &&
        Objects.equals(this.poiMarkerText, georesourcePOSTInputType.poiMarkerText) &&
        Objects.equals(this.poiMarkerColor, georesourcePOSTInputType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourcePOSTInputType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourcePOSTInputType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourcePOSTInputType.topicReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, aoiColor, datasetName, geoJsonString, isAOI, isLOI, isPOI, jsonSchema, loiColor, loiDashArrayString, loiWidth, metadata, periodOfValidity, poiMarkerStyle, poiMarkerText, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePOSTInputType {\n");
    
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    aoiColor: ").append(toIndentedString(aoiColor)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
    sb.append("    isAOI: ").append(toIndentedString(isAOI)).append("\n");
    sb.append("    isLOI: ").append(toIndentedString(isLOI)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
    sb.append("    jsonSchema: ").append(toIndentedString(jsonSchema)).append("\n");
    sb.append("    loiColor: ").append(toIndentedString(loiColor)).append("\n");
    sb.append("    loiDashArrayString: ").append(toIndentedString(loiDashArrayString)).append("\n");
    sb.append("    loiWidth: ").append(toIndentedString(loiWidth)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
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
