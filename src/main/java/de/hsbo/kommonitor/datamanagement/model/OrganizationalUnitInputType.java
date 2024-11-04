package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * organizational unit (group)
 */

@Schema(name = "OrganizationalUnitInputType", description = "organizational unit (group)")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-04T15:13:26.315379200+01:00[Europe/Berlin]")
public class OrganizationalUnitInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String organizationalUnitId;

  private String name;

  private Boolean mandant;

  private String keycloakId;

  private String contact;

  private String description;

  private String parentId;

  public OrganizationalUnitInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrganizationalUnitInputType(String name, Boolean mandant, String keycloakId, String contact) {
    this.name = name;
    this.mandant = mandant;
    this.keycloakId = keycloakId;
    this.contact = contact;
  }

  public OrganizationalUnitInputType organizationalUnitId(String organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
    return this;
  }

  /**
   * unique id of this organizational Unit
   * @return organizationalUnitId
  */
  
  @Schema(name = "organizationalUnitId", description = "unique id of this organizational Unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organizationalUnitId")
  public String getOrganizationalUnitId() {
    return organizationalUnitId;
  }

  public void setOrganizationalUnitId(String organizationalUnitId) {
    this.organizationalUnitId = organizationalUnitId;
  }

  public OrganizationalUnitInputType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of this organizational Unit
   * @return name
  */
  @NotNull 
  @Schema(name = "name", description = "name of this organizational Unit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrganizationalUnitInputType mandant(Boolean mandant) {
    this.mandant = mandant;
    return this;
  }

  /**
   * flag whether this unit is an autonomous mandant
   * @return mandant
  */
  @NotNull 
  @Schema(name = "mandant", description = "flag whether this unit is an autonomous mandant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mandant")
  public Boolean getMandant() {
    return mandant;
  }

  public void setMandant(Boolean mandant) {
    this.mandant = mandant;
  }

  public OrganizationalUnitInputType keycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
    return this;
  }

  /**
   * uuid of the corresponding Keycloak group
   * @return keycloakId
  */
  @NotNull 
  @Schema(name = "keycloakId", description = "uuid of the corresponding Keycloak group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("keycloakId")
  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public OrganizationalUnitInputType contact(String contact) {
    this.contact = contact;
    return this;
  }

  /**
   * contact information of the person responsible for this group
   * @return contact
  */
  @NotNull 
  @Schema(name = "contact", description = "contact information of the person responsible for this group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contact")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public OrganizationalUnitInputType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * additional information
   * @return description
  */
  
  @Schema(name = "description", description = "additional information", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public OrganizationalUnitInputType parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * uuid of the parent group
   * @return parentId
  */
  
  @Schema(name = "parentId", description = "uuid of the parent group", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parentId")
  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationalUnitInputType organizationalUnitInputType = (OrganizationalUnitInputType) o;
    return Objects.equals(this.organizationalUnitId, organizationalUnitInputType.organizationalUnitId) &&
        Objects.equals(this.name, organizationalUnitInputType.name) &&
        Objects.equals(this.mandant, organizationalUnitInputType.mandant) &&
        Objects.equals(this.keycloakId, organizationalUnitInputType.keycloakId) &&
        Objects.equals(this.contact, organizationalUnitInputType.contact) &&
        Objects.equals(this.description, organizationalUnitInputType.description) &&
        Objects.equals(this.parentId, organizationalUnitInputType.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organizationalUnitId, name, mandant, keycloakId, contact, description, parentId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationalUnitInputType {\n");
    sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mandant: ").append(toIndentedString(mandant)).append("\n");
    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
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

