package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ServiceResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.WmsConnectionInfoType;
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
 * WebServiceCreationType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class WebServiceCreationType implements Serializable {

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

  private Boolean isPublic;

  private String ownerId;

  @Valid
  private List<String> permissions = new ArrayList<>();

  public WebServiceCreationType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WebServiceCreationType(String contact, WmsConnectionInfoType connectionDetails, String datasource, String description, String title, String topicReference, Boolean isPublic, String ownerId, List<String> permissions) {
    this.contact = contact;
    this.connectionDetails = connectionDetails;
    this.datasource = datasource;
    this.description = description;
    this.title = title;
    this.topicReference = topicReference;
    this.isPublic = isPublic;
    this.ownerId = ownerId;
    this.permissions = permissions;
  }

  public WebServiceCreationType contact(String contact) {
    this.contact = contact;
    return this;
  }

  /**
   * contact details where additional information can be achieved
   * @return contact
   */
  @NotNull 
  @Schema(name = "contact", description = "contact details where additional information can be achieved", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contact")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public WebServiceCreationType connectionDetails(WmsConnectionInfoType connectionDetails) {
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

  public void setConnectionDetails(WmsConnectionInfoType connectionDetails) {
    this.connectionDetails = connectionDetails;
  }

  public WebServiceCreationType databasis(@Nullable String databasis) {
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

  public void setDatabasis(@Nullable String databasis) {
    this.databasis = databasis;
  }

  public WebServiceCreationType datasource(String datasource) {
    this.datasource = datasource;
    return this;
  }

  /**
   * information about the origin/source of the web service
   * @return datasource
   */
  @NotNull 
  @Schema(name = "datasource", description = "information about the origin/source of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datasource")
  public String getDatasource() {
    return datasource;
  }

  public void setDatasource(String datasource) {
    this.datasource = datasource;
  }

  public WebServiceCreationType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of the web service
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "description of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public WebServiceCreationType note(@Nullable String note) {
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

  public void setNote(@Nullable String note) {
    this.note = note;
  }

  public WebServiceCreationType serviceResource(@Nullable ServiceResourceEnum serviceResource) {
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

  public void setServiceResource(@Nullable ServiceResourceEnum serviceResource) {
    this.serviceResource = serviceResource;
  }

  public WebServiceCreationType title(String title) {
    this.title = title;
    return this;
  }

  /**
   * title of the web service
   * @return title
   */
  @NotNull 
  @Schema(name = "title", description = "title of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public WebServiceCreationType topicReference(String topicReference) {
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

  public WebServiceCreationType isPublic(Boolean isPublic) {
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

  public WebServiceCreationType ownerId(String ownerId) {
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

  public WebServiceCreationType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public WebServiceCreationType addPermissionsItem(String permissionsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WebServiceCreationType webServiceCreationType = (WebServiceCreationType) o;
    return Objects.equals(this.contact, webServiceCreationType.contact) &&
        Objects.equals(this.connectionDetails, webServiceCreationType.connectionDetails) &&
        Objects.equals(this.databasis, webServiceCreationType.databasis) &&
        Objects.equals(this.datasource, webServiceCreationType.datasource) &&
        Objects.equals(this.description, webServiceCreationType.description) &&
        Objects.equals(this.note, webServiceCreationType.note) &&
        Objects.equals(this.serviceResource, webServiceCreationType.serviceResource) &&
        Objects.equals(this.title, webServiceCreationType.title) &&
        Objects.equals(this.topicReference, webServiceCreationType.topicReference) &&
        Objects.equals(this.isPublic, webServiceCreationType.isPublic) &&
        Objects.equals(this.ownerId, webServiceCreationType.ownerId) &&
        Objects.equals(this.permissions, webServiceCreationType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contact, connectionDetails, databasis, datasource, description, note, serviceResource, title, topicReference, isPublic, ownerId, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WebServiceCreationType {\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    connectionDetails: ").append(toIndentedString(connectionDetails)).append("\n");
    sb.append("    databasis: ").append(toIndentedString(databasis)).append("\n");
    sb.append("    datasource: ").append(toIndentedString(datasource)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    serviceResource: ").append(toIndentedString(serviceResource)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

