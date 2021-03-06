package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPUTInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2020-01-04T23:04:55.031+01:00")

public class IndicatorPUTInputType   {
  @JsonProperty("defaultClassificationMapping")
  private DefaultClassificationMappingType defaultClassificationMapping = null;

  @JsonProperty("applicableSpatialUnit")
  private String applicableSpatialUnit = null;

  @JsonProperty("indicatorValues")
  
  private List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = new ArrayList<>();

  @JsonProperty("allowedRoles")
  private List<String> allowedRoles = new ArrayList<>();

  public IndicatorPUTInputType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
    return this;
  }

   /**
   * Get defaultClassificationMapping
   * @return defaultClassificationMapping
  **/
  @ApiModelProperty(value = "")
  public DefaultClassificationMappingType getDefaultClassificationMapping() {
    return defaultClassificationMapping;
  }

  public void setDefaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
  }

  public IndicatorPUTInputType applicableSpatialUnit(String applicableSpatialUnit) {
    this.applicableSpatialUnit = applicableSpatialUnit;
    return this;
  }

   /**
   * Get applicableSpatialUnit
   * @return applicableSpatialUnit
  **/
  @ApiModelProperty(required = true, value = "")
  public String getApplicableSpatialUnit() {
    return applicableSpatialUnit;
  }

  public void setApplicableSpatialUnit(String applicableSpatialUnit) {
    this.applicableSpatialUnit = applicableSpatialUnit;
  }

  public IndicatorPUTInputType indicatorValues(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
    this.indicatorValues = indicatorValues;
    return this;
  }

  public IndicatorPUTInputType addIndicatorValuesItem(IndicatorPOSTInputTypeIndicatorValues indicatorValuesItem) {
    this.indicatorValues.add(indicatorValuesItem);
    return this;
  }

   /**
   * an array of entries containing indicator values and mapping to spatial features via identifiers
   * @return indicatorValues
  **/
  @ApiModelProperty(required = true, value = "an array of entries containing indicator values and mapping to spatial features via identifiers")
  public List<IndicatorPOSTInputTypeIndicatorValues> getIndicatorValues() {
    return indicatorValues;
  }

  public void setIndicatorValues(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
    this.indicatorValues = indicatorValues;
  }

  public IndicatorPUTInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorPUTInputType addAllowedRolesItem(String allowedRolesItem) {
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
   **/
  @ApiModelProperty(required = true, value = "list of role identifiers that have read access rights for this dataset")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPUTInputType indicatorPUTInputType = (IndicatorPUTInputType) o;
    return Objects.equals(this.defaultClassificationMapping, indicatorPUTInputType.defaultClassificationMapping) &&
        Objects.equals(this.applicableSpatialUnit, indicatorPUTInputType.applicableSpatialUnit) &&
        Objects.equals(this.indicatorValues, indicatorPUTInputType.indicatorValues) &&
        Objects.equals(this.allowedRoles, indicatorPUTInputType.allowedRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultClassificationMapping, applicableSpatialUnit, indicatorValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPUTInputType {\n");
    
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    applicableSpatialUnit: ").append(toIndentedString(applicableSpatialUnit)).append("\n");
    sb.append("    indicatorValues: ").append(toIndentedString(indicatorValues)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
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

