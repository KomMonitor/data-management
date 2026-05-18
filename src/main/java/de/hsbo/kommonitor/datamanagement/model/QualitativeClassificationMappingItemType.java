package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CategoricalMappingType;
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
 * QualitativeClassificationMappingItemType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class QualitativeClassificationMappingItemType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String spatialUnitId;

  @Valid
  private List<@Valid CategoricalMappingType> categoricalData = new ArrayList<>();

  public QualitativeClassificationMappingItemType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QualitativeClassificationMappingItemType(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public QualitativeClassificationMappingItemType spatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
    return this;
  }

  /**
   * spatial unit id
   * @return spatialUnitId
   */
  @NotNull 
  @Schema(name = "spatialUnitId", description = "spatial unit id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatialUnitId")
  public String getSpatialUnitId() {
    return spatialUnitId;
  }

  public void setSpatialUnitId(String spatialUnitId) {
    this.spatialUnitId = spatialUnitId;
  }

  public QualitativeClassificationMappingItemType categoricalData(List<@Valid CategoricalMappingType> categoricalData) {
    this.categoricalData = categoricalData;
    return this;
  }

  public QualitativeClassificationMappingItemType addCategoricalDataItem(CategoricalMappingType categoricalDataItem) {
    if (this.categoricalData == null) {
      this.categoricalData = new ArrayList<>();
    }
    this.categoricalData.add(categoricalDataItem);
    return this;
  }

  /**
   * mapping of categorical values, colors and labels
   * @return categoricalData
   */
  @Valid 
  @Schema(name = "categoricalData", description = "mapping of categorical values, colors and labels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categoricalData")
  public List<@Valid CategoricalMappingType> getCategoricalData() {
    return categoricalData;
  }

  public void setCategoricalData(List<@Valid CategoricalMappingType> categoricalData) {
    this.categoricalData = categoricalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QualitativeClassificationMappingItemType qualitativeClassificationMappingItemType = (QualitativeClassificationMappingItemType) o;
    return Objects.equals(this.spatialUnitId, qualitativeClassificationMappingItemType.spatialUnitId) &&
        Objects.equals(this.categoricalData, qualitativeClassificationMappingItemType.categoricalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialUnitId, categoricalData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualitativeClassificationMappingItemType {\n");
    sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
    sb.append("    categoricalData: ").append(toIndentedString(categoricalData)).append("\n");
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

