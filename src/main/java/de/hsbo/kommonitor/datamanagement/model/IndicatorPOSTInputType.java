package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeRefrencesToGeoresources;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeRefrencesToOtherIndicators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * IndicatorPOSTInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class IndicatorPOSTInputType   {
  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("applicableSpatialUnit")
  private String applicableSpatialUnit = null;

  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("processDescription")
  private String processDescription = null;

  /**
   * indicates if the data was simply inserted (INSERTED), computed by an automated script (COMPUTED) or automatically aggregated by a script (AGGREGATED)
   */
  public enum CreationTypeEnum {
    INSERTED("INSERTED"),
    
    COMPUTED("COMPUTED"),
    
    AGGREGATED("AGGREGATED");

    private String value;

    CreationTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CreationTypeEnum fromValue(String text) {
      for (CreationTypeEnum b : CreationTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("creationType")
  private CreationTypeEnum creationType = null;

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = new ArrayList<>();

  @JsonProperty("refrencesToOtherIndicators")
  
  private List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators = null;

  @JsonProperty("refrencesToGeoresources")
  
  private List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources = null;

  @JsonProperty("indicatorValues")
  
  private List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = new ArrayList<>();

  public IndicatorPOSTInputType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

   /**
   * the meaningful name of the indicator
   * @return datasetName
  **/
  @ApiModelProperty(required = true, value = "the meaningful name of the indicator")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public IndicatorPOSTInputType applicableSpatialUnit(String applicableSpatialUnit) {
    this.applicableSpatialUnit = applicableSpatialUnit;
    return this;
  }

   /**
   * identifier/name of the spatial unit level
   * @return applicableSpatialUnit
  **/
  @ApiModelProperty(required = true, value = "identifier/name of the spatial unit level")
  public String getApplicableSpatialUnit() {
    return applicableSpatialUnit;
  }

  public void setApplicableSpatialUnit(String applicableSpatialUnit) {
    this.applicableSpatialUnit = applicableSpatialUnit;
  }

  public IndicatorPOSTInputType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public IndicatorPOSTInputType addApplicableTopicsItem(String applicableTopicsItem) {
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

  public IndicatorPOSTInputType metadata(CommonMetadataType metadata) {
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

  public IndicatorPOSTInputType processDescription(String processDescription) {
    this.processDescription = processDescription;
    return this;
  }

   /**
   * description about how the indicator was computed
   * @return processDescription
  **/
  @ApiModelProperty(value = "description about how the indicator was computed")
  public String getProcessDescription() {
    return processDescription;
  }

  public void setProcessDescription(String processDescription) {
    this.processDescription = processDescription;
  }

  public IndicatorPOSTInputType creationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
    return this;
  }

   /**
   * indicates if the data was simply inserted (INSERTED), computed by an automated script (COMPUTED) or automatically aggregated by a script (AGGREGATED)
   * @return creationType
  **/
  @ApiModelProperty(value = "indicates if the data was simply inserted (INSERTED), computed by an automated script (COMPUTED) or automatically aggregated by a script (AGGREGATED)")
  public CreationTypeEnum getCreationType() {
    return creationType;
  }

  public void setCreationType(CreationTypeEnum creationType) {
    this.creationType = creationType;
  }

  public IndicatorPOSTInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorPOSTInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public IndicatorPOSTInputType refrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
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
  **/
  @ApiModelProperty(value = "array of references to other indicators. E.g., if an indicator is defined by combining four other indicators, then the identifiers of those other indicators can be referenced here")
  public List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> getRefrencesToOtherIndicators() {
    return refrencesToOtherIndicators;
  }

  public void setRefrencesToOtherIndicators(List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators) {
    this.refrencesToOtherIndicators = refrencesToOtherIndicators;
  }

  public IndicatorPOSTInputType refrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
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
  **/
  @ApiModelProperty(value = "array of references to other georesource datasets. E.g., if an indicator is defined by performing geometric-topological operations, then the identifiers of those required georesources can be referenced here")
  public List<IndicatorPOSTInputTypeRefrencesToGeoresources> getRefrencesToGeoresources() {
    return refrencesToGeoresources;
  }

  public void setRefrencesToGeoresources(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources) {
    this.refrencesToGeoresources = refrencesToGeoresources;
  }

  public IndicatorPOSTInputType indicatorValues(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
    this.indicatorValues = indicatorValues;
    return this;
  }

  public IndicatorPOSTInputType addIndicatorValuesItem(IndicatorPOSTInputTypeIndicatorValues indicatorValuesItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputType indicatorPOSTInputType = (IndicatorPOSTInputType) o;
    return Objects.equals(this.datasetName, indicatorPOSTInputType.datasetName) &&
        Objects.equals(this.applicableSpatialUnit, indicatorPOSTInputType.applicableSpatialUnit) &&
        Objects.equals(this.applicableTopics, indicatorPOSTInputType.applicableTopics) &&
        Objects.equals(this.metadata, indicatorPOSTInputType.metadata) &&
        Objects.equals(this.processDescription, indicatorPOSTInputType.processDescription) &&
        Objects.equals(this.creationType, indicatorPOSTInputType.creationType) &&
        Objects.equals(this.allowedRoles, indicatorPOSTInputType.allowedRoles) &&
        Objects.equals(this.refrencesToOtherIndicators, indicatorPOSTInputType.refrencesToOtherIndicators) &&
        Objects.equals(this.refrencesToGeoresources, indicatorPOSTInputType.refrencesToGeoresources) &&
        Objects.equals(this.indicatorValues, indicatorPOSTInputType.indicatorValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasetName, applicableSpatialUnit, applicableTopics, metadata, processDescription, creationType, allowedRoles, refrencesToOtherIndicators, refrencesToGeoresources, indicatorValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputType {\n");
    
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    applicableSpatialUnit: ").append(toIndentedString(applicableSpatialUnit)).append("\n");
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    processDescription: ").append(toIndentedString(processDescription)).append("\n");
    sb.append("    creationType: ").append(toIndentedString(creationType)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    refrencesToOtherIndicators: ").append(toIndentedString(refrencesToOtherIndicators)).append("\n");
    sb.append("    refrencesToGeoresources: ").append(toIndentedString(refrencesToGeoresources)).append("\n");
    sb.append("    indicatorValues: ").append(toIndentedString(indicatorValues)).append("\n");
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

