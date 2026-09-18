package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ServiceResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.WmsConnectionInfoType;
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
 * WebServiceType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class WebServiceType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String contact;

  private WmsConnectionInfoType connectionDetails;

  private @Nullable String databasis;

  private String datasource;

  private String description;

  private @Nullable String note;

  private @Nullable ServiceResourceEnum serviceResource;

  private String title;

  private String topicReference;

  public WebServiceType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WebServiceType(String contact, WmsConnectionInfoType connectionDetails, String datasource, String description, String title, String topicReference) {
    this.contact = contact;
    this.connectionDetails = connectionDetails;
    this.datasource = datasource;
    this.description = description;
    this.title = title;
    this.topicReference = topicReference;
  }

  public WebServiceType contact(String contact) {
    this.contact = contact;
    return this;
  }

  /**
   * contact details where additional information can be achieved
   * @return contact
   */
  @NotNull 
  @Schema(name = "contact", example = "KomMonitor Team, info@kommonitor.de", description = "contact details where additional information can be achieved", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contact")
  public String getContact() {
    return contact;
  }

  @JsonProperty("contact")
  public void setContact(String contact) {
    this.contact = contact;
  }

  public WebServiceType connectionDetails(WmsConnectionInfoType connectionDetails) {
    this.connectionDetails = connectionDetails;
    return this;
  }

  /**
   * Get connectionDetails
   * @return connectionDetails
   */
  @NotNull @Valid 
  @Schema(name = "connectionDetails", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("connectionDetails")
  public WmsConnectionInfoType getConnectionDetails() {
    return connectionDetails;
  }

  @JsonProperty("connectionDetails")
  public void setConnectionDetails(WmsConnectionInfoType connectionDetails) {
    this.connectionDetails = connectionDetails;
  }

  public WebServiceType databasis(@Nullable String databasis) {
    this.databasis = databasis;
    return this;
  }

  /**
   * information about data used as a basis to generate the web service
   * @return databasis
   */
  
  @Schema(name = "databasis", description = "information about data used as a basis to generate the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("databasis")
  public @Nullable String getDatabasis() {
    return databasis;
  }

  @JsonProperty("databasis")
  public void setDatabasis(@Nullable String databasis) {
    this.databasis = databasis;
  }

  public WebServiceType datasource(String datasource) {
    this.datasource = datasource;
    return this;
  }

  /**
   * information about the origin/source of the web service
   * @return datasource
   */
  @NotNull 
  @Schema(name = "datasource", example = "Municipal education department", description = "information about the origin/source of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datasource")
  public String getDatasource() {
    return datasource;
  }

  @JsonProperty("datasource")
  public void setDatasource(String datasource) {
    this.datasource = datasource;
  }

  public WebServiceType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of the web service
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "WMS serving the school locations.", description = "description of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  public WebServiceType note(@Nullable String note) {
    this.note = note;
    return this;
  }

  /**
   * an optional note with background information about the web service
   * @return note
   */
  
  @Schema(name = "note", description = "an optional note with background information about the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public @Nullable String getNote() {
    return note;
  }

  @JsonProperty("note")
  public void setNote(@Nullable String note) {
    this.note = note;
  }

  public WebServiceType serviceResource(@Nullable ServiceResourceEnum serviceResource) {
    this.serviceResource = serviceResource;
    return this;
  }

  /**
   * Get serviceResource
   * @return serviceResource
   */
  @Valid 
  @Schema(name = "serviceResource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceResource")
  public @Nullable ServiceResourceEnum getServiceResource() {
    return serviceResource;
  }

  @JsonProperty("serviceResource")
  public void setServiceResource(@Nullable ServiceResourceEnum serviceResource) {
    this.serviceResource = serviceResource;
  }

  public WebServiceType title(String title) {
    this.title = title;
    return this;
  }

  /**
   * title of the web service
   * @return title
   */
  @NotNull 
  @Schema(name = "title", example = "Schools WMS", description = "title of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  public WebServiceType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity 
   * @return topicReference
   */
  @NotNull 
  @Schema(name = "topicReference", example = "education", description = "id of the last topic hierarchy entity ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicReference")
  public String getTopicReference() {
    return topicReference;
  }

  @JsonProperty("topicReference")
  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WebServiceType webServiceType = (WebServiceType) o;
    return Objects.equals(this.contact, webServiceType.contact) &&
        Objects.equals(this.connectionDetails, webServiceType.connectionDetails) &&
        Objects.equals(this.databasis, webServiceType.databasis) &&
        Objects.equals(this.datasource, webServiceType.datasource) &&
        Objects.equals(this.description, webServiceType.description) &&
        Objects.equals(this.note, webServiceType.note) &&
        Objects.equals(this.serviceResource, webServiceType.serviceResource) &&
        Objects.equals(this.title, webServiceType.title) &&
        Objects.equals(this.topicReference, webServiceType.topicReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contact, connectionDetails, databasis, datasource, description, note, serviceResource, title, topicReference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WebServiceType {\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    connectionDetails: ").append(toIndentedString(connectionDetails)).append("\n");
    sb.append("    databasis: ").append(toIndentedString(databasis)).append("\n");
    sb.append("    datasource: ").append(toIndentedString(datasource)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    serviceResource: ").append(toIndentedString(serviceResource)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

