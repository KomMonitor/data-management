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
 * a spatial unit and its ordered position within a hierarchy
 */

@Schema(name = "SpatialUnitHierarchyMemberInputType", description = "a spatial unit and its ordered position within a hierarchy")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyMemberInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String spatialUnitId;

  private Integer hierarchyLevel;

  public SpatialUnitHierarchyMemberInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyMemberInputType(String spatialUnitId, Integer hierarchyLevel) {
    this.spatialUnitId = spatialUnitId;
    this.hierarchyLevel = hierarchyLevel;
  }

  public SpatialUnitHierarchyMemberInputType spatialUnitId(String spatialUnitId) {
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

  public SpatialUnitHierarchyMemberInputType hierarchyLevel(Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
    return this;
  }

  /**
   * the ordered position of the spatial unit within the hierarchy. Lower values denote upper levels.
   * @return hierarchyLevel
   */
  @NotNull 
  @Schema(name = "hierarchyLevel", description = "the ordered position of the spatial unit within the hierarchy. Lower values denote upper levels.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hierarchyLevel")
  public Integer getHierarchyLevel() {
    return hierarchyLevel;
  }

  @JsonProperty("hierarchyLevel")
  public void setHierarchyLevel(Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitHierarchyMemberInputType spatialUnitHierarchyMemberInputType = (SpatialUnitHierarchyMemberInputType) o;
    return Objects.equals(this.spatialUnitId, spatialUnitHierarchyMemberInputType.spatialUnitId) &&
        Objects.equals(this.hierarchyLevel, spatialUnitHierarchyMemberInputType.hierarchyLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, hierarchyLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyMemberInputType {\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    hierarchyLevel: ").append(toIndentedString(hierarchyLevel)).append("\n");
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

