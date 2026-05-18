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
 * CategoricalMappingType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class CategoricalMappingType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String categoricalValue;

  private String color;

  private @Nullable String label;

  public CategoricalMappingType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CategoricalMappingType(String categoricalValue, String color) {
    this.categoricalValue = categoricalValue;
    this.color = color;
  }

  public CategoricalMappingType categoricalValue(String categoricalValue) {
    this.categoricalValue = categoricalValue;
    return this;
  }

  /**
   * the categorical value
   * @return categoricalValue
   */
  @NotNull 
  @Schema(name = "categoricalValue", description = "the categorical value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("categoricalValue")
  public String getCategoricalValue() {
    return categoricalValue;
  }

  public void setCategoricalValue(String categoricalValue) {
    this.categoricalValue = categoricalValue;
  }

  public CategoricalMappingType color(String color) {
    this.color = color;
    return this;
  }

  /**
   * color to use for the current categorical value as hex color code
   * @return color
   */
  @NotNull 
  @Schema(name = "color", description = "color to use for the current categorical value as hex color code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("color")
  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public CategoricalMappingType label(@Nullable String label) {
    this.label = label;
    return this;
  }

  /**
   * alias label to use for the current categorical value
   * @return label
   */
  
  @Schema(name = "label", description = "alias label to use for the current categorical value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("label")
  public @Nullable String getLabel() {
    return label;
  }

  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoricalMappingType categoricalMappingType = (CategoricalMappingType) o;
    return Objects.equals(this.categoricalValue, categoricalMappingType.categoricalValue) &&
        Objects.equals(this.color, categoricalMappingType.color) &&
        Objects.equals(this.label, categoricalMappingType.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoricalValue, color, label);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoricalMappingType {\n");
    sb.append("    categoricalValue: ").append(toIndentedString(categoricalValue)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
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

