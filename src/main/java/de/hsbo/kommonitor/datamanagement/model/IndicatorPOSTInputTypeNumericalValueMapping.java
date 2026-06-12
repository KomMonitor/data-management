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
 * IndicatorPOSTInputTypeNumericalValueMapping
 */


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class IndicatorPOSTInputTypeNumericalValueMapping extends IndicatorPOSTInputTypeValueMapping implements Serializable {

  private static final long serialVersionUID = 1L;

  private Float indicatorValue;

  public IndicatorPOSTInputTypeNumericalValueMapping() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorPOSTInputTypeNumericalValueMapping(Float indicatorValue, LocalDate timestamp, ValueTypeEnum valueType) {
    super(timestamp, valueType);
    this.indicatorValue = indicatorValue;
  }

  public IndicatorPOSTInputTypeNumericalValueMapping indicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
    return this;
  }

  /**
   * the numeric extent of the indicator for the timestamp
   * @return indicatorValue
   */
  @NotNull 
  @Schema(name = "indicatorValue", example = "0.0", description = "the numeric extent of the indicator for the timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorValue")
  public Float getIndicatorValue() {
    return indicatorValue;
  }

  public void setIndicatorValue(Float indicatorValue) {
    this.indicatorValue = indicatorValue;
  }


  public IndicatorPOSTInputTypeNumericalValueMapping timestamp(LocalDate timestamp) {
    super.timestamp(timestamp);
    return this;
  }

  public IndicatorPOSTInputTypeNumericalValueMapping valueType(ValueTypeEnum valueType) {
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
    IndicatorPOSTInputTypeNumericalValueMapping indicatorPOSTInputTypeNumericalValueMapping = (IndicatorPOSTInputTypeNumericalValueMapping) o;
    return Objects.equals(this.indicatorValue, indicatorPOSTInputTypeNumericalValueMapping.indicatorValue) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorValue, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeNumericalValueMapping {\n");
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

