package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * a spatial unit member of a hierarchy. The ordering is kept coherent in both representations - hierarchyLevel is always set and the neighbouring spatial units are derived from it.
 */

@Schema(name = "SpatialUnitHierarchyMemberType", description = "a spatial unit member of a hierarchy. The ordering is kept coherent in both representations - hierarchyLevel is always set and the neighbouring spatial units are derived from it.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyMemberType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String spatialUnitId;

  private @Nullable String spatialUnitLevel;

  private Integer hierarchyLevel;

  private @Nullable String nextUpperSpatialUnitId;

  private @Nullable String nextLowerSpatialUnitId;

  public SpatialUnitHierarchyMemberType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyMemberType(String spatialUnitId, Integer hierarchyLevel) {
    this.spatialUnitId = spatialUnitId;
    this.hierarchyLevel = hierarchyLevel;
  }

  public SpatialUnitHierarchyMemberType spatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * the unique identifier of the spatial unit
   * @return spatialUnitId
   */
  @NotNull 
  @Schema(name = "spatialUnitId", description = "the unique identifier of the spatial unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitId")
  public String getSpatialUnitId() {
    return spatialUnitId;
  }

  @JsonProperty("spatialUnitId")
  public void setSpatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public SpatialUnitHierarchyMemberType spatialUnitLevel(@Nullable String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
    return this;
  }

  /**
   * the name of the spatial unit level
   * @return spatialUnitLevel
   */
  
  @Schema(name = "spatialUnitLevel", description = "the name of the spatial unit level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spatialUnitLevel")
  public @Nullable String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  @JsonProperty("spatialUnitLevel")
  public void setSpatialUnitLevel(@Nullable String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitHierarchyMemberType hierarchyLevel(Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
    return this;
  }

  /**
   * the ordered position of the spatial unit within the hierarchy. Lower values denote upper levels. Set when the membership was created by adding an existing spatial unit or reordering.
   * @return hierarchyLevel
   */
  @NotNull 
  @Schema(name = "hierarchyLevel", description = "the ordered position of the spatial unit within the hierarchy. Lower values denote upper levels. Set when the membership was created by adding an existing spatial unit or reordering.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hierarchyLevel")
  public Integer getHierarchyLevel() {
    return hierarchyLevel;
  }

  @JsonProperty("hierarchyLevel")
  public void setHierarchyLevel(Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
  }

  public SpatialUnitHierarchyMemberType nextUpperSpatialUnitId(@Nullable String nextUpperSpatialUnitId) {
    this.nextUpperSpatialUnitId = nextUpperSpatialUnitId;
    return this;
  }

  /**
   * the identifier of the next upper spatial unit within this hierarchy. Null denotes the top level.
   * @return nextUpperSpatialUnitId
   */
  
  @Schema(name = "nextUpperSpatialUnitId", description = "the identifier of the next upper spatial unit within this hierarchy. Null denotes the top level.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextUpperSpatialUnitId")
  public @Nullable String getNextUpperSpatialUnitId() {
    return nextUpperSpatialUnitId;
  }

  @JsonProperty("nextUpperSpatialUnitId")
  public void setNextUpperSpatialUnitId(@Nullable String nextUpperSpatialUnitId) {
    this.nextUpperSpatialUnitId = nextUpperSpatialUnitId;
  }

  public SpatialUnitHierarchyMemberType nextLowerSpatialUnitId(@Nullable String nextLowerSpatialUnitId) {
    this.nextLowerSpatialUnitId = nextLowerSpatialUnitId;
    return this;
  }

  /**
   * the identifier of the next lower spatial unit within this hierarchy. Null denotes the bottom level.
   * @return nextLowerSpatialUnitId
   */
  
  @Schema(name = "nextLowerSpatialUnitId", description = "the identifier of the next lower spatial unit within this hierarchy. Null denotes the bottom level.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextLowerSpatialUnitId")
  public @Nullable String getNextLowerSpatialUnitId() {
    return nextLowerSpatialUnitId;
  }

  @JsonProperty("nextLowerSpatialUnitId")
  public void setNextLowerSpatialUnitId(@Nullable String nextLowerSpatialUnitId) {
    this.nextLowerSpatialUnitId = nextLowerSpatialUnitId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitHierarchyMemberType spatialUnitHierarchyMemberType = (SpatialUnitHierarchyMemberType) o;
    return Objects.equals(this.spatialUnitId, spatialUnitHierarchyMemberType.spatialUnitId) &&
        Objects.equals(this.spatialUnitLevel, spatialUnitHierarchyMemberType.spatialUnitLevel) &&
        Objects.equals(this.hierarchyLevel, spatialUnitHierarchyMemberType.hierarchyLevel) &&
        Objects.equals(this.nextUpperSpatialUnitId, spatialUnitHierarchyMemberType.nextUpperSpatialUnitId) &&
        Objects.equals(this.nextLowerSpatialUnitId, spatialUnitHierarchyMemberType.nextLowerSpatialUnitId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, spatialUnitLevel, hierarchyLevel, nextUpperSpatialUnitId, nextLowerSpatialUnitId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyMemberType {\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    hierarchyLevel: ").append(toIndentedString(hierarchyLevel)).append("\n");
    sb.append("    nextUpperSpatialUnitId: ").append(toIndentedString(nextUpperSpatialUnitId)).append("\n");
    sb.append("    nextLowerSpatialUnitId: ").append(toIndentedString(nextLowerSpatialUnitId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

