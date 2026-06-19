package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.ServiceResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.WebServiceType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class WebServiceOverviewType extends WebServiceType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private @Nullable Boolean isPublic;

  private @Nullable String ownerId;

  @Valid
  private List<String> permissions = new ArrayList<>();

  @Valid
  private List<PermissionLevelType> userPermissions = new ArrayList<>();

  public WebServiceOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WebServiceOverviewType(String id, String contact, WmsConnectionInfoType connectionDetails, String datasource, String description, String title, String topicReference) {
    super(contact, connectionDetails, datasource, description, title, topicReference);
    this.id = id;
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

  public WebServiceOverviewType isPublic(@Nullable Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * flag whether the resource is publicly accessible
   * @return isPublic
   */
  
  @Schema(name = "isPublic", description = "flag whether the resource is publicly accessible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPublic")
  public @Nullable Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(@Nullable Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public WebServiceOverviewType ownerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * identifier of the owning group
   * @return ownerId
   */
  
  @Schema(name = "ownerId", description = "identifier of the owning group", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public @Nullable String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(@Nullable String ownerId) {
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


  public WebServiceOverviewType contact(String contact) {
    super.contact(contact);
    return this;
  }

  public WebServiceOverviewType connectionDetails(WmsConnectionInfoType connectionDetails) {
    super.connectionDetails(connectionDetails);
    return this;
  }

  public WebServiceOverviewType databasis(String databasis) {
    super.databasis(databasis);
    return this;
  }

  public WebServiceOverviewType datasource(String datasource) {
    super.datasource(datasource);
    return this;
  }

  public WebServiceOverviewType description(String description) {
    super.description(description);
    return this;
  }

  public WebServiceOverviewType note(String note) {
    super.note(note);
    return this;
  }

  public WebServiceOverviewType serviceResource(ServiceResourceEnum serviceResource) {
    super.serviceResource(serviceResource);
    return this;
  }

  public WebServiceOverviewType title(String title) {
    super.title(title);
    return this;
  }

  public WebServiceOverviewType topicReference(String topicReference) {
    super.topicReference(topicReference);
    return this;
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
    return Objects.equals(this.id, webServiceOverviewType.id) &&
        Objects.equals(this.isPublic, webServiceOverviewType.isPublic) &&
        Objects.equals(this.ownerId, webServiceOverviewType.ownerId) &&
        Objects.equals(this.permissions, webServiceOverviewType.permissions) &&
        Objects.equals(this.userPermissions, webServiceOverviewType.userPermissions) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, isPublic, ownerId, permissions, userPermissions, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WebServiceOverviewType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

