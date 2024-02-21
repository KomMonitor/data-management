package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ColorType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GeoresourcePATCHInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:09.865691461+01:00[Europe/Berlin]")
public class GeoresourcePATCHInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String aoiColor;

  private String datasetName;

  private Boolean isAOI;

  private Boolean isLOI;

  private Boolean isPOI;

  private String loiColor;

  private String loiDashArrayString;

  private BigDecimal loiWidth;

  private CommonMetadataType metadata;

  private ColorType poiMarkerColor;

  private String poiSymbolBootstrap3Name;

  private ColorType poiSymbolColor;

  private String topicReference;

  public GeoresourcePATCHInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GeoresourcePATCHInputType(CommonMetadataType metadata) {
    this.metadata = metadata;
  }

  public GeoresourcePATCHInputType aoiColor(String aoiColor) {
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

  public GeoresourcePATCHInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the dataset
   * @return datasetName
  */
  
  @Schema(name = "datasetName", description = "the meaningful name of the dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("datasetName")
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
  */
  
  @Schema(name = "isAOI", description = "boolean value indicating if the dataset contains areas of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isAOI")
  public Boolean getIsAOI() {
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
  */
  
  @Schema(name = "isLOI", description = "boolean value indicating if the dataset contains lines of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isLOI")
  public Boolean getIsLOI() {
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
  */
  
  @Schema(name = "isPOI", description = "boolean value indicating if the dataset contains points of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPOI")
  public Boolean getIsPOI() {
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
  */
  
  @Schema(name = "loiColor", description = "color name or color code (i.e. hex number) for lines of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiColor")
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
  */
  
  @Schema(name = "loiDashArrayString", description = "string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiDashArrayString")
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

  public GeoresourcePATCHInputType metadata(CommonMetadataType metadata) {
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

  public GeoresourcePATCHInputType poiMarkerColor(ColorType poiMarkerColor) {
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

  public GeoresourcePATCHInputType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
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

  public GeoresourcePATCHInputType poiSymbolColor(ColorType poiSymbolColor) {
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

  public GeoresourcePATCHInputType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity 
   * @return topicReference
  */
  
  @Schema(name = "topicReference", description = "id of the last topic hierarchy entity ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topicReference")
  public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourcePATCHInputType georesourcePATCHInputType = (GeoresourcePATCHInputType) o;
    return Objects.equals(this.aoiColor, georesourcePATCHInputType.aoiColor) &&
        Objects.equals(this.datasetName, georesourcePATCHInputType.datasetName) &&
        Objects.equals(this.isAOI, georesourcePATCHInputType.isAOI) &&
        Objects.equals(this.isLOI, georesourcePATCHInputType.isLOI) &&
        Objects.equals(this.isPOI, georesourcePATCHInputType.isPOI) &&
        Objects.equals(this.loiColor, georesourcePATCHInputType.loiColor) &&
        Objects.equals(this.loiDashArrayString, georesourcePATCHInputType.loiDashArrayString) &&
        Objects.equals(this.loiWidth, georesourcePATCHInputType.loiWidth) &&
        Objects.equals(this.metadata, georesourcePATCHInputType.metadata) &&
        Objects.equals(this.poiMarkerColor, georesourcePATCHInputType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourcePATCHInputType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourcePATCHInputType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourcePATCHInputType.topicReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aoiColor, datasetName, isAOI, isLOI, isPOI, loiColor, loiDashArrayString, loiWidth, metadata, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePATCHInputType {\n");
    sb.append("    aoiColor: ").append(toIndentedString(aoiColor)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    isAOI: ").append(toIndentedString(isAOI)).append("\n");
    sb.append("    isLOI: ").append(toIndentedString(isLOI)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
    sb.append("    loiColor: ").append(toIndentedString(loiColor)).append("\n");
    sb.append("    loiDashArrayString: ").append(toIndentedString(loiDashArrayString)).append("\n");
    sb.append("    loiWidth: ").append(toIndentedString(loiWidth)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

