package de.hsbo.kommonitor.datamanagement.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * DefaultClassificationMappingItemType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-09T13:07:45.192171293Z[GMT]")

@Entity(name = "DefaultClassificationMappingItemType")
public class DefaultClassificationMappingItemType   {
	
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String mappingId; 
	
  @JsonProperty("spatialUnitId")
  private String spatialUnitId = null;

  @JsonProperty("breaks")
  @Valid
  private List<Float> breaks = new ArrayList<Float>();

  public DefaultClassificationMappingItemType spatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * spatial unit id for manual classification
   * @return spatialUnit
   **/
  @Schema(required = true, description = "spatial unit id for manual classification")
      @NotNull

    public String getSpatialUnitId() {
    return spatialUnitId;
  }

  public void setSpatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public DefaultClassificationMappingItemType breaks(List<Float> breaks) {
    this.breaks = breaks;
    return this;
  }

  public DefaultClassificationMappingItemType addBreaksItem(Float breaksItem) {
    this.breaks.add(breaksItem);
    return this;
  }

  /**
   * array of numeric break values
   * @return breaks
   **/
  @Schema(required = true, description = "array of numeric break values")
      @NotNull

    public List<Float> getBreaks() {
    return breaks;
  }

  public void setBreaks(List<Float> breaks) {
    this.breaks = breaks;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultClassificationMappingItemType defaultClassificationMappingItemType = (DefaultClassificationMappingItemType) o;
    return Objects.equals(this.spatialUnitId, defaultClassificationMappingItemType.spatialUnitId) &&
        Objects.equals(this.breaks, defaultClassificationMappingItemType.breaks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, breaks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultClassificationMappingItemType {\n");
    
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    breaks: ").append(toIndentedString(breaks)).append("\n");
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
