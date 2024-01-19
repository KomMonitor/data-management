package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * definition of the period of validity of a certain dataset
 */
public class PeriodOfValidityEntity_spatialUnits   {
	
  private LocalDate startDate = null;

  private LocalDate endDate = null; 

  public PeriodOfValidityEntity_spatialUnits() {

}

  public PeriodOfValidityEntity_spatialUnits(PeriodOfValidityType periodType) {
	// TODO Auto-generated constructor stub
	  this.endDate = periodType.getEndDate();
	  this.startDate = periodType.getStartDate();
}

public PeriodOfValidityEntity_spatialUnits startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * a timestamp representing the starting date according to ISO 8601 (e.g. 2018-01-30)
   * @return startDate
  **/
   @NotNull
   @Schema(name = "startDate", description = "a timestamp representing the starting date according to ISO 8601 (e.g. 2018-01-30)", requiredMode = Schema.RequiredMode.REQUIRED)
   @JsonProperty("startDate")
   public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public PeriodOfValidityEntity_spatialUnits endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.
   * @return endDate
  **/
  /**
   * an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.
   * @return endDate
   **/
  @Schema(name = "endDate", description = "an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDate")
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
    PeriodOfValidityEntity_spatialUnits periodOfValidityType = (PeriodOfValidityEntity_spatialUnits) o;
    return Objects.equals(this.startDate, periodOfValidityType.startDate) &&
        Objects.equals(this.endDate, periodOfValidityType.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PeriodOfValidityType {\n");
    
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
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

