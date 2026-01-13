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
 * WmsConnectionInfoType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class WmsConnectionInfoType implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String id;

  private ServiceTypeEnum serviceType;

  private String baseUrl;

  private String layerName;

  public WmsConnectionInfoType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WmsConnectionInfoType(ServiceTypeEnum serviceType, String baseUrl, String layerName) {
    this.serviceType = serviceType;
    this.baseUrl = baseUrl;
    this.layerName = layerName;
  }

  public WmsConnectionInfoType id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * the unique identifier of the web service
   * @return id
   */
  
  @Schema(name = "id", description = "the unique identifier of the web service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public WmsConnectionInfoType serviceType(ServiceTypeEnum serviceType) {
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

  public WmsConnectionInfoType baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  /**
   * the base URL of the WMS web service
   * @return baseUrl
   */
  @NotNull 
  @Schema(name = "baseUrl", description = "the base URL of the WMS web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseUrl")
  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public WmsConnectionInfoType layerName(String layerName) {
    this.layerName = layerName;
    return this;
  }

  /**
   * the layer Name of the WMS web service
   * @return layerName
   */
  @NotNull 
  @Schema(name = "layerName", description = "the layer Name of the WMS web service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("layerName")
  public String getLayerName() {
    return layerName;
  }

  public void setLayerName(String layerName) {
    this.layerName = layerName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WmsConnectionInfoType wmsConnectionInfoType = (WmsConnectionInfoType) o;
    return Objects.equals(this.id, wmsConnectionInfoType.id) &&
        Objects.equals(this.serviceType, wmsConnectionInfoType.serviceType) &&
        Objects.equals(this.baseUrl, wmsConnectionInfoType.baseUrl) &&
        Objects.equals(this.layerName, wmsConnectionInfoType.layerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, serviceType, baseUrl, layerName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WmsConnectionInfoType {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    serviceType: ").append(toIndentedString(serviceType)).append("\n");
    sb.append("    baseUrl: ").append(toIndentedString(baseUrl)).append("\n");
    sb.append("    layerName: ").append(toIndentedString(layerName)).append("\n");
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

