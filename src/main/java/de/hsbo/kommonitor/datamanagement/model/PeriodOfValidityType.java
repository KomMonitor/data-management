package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * definition of the period of validity of a certain dataset
 */

@Schema(name = "PeriodOfValidityType", description = "definition of the period of validity of a certain dataset")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class PeriodOfValidityType implements Serializable {

  private static final long serialVersionUID = 1L;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  public PeriodOfValidityType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PeriodOfValidityType(LocalDate startDate) {
    this.startDate = startDate;
  }

  public PeriodOfValidityType endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.
   * @return endDate
  */
  @Valid 
  @Schema(name = "endDate", description = "an optional timestamp representing the ending date according to ISO 8601 (e.g. 2018-01-30). The parameter can be omitted, if the end date is unknown.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDate")
  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public PeriodOfValidityType startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * a timestamp representing the starting date according to ISO 8601 (e.g. 2018-01-30)
   * @return startDate
  */
  @NotNull @Valid 
  @Schema(name = "startDate", description = "a timestamp representing the starting date according to ISO 8601 (e.g. 2018-01-30)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startDate")
  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PeriodOfValidityType periodOfValidityType = (PeriodOfValidityType) o;
    return Objects.equals(this.endDate, periodOfValidityType.endDate) &&
        Objects.equals(this.startDate, periodOfValidityType.startDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endDate, startDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PeriodOfValidityType {\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
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

