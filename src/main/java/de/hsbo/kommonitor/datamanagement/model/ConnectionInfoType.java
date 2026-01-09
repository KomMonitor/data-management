package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ServiceTypeEnum;
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
 * ConnectionInfoType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-09T14:32:27.729735900+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class ConnectionInfoType implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String id;

  private ServiceTypeEnum serviceType;

  public ConnectionInfoType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ConnectionInfoType(ServiceTypeEnum serviceType) {
    this.serviceType = serviceType;
  }

  public ConnectionInfoType id(String id) {
    this.id = id;
    return this;
  }

  /**
   * the unique identifier of the web service
   * @return id
   */
  
  @Schema(name = "id", description = "the unique identifier of the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ConnectionInfoType serviceType(ServiceTypeEnum serviceType) {
    this.serviceType = serviceType;
    return this;
  }

  /**
   * Get serviceType
   * @return serviceType
   */
  @NotNull @Valid 
  @Schema(name = "serviceType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("serviceType")
  public ServiceTypeEnum getServiceType() {
    return serviceType;
  }

  public void setServiceType(ServiceTypeEnum serviceType) {
    this.serviceType = serviceType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectionInfoType connectionInfoType = (ConnectionInfoType) o;
    return Objects.equals(this.id, connectionInfoType.id) &&
        Objects.equals(this.serviceType, connectionInfoType.serviceType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectionInfoType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    serviceType: ").append(toIndentedString(serviceType)).append("\n");
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

