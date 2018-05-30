package de.hsbo.kommonitor.datamanagement.model;

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
 * GeoresourcePOSTInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class GeoresourcePOSTInputType   {
  @JsonProperty("datasetName")
  private String datasetName = null;

  @JsonProperty("periodOfValidity")
  private PeriodOfValidityType periodOfValidity = null;

  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = null;

  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("jsonSchema")
  private String jsonSchema = null;

  @JsonProperty("geoJsonString")
  private String geoJsonString = null;

  public GeoresourcePOSTInputType datasetName(String datasetName) {
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

  public GeoresourcePOSTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
    return this;
  }

   /**
   * Get periodOfValidity
   * @return periodOfValidity
  **/
  @ApiModelProperty(required = true, value = "")
  public PeriodOfValidityType getPeriodOfValidity() {
    return periodOfValidity;
  }

  public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
    this.periodOfValidity = periodOfValidity;
  }

  public GeoresourcePOSTInputType metadata(CommonMetadataType metadata) {
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

  public GeoresourcePOSTInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourcePOSTInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public GeoresourcePOSTInputType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public GeoresourcePOSTInputType addApplicableTopicsItem(String applicableTopicsItem) {
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

  public GeoresourcePOSTInputType jsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
    return this;
  }

   /**
   * a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.
   * @return jsonSchema
  **/
  @ApiModelProperty(required = true, value = "a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.")
  public String getJsonSchema() {
    return jsonSchema;
  }

  public void setJsonSchema(String jsonSchema) {
    this.jsonSchema = jsonSchema;
  }

  public GeoresourcePOSTInputType geoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
    return this;
  }

   /**
   * a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset
   * @return geoJsonString
  **/
  @ApiModelProperty(required = true, value = "a valid GeoJSON string containing the features consisting of a geometry and properties specific to the dataset")
  public String getGeoJsonString() {
    return geoJsonString;
  }

  public void setGeoJsonString(String geoJsonString) {
    this.geoJsonString = geoJsonString;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourcePOSTInputType georesourcePOSTInputType = (GeoresourcePOSTInputType) o;
    return Objects.equals(this.datasetName, georesourcePOSTInputType.datasetName) &&
        Objects.equals(this.periodOfValidity, georesourcePOSTInputType.periodOfValidity) &&
        Objects.equals(this.metadata, georesourcePOSTInputType.metadata) &&
        Objects.equals(this.allowedRoles, georesourcePOSTInputType.allowedRoles) &&
        Objects.equals(this.applicableTopics, georesourcePOSTInputType.applicableTopics) &&
        Objects.equals(this.jsonSchema, georesourcePOSTInputType.jsonSchema) &&
        Objects.equals(this.geoJsonString, georesourcePOSTInputType.geoJsonString);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datasetName, periodOfValidity, metadata, allowedRoles, applicableTopics, jsonSchema, geoJsonString);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePOSTInputType {\n");
    
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
    sb.append("    jsonSchema: ").append(toIndentedString(jsonSchema)).append("\n");
    sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
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

