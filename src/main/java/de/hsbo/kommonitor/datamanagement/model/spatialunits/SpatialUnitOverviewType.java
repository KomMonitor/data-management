package de.hsbo.kommonitor.datamanagement.model.spatialunits;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-19T11:48:18.228+02:00")

public class SpatialUnitOverviewType   {
  @JsonProperty("spatialUnitLevel")
  private String spatialUnitLevel = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("nextLowerHierarchyLevel")
  private String nextLowerHierarchyLevel = null;

  @JsonProperty("nextUpperHierarchyLevel")
  private String nextUpperHierarchyLevel = null;

  @JsonProperty("availablePeriodOfValidity")
  private AvailablePeriodOfValidityType availablePeriodOfValidity = null;

  public SpatialUnitOverviewType spatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
    return this;
  }

   /**
   * the name/identifier of the spatial unit level the features apply to
   * @return spatialUnitLevel
  **/
  @ApiModelProperty(required = true, value = "the name/identifier of the spatial unit level the features apply to")
  public String getSpatialUnitLevel() {
    return spatialUnitLevel;
  }

  public void setSpatialUnitLevel(String spatialUnitLevel) {
    this.spatialUnitLevel = spatialUnitLevel;
  }

  public SpatialUnitOverviewType metadata(CommonMetadataType metadata) {
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

  public SpatialUnitOverviewType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
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

  public SpatialUnitOverviewType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
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

  public SpatialUnitOverviewType availablePeriodOfValidity(AvailablePeriodOfValidityType availablePeriodOfValidity) {
    this.availablePeriodOfValidity = availablePeriodOfValidity;
    return this;
  }

   /**
   * Get availablePeriodOfValidity
   * @return availablePeriodOfValidity
  **/
  @ApiModelProperty(value = "")
  public AvailablePeriodOfValidityType getAvailablePeriodOfValidity() {
    return availablePeriodOfValidity;
  }

  public void setAvailablePeriodOfValidity(AvailablePeriodOfValidityType availablePeriodOfValidity) {
    this.availablePeriodOfValidity = availablePeriodOfValidity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpatialUnitOverviewType spatialUnitOverviewType = (SpatialUnitOverviewType) o;
    return Objects.equals(this.spatialUnitLevel, spatialUnitOverviewType.spatialUnitLevel) &&
        Objects.equals(this.metadata, spatialUnitOverviewType.metadata) &&
        Objects.equals(this.nextLowerHierarchyLevel, spatialUnitOverviewType.nextLowerHierarchyLevel) &&
        Objects.equals(this.nextUpperHierarchyLevel, spatialUnitOverviewType.nextUpperHierarchyLevel) &&
        Objects.equals(this.availablePeriodOfValidity, spatialUnitOverviewType.availablePeriodOfValidity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitLevel, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel, availablePeriodOfValidity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpatialUnitOverviewType {\n");
    
    sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
    sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
    sb.append("    availablePeriodOfValidity: ").append(toIndentedString(availablePeriodOfValidity)).append("\n");
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

