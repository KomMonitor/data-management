package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-01-17T10:12:10.704+01:00")

public class IndicatorOverviewType   {
  @JsonProperty("indicatorName")
  private String indicatorName = null;

  @JsonProperty("indicatorId")
  private String indicatorId = null;

  @JsonProperty("indicatorType")
  private IndicatorTypeEnum indicatorType = null;

  @JsonProperty("unit")
  private String unit = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("processDescription")
  private String processDescription = null;

  @JsonProperty("applicableSpatialUnits")
  
  private List<String> applicableSpatialUnits = new ArrayList<>();

  @JsonProperty("applicableDates")
  
  private List<String> applicableDates = new ArrayList<>();

  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = null;

  @JsonProperty("referencedIndicators")
  
  private List<IndicatorReferenceType> referencedIndicators = null;

  @JsonProperty("referencedGeoresources")
  
  private List<GeoresourceReferenceType> referencedGeoresources = null;

  @JsonProperty("creationType")
  private CreationTypeEnum creationType = null;

  @JsonProperty("lowestSpatialUnitForComputation")
  private String lowestSpatialUnitForComputation = null;

  @JsonProperty("defaultClassificationMapping")
  private DefaultClassificationMappingType defaultClassificationMapping = null;

  @JsonProperty("ogcServices")
  
  private List<OgcServicesType> ogcServices = new ArrayList<>();

  public IndicatorOverviewType indicatorName(String indicatorName) {
    this.indicatorName = indicatorName;
    return this;
  }

   /**
   * name of the indicator
   * @return indicatorName
  **/
  @ApiModelProperty(required = true, value = "name of the indicator")
  public String getIndicatorName() {
    return indicatorName;
  }

  public void setIndicatorName(String indicatorName) {
    this.indicatorName = indicatorName;
  }

  public IndicatorOverviewType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

