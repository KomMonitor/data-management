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
 * IndicatorOverviewType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-23T11:43:09.197508532Z[GMT]")


public class IndicatorOverviewType   {
  @JsonProperty("abbreviation")
  private String abbreviation = null;

  @JsonProperty("allowedRoles")
  @Valid
  private List<String> allowedRoles = null;

  @JsonProperty("applicableDates")
  @Valid
  private List<String> applicableDates = new ArrayList<String>();

  @JsonProperty("applicableSpatialUnits")
  @Valid
  private List<IndicatorSpatialUnitJoinItem> applicableSpatialUnits = new ArrayList<IndicatorSpatialUnitJoinItem>();

  @JsonProperty("characteristicValue")
  private String characteristicValue = null;

  @JsonProperty("creationType")
  private CreationTypeEnum creationType = null;

  @JsonProperty("defaultClassificationMapping")
  private DefaultClassificationMappingType defaultClassificationMapping = null;

  @JsonProperty("regionalReferenceValues")
  @Valid
  private List<RegionalReferenceValueType> regionalReferenceValues = new ArrayList<RegionalReferenceValueType>();

  @JsonProperty("displayOrder")
  private BigDecimal displayOrder = null;

  @JsonProperty("indicatorId")
  private String indicatorId = null;

  @JsonProperty("indicatorName")
  private String indicatorName = null;

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

  @JsonProperty("ogcServices")
  @Valid
  private List<OgcServicesType> ogcServices = new ArrayList<OgcServicesType>();

  @JsonProperty("processDescription")
  private String processDescription = null;

  @JsonProperty("referenceDateNote")
  private String referenceDateNote = null;

  @JsonProperty("referencedGeoresources")
  @Valid
  private List<GeoresourceReferenceType> referencedGeoresources = null;

  @JsonProperty("referencedIndicators")
  @Valid
  private List<IndicatorReferenceType> referencedIndicators = null;

  @JsonProperty("tags")
  @Valid
  private List<String> tags = new ArrayList<String>();

  @JsonProperty("topicReference")
  private String topicReference = null;

  @JsonProperty("unit")
  private String unit = null;

  @JsonProperty("userPermissions")
  @Valid
  private List<PermissionLevelType> userPermissions = null;

  public IndicatorOverviewType abbreviation(String abbreviation) {
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

  public IndicatorOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorOverviewType addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<String>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
   **/
  @Schema(description = "list of role identifiers that have read access rights for this dataset")
  
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
    this.applicableDates.add(applicableDatesItem);
    return this;
  }

  /**
   * array of applicable dates (year and month and day as YEAR-MONTH-DAY) according to ISO 8601 (e.g. 2018-01-30)
   * @return applicableDates
   **/
  @Schema(required = true, description = "array of applicable dates (year and month and day as YEAR-MONTH-DAY) according to ISO 8601 (e.g. 2018-01-30)")
      @NotNull

    public List<String> getApplicableDates() {
    return applicableDates;
  }

  public void setApplicableDates(List<String> applicableDates) {
    this.applicableDates = applicableDates;
  }

  public IndicatorOverviewType applicableSpatialUnits(List<IndicatorSpatialUnitJoinItem> applicableSpatialUnits) {
    this.applicableSpatialUnits = applicableSpatialUnits;
    return this;
  }

  public IndicatorOverviewType addApplicableSpatialUnitsItem(IndicatorSpatialUnitJoinItem applicableSpatialUnitsItem) {
    this.applicableSpatialUnits.add(applicableSpatialUnitsItem);
    return this;
  }

  /**
   * array of spatial unit levels for which the dataset is applicable
   * @return applicableSpatialUnits
   **/
  @Schema(required = true, description = "array of spatial unit levels for which the dataset is applicable")
      @NotNull
    @Valid
    public List<IndicatorSpatialUnitJoinItem> getApplicableSpatialUnits() {
    return applicableSpatialUnits;
  }

  public void setApplicableSpatialUnits(List<IndicatorSpatialUnitJoinItem> applicableSpatialUnits) {
    this.applicableSpatialUnits = applicableSpatialUnits;
  }

  public IndicatorOverviewType characteristicValue(String characteristicValue) {
    this.characteristicValue = characteristicValue;
    return this;
  }

  /**
   * the distuingishing characteristic value of the indicator
   * @return characteristicValue
   **/
  @Schema(required = true, description = "the distuingishing characteristic value of the indicator")
      @NotNull

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
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
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
   **/
  @Schema(description = "")
  
    @Valid
    public DefaultClassificationMappingType getDefaultClassificationMapping() {
    return defaultClassificationMapping;
  }

