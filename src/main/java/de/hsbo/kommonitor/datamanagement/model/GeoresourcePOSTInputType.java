package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ColorType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PoiMarkerStyleEnum;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-04T15:13:26.315379200+01:00[Europe/Berlin]")
public class GeoresourcePOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> permissions;

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

  private String ownerId;

  private PeriodOfValidityType periodOfValidity;

  private PoiMarkerStyleEnum poiMarkerStyle;

  private String poiMarkerText;

  private ColorType poiMarkerColor;

  private String poiSymbolBootstrap3Name;

  private ColorType poiSymbolColor;

  private String topicReference;

  private Boolean isPublic;

  public GeoresourcePOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GeoresourcePOSTInputType(String datasetName, Boolean isAOI, Boolean isLOI, Boolean isPOI, CommonMetadataType metadata, String ownerId, PeriodOfValidityType periodOfValidity, Boolean isPublic) {
    this.datasetName = datasetName;
    this.isAOI = isAOI;
    this.isLOI = isLOI;
    this.isPOI = isPOI;
    this.metadata = metadata;
    this.ownerId = ownerId;
    this.periodOfValidity = periodOfValidity;
    this.isPublic = isPublic;
  }

  public GeoresourcePOSTInputType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public GeoresourcePOSTInputType addPermissionsItem(String permissionsItem) {
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
  @NotNull 
  @Schema(name = "isAOI", description = "boolean value indicating if the dataset contains areas of interest", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull 
  @Schema(name = "isLOI", description = "boolean value indicating if the dataset contains lines of interest", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull 
  @Schema(name = "isPOI", description = "boolean value indicating if the dataset contains points of interest", requiredMode = Schema.RequiredMode.REQUIRED)
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

  public GeoresourcePOSTInputType ownerId(String ownerId) {
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

  public GeoresourcePOSTInputType poiMarkerStyle(PoiMarkerStyleEnum poiMarkerStyle) {
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

  public GeoresourcePOSTInputType poiMarkerText(String poiMarkerText) {
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

  public GeoresourcePOSTInputType poiMarkerColor(ColorType poiMarkerColor) {
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

  public GeoresourcePOSTInputType poiSymbolColor(ColorType poiSymbolColor) {
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

  public GeoresourcePOSTInputType isPublic(Boolean isPublic) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourcePOSTInputType georesourcePOSTInputType = (GeoresourcePOSTInputType) o;
    return Objects.equals(this.permissions, georesourcePOSTInputType.permissions) &&
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
        Objects.equals(this.ownerId, georesourcePOSTInputType.ownerId) &&
        Objects.equals(this.periodOfValidity, georesourcePOSTInputType.periodOfValidity) &&
        Objects.equals(this.poiMarkerStyle, georesourcePOSTInputType.poiMarkerStyle) &&
        Objects.equals(this.poiMarkerText, georesourcePOSTInputType.poiMarkerText) &&
        Objects.equals(this.poiMarkerColor, georesourcePOSTInputType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourcePOSTInputType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourcePOSTInputType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourcePOSTInputType.topicReference) &&
        Objects.equals(this.isPublic, georesourcePOSTInputType.isPublic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions, aoiColor, datasetName, geoJsonString, isAOI, isLOI, isPOI, jsonSchema, loiColor, loiDashArrayString, loiWidth, metadata, ownerId, periodOfValidity, poiMarkerStyle, poiMarkerText, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference, isPublic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePOSTInputType {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    poiMarkerStyle: ").append(toIndentedString(poiMarkerStyle)).append("\n");
    sb.append("    poiMarkerText: ").append(toIndentedString(poiMarkerText)).append("\n");
    sb.append("    poiMarkerColor: ").append(toIndentedString(poiMarkerColor)).append("\n");
    sb.append("    poiSymbolBootstrap3Name: ").append(toIndentedString(poiSymbolBootstrap3Name)).append("\n");
    sb.append("    poiSymbolColor: ").append(toIndentedString(poiSymbolColor)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
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

