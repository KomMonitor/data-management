package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
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
 * WebServiceOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-08T15:24:50.334003500+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class WebServiceOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String contact;

  private @Nullable WmsConnectionInfoType connectionDetails;

  private @Nullable String databasis;

  private String datasource;

  private String description;

  private String id;

  private Boolean isPublic;

  private @Nullable String note;

  private String ownerId;

  @Valid
  private List<String> permissions = new ArrayList<>();

  private @Nullable ServiceResourceEnum serviceResource;

  private @Nullable String title;

  private String topicReference;

  @Valid
  private List<PermissionLevelType> userPermissions = new ArrayList<>();

  public WebServiceOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WebServiceOverviewType(String contact, String datasource, String description, String id, Boolean isPublic, String ownerId, String topicReference) {
    this.contact = contact;
    this.datasource = datasource;
    this.description = description;
    this.id = id;
    this.isPublic = isPublic;
    this.ownerId = ownerId;
    this.topicReference = topicReference;
  }

  public WebServiceOverviewType contact(String contact) {
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

  public WebServiceOverviewType connectionDetails(WmsConnectionInfoType connectionDetails) {
    this.connectionDetails = connectionDetails;
    return this;
  }

  /**
   * Get connectionDetails
   * @return connectionDetails
   */
  @Valid 
  @Schema(name = "connectionDetails", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connectionDetails")
  public WmsConnectionInfoType getConnectionDetails() {
    return connectionDetails;
  }

  public void setConnectionDetails(WmsConnectionInfoType connectionDetails) {
    this.connectionDetails = connectionDetails;
  }

  public WebServiceOverviewType databasis(String databasis) {
    this.databasis = databasis;
    return this;
  }

  /**
   * information about data used as a basis to generate the web service
   * @return databasis
   */
  
  @Schema(name = "databasis", description = "information about data used as a basis to generate the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("databasis")
  public String getDatabasis() {
    return databasis;
  }

  public void setDatabasis(String databasis) {
    this.databasis = databasis;
  }

  public WebServiceOverviewType datasource(String datasource) {
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

  public WebServiceOverviewType description(String description) {
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

  public WebServiceOverviewType id(String id) {
    this.id = id;
    return this;
  }

  /**
   * the unique identifier of the web service
   * @return id
   */
  @NotNull 
  @Schema(name = "id", description = "the unique identifier of the web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public WebServiceOverviewType isPublic(Boolean isPublic) {
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

  public WebServiceOverviewType note(String note) {
    this.note = note;
    return this;
  }

  /**
   * an optional note with background information about the web service
   * @return note
   */
  
  @Schema(name = "note", description = "an optional note with background information about the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public WebServiceOverviewType ownerId(String ownerId) {
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

  public WebServiceOverviewType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public WebServiceOverviewType addPermissionsItem(String permissionsItem) {
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
  
  @Schema(name = "permissions", description = "list of permissions on this entity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public WebServiceOverviewType serviceResource(ServiceResourceEnum serviceResource) {
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
  public ServiceResourceEnum getServiceResource() {
    return serviceResource;
  }

  public void setServiceResource(ServiceResourceEnum serviceResource) {
    this.serviceResource = serviceResource;
  }

  public WebServiceOverviewType title(String title) {
    this.title = title;
    return this;
  }

  /**
   * title of the web service
   * @return title
   */
  
  @Schema(name = "title", description = "title of the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public WebServiceOverviewType topicReference(String topicReference) {
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

  public WebServiceOverviewType userPermissions(List<PermissionLevelType> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public WebServiceOverviewType addUserPermissionsItem(PermissionLevelType userPermissionsItem) {
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
  @Schema(name = "userPermissions", description = "list of permissions that are effective on this dataset for the current user ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    WebServiceOverviewType webServiceOverviewType = (WebServiceOverviewType) o;
    return Objects.equals(this.contact, webServiceOverviewType.contact) &&
        Objects.equals(this.connectionDetails, webServiceOverviewType.connectionDetails) &&
        Objects.equals(this.databasis, webServiceOverviewType.databasis) &&
        Objects.equals(this.datasource, webServiceOverviewType.datasource) &&
        Objects.equals(this.description, webServiceOverviewType.description) &&
        Objects.equals(this.id, webServiceOverviewType.id) &&
        Objects.equals(this.isPublic, webServiceOverviewType.isPublic) &&
        Objects.equals(this.note, webServiceOverviewType.note) &&
        Objects.equals(this.ownerId, webServiceOverviewType.ownerId) &&
        Objects.equals(this.permissions, webServiceOverviewType.permissions) &&
        Objects.equals(this.serviceResource, webServiceOverviewType.serviceResource) &&
        Objects.equals(this.title, webServiceOverviewType.title) &&
        Objects.equals(this.topicReference, webServiceOverviewType.topicReference) &&
        Objects.equals(this.userPermissions, webServiceOverviewType.userPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contact, connectionDetails, databasis, datasource, description, id, isPublic, note, ownerId, permissions, serviceResource, title, topicReference, userPermissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WebServiceOverviewType {\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    connectionDetails: ").append(toIndentedString(connectionDetails)).append("\n");
    sb.append("    databasis: ").append(toIndentedString(databasis)).append("\n");
    sb.append("    datasource: ").append(toIndentedString(datasource)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    serviceResource: ").append(toIndentedString(serviceResource)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
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

