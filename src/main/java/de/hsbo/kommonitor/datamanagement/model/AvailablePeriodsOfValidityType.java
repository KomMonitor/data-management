package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;

/**
 * array of periods of validity, each consisting of a start and end date
 */
@ApiModel(description = "array of periods of validity, each consisting of a start and end date")

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-09-17T12:19:42.197+02:00")

public class AvailablePeriodsOfValidityType extends ArrayList<PeriodOfValidityType>  {

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailablePeriodsOfValidityType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

