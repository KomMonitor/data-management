package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPATCHDisplayOrderInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-03-14T22:45:56.158Z")


public class IndicatorPATCHDisplayOrderInputType   {
  @JsonProperty("indicatorId")
  private String indicatorId = null;

  @JsonProperty("displayOrder")
  private BigDecimal displayOrder = null;

  public IndicatorPATCHDisplayOrderInputType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

  /**
   * unique ID of the associated indicator
   * @return indicatorId
  **/
  @ApiModelProperty(required = true, value = "unique ID of the associated indicator")
  @NotNull


  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public IndicatorPATCHDisplayOrderInputType displayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * the new displayOrder value
   * @return displayOrder
  **/
  @ApiModelProperty(required = true, value = "the new displayOrder value", example = "0")
  @NotNull

  @Valid

  public BigDecimal getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPATCHDisplayOrderInputType indicatorPATCHDisplayOrderInputType = (IndicatorPATCHDisplayOrderInputType) o;
    return Objects.equals(this.indicatorId, indicatorPATCHDisplayOrderInputType.indicatorId) &&
        Objects.equals(this.displayOrder, indicatorPATCHDisplayOrderInputType.displayOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorId, displayOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPATCHDisplayOrderInputType {\n");
    
    sb.append("    indicatorId: ").append(toIndentedString(indicatorId)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
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

