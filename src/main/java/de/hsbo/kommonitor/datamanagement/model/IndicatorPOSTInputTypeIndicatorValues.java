package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorValueTypeEnum;
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
 * IndicatorPOSTInputTypeIndicatorValues
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class IndicatorPOSTInputTypeIndicatorValues implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String spatialReferenceKey;

  private IndicatorValueTypeEnum valueType = IndicatorValueTypeEnum.NUMERIC;

  @Valid
  private List<IndicatorPOSTInputTypeValueMapping> valueMapping = new ArrayList<>();

  public IndicatorPOSTInputTypeIndicatorValues() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorPOSTInputTypeIndicatorValues(List<IndicatorPOSTInputTypeValueMapping> valueMapping) {
    this.valueMapping = valueMapping;
  }

  public IndicatorPOSTInputTypeIndicatorValues spatialReferenceKey(@Nullable String spatialReferenceKey) {
    this.spatialReferenceKey = spatialReferenceKey;
    return this;
  }

  /**
   * identifier (uuid) of the spatial feature to which the values shall be applied
   * @return spatialReferenceKey
   */
  
  @Schema(name = "spatialReferenceKey", description = "identifier (uuid) of the spatial feature to which the values shall be applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spatialReferenceKey")
  public @Nullable String getSpatialReferenceKey() {
    return spatialReferenceKey;
  }

  public void setSpatialReferenceKey(@Nullable String spatialReferenceKey) {
    this.spatialReferenceKey = spatialReferenceKey;
  }

  public IndicatorPOSTInputTypeIndicatorValues valueType(IndicatorValueTypeEnum valueType) {
    this.valueType = valueType;
    return this;
  }

  /**
   * Informational field indicating the type of mapping values inside this container.
   * @return valueType
   */
  @Valid 
  @Schema(name = "valueType", description = "Informational field indicating the type of mapping values inside this container.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valueType")
  public IndicatorValueTypeEnum getValueType() {
    return valueType;
  }

  public void setValueType(IndicatorValueTypeEnum valueType) {
    this.valueType = valueType;
  }

  public IndicatorPOSTInputTypeIndicatorValues valueMapping(List<IndicatorPOSTInputTypeValueMapping> valueMapping) {
    this.valueMapping = valueMapping;
    return this;
  }

  public IndicatorPOSTInputTypeIndicatorValues addValueMappingItem(IndicatorPOSTInputTypeValueMapping valueMappingItem) {
    if (this.valueMapping == null) {
      this.valueMapping = new ArrayList<>();
    }
    this.valueMapping.add(valueMappingItem);
    return this;
  }

  /**
   * an array of entries mapping an indicator value to a timestamp as mapping key
   * @return valueMapping
   */
  @NotNull @Valid 
  @Schema(name = "valueMapping", description = "an array of entries mapping an indicator value to a timestamp as mapping key", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("valueMapping")
  public List<IndicatorPOSTInputTypeValueMapping> getValueMapping() {
    return valueMapping;
  }

  public void setValueMapping(List<IndicatorPOSTInputTypeValueMapping> valueMapping) {
    this.valueMapping = valueMapping;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputTypeIndicatorValues indicatorPOSTInputTypeIndicatorValues = (IndicatorPOSTInputTypeIndicatorValues) o;
    return Objects.equals(this.spatialReferenceKey, indicatorPOSTInputTypeIndicatorValues.spatialReferenceKey) &&
        Objects.equals(this.valueType, indicatorPOSTInputTypeIndicatorValues.valueType) &&
        Objects.equals(this.valueMapping, indicatorPOSTInputTypeIndicatorValues.valueMapping);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spatialReferenceKey, valueType, valueMapping);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeIndicatorValues {\n");
    sb.append("    spatialReferenceKey: ").append(toIndentedString(spatialReferenceKey)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    valueMapping: ").append(toIndentedString(valueMapping)).append("\n");
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

