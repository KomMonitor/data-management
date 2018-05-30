package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPOSTInputTypeRefrencesToOtherIndicators
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class IndicatorPOSTInputTypeRefrencesToOtherIndicators   {
  @JsonProperty("indicatorId")
  private String indicatorId = null;

  @JsonProperty("referenceDescription")
  private String referenceDescription = null;

  public IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

   /**
   * identifier of the referenced indicator
   * @return indicatorId
  **/
  @ApiModelProperty(value = "identifier of the referenced indicator")
  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public IndicatorPOSTInputTypeRefrencesToOtherIndicators referenceDescription(String referenceDescription) {
    this.referenceDescription = referenceDescription;
    return this;
  }

   /**
   * short description of how the indicator is referenced to the superior one
   * @return referenceDescription
  **/
  @ApiModelProperty(value = "short description of how the indicator is referenced to the superior one")
  public String getReferenceDescription() {
    return referenceDescription;
  }

  public void setReferenceDescription(String referenceDescription) {
    this.referenceDescription = referenceDescription;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorPOSTInputTypeRefrencesToOtherIndicators = (IndicatorPOSTInputTypeRefrencesToOtherIndicators) o;
    return Objects.equals(this.indicatorId, indicatorPOSTInputTypeRefrencesToOtherIndicators.indicatorId) &&
        Objects.equals(this.referenceDescription, indicatorPOSTInputTypeRefrencesToOtherIndicators.referenceDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorId, referenceDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeRefrencesToOtherIndicators {\n");
    
    sb.append("    indicatorId: ").append(toIndentedString(indicatorId)).append("\n");
    sb.append("    referenceDescription: ").append(toIndentedString(referenceDescription)).append("\n");
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

