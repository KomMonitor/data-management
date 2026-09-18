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
 * places a newly registered spatial unit into a hierarchy by defining its neighbouring spatial units within that hierarchy
 */

@Schema(name = "SpatialUnitHierarchyMembershipPOSTInputType", description = "places a newly registered spatial unit into a hierarchy by defining its neighbouring spatial units within that hierarchy")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyMembershipPOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String hierarchyId;

  private @Nullable String nextUpperSpatialUnitId;

  private @Nullable String nextLowerSpatialUnitId;

  public SpatialUnitHierarchyMembershipPOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyMembershipPOSTInputType(String hierarchyId) {
    this.hierarchyId = hierarchyId;
  }

  public SpatialUnitHierarchyMembershipPOSTInputType hierarchyId(String hierarchyId) {
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

  public SpatialUnitHierarchyMembershipPOSTInputType nextUpperSpatialUnitId(@Nullable String nextUpperSpatialUnitId) {
    this.nextUpperSpatialUnitId = nextUpperSpatialUnitId;
    return this;
  }

  /**
   * the identifier of the next upper spatial unit within this hierarchy. Leave empty if the spatial unit is the top level.
   * @return nextUpperSpatialUnitId
   */
  
  @Schema(name = "nextUpperSpatialUnitId", example = "5a1b2c3d-0001-4e5f-8a9b-000000000001", description = "the identifier of the next upper spatial unit within this hierarchy. Leave empty if the spatial unit is the top level.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextUpperSpatialUnitId")
  public @Nullable String getNextUpperSpatialUnitId() {
    return nextUpperSpatialUnitId;
  }

  @JsonProperty("nextUpperSpatialUnitId")
  public void setNextUpperSpatialUnitId(@Nullable String nextUpperSpatialUnitId) {
    this.nextUpperSpatialUnitId = nextUpperSpatialUnitId;
  }

  public SpatialUnitHierarchyMembershipPOSTInputType nextLowerSpatialUnitId(@Nullable String nextLowerSpatialUnitId) {
    this.nextLowerSpatialUnitId = nextLowerSpatialUnitId;
    return this;
  }

  /**
   * the identifier of the next lower spatial unit within this hierarchy. Leave empty if the spatial unit is the bottom level.
   * @return nextLowerSpatialUnitId
   */
  
  @Schema(name = "nextLowerSpatialUnitId", example = "5a1b2c3d-0002-4e5f-8a9b-000000000002", description = "the identifier of the next lower spatial unit within this hierarchy. Leave empty if the spatial unit is the bottom level.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    SpatialUnitHierarchyMembershipPOSTInputType spatialUnitHierarchyMembershipPOSTInputType = (SpatialUnitHierarchyMembershipPOSTInputType) o;
    return Objects.equals(this.hierarchyId, spatialUnitHierarchyMembershipPOSTInputType.hierarchyId) &&
        Objects.equals(this.nextUpperSpatialUnitId, spatialUnitHierarchyMembershipPOSTInputType.nextUpperSpatialUnitId) &&
        Objects.equals(this.nextLowerSpatialUnitId, spatialUnitHierarchyMembershipPOSTInputType.nextLowerSpatialUnitId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hierarchyId, nextUpperSpatialUnitId, nextLowerSpatialUnitId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyMembershipPOSTInputType {\n");
    sb.append("    hierarchyId: ").append(toIndentedString(hierarchyId)).append("\n");
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

