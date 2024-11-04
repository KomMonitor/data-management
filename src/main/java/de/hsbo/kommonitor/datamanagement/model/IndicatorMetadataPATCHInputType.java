package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IndicatorMetadataPATCHInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class IndicatorMetadataPATCHInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String abbreviation;

  private String characteristicValue;

  private CreationTypeEnum creationType;

  private String datasetName;

  private DefaultClassificationMappingType defaultClassificationMapping;

  @Valid
  private List<@Valid RegionalReferenceValueType> regionalReferenceValues;

  private BigDecimal displayOrder;

  private IndicatorTypeEnum indicatorType;

  private String interpretation;

  private Boolean isHeadlineIndicator;

  private String lowestSpatialUnitForComputation;

  private CommonMetadataType metadata;

  private String processDescription;

  private String referenceDateNote;

  @Valid
  private List<@Valid IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources;

  @Valid
  private List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators;

  @Valid
  private List<String> tags = new ArrayList<>();

  private String topicReference;

  private String unit;

  public IndicatorMetadataPATCHInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorMetadataPATCHInputType(String abbreviation, String interpretation, Boolean isHeadlineIndicator, CommonMetadataType metadata, String processDescription, List<String> tags, String topicReference, String unit) {
    this.abbreviation = abbreviation;
    this.interpretation = interpretation;
    this.isHeadlineIndicator = isHeadlineIndicator;
    this.metadata = metadata;
    this.processDescription = processDescription;
    this.tags = tags;
    this.topicReference = topicReference;
    this.unit = unit;
  }

  public IndicatorMetadataPATCHInputType abbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
    return this;
  }

  /**
   * abbreviated mark of the indicator
   * @return abbreviation
  */
  @NotNull 
  @Schema(name = "abbreviation", description = "abbreviated mark of the indicator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("abbreviation")
  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public IndicatorMetadataPATCHInputType characteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
    return this;
  }

  /**
   * the distuingishing characteristic value of the indicator
   * @return characteristicValue
  */
  
  @Schema(name = "characteristicValue", description = "the distuingishing characteristic value of the indicator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characteristicValue")
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
  */
  @Valid 
  @Schema(name = "creationType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("creationType")
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
  */
  
  @Schema(name = "datasetName", description = "the meaningful name of the indicator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("datasetName")
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
  */
  @Valid 
  @Schema(name = "defaultClassificationMapping", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultClassificationMapping")
  public DefaultClassificationMappingType getDefaultClassificationMapping() {
    return defaultClassificationMapping;
  }

  public void setDefaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
  }

  public IndicatorMetadataPATCHInputType regionalReferenceValues(List<@Valid RegionalReferenceValueType> regionalReferenceValues) {
    this.regionalReferenceValues = regionalReferenceValues;
    return this;
  }

  public IndicatorMetadataPATCHInputType addRegionalReferenceValuesItem(RegionalReferenceValueType regionalReferenceValuesItem) {
    if (this.regionalReferenceValues == null) {
      this.regionalReferenceValues = new ArrayList<>();
    }
    this.regionalReferenceValues.add(regionalReferenceValuesItem);
    return this;
  }

  /**
   * list of optional regional reference values (i.e. regional sum, average, spatiallyUnassignable)
   * @return regionalReferenceValues
  */
  @Valid
  @Schema(name = "regionalReferenceValues", description = "list of optional regional reference values (i.e. regional sum, average, spatiallyUnassignable)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regionalReferenceValues")
  public List<@Valid RegionalReferenceValueType> getRegionalReferenceValues() {
    return regionalReferenceValues;
  }

  public void setRegionalReferenceValues(List<@Valid RegionalReferenceValueType> regionalReferenceValues) {
    this.regionalReferenceValues = regionalReferenceValues;
  }

  public IndicatorMetadataPATCHInputType displayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * an order number to control display order in clients
   * @return displayOrder
  */
  @Valid 
  @Schema(name = "displayOrder", example = "0.0", description = "an order number to control display order in clients", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayOrder")
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
  */
  @Valid 
  @Schema(name = "indicatorType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorType")
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
  */
  @NotNull 
  @Schema(name = "interpretation", description = "interpretation of the indicator values", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("interpretation")
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
  */
  @NotNull 
  @Schema(name = "isHeadlineIndicator", description = "boolean value indicating if the indicator is a headline indicator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isHeadlineIndicator")
  public Boolean getIsHeadlineIndicator() {
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
  */
  
  @Schema(name = "lowestSpatialUnitForComputation", description = "identifier/name of the lowest spatial unit for which the indicator can be computed and thus is available (only necessary for computable indicators)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lowestSpatialUnitForComputation")
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
  */
  @NotNull @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metadata")
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
  */
  @NotNull 
  @Schema(name = "processDescription", description = "description about how the indicator was computed", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("processDescription")
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
  */
  
  @Schema(name = "referenceDateNote", description = "an optional note on the reference date of the indicator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceDateNote")
  public String getReferenceDateNote() {
    return referenceDateNote;
  }

  public void setReferenceDateNote(String referenceDateNote) {
    this.referenceDateNote = referenceDateNote;
  }

  public IndicatorMetadataPATCHInputType refrencesToGeoresources(List<@Valid IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
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
  */
  @Valid 
  @Schema(name = "refrencesToGeoresources", description = "array of references to other georesource datasets. E.g., if an indicator is defined by performing geometric-topological operations, then the identifiers of those required georesources can be referenced here", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refrencesToGeoresources")
  public List<@Valid IndicatorPOSTInputTypeRefrencesToGeoresources> getRefrencesToGeoresources() {
    return refrencesToGeoresources;
  }

  public void setRefrencesToGeoresources(List<@Valid IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
  }

  public IndicatorMetadataPATCHInputType refrencesToOtherIndicators(List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
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
  */
  @Valid 
  @Schema(name = "refrencesToOtherIndicators", description = "array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those four indicators can be referenced here", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refrencesToOtherIndicators")
  public List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> getRefrencesToOtherIndicators() {
    return refrencesToOtherIndicators;
  }

  public void setRefrencesToOtherIndicators(List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
  }

  public IndicatorMetadataPATCHInputType tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public IndicatorMetadataPATCHInputType addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * list of tag labels for the indicator
   * @return tags
  */
  @NotNull 
  @Schema(name = "tags", description = "list of tag labels for the indicator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tags")
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
  */
  @NotNull 
  @Schema(name = "topicReference", description = "id of the last topic hierarchy entity ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicReference")
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
  */
  @NotNull 
  @Schema(name = "unit", description = "unit of the indicator values", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("unit")
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorMetadataPATCHInputType indicatorMetadataPATCHInputType = (IndicatorMetadataPATCHInputType) o;
    return Objects.equals(this.abbreviation, indicatorMetadataPATCHInputType.abbreviation) &&
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
    return Objects.hash(abbreviation, characteristicValue, creationType, datasetName, defaultClassificationMapping, regionalReferenceValues, displayOrder, indicatorType, interpretation, isHeadlineIndicator, lowestSpatialUnitForComputation, metadata, processDescription, referenceDateNote, refrencesToGeoresources, refrencesToOtherIndicators, tags, topicReference, unit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorMetadataPATCHInputType {\n");
    sb.append("    abbreviation: ").append(toIndentedString(abbreviation)).append("\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

