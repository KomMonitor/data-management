package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.ConnectionInfoType;
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
public class WmsConnectionInfoType extends ConnectionInfoType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String baseUrl;

  private String layerName;

  public WmsConnectionInfoType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WmsConnectionInfoType(String baseUrl, String layerName, ServiceTypeEnum serviceType) {
    super(serviceType);
    this.baseUrl = baseUrl;
    this.layerName = layerName;
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


  public WmsConnectionInfoType id(String id) {
    super.id(id);
    return this;
  }

  public WmsConnectionInfoType serviceType(ServiceTypeEnum serviceType) {
    super.serviceType(serviceType);
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
    WmsConnectionInfoType wmsConnectionInfoType = (WmsConnectionInfoType) o;
    return Objects.equals(this.baseUrl, wmsConnectionInfoType.baseUrl) &&
        Objects.equals(this.layerName, wmsConnectionInfoType.layerName) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseUrl, layerName, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WmsConnectionInfoType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

