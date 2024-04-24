package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;
import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * RegionalReferenceValueType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-23T11:43:09.197508532Z[GMT]")

@Entity(name = "RegionalReferenceValueType")
public class RegionalReferenceValueType   {
	
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String mappingId; 
	
  @JsonProperty("referenceDate")
  private String referenceDate = null;

  @JsonProperty("regionalSum")
  private Float regionalSum = null;

  @JsonProperty("regionalAverage")
  private Float regionalAverage = null;

  @JsonProperty("spatiallyUnassignable")
  private Float spatiallyUnassignable = null;

  public RegionalReferenceValueType referenceDate(String referenceDate) {
    this.referenceDate = referenceDate;
    return this;
  }

  /**
   * reference date according to ISO 8601 (e.g. 2018-01-30)
   * @return referenceDate
   **/
  @Schema(required = true, description = "reference date according to ISO 8601 (e.g. 2018-01-30)")
      @NotNull

    @Valid
    public String getReferenceDate() {
    return referenceDate;
  }

  public void setReferenceDate(String referenceDate) {
    this.referenceDate = referenceDate;
  }

  public RegionalReferenceValueType regionalSum(Float regionalSum) {
    this.regionalSum = regionalSum;
    return this;
  }

  /**
   * regional sum value
   * @return regionalSum
   **/
  @Schema(required = true, description = "regional sum value")
      @NotNull

    public Float getRegionalSum() {
    return regionalSum;
  }

  public void setRegionalSum(Float regionalSum) {
    this.regionalSum = regionalSum;
  }

  public RegionalReferenceValueType regionalAverage(Float regionalAverage) {
    this.regionalAverage = regionalAverage;
    return this;
  }

  /**
   * regional average value
   * @return regionalAverage
   **/
  @Schema(required = true, description = "regional average value")
      @NotNull

    public Float getRegionalAverage() {
    return regionalAverage;
  }

  public void setRegionalAverage(Float regionalAverage) {
    this.regionalAverage = regionalAverage;
  }

  public RegionalReferenceValueType spatiallyUnassignable(Float spatiallyUnassignable) {
    this.spatiallyUnassignable = spatiallyUnassignable;
    return this;
  }

  /**
   * number of items that cannot be spatially assigned to any spatial unit
   * @return spatiallyUnassignable
   **/
  @Schema(required = true, description = "number of items that cannot be spatially assigned to any spatial unit")
      @NotNull

    public Float getSpatiallyUnassignable() {
    return spatiallyUnassignable;
  }

  public void setSpatiallyUnassignable(Float spatiallyUnassignable) {
    this.spatiallyUnassignable = spatiallyUnassignable;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionalReferenceValueType regionalReferenceValueType = (RegionalReferenceValueType) o;
    return Objects.equals(this.referenceDate, regionalReferenceValueType.referenceDate) &&
        Objects.equals(this.regionalSum, regionalReferenceValueType.regionalSum) &&
        Objects.equals(this.regionalAverage, regionalReferenceValueType.regionalAverage) &&
        Objects.equals(this.spatiallyUnassignable, regionalReferenceValueType.spatiallyUnassignable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referenceDate, regionalSum, regionalAverage, spatiallyUnassignable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionalReferenceValueType {\n");
    
    sb.append("    referenceDate: ").append(toIndentedString(referenceDate)).append("\n");
    sb.append("    regionalSum: ").append(toIndentedString(regionalSum)).append("\n");
    sb.append("    regionalAverage: ").append(toIndentedString(regionalAverage)).append("\n");
    sb.append("    spatiallyUnassignable: ").append(toIndentedString(spatiallyUnassignable)).append("\n");
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
