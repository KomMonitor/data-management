package de.hsbo.kommonitor.datamanagement.model.spatialunits;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitPOSTInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class SpatialUnitPOSTInputType   {
  @JsonProperty("spatialUnitLevel")
  private String spatialUnitLevel = null;

  @JsonProperty("periodOfValidity")
  private PeriodOfValidityType periodOfValidity = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("nextLowerHierarchyLevel")
  private String nextLowerHierarchyLevel = null;

  @JsonProperty("nextUpperHierarchyLevel")
  private String nextUpperHierarchyLevel = null;

  @JsonProperty("jsonSchema")
  private String jsonSchema = null;

  @JsonProperty("geoJsonString")
  private String geoJsonString = null;

  public SpatialUnitPOSTInputType spatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
    return this;
  }

   /**
   * the name and identifier of the spatial unit level the features apply to
   * @return spatialUnitLevel
  **/
  @ApiModelProperty(required = true, value = "the name and identifier of the spatial unit level the features apply to")
  public String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  public void setSpatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitPOSTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

   /**
   * Get periodOfValidity
   * @return periodOfValidity
  **/
  @ApiModelProperty(required = true, value = "")
  public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  public SpatialUnitPOSTInputType metadata(CommonMetadataType metadata) {
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

  public SpatialUnitPOSTInputType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
    return this;
  }

   /**
   * the identifier/name of the spatial unit level that contains the features of the nearest lower hierarchy level
   * @return nextLowerHierarchyLevel
  **/
  @ApiModelProperty(required = true, value = "the identifier/name of the spatial unit level that contains the features of the nearest lower hierarchy level")
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
  **/
  @ApiModelProperty(required = true, value = "the identifier/name of the spatial unit level that contains the features of the nearest upper hierarchy level")
  public String getNextUpperHierarchyLevel() {
    return nextUpperHierarchyLevel;
  }

  public void setNextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
  }

  public SpatialUnitPOSTInputType jsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
    return this;
  }

   /**
   * a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.
   * @return jsonSchema
  **/
  @ApiModelProperty(required = true, value = "a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.")
  public String getJsonSchema() {
    return jsonSchema;
  }

  public void setJsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
  }

  public SpatialUnitPOSTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

   /**
   * a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'
   * @return geoJsonString
  **/
  @ApiModelProperty(required = true, value = "a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'")
  public String getGeoJsonString() {
    return geoJsonString;
  }

  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitPOSTInputType spatialUnitPOSTInputType = (SpatialUnitPOSTInputType) o;
    return Objects.equals(this.spatialUnitLevel, spatialUnitPOSTInputType.spatialUnitLevel) &&
        Objects.equals(this.periodOfValidity, spatialUnitPOSTInputType.periodOfValidity) &&
        Objects.equals(this.metadata, spatialUnitPOSTInputType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitPOSTInputType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitPOSTInputType.nextUpperHierarchyLevel) &&
        Objects.equals(this.jsonSchema, spatialUnitPOSTInputType.jsonSchema) &&
        Objects.equals(this.geoJsonString, spatialUnitPOSTInputType.geoJsonString);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitLevel, periodOfValidity, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel, jsonSchema, geoJsonString);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitPOSTInputType {\n");
    
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
    sb.append("    jsonSchema: ").append(toIndentedString(jsonSchema)).append("\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
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

