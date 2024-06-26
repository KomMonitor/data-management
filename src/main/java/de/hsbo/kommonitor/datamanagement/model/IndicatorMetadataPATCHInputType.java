package de.hsbo.kommonitor.datamanagement.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * IndicatorMetadataPATCHInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-23T11:43:09.197508532Z[GMT]")


public class IndicatorMetadataPATCHInputType   {
  @JsonProperty("abbreviation")
  private String abbreviation = null;

  @JsonProperty("allowedRoles")
  @Valid
  private List<String> allowedRoles = new ArrayList<String>();

  @JsonProperty("characteristicValue")
  private String characteristicValue = null;

  @JsonProperty("creationType")
  private CreationTypeEnum creationType = null;

  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("defaultClassificationMapping")
  private DefaultClassificationMappingType defaultClassificationMapping = null;

  @JsonProperty("regionalReferenceValues")
  @Valid
  private List<RegionalReferenceValueType> regionalReferenceValues = null;

  @JsonProperty("displayOrder")
  private BigDecimal displayOrder = null;

  @JsonProperty("indicatorType")
  private IndicatorTypeEnum indicatorType = null;

  @JsonProperty("interpretation")
  private String interpretation = null;

  @JsonProperty("isHeadlineIndicator")
  private Boolean isHeadlineIndicator = null;

  @JsonProperty("lowestSpatialUnitForComputation")
  private String lowestSpatialUnitForComputation = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("processDescription")
  private String processDescription = null;

  @JsonProperty("referenceDateNote")
  private String referenceDateNote = null;

  @JsonProperty("refrencesToGeoresources")
  @Valid
  private List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources = null;

  @JsonProperty("refrencesToOtherIndicators")
  @Valid
  private List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators = null;

  @JsonProperty("tags")
  @Valid
  private List<String> tags = new ArrayList<String>();

  @JsonProperty("topicReference")
  private String topicReference = null;

  @JsonProperty("unit")
  private String unit = null;

