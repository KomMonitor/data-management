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
 * places an existing spatial unit into a hierarchy at a given ordered position (level)
 */

@Schema(name = "SpatialUnitHierarchyMembershipInputType", description = "places an existing spatial unit into a hierarchy at a given ordered position (level)")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyMembershipInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String hierarchyId;

  private Integer hierarchyLevel;

  public SpatialUnitHierarchyMembershipInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyMembershipInputType(String hierarchyId, Integer hierarchyLevel) {
    this.hierarchyId = hierarchyId;
    this.hierarchyLevel = hierarchyLevel;
  }

  public SpatialUnitHierarchyMembershipInputType hierarchyId(String hierarchyId) {
    this.hierarchyId = hierarchyId;
    return this;
  }

  /**
   * the unique identifier of the hierarchy the spatial unit shall be a member of
   * @return hierarchyId
   */
  @NotNull 
  @Schema(name = "hierarchyId", example = "7b3f9a10-0001-4c2d-8e3f-000000000001", description = "the unique identifier of the hierarchy the spatial unit shall be a member of", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hierarchyId")
  public String getHierarchyId() {
    return hierarchyId;
  }

  @JsonProperty("hierarchyId")
  public void setHierarchyId(String hierarchyId) {
    this.hierarchyId = hierarchyId;
  }

  public SpatialUnitHierarchyMembershipInputType hierarchyLevel(Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
    return this;
  }

  /**
   * the ordered position of the spatial unit within the hierarchy. Lower values denote upper levels.
   * @return hierarchyLevel
   */
  @NotNull 
  @Schema(name = "hierarchyLevel", example = "1", description = "the ordered position of the spatial unit within the hierarchy. Lower values denote upper levels.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    SpatialUnitHierarchyMembershipInputType spatialUnitHierarchyMembershipInputType = (SpatialUnitHierarchyMembershipInputType) o;
    return Objects.equals(this.hierarchyId, spatialUnitHierarchyMembershipInputType.hierarchyId) &&
        Objects.equals(this.hierarchyLevel, spatialUnitHierarchyMembershipInputType.hierarchyLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hierarchyId, hierarchyLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyMembershipInputType {\n");
    sb.append("    hierarchyId: ").append(toIndentedString(hierarchyId)).append("\n");
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

