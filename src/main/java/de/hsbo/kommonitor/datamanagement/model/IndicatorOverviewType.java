package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.CreationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorReferenceType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorSpatialUnitJoinItem;
import de.hsbo.kommonitor.datamanagement.model.IndicatorTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.OgcServicesType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IndicatorOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-13T09:18:57.441387500+01:00[Europe/Berlin]")
public class IndicatorOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String abbreviation;

  @Valid
  private List<String> allowedRoles;

  @Valid
  private List<String> applicableDates = new ArrayList<>();

  @Valid
  private List<@Valid IndicatorSpatialUnitJoinItem> applicableSpatialUnits = new ArrayList<>();

  private String characteristicValue;

  private CreationTypeEnum creationType;

  private DefaultClassificationMappingType defaultClassificationMapping;

  private BigDecimal displayOrder;

  private String indicatorId;

  private String indicatorName;

  private IndicatorTypeEnum indicatorType;

  private String interpretation;

  private Boolean isHeadlineIndicator;

  private String lowestSpatialUnitForComputation;

  private CommonMetadataType metadata;

  @Valid
  private List<@Valid OgcServicesType> ogcServices = new ArrayList<>();

  private String processDescription;

  private String referenceDateNote;

  @Valid
  private List<@Valid GeoresourceReferenceType> referencedGeoresources;

  @Valid
  private List<@Valid IndicatorReferenceType> referencedIndicators;

  @Valid
  private List<String> tags = new ArrayList<>();

  private String topicReference;

  private String unit;

  @Valid
  private List<PermissionLevelType> userPermissions;

  public IndicatorOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorOverviewType(String abbreviation, List<String> applicableDates, List<@Valid IndicatorSpatialUnitJoinItem> applicableSpatialUnits, String characteristicValue, CreationTypeEnum creationType, String indicatorId, String indicatorName, String interpretation, Boolean isHeadlineIndicator, CommonMetadataType metadata, List<@Valid OgcServicesType> ogcServices, String processDescription, List<String> tags, String topicReference, String unit) {
    this.abbreviation = abbreviation;
    this.applicableDates = applicableDates;
    this.applicableSpatialUnits = applicableSpatialUnits;
    this.characteristicValue = characteristicValue;
    this.creationType = creationType;
    this.indicatorId = indicatorId;
    this.indicatorName = indicatorName;
    this.interpretation = interpretation;
    this.isHeadlineIndicator = isHeadlineIndicator;
    this.metadata = metadata;
    this.ogcServices = ogcServices;
    this.processDescription = processDescription;
    this.tags = tags;
    this.topicReference = topicReference;
    this.unit = unit;
  }

  public IndicatorOverviewType abbreviation(String abbreviation) {
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

  public IndicatorOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorOverviewType addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
  */
  
  @Schema(name = "allowedRoles", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedRoles")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public IndicatorOverviewType applicableDates(List<String> applicableDates) {
    this.applicableDates = applicableDates;
    return this;
  }

  public IndicatorOverviewType addApplicableDatesItem(String applicableDatesItem) {
    if (this.applicableDates == null) {
      this.applicableDates = new ArrayList<>();
    }
    this.applicableDates.add(applicableDatesItem);
    return this;
  }

  /**
   * array of applicable dates (year and month and day as YEAR-MONTH-DAY) according to ISO 8601 (e.g. 2018-01-30)
   * @return applicableDates
  */
  @NotNull 
  @Schema(name = "applicableDates", description = "array of applicable dates (year and month and day as YEAR-MONTH-DAY) according to ISO 8601 (e.g. 2018-01-30)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("applicableDates")
  public List<String> getApplicableDates() {
    return applicableDates;
  }

  public void setApplicableDates(List<String> applicableDates) {
    this.applicableDates = applicableDates;
  }

  public IndicatorOverviewType applicableSpatialUnits(List<@Valid IndicatorSpatialUnitJoinItem> applicableSpatialUnits) {
    this.applicableSpatialUnits = applicableSpatialUnits;
    return this;
  }

  public IndicatorOverviewType addApplicableSpatialUnitsItem(IndicatorSpatialUnitJoinItem applicableSpatialUnitsItem) {
    if (this.applicableSpatialUnits == null) {
      this.applicableSpatialUnits = new ArrayList<>();
    }
    this.applicableSpatialUnits.add(applicableSpatialUnitsItem);
    return this;
  }

  /**
   * array of spatial unit levels for which the dataset is applicable
   * @return applicableSpatialUnits
  */
  @NotNull @Valid 
  @Schema(name = "applicableSpatialUnits", description = "array of spatial unit levels for which the dataset is applicable", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("applicableSpatialUnits")
  public List<@Valid IndicatorSpatialUnitJoinItem> getApplicableSpatialUnits() {
    return applicableSpatialUnits;
  }

  public void setApplicableSpatialUnits(List<@Valid IndicatorSpatialUnitJoinItem> applicableSpatialUnits) {
    this.applicableSpatialUnits = applicableSpatialUnits;
  }

  public IndicatorOverviewType characteristicValue(String characteristicValue) {
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

  public IndicatorOverviewType creationType(CreationTypeEnum creationType) {
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

  public IndicatorOverviewType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
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

  public IndicatorOverviewType displayOrder(BigDecimal displayOrder) {
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

  public IndicatorOverviewType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

  /**
   * unique identifier of this resource
   * @return indicatorId
  */
  @NotNull 
  @Schema(name = "indicatorId", description = "unique identifier of this resource", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorId")
  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public IndicatorOverviewType indicatorName(String indicatorName) {
    this.indicatorName = indicatorName;
    return this;
  }

  /**
   * name of the indicator
   * @return indicatorName
  */
  @NotNull 
  @Schema(name = "indicatorName", description = "name of the indicator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorName")
  public String getIndicatorName() {
    return indicatorName;
  }

  public void setIndicatorName(String indicatorName) {
    this.indicatorName = indicatorName;
  }

  public IndicatorOverviewType indicatorType(IndicatorTypeEnum indicatorType) {
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

  public IndicatorOverviewType interpretation(String interpretation) {
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

  public IndicatorOverviewType isHeadlineIndicator(Boolean isHeadlineIndicator) {
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

  public IndicatorOverviewType lowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
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

  public IndicatorOverviewType metadata(CommonMetadataType metadata) {
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

  public IndicatorOverviewType ogcServices(List<@Valid OgcServicesType> ogcServices) {
    this.ogcServices = ogcServices;
    return this;
  }

  public IndicatorOverviewType addOgcServicesItem(OgcServicesType ogcServicesItem) {
    if (this.ogcServices == null) {
      this.ogcServices = new ArrayList<>();
    }
    this.ogcServices.add(ogcServicesItem);
    return this;
  }

  /**
   * list of available OGC services for that indicator for different spatial units
   * @return ogcServices
  */
  @NotNull @Valid 
  @Schema(name = "ogcServices", description = "list of available OGC services for that indicator for different spatial units", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ogcServices")
  public List<@Valid OgcServicesType> getOgcServices() {
    return ogcServices;
  }

  public void setOgcServices(List<@Valid OgcServicesType> ogcServices) {
    this.ogcServices = ogcServices;
  }

  public IndicatorOverviewType processDescription(String processDescription) {
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

  public IndicatorOverviewType referenceDateNote(String referenceDateNote) {
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

  public IndicatorOverviewType referencedGeoresources(List<@Valid GeoresourceReferenceType> referencedGeoresources) {
    this.referencedGeoresources = referencedGeoresources;
    return this;
  }

  public IndicatorOverviewType addReferencedGeoresourcesItem(GeoresourceReferenceType referencedGeoresourcesItem) {
    if (this.referencedGeoresources == null) {
      this.referencedGeoresources = new ArrayList<>();
    }
    this.referencedGeoresources.add(referencedGeoresourcesItem);
    return this;
  }

  /**
   * list of references to georesources
   * @return referencedGeoresources
  */
  @Valid 
  @Schema(name = "referencedGeoresources", description = "list of references to georesources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referencedGeoresources")
  public List<@Valid GeoresourceReferenceType> getReferencedGeoresources() {
    return referencedGeoresources;
  }

  public void setReferencedGeoresources(List<@Valid GeoresourceReferenceType> referencedGeoresources) {
    this.referencedGeoresources = referencedGeoresources;
  }

  public IndicatorOverviewType referencedIndicators(List<@Valid IndicatorReferenceType> referencedIndicators) {
    this.referencedIndicators = referencedIndicators;
    return this;
  }

  public IndicatorOverviewType addReferencedIndicatorsItem(IndicatorReferenceType referencedIndicatorsItem) {
    if (this.referencedIndicators == null) {
      this.referencedIndicators = new ArrayList<>();
    }
    this.referencedIndicators.add(referencedIndicatorsItem);
    return this;
  }

  /**
   * list of references to other indicators
   * @return referencedIndicators
  */
  @Valid 
  @Schema(name = "referencedIndicators", description = "list of references to other indicators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referencedIndicators")
  public List<@Valid IndicatorReferenceType> getReferencedIndicators() {
    return referencedIndicators;
  }

  public void setReferencedIndicators(List<@Valid IndicatorReferenceType> referencedIndicators) {
    this.referencedIndicators = referencedIndicators;
  }

  public IndicatorOverviewType tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public IndicatorOverviewType addTagsItem(String tagsItem) {
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

  public IndicatorOverviewType topicReference(String topicReference) {
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

  public IndicatorOverviewType unit(String unit) {
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

  public IndicatorOverviewType userPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public IndicatorOverviewType addUserPermissionsItem(PermissionLevelType userPermissionsItem) {
    if (this.userPermissions == null) {
      this.userPermissions = new ArrayList<>();
    }
    this.userPermissions.add(userPermissionsItem);
    return this;
  }

  /**
   * list of permissions that are effective on this dataset for the current user
   * @return userPermissions
  */
  @Valid 
  @Schema(name = "userPermissions", description = "list of permissions that are effective on this dataset for the current user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userPermissions")
  public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorOverviewType indicatorOverviewType = (IndicatorOverviewType) o;
    return Objects.equals(this.abbreviation, indicatorOverviewType.abbreviation) &&
        Objects.equals(this.allowedRoles, indicatorOverviewType.allowedRoles) &&
        Objects.equals(this.applicableDates, indicatorOverviewType.applicableDates) &&
        Objects.equals(this.applicableSpatialUnits, indicatorOverviewType.applicableSpatialUnits) &&
        Objects.equals(this.characteristicValue, indicatorOverviewType.characteristicValue) &&
        Objects.equals(this.creationType, indicatorOverviewType.creationType) &&
        Objects.equals(this.defaultClassificationMapping, indicatorOverviewType.defaultClassificationMapping) &&
        Objects.equals(this.displayOrder, indicatorOverviewType.displayOrder) &&
        Objects.equals(this.indicatorId, indicatorOverviewType.indicatorId) &&
        Objects.equals(this.indicatorName, indicatorOverviewType.indicatorName) &&
        Objects.equals(this.indicatorType, indicatorOverviewType.indicatorType) &&
        Objects.equals(this.interpretation, indicatorOverviewType.interpretation) &&
        Objects.equals(this.isHeadlineIndicator, indicatorOverviewType.isHeadlineIndicator) &&
        Objects.equals(this.lowestSpatialUnitForComputation, indicatorOverviewType.lowestSpatialUnitForComputation) &&
        Objects.equals(this.metadata, indicatorOverviewType.metadata) &&
        Objects.equals(this.ogcServices, indicatorOverviewType.ogcServices) &&
        Objects.equals(this.processDescription, indicatorOverviewType.processDescription) &&
        Objects.equals(this.referenceDateNote, indicatorOverviewType.referenceDateNote) &&
        Objects.equals(this.referencedGeoresources, indicatorOverviewType.referencedGeoresources) &&
        Objects.equals(this.referencedIndicators, indicatorOverviewType.referencedIndicators) &&
        Objects.equals(this.tags, indicatorOverviewType.tags) &&
        Objects.equals(this.topicReference, indicatorOverviewType.topicReference) &&
        Objects.equals(this.unit, indicatorOverviewType.unit) &&
        Objects.equals(this.userPermissions, indicatorOverviewType.userPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abbreviation, allowedRoles, applicableDates, applicableSpatialUnits, characteristicValue, creationType, defaultClassificationMapping, displayOrder, indicatorId, indicatorName, indicatorType, interpretation, isHeadlineIndicator, lowestSpatialUnitForComputation, metadata, ogcServices, processDescription, referenceDateNote, referencedGeoresources, referencedIndicators, tags, topicReference, unit, userPermissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorOverviewType {\n");
    sb.append("    abbreviation: ").append(toIndentedString(abbreviation)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    applicableDates: ").append(toIndentedString(applicableDates)).append("\n");
    sb.append("    applicableSpatialUnits: ").append(toIndentedString(applicableSpatialUnits)).append("\n");
    sb.append("    characteristicValue: ").append(toIndentedString(characteristicValue)).append("\n");
    sb.append("    creationType: ").append(toIndentedString(creationType)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    indicatorId: ").append(toIndentedString(indicatorId)).append("\n");
    sb.append("    indicatorName: ").append(toIndentedString(indicatorName)).append("\n");
    sb.append("    indicatorType: ").append(toIndentedString(indicatorType)).append("\n");
    sb.append("    interpretation: ").append(toIndentedString(interpretation)).append("\n");
    sb.append("    isHeadlineIndicator: ").append(toIndentedString(isHeadlineIndicator)).append("\n");
    sb.append("    lowestSpatialUnitForComputation: ").append(toIndentedString(lowestSpatialUnitForComputation)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    ogcServices: ").append(toIndentedString(ogcServices)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    referenceDateNote: ").append(toIndentedString(referenceDateNote)).append("\n");
    sb.append("    referencedGeoresources: ").append(toIndentedString(referencedGeoresources)).append("\n");
    sb.append("    referencedIndicators: ").append(toIndentedString(referencedIndicators)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
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

