package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPATCHInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-01-17T10:12:10.704+01:00")

public class IndicatorPATCHInputType   {
  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("processDescription")
  private String processDescription = null;

  @JsonProperty("unit")
  private String unit = null;

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = new ArrayList<>();

  @JsonProperty("lowestSpatialUnitForComputation")
  private String lowestSpatialUnitForComputation = null;

  @JsonProperty("defaultClassificationMapping")
  private DefaultClassificationMappingType defaultClassificationMapping = null;

  @JsonProperty("refrencesToOtherIndicators")
  
  private List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators = null;

  @JsonProperty("refrencesToGeoresources")
  
  private List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources = null;

  public IndicatorPATCHInputType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public IndicatorPATCHInputType addApplicableTopicsItem(String applicableTopicsItem) {
    this.applicableTopics.add(applicableTopicsItem);
    return this;
  }

   /**
   * array of thematic categories for which the dataset is applicable. Note that the used topicName has to be defined under /topics
   * @return applicableTopics
  **/
  @ApiModelProperty(required = true, value = "array of thematic categories for which the dataset is applicable. Note that the used topicName has to be defined under /topics")
  public List<String> getApplicableTopics() {
    return applicableTopics;
  }

  public void setApplicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
  }

  public IndicatorPATCHInputType metadata(CommonMetadataType metadata) {
    this.metadata = metadata;
    return this;
  }

   /**
   * Get metadata
   * @return metadata
  **/
  @ApiModelProperty(required = true, value = "")
  public CommonMetadataType getMetadata() {
    return metadata;
  }

  public void setMetadata(CommonMetadataType metadata) {
    this.metadata = metadata;
  }

  public IndicatorPATCHInputType processDescription(String processDescription) {
    this.processDescription = processDescription;
    return this;
  }

   /**
   * description about how the indicator was computed
   * @return processDescription
  **/
  @ApiModelProperty(required = true, value = "description about how the indicator was computed")
  public String getProcessDescription() {
    return processDescription;
  }

  public void setProcessDescription(String processDescription) {
    this.processDescription = processDescription;
  }

  public IndicatorPATCHInputType unit(String unit) {
    this.unit = unit;
    return this;
  }

   /**
   * unit of the indicator values
   * @return unit
  **/
  @ApiModelProperty(required = true, value = "unit of the indicator values")
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public IndicatorPATCHInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorPATCHInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public IndicatorPATCHInputType lowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
    this.lowestSpatialUnitForComputation = lowestSpatialUnitForComputation;
    return this;
  }

   /**
   * identifier/name of the lowest spatial unit for which the indicator can be computed and thus is available (only necessary for computable indicators)
   * @return lowestSpatialUnitForComputation
  **/
  @ApiModelProperty(value = "identifier/name of the lowest spatial unit for which the indicator can be computed and thus is available (only necessary for computable indicators)")
  public String getLowestSpatialUnitForComputation() {
    return lowestSpatialUnitForComputation;
  }

  public void setLowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
    this.lowestSpatialUnitForComputation = lowestSpatialUnitForComputation;
  }

  public IndicatorPATCHInputType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
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

  public IndicatorPATCHInputType refrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
    return this;
  }

  public IndicatorPATCHInputType addRefrencesToOtherIndicatorsItem(IndicatorPOSTInputTypeRefrencesToOtherIndicators refrencesToOtherIndicatorsItem) {
    if (this.refrencesToOtherIndicators == null) {
      this.refrencesToOtherIndicators = new ArrayList<>();
    }
    this.refrencesToOtherIndicators.add(refrencesToOtherIndicatorsItem);
    return this;
  }

   /**
   * array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those four indicators can be referenced here
   * @return refrencesToOtherIndicators
  **/
  @ApiModelProperty(value = "array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those four indicators can be referenced here")
  public List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> getRefrencesToOtherIndicators() {
    return refrencesToOtherIndicators;
  }

  public void setRefrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
  }

  public IndicatorPATCHInputType refrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
    return this;
  }

  public IndicatorPATCHInputType addRefrencesToGeoresourcesItem(IndicatorPOSTInputTypeRefrencesToGeoresources refrencesToGeoresourcesItem) {
    if (this.refrencesToGeoresources == null) {
      this.refrencesToGeoresources = new ArrayList<>();
    }
    this.refrencesToGeoresources.add(refrencesToGeoresourcesItem);
    return this;
  }

   /**
   * array of references to other georesource datasets. E.g., if an indicator is defined by performing geometric-topological operations, then the identifiers of those required georesources can be referenced here
   * @return refrencesToGeoresources
  **/
  @ApiModelProperty(value = "array of references to other georesource datasets. E.g., if an indicator is defined by performing geometric-topological operations, then the identifiers of those required georesources can be referenced here")
  public List<IndicatorPOSTInputTypeRefrencesToGeoresources> getRefrencesToGeoresources() {
    return refrencesToGeoresources;
  }

  public void setRefrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPATCHInputType indicatorPATCHInputType = (IndicatorPATCHInputType) o;
    return Objects.equals(this.applicableTopics, indicatorPATCHInputType.applicableTopics) &&
        Objects.equals(this.metadata, indicatorPATCHInputType.metadata) &&
        Objects.equals(this.processDescription, indicatorPATCHInputType.processDescription) &&
        Objects.equals(this.unit, indicatorPATCHInputType.unit) &&
        Objects.equals(this.allowedRoles, indicatorPATCHInputType.allowedRoles) &&
        Objects.equals(this.lowestSpatialUnitForComputation, indicatorPATCHInputType.lowestSpatialUnitForComputation) &&
        Objects.equals(this.defaultClassificationMapping, indicatorPATCHInputType.defaultClassificationMapping) &&
        Objects.equals(this.refrencesToOtherIndicators, indicatorPATCHInputType.refrencesToOtherIndicators) &&
        Objects.equals(this.refrencesToGeoresources, indicatorPATCHInputType.refrencesToGeoresources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicableTopics, metadata, processDescription, unit, allowedRoles, lowestSpatialUnitForComputation, defaultClassificationMapping, refrencesToOtherIndicators, refrencesToGeoresources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPATCHInputType {\n");
    
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    lowestSpatialUnitForComputation: ").append(toIndentedString(lowestSpatialUnitForComputation)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    refrencesToOtherIndicators: ").append(toIndentedString(refrencesToOtherIndicators)).append("\n");
    sb.append("    refrencesToGeoresources: ").append(toIndentedString(refrencesToGeoresources)).append("\n");
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

