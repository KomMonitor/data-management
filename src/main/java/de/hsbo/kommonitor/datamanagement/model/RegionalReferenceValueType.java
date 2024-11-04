package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegionalReferenceValueType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class RegionalReferenceValueType implements Serializable {

  private static final long serialVersionUID = 1L;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate referenceDate;

  private JsonNullable<Float> regionalSum = JsonNullable.<Float>undefined();

  private JsonNullable<Float> regionalAverage = JsonNullable.<Float>undefined();

  private JsonNullable<Float> spatiallyUnassignable = JsonNullable.<Float>undefined();

  public RegionalReferenceValueType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegionalReferenceValueType(LocalDate referenceDate, Float regionalSum, Float regionalAverage, Float spatiallyUnassignable) {
    this.referenceDate = referenceDate;
    this.regionalSum = JsonNullable.of(regionalSum);
    this.regionalAverage = JsonNullable.of(regionalAverage);
    this.spatiallyUnassignable = JsonNullable.of(spatiallyUnassignable);
  }

  public RegionalReferenceValueType referenceDate(LocalDate referenceDate) {
    this.referenceDate = referenceDate;
    return this;
  }

  /**
   * reference date according to ISO 8601 (e.g. 2018-01-30)
   * @return referenceDate
  */
  @NotNull @Valid 
  @Schema(name = "referenceDate", description = "reference date according to ISO 8601 (e.g. 2018-01-30)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referenceDate")
  public LocalDate getReferenceDate() {
    return referenceDate;
  }

  public void setReferenceDate(LocalDate referenceDate) {
    this.referenceDate = referenceDate;
  }

  public RegionalReferenceValueType regionalSum(Float regionalSum) {
    this.regionalSum = JsonNullable.of(regionalSum);
    return this;
  }

  /**
   * regional sum value
   * @return regionalSum
  */
  @NotNull 
  @Schema(name = "regionalSum", description = "regional sum value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionalSum")
  public JsonNullable<Float> getRegionalSum() {
    return regionalSum;
  }

  public void setRegionalSum(JsonNullable<Float> regionalSum) {
    this.regionalSum = regionalSum;
  }

  public RegionalReferenceValueType regionalAverage(Float regionalAverage) {
    this.regionalAverage = JsonNullable.of(regionalAverage);
    return this;
  }

  /**
   * regional average value
   * @return regionalAverage
  */
  @NotNull 
  @Schema(name = "regionalAverage", description = "regional average value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionalAverage")
  public JsonNullable<Float> getRegionalAverage() {
    return regionalAverage;
  }

  public void setRegionalAverage(JsonNullable<Float> regionalAverage) {
    this.regionalAverage = regionalAverage;
  }

  public RegionalReferenceValueType spatiallyUnassignable(Float spatiallyUnassignable) {
    this.spatiallyUnassignable = JsonNullable.of(spatiallyUnassignable);
    return this;
  }

  /**
   * number of items that cannot be spatially assigned to any spatial unit
   * @return spatiallyUnassignable
  */
  @NotNull 
  @Schema(name = "spatiallyUnassignable", description = "number of items that cannot be spatially assigned to any spatial unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spatiallyUnassignable")
  public JsonNullable<Float> getSpatiallyUnassignable() {
    return spatiallyUnassignable;
  }

  public void setSpatiallyUnassignable(JsonNullable<Float> spatiallyUnassignable) {
    this.spatiallyUnassignable = spatiallyUnassignable;
  }

  @Override
  public boolean equals(Object o) {
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

