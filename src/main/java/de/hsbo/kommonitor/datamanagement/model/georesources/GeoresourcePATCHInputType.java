package de.hsbo.kommonitor.datamanagement.model.georesources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * GeoresourcePATCHInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2019-02-12T08:08:25.323+01:00")

public class GeoresourcePATCHInputType   {
  @JsonProperty("metadata")
  private CommonMetadataType metadata = null;

  @JsonProperty("allowedRoles")
  
  private List<String> allowedRoles = null;

  @JsonProperty("applicableTopics")
  
  private List<String> applicableTopics = new ArrayList<>();

  @JsonProperty("isPOI")
  private Boolean isPOI = false;

  public GeoresourcePATCHInputType metadata(CommonMetadataType metadata) {
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

  public GeoresourcePATCHInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourcePATCHInputType addAllowedRolesItem(String allowedRolesItem) {
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

  public GeoresourcePATCHInputType applicableTopics(List<String> applicableTopics) {
    this.applicableTopics = applicableTopics;
    return this;
  }

  public GeoresourcePATCHInputType addApplicableTopicsItem(String applicableTopicsItem) {
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

  public GeoresourcePATCHInputType isPOI(Boolean isPOI) {
    this.isPOI = isPOI;
    return this;
  }

   /**
   * boolean value indicating if the dataset contains points of interest
   * @return isPOI
  **/
  @ApiModelProperty(value = "boolean value indicating if the dataset contains points of interest")
  public Boolean isIsPOI() {
    return isPOI;
  }

  public void setIsPOI(Boolean isPOI) {
    this.isPOI = isPOI;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourcePATCHInputType georesourcePATCHInputType = (GeoresourcePATCHInputType) o;
    return Objects.equals(this.metadata, georesourcePATCHInputType.metadata) &&
        Objects.equals(this.allowedRoles, georesourcePATCHInputType.allowedRoles) &&
        Objects.equals(this.applicableTopics, georesourcePATCHInputType.applicableTopics) &&
        Objects.equals(this.isPOI, georesourcePATCHInputType.isPOI);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadata, allowedRoles, applicableTopics, isPOI);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourcePATCHInputType {\n");
    
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    applicableTopics: ").append(toIndentedString(applicableTopics)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
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

