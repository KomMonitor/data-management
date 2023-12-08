package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import java.math.BigDecimal;
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
 * GeoresourcePOSTInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T15:24:58.815569400+01:00[Europe/Berlin]")
public class GeoresourcePOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> allowedRoles;

  private String aoiColor;

  private String datasetName;

  private String geoJsonString;

  private Boolean isAOI;

  private Boolean isLOI;

  private Boolean isPOI;

  private String jsonSchema;

  private String loiColor;

  private String loiDashArrayString;

  private BigDecimal loiWidth;

  private CommonMetadataType metadata;

  private PeriodOfValidityType periodOfValidity;

  /**
   * If georesource is a POI then custom POI marker color can be set by specifying one of the following color names
   */
  public enum PoiMarkerColorEnum {
    WHITE("white"),
    
    RED("red"),
    
    ORANGE("orange"),
    
    BEIGE("beige"),
    
    GREEN("green"),
    
    BLUE("blue"),
    
    PURPLE("purple"),
    
    PINK("pink"),
    
    GRAY("gray"),
    
    BLACK("black");

    private String value;

    PoiMarkerColorEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PoiMarkerColorEnum fromValue(String value) {
      for (PoiMarkerColorEnum b : PoiMarkerColorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PoiMarkerColorEnum poiMarkerColor;

  private String poiSymbolBootstrap3Name;

  /**
   * If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names
   */
  public enum PoiSymbolColorEnum {
    WHITE("white"),
    
    RED("red"),
    
    ORANGE("orange"),
    
    BEIGE("beige"),
    
    GREEN("green"),
    
    BLUE("blue"),
    
    PURPLE("purple"),
    
    PINK("pink"),
    
    GRAY("gray"),
    
    BLACK("black");

    private String value;

    PoiSymbolColorEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PoiSymbolColorEnum fromValue(String value) {
      for (PoiSymbolColorEnum b : PoiSymbolColorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PoiSymbolColorEnum poiSymbolColor;

  private String topicReference;

  public GeoresourcePOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GeoresourcePOSTInputType(String datasetName, CommonMetadataType metadata, PeriodOfValidityType periodOfValidity) {
    this.datasetName = datasetName;
    this.metadata = metadata;
    this.periodOfValidity = periodOfValidity;
  }

  public GeoresourcePOSTInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourcePOSTInputType addAllowedRolesItem(String allowedRolesItem) {
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
  
  @Schema(name = "allowedRoles", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedRoles")
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
  */
  
  @Schema(name = "aoiColor", description = "color name or color code (i.e. hex number) for areas of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aoiColor")
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

  public GeoresourcePOSTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

  /**
   * a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset
   * @return geoJsonString
  */
  
  @Schema(name = "geoJsonString", description = "a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("geoJsonString")
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
  */
  
  @Schema(name = "isAOI", description = "boolean value indicating if the dataset contains areas of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isAOI")
  public Boolean getIsAOI() {
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
  */
  
  @Schema(name = "isLOI", description = "boolean value indicating if the dataset contains lines of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isLOI")
  public Boolean getIsLOI() {
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
  */
  
  @Schema(name = "isPOI", description = "boolean value indicating if the dataset contains points of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPOI")
  public Boolean getIsPOI() {
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
  */
  
  @Schema(name = "jsonSchema", description = "a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("jsonSchema")
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
  */
  
  @Schema(name = "loiColor", description = "color name or color code (i.e. hex number) for lines of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiColor")
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
  */
  
  @Schema(name = "loiDashArrayString", description = "sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiDashArrayString")
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

  public GeoresourcePOSTInputType metadata(CommonMetadataType metadata) {
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

  public GeoresourcePOSTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

  /**
   * Get periodOfValidity
   * @return periodOfValidity
  */
  @NotNull @Valid 
  @Schema(name = "periodOfValidity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("periodOfValidity")
  public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  public GeoresourcePOSTInputType poiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
    return this;
  }

  /**
   * If georesource is a POI then custom POI marker color can be set by specifying one of the following color names
   * @return poiMarkerColor
  */
  
  @Schema(name = "poiMarkerColor", description = "If georesource is a POI then custom POI marker color can be set by specifying one of the following color names", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiMarkerColor")
  public PoiMarkerColorEnum getPoiMarkerColor() {
    return poiMarkerColor;
  }

  public void setPoiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
  }

  public GeoresourcePOSTInputType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
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

  public GeoresourcePOSTInputType poiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
    return this;
  }

  /**
   * If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names
   * @return poiSymbolColor
  */
  
  @Schema(name = "poiSymbolColor", description = "If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiSymbolColor")
  public PoiSymbolColorEnum getPoiSymbolColor() {
    return poiSymbolColor;
  }

  public void setPoiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
  }

  public GeoresourcePOSTInputType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity
   * @return topicReference
  */
  
  @Schema(name = "topicReference", description = "id of the last topic hierarchy entity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
        Objects.equals(this.poiMarkerColor, georesourcePOSTInputType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourcePOSTInputType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourcePOSTInputType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourcePOSTInputType.topicReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, aoiColor, datasetName, geoJsonString, isAOI, isLOI, isPOI, jsonSchema, loiColor, loiDashArrayString, loiWidth, metadata, periodOfValidity, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference);
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

