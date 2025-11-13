package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.CreationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeRefrencesToGeoresources;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeRefrencesToOtherIndicators;
import de.hsbo.kommonitor.datamanagement.model.IndicatorTypeEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IndicatorPOSTInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-13T11:45:45.252135300+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class IndicatorPOSTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String abbreviation;

  @Valid
  private List<String> permissions = new ArrayList<>();

  private String characteristicValue;

  private CreationTypeEnum creationType;

  private String datasetName;

  private DefaultClassificationMappingType defaultClassificationMapping;

  private @Nullable BigDecimal displayOrder;

  private @Nullable IndicatorTypeEnum indicatorType;

  private String interpretation;

  private Boolean isHeadlineIndicator;

  private @Nullable String lowestSpatialUnitForComputation;

  private CommonMetadataType metadata;

  private @Nullable Integer precision;

  private String ownerId;

  private String processDescription;

  private @Nullable String referenceDateNote;

  @Valid
  private List<@Valid IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources = new ArrayList<>();

  @Valid
  private List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators = new ArrayList<>();

  @Valid
  private List<String> tags = new ArrayList<>();

  private String topicReference;

  private String unit;

  private Boolean isPublic;

  public IndicatorPOSTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorPOSTInputType(String abbreviation, List<String> permissions, String characteristicValue, CreationTypeEnum creationType, String datasetName, DefaultClassificationMappingType defaultClassificationMapping, String interpretation, Boolean isHeadlineIndicator, CommonMetadataType metadata, String ownerId, String processDescription, List<String> tags, String topicReference, String unit, Boolean isPublic) {
    this.abbreviation = abbreviation;
    this.permissions = permissions;
    this.characteristicValue = characteristicValue;
    this.creationType = creationType;
    this.datasetName = datasetName;
    this.defaultClassificationMapping = defaultClassificationMapping;
    this.interpretation = interpretation;
    this.isHeadlineIndicator = isHeadlineIndicator;
    this.metadata = metadata;
    this.ownerId = ownerId;
    this.processDescription = processDescription;
    this.tags = tags;
    this.topicReference = topicReference;
    this.unit = unit;
    this.isPublic = isPublic;
  }

  public IndicatorPOSTInputType abbreviation(String abbreviation) {
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

  public IndicatorPOSTInputType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public IndicatorPOSTInputType addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * list of permissions on this entity
   * @return permissions
   */
  @NotNull 
  @Schema(name = "permissions", description = "list of permissions on this entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public IndicatorPOSTInputType characteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
    return this;
  }

  /**
   * the distuingishing characteristic value of the indicator
   * @return characteristicValue
   */
  @NotNull 
  @Schema(name = "characteristicValue", description = "the distuingishing characteristic value of the indicator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characteristicValue")
  public String getCharacteristicValue() {
    return characteristicValue;
  }

  public void setCharacteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
  }

  public IndicatorPOSTInputType creationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
    return this;
  }

  /**
   * Get creationType
   * @return creationType
   */
  @NotNull @Valid 
  @Schema(name = "creationType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("creationType")
  public CreationTypeEnum getCreationType() {
    return creationType;
  }

  public void setCreationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
  }

  public IndicatorPOSTInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the indicator
   * @return datasetName
   */
  @NotNull 
  @Schema(name = "datasetName", description = "the meaningful name of the indicator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datasetName")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public IndicatorPOSTInputType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
    return this;
  }

  /**
   * Get defaultClassificationMapping
   * @return defaultClassificationMapping
   */
  @NotNull @Valid 
  @Schema(name = "defaultClassificationMapping", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defaultClassificationMapping")
  public DefaultClassificationMappingType getDefaultClassificationMapping() {
    return defaultClassificationMapping;
  }

  public void setDefaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
  }

  public IndicatorPOSTInputType displayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * an order number to control display order in clients
   * @return displayOrder
   */
  @Valid 
  @Schema(name = "displayOrder", example = "0", description = "an order number to control display order in clients", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayOrder")
  public BigDecimal getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(BigDecimal displayOrder) {
    this.displayOrder = displayOrder;
  }

  public IndicatorPOSTInputType indicatorType(IndicatorTypeEnum indicatorType) {
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

  public IndicatorPOSTInputType interpretation(String interpretation) {
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

  public IndicatorPOSTInputType isHeadlineIndicator(Boolean isHeadlineIndicator) {
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

  public IndicatorPOSTInputType lowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
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

  public IndicatorPOSTInputType metadata(CommonMetadataType metadata) {
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

  public IndicatorPOSTInputType precision(Integer precision) {
    this.precision = precision;
    return this;
  }

  /**
   * Defines the number of decimal places for indicator values. If null, there is no predefined precision for this indicator.
   * @return precision
   */
  
  @Schema(name = "precision", description = "Defines the number of decimal places for indicator values. If null, there is no predefined precision for this indicator.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("precision")
  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public IndicatorPOSTInputType ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * identifier of the owning group
   * @return ownerId
   */
  @NotNull 
  @Schema(name = "ownerId", description = "identifier of the owning group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerId")
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public IndicatorPOSTInputType processDescription(String processDescription) {
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

  public IndicatorPOSTInputType referenceDateNote(String referenceDateNote) {
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

  public IndicatorPOSTInputType refrencesToGeoresources(List<@Valid IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
    return this;
  }

  public IndicatorPOSTInputType addRefrencesToGeoresourcesItem(IndicatorPOSTInputTypeRefrencesToGeoresources refrencesToGeoresourcesItem) {
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

  public IndicatorPOSTInputType refrencesToOtherIndicators(List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
    return this;
  }

  public IndicatorPOSTInputType addRefrencesToOtherIndicatorsItem(IndicatorPOSTInputTypeRefrencesToOtherIndicators refrencesToOtherIndicatorsItem) {
    if (this.refrencesToOtherIndicators == null) {
      this.refrencesToOtherIndicators = new ArrayList<>();
    }
    this.refrencesToOtherIndicators.add(refrencesToOtherIndicatorsItem);
    return this;
  }

  /**
   * array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those other indicators can be referenced here
   * @return refrencesToOtherIndicators
   */
  @Valid 
  @Schema(name = "refrencesToOtherIndicators", description = "array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those other indicators can be referenced here", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refrencesToOtherIndicators")
  public List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> getRefrencesToOtherIndicators() {
    return refrencesToOtherIndicators;
  }

  public void setRefrencesToOtherIndicators(List<@Valid IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
  }

  public IndicatorPOSTInputType tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public IndicatorPOSTInputType addTagsItem(String tagsItem) {
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

  public IndicatorPOSTInputType topicReference(String topicReference) {
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

  public IndicatorPOSTInputType unit(String unit) {
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

  public IndicatorPOSTInputType isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether the resource is publicly accessible
   * @return isPublic
   */
  @NotNull 
  @Schema(name = "isPublic", description = "flag whether the resource is publicly accessible", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputType indicatorPOSTInputType = (IndicatorPOSTInputType) o;
    return Objects.equals(this.abbreviation, indicatorPOSTInputType.abbreviation) &&
        Objects.equals(this.permissions, indicatorPOSTInputType.permissions) &&
        Objects.equals(this.characteristicValue, indicatorPOSTInputType.characteristicValue) &&
        Objects.equals(this.creationType, indicatorPOSTInputType.creationType) &&
        Objects.equals(this.datasetName, indicatorPOSTInputType.datasetName) &&
        Objects.equals(this.defaultClassificationMapping, indicatorPOSTInputType.defaultClassificationMapping) &&
        Objects.equals(this.displayOrder, indicatorPOSTInputType.displayOrder) &&
        Objects.equals(this.indicatorType, indicatorPOSTInputType.indicatorType) &&
        Objects.equals(this.interpretation, indicatorPOSTInputType.interpretation) &&
        Objects.equals(this.isHeadlineIndicator, indicatorPOSTInputType.isHeadlineIndicator) &&
        Objects.equals(this.lowestSpatialUnitForComputation, indicatorPOSTInputType.lowestSpatialUnitForComputation) &&
        Objects.equals(this.metadata, indicatorPOSTInputType.metadata) &&
        Objects.equals(this.precision, indicatorPOSTInputType.precision) &&
        Objects.equals(this.ownerId, indicatorPOSTInputType.ownerId) &&
        Objects.equals(this.processDescription, indicatorPOSTInputType.processDescription) &&
        Objects.equals(this.referenceDateNote, indicatorPOSTInputType.referenceDateNote) &&
        Objects.equals(this.refrencesToGeoresources, indicatorPOSTInputType.refrencesToGeoresources) &&
        Objects.equals(this.refrencesToOtherIndicators, indicatorPOSTInputType.refrencesToOtherIndicators) &&
        Objects.equals(this.tags, indicatorPOSTInputType.tags) &&
        Objects.equals(this.topicReference, indicatorPOSTInputType.topicReference) &&
        Objects.equals(this.unit, indicatorPOSTInputType.unit) &&
        Objects.equals(this.isPublic, indicatorPOSTInputType.isPublic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abbreviation, permissions, characteristicValue, creationType, datasetName, defaultClassificationMapping, displayOrder, indicatorType, interpretation, isHeadlineIndicator, lowestSpatialUnitForComputation, metadata, precision, ownerId, processDescription, referenceDateNote, refrencesToGeoresources, refrencesToOtherIndicators, tags, topicReference, unit, isPublic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputType {\n");
    sb.append("    abbreviation: ").append(toIndentedString(abbreviation)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    characteristicValue: ").append(toIndentedString(characteristicValue)).append("\n");
    sb.append("    creationType: ").append(toIndentedString(creationType)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    indicatorType: ").append(toIndentedString(indicatorType)).append("\n");
    sb.append("    interpretation: ").append(toIndentedString(interpretation)).append("\n");
    sb.append("    isHeadlineIndicator: ").append(toIndentedString(isHeadlineIndicator)).append("\n");
    sb.append("    lowestSpatialUnitForComputation: ").append(toIndentedString(lowestSpatialUnitForComputation)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    precision: ").append(toIndentedString(precision)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    referenceDateNote: ").append(toIndentedString(referenceDateNote)).append("\n");
    sb.append("    refrencesToGeoresources: ").append(toIndentedString(refrencesToGeoresources)).append("\n");
    sb.append("    refrencesToOtherIndicators: ").append(toIndentedString(refrencesToOtherIndicators)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
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