   /**
   * unique identifier of this resource
   * @return indicatorId
  **/
  @ApiModelProperty(required = true, value = "unique identifier of this resource")
  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public IndicatorOverviewType indicatorType(IndicatorTypeEnum indicatorType) {
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

  public IndicatorOverviewType unit(String unit) {
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

  public IndicatorOverviewType metadata(CommonMetadataType metadata) {
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

  public IndicatorOverviewType processDescription(String processDescription) {
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

  public IndicatorOverviewType applicableSpatialUnits(List<String> applicableSpatialUnits) {
    this.applicableSpatialUnits = applicableSpatialUnits;
    return this;
  }

  public IndicatorOverviewType addApplicableSpatialUnitsItem(String applicableSpatialUnitsItem) {
    this.applicableSpatialUnits.add(applicableSpatialUnitsItem);
    return this;
  }

   /**
   * array of spatial unit levels for which the dataset is applicable
   * @return applicableSpatialUnits
  **/
  @ApiModelProperty(required = true, value = "array of spatial unit levels for which the dataset is applicable")
  public List<String> getApplicableSpatialUnits() {
    return applicableSpatialUnits;
  }

  public void setApplicableSpatialUnits(List<String> applicableSpatialUnits) {
    this.applicableSpatialUnits = applicableSpatialUnits;
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
  @ApiModelProperty(required = true, value = "array of applicable dates (year and month and day as YEAR-MONTH-DAY) according to ISO 8601 (e.g. 2018-01-30)")
  public List<String> getApplicableDates() {
    return applicableDates;
  }

  public void setApplicableDates(List<String> applicableDates) {
    this.applicableDates = applicableDates;
  }

  public IndicatorOverviewType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public IndicatorOverviewType addApplicableTopicsItem(String applicableTopicsItem) {
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
  **/
  @ApiModelProperty(value = "list of role identifiers that have read access rights for this dataset")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public IndicatorOverviewType referencedIndicators(List<IndicatorReferenceType> referencedIndicators) {
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
  **/
  @ApiModelProperty(value = "list of references to other indicators")
  public List<IndicatorReferenceType> getReferencedIndicators() {
    return referencedIndicators;
  }

  public void setReferencedIndicators(List<IndicatorReferenceType> referencedIndicators) {
    this.referencedIndicators = referencedIndicators;
  }

  public IndicatorOverviewType referencedGeoresources(List<GeoresourceReferenceType> referencedGeoresources) {
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
  **/
  @ApiModelProperty(value = "list of references to georesources")
  public List<GeoresourceReferenceType> getReferencedGeoresources() {
    return referencedGeoresources;
  }

  public void setReferencedGeoresources(List<GeoresourceReferenceType> referencedGeoresources) {
    this.referencedGeoresources = referencedGeoresources;
  }

  public IndicatorOverviewType creationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
    return this;
  }

   /**
   * indicates if the data is simply inserted (INSERTION), computed by an automated script (COMPUTATION) or automatically aggregated by a script (AGGREGATION)
   * @return creationType
  **/
  @ApiModelProperty(required = true, value = "indicates if the data is simply inserted (INSERTION), computed by an automated script (COMPUTATION) or automatically aggregated by a script (AGGREGATION)")
  public CreationTypeEnum getCreationType() {
    return creationType;
  }

  public void setCreationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
  }

  public IndicatorOverviewType lowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
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

  public IndicatorOverviewType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
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
  @ApiModelProperty(required = true, value = "list of available OGC services for that indicator for different spatial units")
  public List<OgcServicesType> getOgcServices() {
    return ogcServices;
  }

  public void setOgcServices(List<OgcServicesType> ogcServices) {
    this.ogcServices = ogcServices;
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
    return Objects.equals(this.indicatorName, indicatorOverviewType.indicatorName) &&
        Objects.equals(this.indicatorId, indicatorOverviewType.indicatorId) &&
        Objects.equals(this.indicatorType, indicatorOverviewType.indicatorType) &&
        Objects.equals(this.unit, indicatorOverviewType.unit) &&
        Objects.equals(this.metadata, indicatorOverviewType.metadata) &&
        Objects.equals(this.processDescription, indicatorOverviewType.processDescription) &&
        Objects.equals(this.applicableSpatialUnits, indicatorOverviewType.applicableSpatialUnits) &&
        Objects.equals(this.applicableDates, indicatorOverviewType.applicableDates) &&
        Objects.equals(this.applicableTopics, indicatorOverviewType.applicableTopics) &&
        Objects.equals(this.allowedRoles, indicatorOverviewType.allowedRoles) &&
        Objects.equals(this.referencedIndicators, indicatorOverviewType.referencedIndicators) &&
        Objects.equals(this.referencedGeoresources, indicatorOverviewType.referencedGeoresources) &&
        Objects.equals(this.creationType, indicatorOverviewType.creationType) &&
        Objects.equals(this.lowestSpatialUnitForComputation, indicatorOverviewType.lowestSpatialUnitForComputation) &&
        Objects.equals(this.defaultClassificationMapping, indicatorOverviewType.defaultClassificationMapping) &&
        Objects.equals(this.ogcServices, indicatorOverviewType.ogcServices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indicatorName, indicatorId, indicatorType, unit, metadata, processDescription, applicableSpatialUnits, applicableDates, applicableTopics, allowedRoles, referencedIndicators, referencedGeoresources, creationType, lowestSpatialUnitForComputation, defaultClassificationMapping, ogcServices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorOverviewType {\n");
    
    sb.append("    indicatorName: ").append(toIndentedString(indicatorName)).append("\n");
    sb.append("    indicatorId: ").append(toIndentedString(indicatorId)).append("\n");
    sb.append("    indicatorType: ").append(toIndentedString(indicatorType)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    applicableSpatialUnits: ").append(toIndentedString(applicableSpatialUnits)).append("\n");
    sb.append("    applicableDates: ").append(toIndentedString(applicableDates)).append("\n");
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    referencedIndicators: ").append(toIndentedString(referencedIndicators)).append("\n");
    sb.append("    referencedGeoresources: ").append(toIndentedString(referencedGeoresources)).append("\n");
    sb.append("    creationType: ").append(toIndentedString(creationType)).append("\n");
    sb.append("    lowestSpatialUnitForComputation: ").append(toIndentedString(lowestSpatialUnitForComputation)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    ogcServices: ").append(toIndentedString(ogcServices)).append("\n");
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

