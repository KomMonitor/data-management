package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorValueTypeEnum;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
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
 * IndicatorPOSTInputTypeCategoricalValueMapping
 */


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class IndicatorPOSTInputTypeCategoricalValueMapping extends IndicatorPOSTInputTypeValueMapping implements Serializable {

  private static final long serialVersionUID = 1L;

  private String indicatorValue;

  public IndicatorPOSTInputTypeCategoricalValueMapping() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorPOSTInputTypeCategoricalValueMapping(String indicatorValue, LocalDate timestamp, IndicatorValueTypeEnum valueType) {
    super(timestamp, valueType);
    this.indicatorValue = indicatorValue;
  }

  public IndicatorPOSTInputTypeCategoricalValueMapping indicatorValue(String indicatorValue) {
    this.indicatorValue = indicatorValue;
    return this;
  }

  /**
   * the text-based categorical value of the indicator for the timestamp
   * @return indicatorValue
   */
  @NotNull 
  @Schema(name = "indicatorValue", example = "high", description = "the text-based categorical value of the indicator for the timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorValue")
  public String getIndicatorValue() {
    return indicatorValue;
  }

  public void setIndicatorValue(String indicatorValue) {
    this.indicatorValue = indicatorValue;
  }


  public IndicatorPOSTInputTypeCategoricalValueMapping timestamp(LocalDate timestamp) {
    super.timestamp(timestamp);
    return this;
  }

  public IndicatorPOSTInputTypeCategoricalValueMapping valueType(IndicatorValueTypeEnum valueType) {
    super.valueType(valueType);
    return this;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputTypeCategoricalValueMapping indicatorPOSTInputTypeCategoricalValueMapping = (IndicatorPOSTInputTypeCategoricalValueMapping) o;
    return Objects.equals(this.indicatorValue, indicatorPOSTInputTypeCategoricalValueMapping.indicatorValue) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorValue, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeCategoricalValueMapping {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    indicatorValue: ").append(toIndentedString(indicatorValue)).append("\n");
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

