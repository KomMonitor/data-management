package de.hsbo.kommonitor.datamanagement.model.legacy.indicators;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * a reference to another indicator, e.g. a sub-indicator that is used to compute the main indicator
 */
@ApiModel(description = "a reference to another indicator, e.g. a sub-indicator that is used to compute the main indicator")

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-26T10:18:13.375+02:00")

public class IndicatorReferenceType   {
  @JsonProperty("referencedIndicatorId")
  private String referencedIndicatorId = null;

  @JsonProperty("referencedIndicatorName")
  private String referencedIndicatorName = null;

  @JsonProperty("referencedIndicatorDescription")
  private String referencedIndicatorDescription = null;

  public IndicatorReferenceType referencedIndicatorId(String referencedIndicatorId) {
    this.referencedIndicatorId = referencedIndicatorId;
    return this;
  }

   /**
   * unique identifier of the referenced indicator
   * @return referencedIndicatorId
  **/
  @ApiModelProperty(required = true, value = "unique identifier of the referenced indicator")
  public String getReferencedIndicatorId() {
    return referencedIndicatorId;
  }

  public void setReferencedIndicatorId(String referencedIndicatorId) {
    this.referencedIndicatorId = referencedIndicatorId;
  }

  public IndicatorReferenceType referencedIndicatorName(String referencedIndicatorName) {
    this.referencedIndicatorName = referencedIndicatorName;
    return this;
  }

   /**
   * the meaningful name of the referenced indicator
   * @return referencedIndicatorName
  **/
  @ApiModelProperty(required = true, value = "the meaningful name of the referenced indicator")
  public String getReferencedIndicatorName() {
    return referencedIndicatorName;
  }

  public void setReferencedIndicatorName(String referencedIndicatorName) {
    this.referencedIndicatorName = referencedIndicatorName;
  }

  public IndicatorReferenceType referencedIndicatorDescription(String referencedIndicatorDescription) {
    this.referencedIndicatorDescription = referencedIndicatorDescription;
    return this;
  }

   /**
   * a meaningful description of how the referenced indicator is related to the main indicator
   * @return referencedIndicatorDescription
  **/
  @ApiModelProperty(required = true, value = "a meaningful description of how the referenced indicator is related to the main indicator")
  public String getReferencedIndicatorDescription() {
    return referencedIndicatorDescription;
  }

  public void setReferencedIndicatorDescription(String referencedIndicatorDescription) {
    this.referencedIndicatorDescription = referencedIndicatorDescription;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorReferenceType indicatorReferenceType = (IndicatorReferenceType) o;
    return Objects.equals(this.referencedIndicatorId, indicatorReferenceType.referencedIndicatorId) &&
        Objects.equals(this.referencedIndicatorName, indicatorReferenceType.referencedIndicatorName) &&
        Objects.equals(this.referencedIndicatorDescription, indicatorReferenceType.referencedIndicatorDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referencedIndicatorId, referencedIndicatorName, referencedIndicatorDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorReferenceType {\n");
    
    sb.append("    referencedIndicatorId: ").append(toIndentedString(referencedIndicatorId)).append("\n");
    sb.append("    referencedIndicatorName: ").append(toIndentedString(referencedIndicatorName)).append("\n");
    sb.append("    referencedIndicatorDescription: ").append(toIndentedString(referencedIndicatorDescription)).append("\n");
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

