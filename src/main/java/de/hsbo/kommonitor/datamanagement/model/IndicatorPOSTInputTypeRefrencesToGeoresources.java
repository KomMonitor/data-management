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
 * IndicatorPOSTInputTypeRefrencesToGeoresources
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T15:24:58.815569400+01:00[Europe/Berlin]")
public class IndicatorPOSTInputTypeRefrencesToGeoresources implements Serializable {

  private static final long serialVersionUID = 1L;

  private String georesourceId;

  private String referenceDescription;

  public IndicatorPOSTInputTypeRefrencesToGeoresources georesourceId(String georesourceId) {
    this.georesourceId = georesourceId;
    return this;
  }

  /**
   * identifier of the referenced georesource
   * @return georesourceId
  */
  
  @Schema(name = "georesourceId", description = "identifier of the referenced georesource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("georesourceId")
  public String getGeoresourceId() {
    return georesourceId;
  }

  public void setGeoresourceId(String georesourceId) {
    this.georesourceId = georesourceId;
  }

  public IndicatorPOSTInputTypeRefrencesToGeoresources referenceDescription(String referenceDescription) {
    this.referenceDescription = referenceDescription;
    return this;
  }

  /**
   * short description of how the georesource is referenced to the indicator
   * @return referenceDescription
  */
  
  @Schema(name = "referenceDescription", description = "short description of how the georesource is referenced to the indicator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceDescription")
  public String getReferenceDescription() {
    return referenceDescription;
  }

  public void setReferenceDescription(String referenceDescription) {
    this.referenceDescription = referenceDescription;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPOSTInputTypeRefrencesToGeoresources indicatorPOSTInputTypeRefrencesToGeoresources = (IndicatorPOSTInputTypeRefrencesToGeoresources) o;
    return Objects.equals(this.georesourceId, indicatorPOSTInputTypeRefrencesToGeoresources.georesourceId) &&
        Objects.equals(this.referenceDescription, indicatorPOSTInputTypeRefrencesToGeoresources.referenceDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(georesourceId, referenceDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPOSTInputTypeRefrencesToGeoresources {\n");
    sb.append("    georesourceId: ").append(toIndentedString(georesourceId)).append("\n");
    sb.append("    referenceDescription: ").append(toIndentedString(referenceDescription)).append("\n");
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

