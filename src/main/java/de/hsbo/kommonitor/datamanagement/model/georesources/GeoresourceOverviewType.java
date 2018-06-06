package de.hsbo.kommonitor.datamanagement.model.georesources;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * GeoresourceOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class GeoresourceOverviewType   {
  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("georesourceId")
  private String georesourceId = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("availablePeriodsOfValidity")
  
  private List<PeriodOfValidityType> availablePeriodsOfValidity = new ArrayList<>();

  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = null;

  public GeoresourceOverviewType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

   /**
   * the meaningful name of the dataset
   * @return datasetName
  **/
  @ApiModelProperty(required = true, value = "the meaningful name of the dataset")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public GeoresourceOverviewType georesourceId(String georesourceId) {
    this.georesourceId = georesourceId;
    return this;
  }

   /**
   * the unique identifier of the dataset
   * @return georesourceId
  **/
  @ApiModelProperty(required = true, value = "the unique identifier of the dataset")
  public String getGeoresourceId() {
    return georesourceId;
  }

  public void setGeoresourceId(String georesourceId) {
    this.georesourceId = georesourceId;
  }

  public GeoresourceOverviewType metadata(CommonMetadataType metadata) {
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

  public GeoresourceOverviewType availablePeriodsOfValidity(List<PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    return this;
  }

  public GeoresourceOverviewType addAvailablePeriodsOfValidityItem(PeriodOfValidityType availablePeriodsOfValidityItem) {
    this.availablePeriodsOfValidity.add(availablePeriodsOfValidityItem);
    return this;
  }

   /**
   * Get availablePeriodsOfValidity
   * @return availablePeriodsOfValidity
  **/
  @ApiModelProperty(required = true, value = "")
  public List<PeriodOfValidityType> getAvailablePeriodsOfValidity() {
    return availablePeriodsOfValidity;
  }

  public void setAvailablePeriodsOfValidity(List<PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public GeoresourceOverviewType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public GeoresourceOverviewType addApplicableTopicsItem(String applicableTopicsItem) {
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

  public GeoresourceOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourceOverviewType addAllowedRolesItem(String allowedRolesItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourceOverviewType georesourceOverviewType = (GeoresourceOverviewType) o;
    return Objects.equals(this.datasetName, georesourceOverviewType.datasetName) &&
        Objects.equals(this.georesourceId, georesourceOverviewType.georesourceId) &&
        Objects.equals(this.metadata, georesourceOverviewType.metadata) &&
        Objects.equals(this.availablePeriodsOfValidity, georesourceOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.applicableTopics, georesourceOverviewType.applicableTopics) &&
        Objects.equals(this.allowedRoles, georesourceOverviewType.allowedRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasetName, georesourceId, metadata, availablePeriodsOfValidity, applicableTopics, allowedRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourceOverviewType {\n");
    
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    georesourceId: ").append(toIndentedString(georesourceId)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
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

