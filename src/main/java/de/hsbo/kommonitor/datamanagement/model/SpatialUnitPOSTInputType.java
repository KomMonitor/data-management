package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-11T11:40:25.959811900+01:00[Europe/Berlin]")
public class SpatialUnitPOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  private String geoJsonString;

  private String jsonSchema;

  private CommonMetadataType metadata;

  private String nextLowerHierarchyLevel;

  private String nextUpperHierarchyLevel;

  private PeriodOfValidityType periodOfValidity;

  private String spatialUnitLevel;

  public SpatialUnitPOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitPOSTInputType(List<String> allowedRoles, String geoJsonString, String jsonSchema, CommonMetadataType metadata, String nextLowerHierarchyLevel, String nextUpperHierarchyLevel, PeriodOfValidityType periodOfValidity, String spatialUnitLevel) {
    this.allowedRoles = allowedRoles;
    this.geoJsonString = geoJsonString;
    this.jsonSchema = jsonSchema;
    this.metadata = metadata;
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
    this.periodOfValidity = periodOfValidity;
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitPOSTInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public SpatialUnitPOSTInputType addAllowedRolesItem(String allowedRolesItem) {
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
  @NotNull 
  @Schema(name = "allowedRoles", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("allowedRoles")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitPOSTInputType spatialUnitPOSTInputType = (SpatialUnitPOSTInputType) o;
    return Objects.equals(this.allowedRoles, spatialUnitPOSTInputType.allowedRoles) &&
        Objects.equals(this.geoJsonString, spatialUnitPOSTInputType.geoJsonString) &&
        Objects.equals(this.jsonSchema, spatialUnitPOSTInputType.jsonSchema) &&
        Objects.equals(this.metadata, spatialUnitPOSTInputType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitPOSTInputType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitPOSTInputType.nextUpperHierarchyLevel) &&
        Objects.equals(this.periodOfValidity, spatialUnitPOSTInputType.periodOfValidity) &&
        Objects.equals(this.spatialUnitLevel, spatialUnitPOSTInputType.spatialUnitLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, geoJsonString, jsonSchema, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel, periodOfValidity, spatialUnitLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitPOSTInputType {\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
    sb.append("    jsonSchema: ").append(toIndentedString(jsonSchema)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
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

