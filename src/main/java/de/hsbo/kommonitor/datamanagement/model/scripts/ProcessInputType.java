package de.hsbo.kommonitor.datamanagement.model.scripts;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

/**
 * ProcessInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class ProcessInputType   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

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

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DataTypeEnum fromValue(String text) {
      for (DataTypeEnum b : DataTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("dataType")
  private DataTypeEnum dataType = null;

  @JsonProperty("defaultValue")
  private String defaultValue = null;

  @JsonProperty("maxParameterValueForNumericInputs")
  private BigDecimal maxParameterValueForNumericInputs = null;

  @JsonProperty("minParameterValueForNumericInputs")
  private BigDecimal minParameterValueForNumericInputs = null;

  public ProcessInputType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * the name of the process input parameter
   * @return name
  **/
  @ApiModelProperty(required = true, value = "the name of the process input parameter")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProcessInputType description(String description) {
    this.description = description;
    return this;
  }

   /**
   * a short description of the process input
   * @return description
  **/
  @ApiModelProperty(required = true, value = "a short description of the process input")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProcessInputType dataType(DataTypeEnum dataType) {
    this.dataType = dataType;
    return this;
  }

   /**
   * the data type of the process input
   * @return dataType
  **/
  @ApiModelProperty(required = true, value = "the data type of the process input")
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
  **/
  @ApiModelProperty(required = true, value = "the default value of the process parameter")
  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public ProcessInputType maxParameterValueForNumericInputs(BigDecimal maxParameterValueForNumericInputs) {
    this.maxParameterValueForNumericInputs = maxParameterValueForNumericInputs;
    return this;
  }

   /**
   * the maximum value that is allowed for the process parameter
   * @return maxParameterValueForNumericInputs
  **/
  @ApiModelProperty(value = "the maximum value that is allowed for the process parameter", example = "0")
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
  **/
  @ApiModelProperty(value = "the minimum value that is allowed for the process parameter", example = "0")
  public BigDecimal getMinParameterValueForNumericInputs() {
    return minParameterValueForNumericInputs;
  }

  public void setMinParameterValueForNumericInputs(BigDecimal minParameterValueForNumericInputs) {
    this.minParameterValueForNumericInputs = minParameterValueForNumericInputs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcessInputType processInputType = (ProcessInputType) o;
    return Objects.equals(this.name, processInputType.name) &&
        Objects.equals(this.description, processInputType.description) &&
        Objects.equals(this.dataType, processInputType.dataType) &&
        Objects.equals(this.defaultValue, processInputType.defaultValue) &&
        Objects.equals(this.maxParameterValueForNumericInputs, processInputType.maxParameterValueForNumericInputs) &&
        Objects.equals(this.minParameterValueForNumericInputs, processInputType.minParameterValueForNumericInputs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, dataType, defaultValue, maxParameterValueForNumericInputs, minParameterValueForNumericInputs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessInputType {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
    sb.append("    maxParameterValueForNumericInputs: ").append(toIndentedString(maxParameterValueForNumericInputs)).append("\n");
    sb.append("    minParameterValueForNumericInputs: ").append(toIndentedString(minParameterValueForNumericInputs)).append("\n");
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

