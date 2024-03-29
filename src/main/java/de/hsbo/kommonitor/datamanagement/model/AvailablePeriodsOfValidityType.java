package de.hsbo.kommonitor.datamanagement.model;

import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Objects;

/**
 * array of periods of validity, each consisting of a start and end date
 */
@Schema(description = "definition of the period of validity of a certain dataset")
public class AvailablePeriodsOfValidityType extends ArrayList<PeriodOfValidityType>  {

  @Override
  public boolean equals(Object o) {
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

