package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorMetadataPATCHInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2020-01-04T23:51:22.008+01:00")

public class IndicatorMetadataPATCHInputType {
  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("characteristicValue")
  private String characteristicValue = null;
  
  @JsonProperty("indicatorType")
  private IndicatorTypeEnum indicatorType = null;

  @JsonProperty("creationType")
  private CreationTypeEnum creationType = null;

  @JsonProperty("topicReference")
  private String topicReference = null;

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

  @JsonProperty("abbreviation")
  private String abbreviation = null;

  @JsonProperty("interpretation")
  private String interpretation = null;

  @JsonProperty("isHeadlineIndicator")
  private Boolean isHeadlineIndicator = false;

  @JsonProperty("tags")
  
  private List<String> tags = new ArrayList<>();

  @JsonProperty("refrencesToOtherIndicators")
  
  private List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators = null;

  @JsonProperty("refrencesToGeoresources")
  
  private List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources = null;

  public IndicatorMetadataPATCHInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

   /**
   * the meaningful name of the indicator
   * @return datasetName
  **/
  @ApiModelProperty(value = "the meaningful name of the indicator")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public IndicatorMetadataPATCHInputType characteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
    return this;
  }

   /**
   * the distuingishing characteristic value of the indicator
   * @return characteristicValue
  **/
  @ApiModelProperty(value = "the distuingishing characteristic value of the indicator")
  public String getCharacteristicValue() {
    return characteristicValue;
  }

  public void setCharacteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
  }

  public IndicatorMetadataPATCHInputType indicatorType(IndicatorTypeEnum indicatorType) {
    this.indicatorType = indicatorType;
    return this;
  }

   /**
   * indicates whether the indicator is a status indicator (values represent the extent of the watched phenomenon for a certain point in time) or a dynamic indicator (values represent the change of extent of the watched phenomenon within a certain period of time)
   * @return indicatorType
  **/
  @ApiModelProperty(value = "indicates whether the indicator is a status indicator (values represent the extent of the watched phenomenon for a certain point in time) or a dynamic indicator (values represent the change of extent of the watched phenomenon within a certain period of time)")
  public IndicatorTypeEnum getIndicatorType() {
    return indicatorType;
  }

  public void setIndicatorType(IndicatorTypeEnum indicatorType) {
    this.indicatorType = indicatorType;
  }

  public IndicatorMetadataPATCHInputType creationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
    return this;
  }

   /**
   * indicates if the data is simply inserted (INSERTION), computed by an automated script (COMPUTATION) or automatically aggregated by a script (AGGREGATION)
   * @return creationType
  **/
  @ApiModelProperty(value = "indicates if the data is simply inserted (INSERTION), computed by an automated script (COMPUTATION) or automatically aggregated by a script (AGGREGATION)")
  public CreationTypeEnum getCreationType() {
    return creationType;
  }

  public void setCreationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
  }

  public IndicatorMetadataPATCHInputType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

   /**
   * id of the last topic hierarchy entity 
   * @return topicReference
  **/
  @ApiModelProperty(required = true, value = "id of the last topic hierarchy entity ")
  public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  public IndicatorMetadataPATCHInputType metadata(CommonMetadataType metadata) {
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

  public IndicatorMetadataPATCHInputType processDescription(String processDescription) {
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

  public IndicatorMetadataPATCHInputType unit(String unit) {
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

  public IndicatorMetadataPATCHInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorMetadataPATCHInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public IndicatorMetadataPATCHInputType lowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
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

  public IndicatorMetadataPATCHInputType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
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

  public IndicatorMetadataPATCHInputType abbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
    return this;
  }

   /**
   * abbreviated mark of the indicator
   * @return abbreviation
  **/
  @ApiModelProperty(required = true, value = "abbreviated mark of the indicator")
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public IndicatorMetadataPATCHInputType interpretation(String interpretation) {
    this.interpretation = interpretation;
    return this;
  }

   /**
   * interpretation of the indicator values
   * @return interpretation
  **/
  @ApiModelProperty(required = true, value = "interpretation of the indicator values")
  public String getInterpretation() {
    return interpretation;
  }

  public void setInterpretation(String interpretation) {
    this.interpretation = interpretation;
  }

  public IndicatorMetadataPATCHInputType isHeadlineIndicator(Boolean isHeadlineIndicator) {
    this.isHeadlineIndicator = isHeadlineIndicator;
    return this;
  }

   /**
   * boolean value indicating if the indicator is a headline indicator
   * @return isHeadlineIndicator
  **/
  @ApiModelProperty(required = true, value = "boolean value indicating if the indicator is a headline indicator")
  public Boolean isIsHeadlineIndicator() {
    return isHeadlineIndicator;
  }

  public void setIsHeadlineIndicator(Boolean isHeadlineIndicator) {
    this.isHeadlineIndicator = isHeadlineIndicator;
  }

  public IndicatorMetadataPATCHInputType tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public IndicatorMetadataPATCHInputType addTagsItem(String tagsItem) {
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * list of tag labels for the indicator
   * @return tags
  **/
  @ApiModelProperty(required = true, value = "list of tag labels for the indicator")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public IndicatorMetadataPATCHInputType refrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
    return this;
  }

  public IndicatorMetadataPATCHInputType addRefrencesToOtherIndicatorsItem(IndicatorPOSTInputTypeRefrencesToOtherIndicators refrencesToOtherIndicatorsItem) {
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

  public IndicatorMetadataPATCHInputType refrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
    return this;
  }

  public IndicatorMetadataPATCHInputType addRefrencesToGeoresourcesItem(IndicatorPOSTInputTypeRefrencesToGeoresources refrencesToGeoresourcesItem) {
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
    IndicatorMetadataPATCHInputType indicatorPATCHInputType = (IndicatorMetadataPATCHInputType) o;
    return Objects.equals(this.datasetName, indicatorPATCHInputType.datasetName) &&
        Objects.equals(this.characteristicValue, indicatorPATCHInputType.characteristicValue) &&
        Objects.equals(this.indicatorType, indicatorPATCHInputType.indicatorType) &&
        Objects.equals(this.creationType, indicatorPATCHInputType.creationType) &&
        Objects.equals(this.topicReference, indicatorPATCHInputType.topicReference) &&
        Objects.equals(this.metadata, indicatorPATCHInputType.metadata) &&
        Objects.equals(this.processDescription, indicatorPATCHInputType.processDescription) &&
        Objects.equals(this.unit, indicatorPATCHInputType.unit) &&
        Objects.equals(this.allowedRoles, indicatorPATCHInputType.allowedRoles) &&
        Objects.equals(this.lowestSpatialUnitForComputation, indicatorPATCHInputType.lowestSpatialUnitForComputation) &&
        Objects.equals(this.defaultClassificationMapping, indicatorPATCHInputType.defaultClassificationMapping) &&
        Objects.equals(this.abbreviation, indicatorPATCHInputType.abbreviation) &&
        Objects.equals(this.interpretation, indicatorPATCHInputType.interpretation) &&
        Objects.equals(this.isHeadlineIndicator, indicatorPATCHInputType.isHeadlineIndicator) &&
        Objects.equals(this.tags, indicatorPATCHInputType.tags) &&
        Objects.equals(this.refrencesToOtherIndicators, indicatorPATCHInputType.refrencesToOtherIndicators) &&
        Objects.equals(this.refrencesToGeoresources, indicatorPATCHInputType.refrencesToGeoresources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasetName, characteristicValue, indicatorType, creationType, topicReference, metadata, processDescription, unit, allowedRoles, lowestSpatialUnitForComputation, defaultClassificationMapping, abbreviation, interpretation, isHeadlineIndicator, tags, refrencesToOtherIndicators, refrencesToGeoresources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPATCHInputType {\n");
    
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    characteristicValue: ").append(toIndentedString(characteristicValue)).append("\n");
    sb.append("    indicatorType: ").append(toIndentedString(indicatorType)).append("\n");
    sb.append("    creationType: ").append(toIndentedString(creationType)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    lowestSpatialUnitForComputation: ").append(toIndentedString(lowestSpatialUnitForComputation)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    abbreviation: ").append(toIndentedString(abbreviation)).append("\n");
    sb.append("    interpretation: ").append(toIndentedString(interpretation)).append("\n");
    sb.append("    isHeadlineIndicator: ").append(toIndentedString(isHeadlineIndicator)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

