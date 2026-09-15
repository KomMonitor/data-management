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
 * membership of a spatial unit within a hierarchy. The ordering is kept coherent in both representations - hierarchyLevel is always set and the neighbouring spatial units are derived from it.
 */

@Schema(name = "SpatialUnitHierarchyMembershipType", description = "membership of a spatial unit within a hierarchy. The ordering is kept coherent in both representations - hierarchyLevel is always set and the neighbouring spatial units are derived from it.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyMembershipType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String hierarchyId;

  private @Nullable String hierarchyName;

  private Integer hierarchyLevel;

  private @Nullable String nextUpperSpatialUnitId;

  private @Nullable String nextLowerSpatialUnitId;

  public SpatialUnitHierarchyMembershipType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyMembershipType(String hierarchyId, Integer hierarchyLevel) {
    this.hierarchyId = hierarchyId;
    this.hierarchyLevel = hierarchyLevel;
  }

  public SpatialUnitHierarchyMembershipType hierarchyId(String hierarchyId) {
    this.hierarchyId = hierarchyId;
    return this;
  }

  /**
   * the unique identifier of the hierarchy
   * @return hierarchyId
   */
  @NotNull 
  @Schema(name = "hierarchyId", description = "the unique identifier of the hierarchy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hierarchyId")
  public String getHierarchyId() {
    return hierarchyId;
  }

  @JsonProperty("hierarchyId")
  public void setHierarchyId(String hierarchyId) {
    this.hierarchyId = hierarchyId;
  }

  public SpatialUnitHierarchyMembershipType hierarchyName(@Nullable String hierarchyName) {
    this.hierarchyName = hierarchyName;
    return this;
  }

  /**
   * the name of the hierarchy
   * @return hierarchyName
   */
  
  @Schema(name = "hierarchyName", description = "the name of the hierarchy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hierarchyName")
  public @Nullable String getHierarchyName() {
    return hierarchyName;
  }

  @JsonProperty("hierarchyName")
  public void setHierarchyName(@Nullable String hierarchyName) {
    this.hierarchyName = hierarchyName;
  }

  public SpatialUnitHierarchyMembershipType hierarchyLevel(Integer hierarchyLevel) {
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

  public SpatialUnitHierarchyMembershipType nextUpperSpatialUnitId(@Nullable String nextUpperSpatialUnitId) {
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

  public SpatialUnitHierarchyMembershipType nextLowerSpatialUnitId(@Nullable String nextLowerSpatialUnitId) {
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
    SpatialUnitHierarchyMembershipType spatialUnitHierarchyMembershipType = (SpatialUnitHierarchyMembershipType) o;
    return Objects.equals(this.hierarchyId, spatialUnitHierarchyMembershipType.hierarchyId) &&
        Objects.equals(this.hierarchyName, spatialUnitHierarchyMembershipType.hierarchyName) &&
        Objects.equals(this.hierarchyLevel, spatialUnitHierarchyMembershipType.hierarchyLevel) &&
        Objects.equals(this.nextUpperSpatialUnitId, spatialUnitHierarchyMembershipType.nextUpperSpatialUnitId) &&
        Objects.equals(this.nextLowerSpatialUnitId, spatialUnitHierarchyMembershipType.nextLowerSpatialUnitId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hierarchyId, hierarchyName, hierarchyLevel, nextUpperSpatialUnitId, nextLowerSpatialUnitId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyMembershipType {\n");
    sb.append("    hierarchyId: ").append(toIndentedString(hierarchyId)).append("\n");
    sb.append("    hierarchyName: ").append(toIndentedString(hierarchyName)).append("\n");
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

