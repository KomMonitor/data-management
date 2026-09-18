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
 * input for updating a mandant-owned spatial unit hierarchy. The owning mandant is immutable and must match the hierarchy&#39;s current mandant.
 */

@Schema(name = "SpatialUnitHierarchyInputType", description = "input for updating a mandant-owned spatial unit hierarchy. The owning mandant is immutable and must match the hierarchy's current mandant.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class SpatialUnitHierarchyInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;

  private String mandantId;

  private Boolean isPublic = false;

  public SpatialUnitHierarchyInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitHierarchyInputType(String name, String mandantId) {
    this.name = name;
    this.mandantId = mandantId;
  }

  public SpatialUnitHierarchyInputType name(String name) {
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

  public SpatialUnitHierarchyInputType mandantId(String mandantId) {
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

  public SpatialUnitHierarchyInputType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether the hierarchy is publicly accessible
   * @return isPublic
   */
  
  @Schema(name = "isPublic", example = "true", description = "flag whether the hierarchy is publicly accessible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  @JsonProperty("isPublic")
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
    SpatialUnitHierarchyInputType spatialUnitHierarchyInputType = (SpatialUnitHierarchyInputType) o;
    return Objects.equals(this.name, spatialUnitHierarchyInputType.name) &&
        Objects.equals(this.mandantId, spatialUnitHierarchyInputType.mandantId) &&
        Objects.equals(this.isPublic, spatialUnitHierarchyInputType.isPublic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, mandantId, isPublic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitHierarchyInputType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mandantId: ").append(toIndentedString(mandantId)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
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

