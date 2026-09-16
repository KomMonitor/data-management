package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMemberType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * metadata of a mandant-owned spatial unit hierarchy and its ordered members
 */

@Schema(name = "SpatialUnitHierarchyOverviewType", description = "metadata of a mandant-owned spatial unit hierarchy and its ordered members")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String hierarchyId;

  private String name;

  private String mandantId;

  private Boolean isPublic;

  private List<@Valid SpatialUnitHierarchyMemberType> members = new ArrayList<>();

  public SpatialUnitHierarchyOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyOverviewType(String hierarchyId, String name, String mandantId, Boolean isPublic) {
    this.hierarchyId = hierarchyId;
    this.name = name;
    this.mandantId = mandantId;
    this.isPublic = isPublic;
  }

  public SpatialUnitHierarchyOverviewType hierarchyId(String hierarchyId) {
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

  public SpatialUnitHierarchyOverviewType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * the name of the hierarchy. Unique within the owning mandant.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "the name of the hierarchy. Unique within the owning mandant.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public SpatialUnitHierarchyOverviewType mandantId(String mandantId) {
    this.mandantId = mandantId;
    return this;
  }

  /**
   * identifier of the mandant (organizational unit) that owns the hierarchy
   * @return mandantId
   */
  @NotNull 
  @Schema(name = "mandantId", description = "identifier of the mandant (organizational unit) that owns the hierarchy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mandantId")
  public String getMandantId() {
    return mandantId;
  }

  @JsonProperty("mandantId")
  public void setMandantId(String mandantId) {
    this.mandantId = mandantId;
  }

  public SpatialUnitHierarchyOverviewType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether the hierarchy is publicly accessible
   * @return isPublic
   */
  @NotNull 
  @Schema(name = "isPublic", description = "flag whether the hierarchy is publicly accessible", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public SpatialUnitHierarchyOverviewType members(List<@Valid SpatialUnitHierarchyMemberType> members) {
    this.members = members;
    return this;
  }

  public SpatialUnitHierarchyOverviewType addMembersItem(SpatialUnitHierarchyMemberType membersItem) {
    if (this.members == null) {
      this.members = new ArrayList<>();
    }
    this.members.add(membersItem);
    return this;
  }

  /**
   * the ordered spatial units that are members of this hierarchy
   * @return members
   */
  @Valid 
  @Schema(name = "members", description = "the ordered spatial units that are members of this hierarchy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("members")
  public List<@Valid SpatialUnitHierarchyMemberType> getMembers() {
    return members;
  }

  @JsonProperty("members")
  public void setMembers(List<@Valid SpatialUnitHierarchyMemberType> members) {
    this.members = members;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitHierarchyOverviewType spatialUnitHierarchyOverviewType = (SpatialUnitHierarchyOverviewType) o;
    return Objects.equals(this.hierarchyId, spatialUnitHierarchyOverviewType.hierarchyId) &&
        Objects.equals(this.name, spatialUnitHierarchyOverviewType.name) &&
        Objects.equals(this.mandantId, spatialUnitHierarchyOverviewType.mandantId) &&
        Objects.equals(this.isPublic, spatialUnitHierarchyOverviewType.isPublic) &&
        Objects.equals(this.members, spatialUnitHierarchyOverviewType.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hierarchyId, name, mandantId, isPublic, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyOverviewType {\n");
    sb.append("    hierarchyId: ").append(toIndentedString(hierarchyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mandantId: ").append(toIndentedString(mandantId)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
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

