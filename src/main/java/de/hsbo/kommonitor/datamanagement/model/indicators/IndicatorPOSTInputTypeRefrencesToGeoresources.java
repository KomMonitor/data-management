package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPOSTInputTypeRefrencesToGeoresources
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-18T20:11:44.438+02:00")

public class IndicatorPOSTInputTypeRefrencesToGeoresources   {
  @JsonProperty("indicatorId")
  private String indicatorId = null;

  @JsonProperty("referenceDescription")
  private String referenceDescription = null;

  public IndicatorPOSTInputTypeRefrencesToGeoresources indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

   /**
   * identifier of the referenced georesource
   * @return indicatorId
  **/
  @ApiModelProperty(value = "identifier of the referenced georesource")
  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public IndicatorPOSTInputTypeRefrencesToGeoresources referenceDescription(String referenceDescription) {
    this.referenceDescription = referenceDescription;
    return this;
  }

   /**
   * short description of how the georesource is referenced to the indicator
   * @return referenceDescription
  **/
  @ApiModelProperty(value = "short description of how the georesource is referenced to the indicator")
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
    IndicatorPOSTInputTypeRefrencesToGeoresources indicatorPOSTInputTypeRefrencesToGeoresources = (IndicatorPOSTInputTypeRefrencesToGeoresources) o;
    return Objects.equals(this.indicatorId, indicatorPOSTInputTypeRefrencesToGeoresources.indicatorId) &&
        Objects.equals(this.referenceDescription, indicatorPOSTInputTypeRefrencesToGeoresources.referenceDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorId, referenceDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeRefrencesToGeoresources {\n");
    
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

