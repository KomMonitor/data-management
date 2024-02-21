package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProcessInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:09.865691461+01:00[Europe/Berlin]")
public class ProcessInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * the data type of the process input
   */
  public enum DataTypeEnum {
    STRING("string"),
    
    BOOLEAN("boolean"),
    
    INTEGER("integer"),
    
    DOUBLE("double");

    private String value;

    DataTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DataTypeEnum fromValue(String value) {
      for (DataTypeEnum b : DataTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DataTypeEnum dataType;

  private String defaultValue;

  private String description;

  private BigDecimal maxParameterValueForNumericInputs;

  private BigDecimal minParameterValueForNumericInputs;

  private String name;

  public ProcessInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProcessInputType(DataTypeEnum dataType, String defaultValue, String description, String name) {
    this.dataType = dataType;
    this.defaultValue = defaultValue;
    this.description = description;
    this.name = name;
  }

  public ProcessInputType dataType(DataTypeEnum dataType) {
    this.dataType = dataType;
    return this;
  }

  /**
   * the data type of the process input
   * @return dataType
  */
  @NotNull 
  @Schema(name = "dataType", description = "the data type of the process input", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dataType")
  public DataTypeEnum getDataType() {
    return dataType;
  }

  public void setDataType(DataTypeEnum dataType) {
    this.dataType = dataType;
  }

  public ProcessInputType defaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * the default value of the process parameter
   * @return defaultValue
  */
  @NotNull 
  @Schema(name = "defaultValue", description = "the default value of the process parameter", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defaultValue")
  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public ProcessInputType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * a short description of the process input
   * @return description
  */
  @NotNull 
  @Schema(name = "description", description = "a short description of the process input", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProcessInputType maxParameterValueForNumericInputs(BigDecimal maxParameterValueForNumericInputs) {
    this.maxParameterValueForNumericInputs = maxParameterValueForNumericInputs;
    return this;
  }

  /**
   * the maximum value that is allowed for the process parameter
   * @return maxParameterValueForNumericInputs
  */
  @Valid 
  @Schema(name = "maxParameterValueForNumericInputs", example = "0.0", description = "the maximum value that is allowed for the process parameter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParameterValueForNumericInputs")
  public BigDecimal getMaxParameterValueForNumericInputs() {
    return maxParameterValueForNumericInputs;
  }

  public void setMaxParameterValueForNumericInputs(BigDecimal maxParameterValueForNumericInputs) {
    this.maxParameterValueForNumericInputs = maxParameterValueForNumericInputs;
  }

  public ProcessInputType minParameterValueForNumericInputs(BigDecimal minParameterValueForNumericInputs) {
    this.minParameterValueForNumericInputs = minParameterValueForNumericInputs;
    return this;
  }

  /**
   * the minimum value that is allowed for the process parameter
   * @return minParameterValueForNumericInputs
  */
  @Valid 
  @Schema(name = "minParameterValueForNumericInputs", example = "0.0", description = "the minimum value that is allowed for the process parameter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minParameterValueForNumericInputs")
  public BigDecimal getMinParameterValueForNumericInputs() {
    return minParameterValueForNumericInputs;
  }

  public void setMinParameterValueForNumericInputs(BigDecimal minParameterValueForNumericInputs) {
    this.minParameterValueForNumericInputs = minParameterValueForNumericInputs;
  }

  public ProcessInputType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * the name of the process input parameter
   * @return name
  */
  @NotNull 
  @Schema(name = "name", description = "the name of the process input parameter", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcessInputType processInputType = (ProcessInputType) o;
    return Objects.equals(this.dataType, processInputType.dataType) &&
        Objects.equals(this.defaultValue, processInputType.defaultValue) &&
        Objects.equals(this.description, processInputType.description) &&
        Objects.equals(this.maxParameterValueForNumericInputs, processInputType.maxParameterValueForNumericInputs) &&
        Objects.equals(this.minParameterValueForNumericInputs, processInputType.minParameterValueForNumericInputs) &&
        Objects.equals(this.name, processInputType.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType, defaultValue, description, maxParameterValueForNumericInputs, minParameterValueForNumericInputs, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessInputType {\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    maxParameterValueForNumericInputs: ").append(toIndentedString(maxParameterValueForNumericInputs)).append("\n");
    sb.append("    minParameterValueForNumericInputs: ").append(toIndentedString(minParameterValueForNumericInputs)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

