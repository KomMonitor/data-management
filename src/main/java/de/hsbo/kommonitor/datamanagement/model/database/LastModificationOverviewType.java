package de.hsbo.kommonitor.datamanagement.model.database;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * LastModificationOverviewType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-09-19T19:11:37.856Z")


public class LastModificationOverviewType   {
  @JsonProperty("topics")
  private OffsetDateTime topics = null;

  @JsonProperty("spatial-units")
  private OffsetDateTime spatialUnits = null;

  @JsonProperty("indicators")
  private OffsetDateTime indicators = null;

  @JsonProperty("georesources")
  private OffsetDateTime georesources = null;

  @JsonProperty("process-scripts")
  private OffsetDateTime processScripts = null;

  @JsonProperty("roles")
  private OffsetDateTime roles = null;

  public LastModificationOverviewType topics(OffsetDateTime topics) {
    this.topics = topics;
    return this;
  }

  /**
   * Get topics
   * @return topics
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getTopics() {
    return topics;
  }

  public void setTopics(OffsetDateTime topics) {
    this.topics = topics;
  }

  public LastModificationOverviewType spatialUnits(OffsetDateTime spatialUnits) {
    this.spatialUnits = spatialUnits;
    return this;
  }

  /**
   * Get spatialUnits
   * @return spatialUnits
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getSpatialUnits() {
    return spatialUnits;
  }

  public void setSpatialUnits(OffsetDateTime spatialUnits) {
    this.spatialUnits = spatialUnits;
  }

  public LastModificationOverviewType indicators(OffsetDateTime indicators) {
    this.indicators = indicators;
    return this;
  }

  /**
   * Get indicators
   * @return indicators
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getIndicators() {
    return indicators;
  }

  public void setIndicators(OffsetDateTime indicators) {
    this.indicators = indicators;
  }

  public LastModificationOverviewType georesources(OffsetDateTime georesources) {
    this.georesources = georesources;
    return this;
  }

  /**
   * Get georesources
   * @return georesources
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getGeoresources() {
    return georesources;
  }

  public void setGeoresources(OffsetDateTime georesources) {
    this.georesources = georesources;
  }

  public LastModificationOverviewType processScripts(OffsetDateTime processScripts) {
    this.processScripts = processScripts;
    return this;
  }

  /**
   * Get processScripts
   * @return processScripts
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getProcessScripts() {
    return processScripts;
  }

  public void setProcessScripts(OffsetDateTime processScripts) {
    this.processScripts = processScripts;
  }

  public LastModificationOverviewType roles(OffsetDateTime roles) {
    this.roles = roles;
    return this;
  }

  /**
   * Get roles
   * @return roles
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getRoles() {
    return roles;
  }

  public void setRoles(OffsetDateTime roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LastModificationOverviewType lastModificationOverviewType = (LastModificationOverviewType) o;
    return Objects.equals(this.topics, lastModificationOverviewType.topics) &&
        Objects.equals(this.spatialUnits, lastModificationOverviewType.spatialUnits) &&
        Objects.equals(this.indicators, lastModificationOverviewType.indicators) &&
        Objects.equals(this.georesources, lastModificationOverviewType.georesources) &&
        Objects.equals(this.processScripts, lastModificationOverviewType.processScripts) &&
        Objects.equals(this.roles, lastModificationOverviewType.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topics, spatialUnits, indicators, georesources, processScripts, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LastModificationOverviewType {\n");
    
    sb.append("    topics: ").append(toIndentedString(topics)).append("\n");
    sb.append("    spatialUnits: ").append(toIndentedString(spatialUnits)).append("\n");
    sb.append("    indicators: ").append(toIndentedString(indicators)).append("\n");
    sb.append("    georesources: ").append(toIndentedString(georesources)).append("\n");
    sb.append("    processScripts: ").append(toIndentedString(processScripts)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

