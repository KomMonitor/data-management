package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModelProperty;

/**
 * definition of the period of validity of a certain dataset
 */
public class PeriodOfValidityEntity_georesources   {

	
  private LocalDate startDate = null;

  private LocalDate endDate = null;

  public PeriodOfValidityEntity_georesources() {

}

  public PeriodOfValidityEntity_georesources(PeriodOfValidityType periodType) {
	// TODO Auto-generated constructor stub
	  this.endDate = periodType.getEndDate();
	  this.startDate = periodType.getStartDate();
}

public PeriodOfValidityEntity_georesources startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * a timestamp representing the starting date according to ISO 8601 (e.g. 2018-01-30)
   * @return startDate
  **/
  @ApiModelProperty(required = true, value = "a timestamp representing the starting date according to ISO 8601 (e.g. 2018-01-30)")
  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public PeriodOfValidityEntity_georesources endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.
   * @return endDate
  **/
  @ApiModelProperty(value = "an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.")
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
    PeriodOfValidityEntity_georesources periodOfValidityType = (PeriodOfValidityEntity_georesources) o;
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

