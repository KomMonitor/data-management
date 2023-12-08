package de.hsbo.kommonitor.datamanagement.model.legacy;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * ErrorType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-14T08:31:38.846+02:00")

public class ErrorType   {
  @JsonProperty("type")
  private String type = null;

  @JsonProperty("label")
  private String label = null;

  @JsonProperty("message")
  private String message = null;

  public ErrorType type(String type) {
    this.type = type;
    return this;
  }

   /**
   * type of the error
   * @return type
  **/
  @ApiModelProperty(required = true, value = "type of the error")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ErrorType label(String label) {
    this.label = label;
    return this;
  }

   /**
   * label of the error
   * @return label
  **/
  @ApiModelProperty(required = true, value = "label of the error")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public ErrorType message(String message) {
    this.message = message;
    return this;
  }

   /**
   * additional information about the error
   * @return message
  **/
  @ApiModelProperty(required = true, value = "additional information about the error")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorType errorType = (ErrorType) o;
    return Objects.equals(this.type, errorType.type) &&
        Objects.equals(this.label, errorType.label) &&
        Objects.equals(this.message, errorType.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, label, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorType {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

