package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * WebServiceCreationType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class WebServiceCreationType extends WebServiceType implements Serializable {

  private static final long serialVersionUID = 1L;

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
  public WebServiceCreationType(Boolean isPublic, String ownerId, List<String> permissions, String contact, WmsConnectionInfoType connectionDetails, String datasource, String description, String title, String topicReference) {
    super(contact, connectionDetails, datasource, description, title, topicReference);
    this.isPublic = isPublic;
    this.ownerId = ownerId;
    this.permissions = permissions;
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


  public WebServiceCreationType contact(String contact) {
    super.contact(contact);
    return this;
  }

  public WebServiceCreationType connectionDetails(WmsConnectionInfoType connectionDetails) {
    super.connectionDetails(connectionDetails);
    return this;
  }

  public WebServiceCreationType databasis(String databasis) {
    super.databasis(databasis);
    return this;
  }

  public WebServiceCreationType datasource(String datasource) {
    super.datasource(datasource);
    return this;
  }

  public WebServiceCreationType description(String description) {
    super.description(description);
    return this;
  }

  public WebServiceCreationType note(String note) {
    super.note(note);
    return this;
  }

  public WebServiceCreationType serviceResource(ServiceResourceEnum serviceResource) {
    super.serviceResource(serviceResource);
    return this;
  }

  public WebServiceCreationType title(String title) {
    super.title(title);
    return this;
  }

  public WebServiceCreationType topicReference(String topicReference) {
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
    WebServiceCreationType webServiceCreationType = (WebServiceCreationType) o;
    return Objects.equals(this.isPublic, webServiceCreationType.isPublic) &&
        Objects.equals(this.ownerId, webServiceCreationType.ownerId) &&
        Objects.equals(this.permissions, webServiceCreationType.permissions) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isPublic, ownerId, permissions, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WebServiceCreationType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