  public IndicatorMetadataPATCHInputType abbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
    return this;
  }

  /**
   * abbreviated mark of the indicator
   * @return abbreviation
   **/
  @Schema(required = true, description = "abbreviated mark of the indicator")
      @NotNull

    public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
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
  @Schema(required = true, description = "list of role identifiers that have read access rights for this dataset")
      @NotNull

    public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public IndicatorMetadataPATCHInputType characteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
    return this;
  }

  /**
   * the distuingishing characteristic value of the indicator
   * @return characteristicValue
   **/
  @Schema(description = "the distuingishing characteristic value of the indicator")
  
    public String getCharacteristicValue() {
    return characteristicValue;
  }

  public void setCharacteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
  }

  public IndicatorMetadataPATCHInputType creationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
    return this;
  }

  /**
   * Get creationType
   * @return creationType
   **/
  @Schema(description = "")
  
    @Valid
    public CreationTypeEnum getCreationType() {
    return creationType;
  }

  public void setCreationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
  }

  public IndicatorMetadataPATCHInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the indicator
   * @return datasetName
   **/
  @Schema(description = "the meaningful name of the indicator")
  
    public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public IndicatorMetadataPATCHInputType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
    return this;
  }

  /**
   * Get defaultClassificationMapping
   * @return defaultClassificationMapping
   **/
  @Schema(description = "")
  
    @Valid
    public DefaultClassificationMappingType getDefaultClassificationMapping() {
    return defaultClassificationMapping;
  }

  public void setDefaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
  }

  public IndicatorMetadataPATCHInputType regionalReferenceValues(List<RegionalReferenceValueType> regionalReferenceValues) {
    this.regionalReferenceValues = regionalReferenceValues;
    return this;
  }

  public IndicatorMetadataPATCHInputType addRegionalReferenceValuesItem(RegionalReferenceValueType regionalReferenceValuesItem) {
    if (this.regionalReferenceValues == null) {
      this.regionalReferenceValues = new ArrayList<RegionalReferenceValueType>();
    }
    this.regionalReferenceValues.add(regionalReferenceValuesItem);
    return this;
  }

  /**
   * list of optional regional reference values (i.e. regional sum, average, spatiallyUnassignable)
   * @return regionalReferenceValues
   **/
  @Schema(description = "list of optional regional reference values (i.e. regional sum, average, spatiallyUnassignable)")
      @Valid
    public List<RegionalReferenceValueType> getRegionalReferenceValues() {
    return regionalReferenceValues;
  }

  public void setRegionalReferenceValues(List<RegionalReferenceValueType> regionalReferenceValues) {
    this.regionalReferenceValues = regionalReferenceValues;
  }

  public IndicatorMetadataPATCHInputType displayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * an order number to control display order in clients
   * @return displayOrder
   **/
  @Schema(example = "0", description = "an order number to control display order in clients")
  
    @Valid
    public BigDecimal getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
  }

  public IndicatorMetadataPATCHInputType indicatorType(IndicatorTypeEnum indicatorType) {
    this.indicatorType = indicatorType;
    return this;
  }

  /**
   * Get indicatorType
   * @return indicatorType
   **/
  @Schema(description = "")
  
    @Valid
    public IndicatorTypeEnum getIndicatorType() {
    return indicatorType;
  }

  public void setIndicatorType(IndicatorTypeEnum indicatorType) {
    this.indicatorType = indicatorType;
  }

  public IndicatorMetadataPATCHInputType interpretation(String interpretation) {
    this.interpretation = interpretation;
    return this;
  }

  /**
   * interpretation of the indicator values
   * @return interpretation
   **/
  @Schema(required = true, description = "interpretation of the indicator values")
      @NotNull

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
  @Schema(required = true, description = "boolean value indicating if the indicator is a headline indicator")
      @NotNull

    public Boolean isIsHeadlineIndicator() {
    return isHeadlineIndicator;
  }

  public void setIsHeadlineIndicator(Boolean isHeadlineIndicator) {
    this.isHeadlineIndicator = isHeadlineIndicator;
  }

  public IndicatorMetadataPATCHInputType lowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
    this.lowestSpatialUnitForComputation = lowestSpatialUnitForComputation;
    return this;
  }

  /**
   * identifier/name of the lowest spatial unit for which the indicator can be computed and thus is available (only necessary for computable indicators)
   * @return lowestSpatialUnitForComputation
   **/
  @Schema(description = "identifier/name of the lowest spatial unit for which the indicator can be computed and thus is available (only necessary for computable indicators)")
  
    public String getLowestSpatialUnitForComputation() {
    return lowestSpatialUnitForComputation;
  }

  public void setLowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
    this.lowestSpatialUnitForComputation = lowestSpatialUnitForComputation;
  }

  public IndicatorMetadataPATCHInputType metadata(CommonMetadataType metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
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
  @Schema(required = true, description = "description about how the indicator was computed")
      @NotNull

    public String getProcessDescription() {
    return processDescription;
  }

  public void setProcessDescription(String processDescription) {
    this.processDescription = processDescription;
  }

  public IndicatorMetadataPATCHInputType referenceDateNote(String referenceDateNote) {
    this.referenceDateNote = referenceDateNote;
    return this;
  }

  /**
   * an optional note on the reference date of the indicator
   * @return referenceDateNote
   **/
  @Schema(description = "an optional note on the reference date of the indicator")
  
    public String getReferenceDateNote() {
    return referenceDateNote;
  }

  public void setReferenceDateNote(String referenceDateNote) {
    this.referenceDateNote = referenceDateNote;
  }

  public IndicatorMetadataPATCHInputType refrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
    return this;
  }

  public IndicatorMetadataPATCHInputType addRefrencesToGeoresourcesItem(IndicatorPOSTInputTypeRefrencesToGeoresources refrencesToGeoresourcesItem) {
    if (this.refrencesToGeoresources == null) {
      this.refrencesToGeoresources = new ArrayList<IndicatorPOSTInputTypeRefrencesToGeoresources>();
    }
    this.refrencesToGeoresources.add(refrencesToGeoresourcesItem);
    return this;
  }

  /**
   * array of references to other georesource datasets. E.g., if an indicator is defined by performing geometric-topological operations, then the identifiers of those required georesources can be referenced here
   * @return refrencesToGeoresources
   **/
  @Schema(description = "array of references to other georesource datasets. E.g., if an indicator is defined by performing geometric-topological operations, then the identifiers of those required georesources can be referenced here")
      @Valid
    public List<IndicatorPOSTInputTypeRefrencesToGeoresources> getRefrencesToGeoresources() {
    return refrencesToGeoresources;
  }

  public void setRefrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
  }

  public IndicatorMetadataPATCHInputType refrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
    return this;
  }

  public IndicatorMetadataPATCHInputType addRefrencesToOtherIndicatorsItem(IndicatorPOSTInputTypeRefrencesToOtherIndicators refrencesToOtherIndicatorsItem) {
    if (this.refrencesToOtherIndicators == null) {
      this.refrencesToOtherIndicators = new ArrayList<IndicatorPOSTInputTypeRefrencesToOtherIndicators>();
    }
    this.refrencesToOtherIndicators.add(refrencesToOtherIndicatorsItem);
    return this;
  }

  /**
   * array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those four indicators can be referenced here
   * @return refrencesToOtherIndicators
   **/
  @Schema(description = "array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those four indicators can be referenced here")
      @Valid
    public List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> getRefrencesToOtherIndicators() {
    return refrencesToOtherIndicators;
  }

  public void setRefrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
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
  @Schema(required = true, description = "list of tag labels for the indicator")
      @NotNull

    public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public IndicatorMetadataPATCHInputType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity 
   * @return topicReference
   **/
  @Schema(required = true, description = "id of the last topic hierarchy entity ")
      @NotNull

    public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  public IndicatorMetadataPATCHInputType unit(String unit) {
    this.unit = unit;
    return this;
  }

  /**
   * unit of the indicator values
   * @return unit
   **/
  @Schema(required = true, description = "unit of the indicator values")
      @NotNull

    public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorMetadataPATCHInputType indicatorMetadataPATCHInputType = (IndicatorMetadataPATCHInputType) o;
    return Objects.equals(this.abbreviation, indicatorMetadataPATCHInputType.abbreviation) &&
        Objects.equals(this.allowedRoles, indicatorMetadataPATCHInputType.allowedRoles) &&
        Objects.equals(this.characteristicValue, indicatorMetadataPATCHInputType.characteristicValue) &&
        Objects.equals(this.creationType, indicatorMetadataPATCHInputType.creationType) &&
        Objects.equals(this.datasetName, indicatorMetadataPATCHInputType.datasetName) &&
        Objects.equals(this.defaultClassificationMapping, indicatorMetadataPATCHInputType.defaultClassificationMapping) &&
        Objects.equals(this.regionalReferenceValues, indicatorMetadataPATCHInputType.regionalReferenceValues) &&
        Objects.equals(this.displayOrder, indicatorMetadataPATCHInputType.displayOrder) &&
        Objects.equals(this.indicatorType, indicatorMetadataPATCHInputType.indicatorType) &&
        Objects.equals(this.interpretation, indicatorMetadataPATCHInputType.interpretation) &&
        Objects.equals(this.isHeadlineIndicator, indicatorMetadataPATCHInputType.isHeadlineIndicator) &&
        Objects.equals(this.lowestSpatialUnitForComputation, indicatorMetadataPATCHInputType.lowestSpatialUnitForComputation) &&
        Objects.equals(this.metadata, indicatorMetadataPATCHInputType.metadata) &&
        Objects.equals(this.processDescription, indicatorMetadataPATCHInputType.processDescription) &&
        Objects.equals(this.referenceDateNote, indicatorMetadataPATCHInputType.referenceDateNote) &&
        Objects.equals(this.refrencesToGeoresources, indicatorMetadataPATCHInputType.refrencesToGeoresources) &&
        Objects.equals(this.refrencesToOtherIndicators, indicatorMetadataPATCHInputType.refrencesToOtherIndicators) &&
        Objects.equals(this.tags, indicatorMetadataPATCHInputType.tags) &&
        Objects.equals(this.topicReference, indicatorMetadataPATCHInputType.topicReference) &&
        Objects.equals(this.unit, indicatorMetadataPATCHInputType.unit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abbreviation, allowedRoles, characteristicValue, creationType, datasetName, defaultClassificationMapping, regionalReferenceValues, displayOrder, indicatorType, interpretation, isHeadlineIndicator, lowestSpatialUnitForComputation, metadata, processDescription, referenceDateNote, refrencesToGeoresources, refrencesToOtherIndicators, tags, topicReference, unit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorMetadataPATCHInputType {\n");
    
    sb.append("    abbreviation: ").append(toIndentedString(abbreviation)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    characteristicValue: ").append(toIndentedString(characteristicValue)).append("\n");
    sb.append("    creationType: ").append(toIndentedString(creationType)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    regionalReferenceValues: ").append(toIndentedString(regionalReferenceValues)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    indicatorType: ").append(toIndentedString(indicatorType)).append("\n");
    sb.append("    interpretation: ").append(toIndentedString(interpretation)).append("\n");
    sb.append("    isHeadlineIndicator: ").append(toIndentedString(isHeadlineIndicator)).append("\n");
    sb.append("    lowestSpatialUnitForComputation: ").append(toIndentedString(lowestSpatialUnitForComputation)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    referenceDateNote: ").append(toIndentedString(referenceDateNote)).append("\n");
    sb.append("    refrencesToGeoresources: ").append(toIndentedString(refrencesToGeoresources)).append("\n");
    sb.append("    refrencesToOtherIndicators: ").append(toIndentedString(refrencesToOtherIndicators)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
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
