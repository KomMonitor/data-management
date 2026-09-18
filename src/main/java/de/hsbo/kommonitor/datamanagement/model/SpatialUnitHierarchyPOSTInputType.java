package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMemberInputType;
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
 * input for creating a mandant-owned spatial unit hierarchy, optionally with its ordered spatial unit members
 */

@Schema(name = "SpatialUnitHierarchyPOSTInputType", description = "input for creating a mandant-owned spatial unit hierarchy, optionally with its ordered spatial unit members")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyPOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;

  private String mandantId;

  private Boolean isPublic = false;

  private List<@Valid SpatialUnitHierarchyMemberInputType> members = new ArrayList<>();

  public SpatialUnitHierarchyPOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyPOSTInputType(String name, String mandantId) {
    this.name = name;
    this.mandantId = mandantId;
  }

  public SpatialUnitHierarchyPOSTInputType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * the name of the hierarchy. Unique within the owning mandant.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Administrative hierarchy", description = "the name of the hierarchy. Unique within the owning mandant.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public SpatialUnitHierarchyPOSTInputType mandantId(String mandantId) {
    this.mandantId = mandantId;
    return this;
  }

  /**
   * identifier of the mandant (organizational unit) that owns the hierarchy
   * @return mandantId
   */
  @NotNull 
  @Schema(name = "mandantId", example = "3c9f8b12-0001-4a1b-9c33-1a2b3c4d5e01", description = "identifier of the mandant (organizational unit) that owns the hierarchy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mandantId")
  public String getMandantId() {
    return mandantId;
  }

  @JsonProperty("mandantId")
  public void setMandantId(String mandantId) {
    this.mandantId = mandantId;
  }

  public SpatialUnitHierarchyPOSTInputType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether the hierarchy is publicly accessible
   * @return isPublic
   */
  
  @Schema(name = "isPublic", example = "false", description = "flag whether the hierarchy is publicly accessible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public SpatialUnitHierarchyPOSTInputType members(List<@Valid SpatialUnitHierarchyMemberInputType> members) {
    this.members = members;
    return this;
  }

  public SpatialUnitHierarchyPOSTInputType addMembersItem(SpatialUnitHierarchyMemberInputType membersItem) {
    if (this.members == null) {
      this.members = new ArrayList<>();
    }
    this.members.add(membersItem);
    return this;
  }

  /**
   * optional ordered list of existing spatial units to place into the new hierarchy. All members must belong to the same mandant as the hierarchy.
   * @return members
   */
  @Valid 
  @Schema(name = "members", description = "optional ordered list of existing spatial units to place into the new hierarchy. All members must belong to the same mandant as the hierarchy.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("members")
  public List<@Valid SpatialUnitHierarchyMemberInputType> getMembers() {
    return members;
  }

  @JsonProperty("members")
  public void setMembers(List<@Valid SpatialUnitHierarchyMemberInputType> members) {
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
    SpatialUnitHierarchyPOSTInputType spatialUnitHierarchyPOSTInputType = (SpatialUnitHierarchyPOSTInputType) o;
    return Objects.equals(this.name, spatialUnitHierarchyPOSTInputType.name) &&
        Objects.equals(this.mandantId, spatialUnitHierarchyPOSTInputType.mandantId) &&
        Objects.equals(this.isPublic, spatialUnitHierarchyPOSTInputType.isPublic) &&
        Objects.equals(this.members, spatialUnitHierarchyPOSTInputType.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, mandantId, isPublic, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyPOSTInputType {\n");
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

