package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SpatialUnitPOSTInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-03-08T11:42:46.348441096+01:00[Europe/Berlin]")
public class SpatialUnitPOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> permissions = new ArrayList<>();

  private String geoJsonString;

  private String jsonSchema;

  private CommonMetadataType metadata;

  private String nextLowerHierarchyLevel;

  private String nextUpperHierarchyLevel;

  private PeriodOfValidityType periodOfValidity;

  private String spatialUnitLevel;

  private Boolean isOutlineLayer = false;

  private String outlineColor;

  private BigDecimal outlineWidth;

  private String outlineDashArrayString;

  private String ownerId;

  private Boolean isPublic;

  public SpatialUnitPOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitPOSTInputType(List<String> permissions, String geoJsonString, String jsonSchema, CommonMetadataType metadata, String nextLowerHierarchyLevel, String nextUpperHierarchyLevel, PeriodOfValidityType periodOfValidity, String spatialUnitLevel, Boolean isPublic) {
    this.permissions = permissions;
    this.geoJsonString = geoJsonString;
    this.jsonSchema = jsonSchema;
    this.metadata = metadata;
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
    this.periodOfValidity = periodOfValidity;
    this.spatialUnitLevel = spatialUnitLevel;
    this.isPublic = isPublic;
  }

  public SpatialUnitPOSTInputType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public SpatialUnitPOSTInputType addPermissionsItem(String permissionsItem) {
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

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public SpatialUnitPOSTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

  /**
   * a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'
   * @return geoJsonString
  */
  @NotNull 
  @Schema(name = "geoJsonString", description = "a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("geoJsonString")
  public String getGeoJsonString() {
    return geoJsonString;
  }

  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }

  public SpatialUnitPOSTInputType jsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
    return this;
  }

  /**
   * a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.
   * @return jsonSchema
  */
  @NotNull 
  @Schema(name = "jsonSchema", description = "a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("jsonSchema")
  public String getJsonSchema() {
    return jsonSchema;
  }

  public void setJsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
  }

  public SpatialUnitPOSTInputType metadata(CommonMetadataType metadata) {
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

  public SpatialUnitPOSTInputType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
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

  public SpatialUnitPOSTInputType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
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

  public SpatialUnitPOSTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
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

  public SpatialUnitPOSTInputType spatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
    return this;
  }

  /**
   * the name and identifier of the spatial unit level the features apply to
   * @return spatialUnitLevel
  */
  @NotNull 
  @Schema(name = "spatialUnitLevel", description = "the name and identifier of the spatial unit level the features apply to", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitLevel")
  public String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  public void setSpatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitPOSTInputType isOutlineLayer(Boolean isOutlineLayer) {
    this.isOutlineLayer = isOutlineLayer;
    return this;
  }

  /**
   * if true, then KomMonitor web client map application will offer this spatial unit as outline layer in legend control
   * @return isOutlineLayer
  */
  
  @Schema(name = "isOutlineLayer", description = "if true, then KomMonitor web client map application will offer this spatial unit as outline layer in legend control", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isOutlineLayer")
  public Boolean getIsOutlineLayer() {
    return isOutlineLayer;
  }

  public void setIsOutlineLayer(Boolean isOutlineLayer) {
    this.isOutlineLayer = isOutlineLayer;
  }

  public SpatialUnitPOSTInputType outlineColor(String outlineColor) {
    this.outlineColor = outlineColor;
    return this;
  }

  /**
   * outline color for this layer as hex code
   * @return outlineColor
  */
  
  @Schema(name = "outlineColor", description = "outline color for this layer as hex code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outlineColor")
  public String getOutlineColor() {
    return outlineColor;
  }

  public void setOutlineColor(String outlineColor) {
    this.outlineColor = outlineColor;
  }

  public SpatialUnitPOSTInputType outlineWidth(BigDecimal outlineWidth) {
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
  public BigDecimal getOutlineWidth() {
    return outlineWidth;
  }

  public void setOutlineWidth(BigDecimal outlineWidth) {
    this.outlineWidth = outlineWidth;
  }

  public SpatialUnitPOSTInputType outlineDashArrayString(String outlineDashArrayString) {
    this.outlineDashArrayString = outlineDashArrayString;
    return this;
  }

  /**
   * string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return outlineDashArrayString
  */
  
  @Schema(name = "outlineDashArrayString", description = "string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outlineDashArrayString")
  public String getOutlineDashArrayString() {
    return outlineDashArrayString;
  }

  public void setOutlineDashArrayString(String outlineDashArrayString) {
    this.outlineDashArrayString = outlineDashArrayString;
  }

  public SpatialUnitPOSTInputType ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * identifier of the owning group
   * @return ownerId
  */
  
  @Schema(name = "ownerId", description = "identifier of the owning group", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public SpatialUnitPOSTInputType isPublic(Boolean isPublic) {
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
    SpatialUnitPOSTInputType spatialUnitPOSTInputType = (SpatialUnitPOSTInputType) o;
    return Objects.equals(this.permissions, spatialUnitPOSTInputType.permissions) &&
        Objects.equals(this.geoJsonString, spatialUnitPOSTInputType.geoJsonString) &&
        Objects.equals(this.jsonSchema, spatialUnitPOSTInputType.jsonSchema) &&
        Objects.equals(this.metadata, spatialUnitPOSTInputType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitPOSTInputType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitPOSTInputType.nextUpperHierarchyLevel) &&
        Objects.equals(this.periodOfValidity, spatialUnitPOSTInputType.periodOfValidity) &&
        Objects.equals(this.spatialUnitLevel, spatialUnitPOSTInputType.spatialUnitLevel) &&
        Objects.equals(this.isOutlineLayer, spatialUnitPOSTInputType.isOutlineLayer) &&
        Objects.equals(this.outlineColor, spatialUnitPOSTInputType.outlineColor) &&
        Objects.equals(this.outlineWidth, spatialUnitPOSTInputType.outlineWidth) &&
        Objects.equals(this.outlineDashArrayString, spatialUnitPOSTInputType.outlineDashArrayString) &&
        Objects.equals(this.ownerId, spatialUnitPOSTInputType.ownerId) &&
        Objects.equals(this.isPublic, spatialUnitPOSTInputType.isPublic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions, geoJsonString, jsonSchema, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel, periodOfValidity, spatialUnitLevel, isOutlineLayer, outlineColor, outlineWidth, outlineDashArrayString, ownerId, isPublic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitPOSTInputType {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
    sb.append("    jsonSchema: ").append(toIndentedString(jsonSchema)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    isOutlineLayer: ").append(toIndentedString(isOutlineLayer)).append("\n");
    sb.append("    outlineColor: ").append(toIndentedString(outlineColor)).append("\n");
    sb.append("    outlineWidth: ").append(toIndentedString(outlineWidth)).append("\n");
    sb.append("    outlineDashArrayString: ").append(toIndentedString(outlineDashArrayString)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
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

