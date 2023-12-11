package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
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
 * SpatialUnitPATCHInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-11T11:40:25.959811900+01:00[Europe/Berlin]")
public class SpatialUnitPATCHInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  private String datasetName;

  private CommonMetadataType metadata;

  private String nextLowerHierarchyLevel;

  private String nextUpperHierarchyLevel;

  public SpatialUnitPATCHInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpatialUnitPATCHInputType(List<String> allowedRoles, CommonMetadataType metadata, String nextLowerHierarchyLevel, String nextUpperHierarchyLevel) {
    this.allowedRoles = allowedRoles;
    this.metadata = metadata;
    this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
    this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
  }

  public SpatialUnitPATCHInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public SpatialUnitPATCHInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public SpatialUnitPATCHInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * Get datasetName
   * @return datasetName
  */
  
  @Schema(name = "datasetName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("datasetName")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public SpatialUnitPATCHInputType metadata(CommonMetadataType metadata) {
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

  public SpatialUnitPATCHInputType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
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

  public SpatialUnitPATCHInputType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitPATCHInputType spatialUnitPATCHInputType = (SpatialUnitPATCHInputType) o;
    return Objects.equals(this.allowedRoles, spatialUnitPATCHInputType.allowedRoles) &&
        Objects.equals(this.datasetName, spatialUnitPATCHInputType.datasetName) &&
        Objects.equals(this.metadata, spatialUnitPATCHInputType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitPATCHInputType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitPATCHInputType.nextUpperHierarchyLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, datasetName, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitPATCHInputType {\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
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

