package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * DefaultClassificationMappingItemType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-10-16T08:10:08.922+02:00")

public class DefaultClassificationMappingItemType   {
  @JsonProperty("lowerInclusiveValue")
  private Float lowerInclusiveValue = null;

  @JsonProperty("upperExclusiveValue")
  private Float upperExclusiveValue = null;

  @JsonProperty("defaultColorAsHex")
  private String defaultColorAsHex = null;

  @JsonProperty("defaultCustomRating")
  private String defaultCustomRating = null;

  public DefaultClassificationMappingItemType lowerInclusiveValue(Float lowerInclusiveValue) {
    this.lowerInclusiveValue = lowerInclusiveValue;
    return this;
  }

   /**
   * lower value of the value interval. This value ist inclusive, hence the specified value will be part of the interval
   * @return lowerInclusiveValue
  **/
  @ApiModelProperty(required = true, value = "lower value of the value interval. This value ist inclusive, hence the specified value will be part of the interval")
  public Float getLowerInclusiveValue() {
    return lowerInclusiveValue;
  }

  public void setLowerInclusiveValue(Float lowerInclusiveValue) {
    this.lowerInclusiveValue = lowerInclusiveValue;
  }

  public DefaultClassificationMappingItemType upperExclusiveValue(Float upperExclusiveValue) {
    this.upperExclusiveValue = upperExclusiveValue;
    return this;
  }

   /**
   * upper value of the value interval. This value ist exclusive, hence the specified value will not be part of the interval
   * @return upperExclusiveValue
  **/
  @ApiModelProperty(required = true, value = "upper value of the value interval. This value ist exclusive, hence the specified value will not be part of the interval")
  public Float getUpperExclusiveValue() {
    return upperExclusiveValue;
  }

  public void setUpperExclusiveValue(Float upperExclusiveValue) {
    this.upperExclusiveValue = upperExclusiveValue;
  }

  public DefaultClassificationMappingItemType defaultColorAsHex(String defaultColorAsHex) {
    this.defaultColorAsHex = defaultColorAsHex;
    return this;
  }

   /**
   * the default color for the specified value interval as hex string inclusive leading #, i.e. '#ffffff'
   * @return defaultColorAsHex
  **/
  @ApiModelProperty(required = true, value = "the default color for the specified value interval as hex string inclusive leading #, i.e. '#ffffff'")
  public String getDefaultColorAsHex() {
    return defaultColorAsHex;
  }

  public void setDefaultColorAsHex(String defaultColorAsHex) {
    this.defaultColorAsHex = defaultColorAsHex;
  }

  public DefaultClassificationMappingItemType defaultCustomRating(String defaultCustomRating) {
    this.defaultCustomRating = defaultCustomRating;
    return this;
  }

   /**
   * the default custom rating string for the specified value interval, i.e. 'very high'/'very low' or 'good'/'bad'
   * @return defaultCustomRating
  **/
  @ApiModelProperty(required = true, value = "the default custom rating string for the specified value interval, i.e. 'very high'/'very low' or 'good'/'bad'")
  public String getDefaultCustomRating() {
    return defaultCustomRating;
  }

  public void setDefaultCustomRating(String defaultCustomRating) {
    this.defaultCustomRating = defaultCustomRating;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultClassificationMappingItemType defaultClassificationMappingItemType = (DefaultClassificationMappingItemType) o;
    return Objects.equals(this.lowerInclusiveValue, defaultClassificationMappingItemType.lowerInclusiveValue) &&
        Objects.equals(this.upperExclusiveValue, defaultClassificationMappingItemType.upperExclusiveValue) &&
        Objects.equals(this.defaultColorAsHex, defaultClassificationMappingItemType.defaultColorAsHex) &&
        Objects.equals(this.defaultCustomRating, defaultClassificationMappingItemType.defaultCustomRating);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lowerInclusiveValue, upperExclusiveValue, defaultColorAsHex, defaultCustomRating);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultClassificationMappingItemType {\n");
    
    sb.append("    lowerInclusiveValue: ").append(toIndentedString(lowerInclusiveValue)).append("\n");
    sb.append("    upperExclusiveValue: ").append(toIndentedString(upperExclusiveValue)).append("\n");
    sb.append("    defaultColorAsHex: ").append(toIndentedString(defaultColorAsHex)).append("\n");
    sb.append("    defaultCustomRating: ").append(toIndentedString(defaultCustomRating)).append("\n");
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

