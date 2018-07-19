package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;

/**
 * definition of earliest and latest date of validity. May be used to understand, which timestamps can be used to query the data
 */
@ApiModel(description = "definition of earliest and latest date of validity. May be used to understand, which timestamps can be used to query the data")

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-07-19T11:48:18.228+02:00")

public class AvailablePeriodOfValidityType   {
  @JsonProperty("earliestStartDate")
  private LocalDate earliestStartDate = null;

  @JsonProperty("endDate")
  private LocalDate endDate = null;

  public AvailablePeriodOfValidityType earliestStartDate(LocalDate earliestStartDate) {
    this.earliestStartDate = earliestStartDate;
    return this;
  }

   /**
   * a timestamp representing the earliest starting date according to ISO 8601 (e.g. 2018-01-30), for which the dataset can be queried
   * @return earliestStartDate
  **/
  @ApiModelProperty(required = true, value = "a timestamp representing the earliest starting date according to ISO 8601 (e.g. 2018-01-30), for which the dataset can be queried")
  public LocalDate getEarliestStartDate() {
    return earliestStartDate;
  }

  public void setEarliestStartDate(LocalDate earliestStartDate) {
    this.earliestStartDate = earliestStartDate;
  }

  public AvailablePeriodOfValidityType endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * an optional timestamp representing the latest ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.
   * @return endDate
  **/
  @ApiModelProperty(value = "an optional timestamp representing the latest ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.")
  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvailablePeriodOfValidityType availablePeriodOfValidityType = (AvailablePeriodOfValidityType) o;
    return Objects.equals(this.earliestStartDate, availablePeriodOfValidityType.earliestStartDate) &&
        Objects.equals(this.endDate, availablePeriodOfValidityType.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(earliestStartDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailablePeriodOfValidityType {\n");
    
    sb.append("    earliestStartDate: ").append(toIndentedString(earliestStartDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