  public void setDefaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
  }

  public IndicatorOverviewType regionalReferenceValues(List<RegionalReferenceValueType> regionalReferenceValues) {
    this.regionalReferenceValues = regionalReferenceValues;
    return this;
  }

  public IndicatorOverviewType addRegionalReferenceValuesItem(RegionalReferenceValueType regionalReferenceValuesItem) {
    this.regionalReferenceValues.add(regionalReferenceValuesItem);
    return this;
  }

  /**
   * list of optional regional reference values (i.e. regional sum, average, spatiallyUnassignable)
   * @return regionalReferenceValues
   **/
  @Schema(required = true, description = "list of optional regional reference values (i.e. regional sum, average, spatiallyUnassignable)")
      @NotNull
    @Valid
    public List<RegionalReferenceValueType> getRegionalReferenceValues() {
    return regionalReferenceValues;
  }

  public void setRegionalReferenceValues(List<RegionalReferenceValueType> regionalReferenceValues) {
    this.regionalReferenceValues = regionalReferenceValues;
  }

  public IndicatorOverviewType displayOrder(BigDecimal displayOrder) {
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

  public IndicatorOverviewType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

  /**
   * unique identifier of this resource
   * @return indicatorId
   **/
  @Schema(required = true, description = "unique identifier of this resource")
      @NotNull

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
   **/
  @Schema(required = true, description = "name of the indicator")
      @NotNull

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
   **/
  @Schema(description = "")
  
    @Valid
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
   **/
  @Schema(required = true, description = "interpretation of the indicator values")
      @NotNull

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
   **/
  @Schema(required = true, description = "boolean value indicating if the indicator is a headline indicator")
      @NotNull

    public Boolean isIsHeadlineIndicator() {
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
   **/
  @Schema(description = "identifier/name of the lowest spatial unit for which the indicator can be computed and thus is available (only necessary for computable indicators)")
  
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

  public IndicatorOverviewType ogcServices(List<OgcServicesType> ogcServices) {
    this.ogcServices = ogcServices;
    return this;
  }

  public IndicatorOverviewType addOgcServicesItem(OgcServicesType ogcServicesItem) {
    this.ogcServices.add(ogcServicesItem);
    return this;
  }

  /**
   * list of available OGC services for that indicator for different spatial units
   * @return ogcServices
   **/
  @Schema(required = true, description = "list of available OGC services for that indicator for different spatial units")
      @NotNull
    @Valid
    public List<OgcServicesType> getOgcServices() {
    return ogcServices;
  }

  public void setOgcServices(List<OgcServicesType> ogcServices) {
    this.ogcServices = ogcServices;
  }

  public IndicatorOverviewType processDescription(String processDescription) {
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

  public IndicatorOverviewType referenceDateNote(String referenceDateNote) {
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

  public IndicatorOverviewType referencedGeoresources(List<GeoresourceReferenceType> referencedGeoresources) {
    this.referencedGeoresources = referencedGeoresources;
    return this;
  }

  public IndicatorOverviewType addReferencedGeoresourcesItem(GeoresourceReferenceType referencedGeoresourcesItem) {
    if (this.referencedGeoresources == null) {
      this.referencedGeoresources = new ArrayList<GeoresourceReferenceType>();
    }
    this.referencedGeoresources.add(referencedGeoresourcesItem);
    return this;
  }

  /**
   * list of references to georesources
   * @return referencedGeoresources
   **/
  @Schema(description = "list of references to georesources")
      @Valid
    public List<GeoresourceReferenceType> getReferencedGeoresources() {
    return referencedGeoresources;
  }

  public void setReferencedGeoresources(List<GeoresourceReferenceType> referencedGeoresources) {
    this.referencedGeoresources = referencedGeoresources;
  }

  public IndicatorOverviewType referencedIndicators(List<IndicatorReferenceType> referencedIndicators) {
    this.referencedIndicators = referencedIndicators;
    return this;
  }

  public IndicatorOverviewType addReferencedIndicatorsItem(IndicatorReferenceType referencedIndicatorsItem) {
    if (this.referencedIndicators == null) {
      this.referencedIndicators = new ArrayList<IndicatorReferenceType>();
    }
    this.referencedIndicators.add(referencedIndicatorsItem);
    return this;
  }

  /**
   * list of references to other indicators
   * @return referencedIndicators
   **/
  @Schema(description = "list of references to other indicators")
      @Valid
    public List<IndicatorReferenceType> getReferencedIndicators() {
    return referencedIndicators;
  }

  public void setReferencedIndicators(List<IndicatorReferenceType> referencedIndicators) {
    this.referencedIndicators = referencedIndicators;
  }

  public IndicatorOverviewType tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public IndicatorOverviewType addTagsItem(String tagsItem) {
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

  public IndicatorOverviewType topicReference(String topicReference) {
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

  public IndicatorOverviewType unit(String unit) {
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

  public IndicatorOverviewType userPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public IndicatorOverviewType addUserPermissionsItem(PermissionLevelType userPermissionsItem) {
    if (this.userPermissions == null) {
      this.userPermissions = new ArrayList<PermissionLevelType>();
    }
    this.userPermissions.add(userPermissionsItem);
    return this;
  }

  /**
   * list of permissions that are effective on this dataset for the current user
   * @return userPermissions
   **/
  @Schema(description = "list of permissions that are effective on this dataset for the current user")
      @Valid
    public List<PermissionLevelType> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
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
        Objects.equals(this.regionalReferenceValues, indicatorOverviewType.regionalReferenceValues) &&
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
    return Objects.hash(abbreviation, allowedRoles, applicableDates, applicableSpatialUnits, characteristicValue, creationType, defaultClassificationMapping, regionalReferenceValues, displayOrder, indicatorId, indicatorName, indicatorType, interpretation, isHeadlineIndicator, lowestSpatialUnitForComputation, metadata, ogcServices, processDescription, referenceDateNote, referencedGeoresources, referencedIndicators, tags, topicReference, unit, userPermissions);
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
    sb.append("    regionalReferenceValues: ").append(toIndentedString(regionalReferenceValues)).append("\n");
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
