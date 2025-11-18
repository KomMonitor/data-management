package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.TopicOrderModeEnum;
import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
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
 * TopicDisplayOrderModeOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-18T13:18:46.199354100+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class TopicDisplayOrderModeOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private TopicResourceEnum topicResource;

  private TopicOrderModeEnum orderMode;

  public TopicDisplayOrderModeOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TopicDisplayOrderModeOverviewType(TopicResourceEnum topicResource, TopicOrderModeEnum orderMode) {
    this.topicResource = topicResource;
    this.orderMode = orderMode;
  }

  public TopicDisplayOrderModeOverviewType topicResource(TopicResourceEnum topicResource) {
    this.topicResource = topicResource;
    return this;
  }

  /**
   * Get topicResource
   * @return topicResource
   */
  @NotNull @Valid 
  @Schema(name = "topicResource", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicResource")
  public TopicResourceEnum getTopicResource() {
    return topicResource;
  }

  public void setTopicResource(TopicResourceEnum topicResource) {
    this.topicResource = topicResource;
  }

  public TopicDisplayOrderModeOverviewType orderMode(TopicOrderModeEnum orderMode) {
    this.orderMode = orderMode;
    return this;
  }

  /**
   * Get orderMode
   * @return orderMode
   */
  @NotNull @Valid 
  @Schema(name = "orderMode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderMode")
  public TopicOrderModeEnum getOrderMode() {
    return orderMode;
  }

  public void setOrderMode(TopicOrderModeEnum orderMode) {
    this.orderMode = orderMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopicDisplayOrderModeOverviewType topicDisplayOrderModeOverviewType = (TopicDisplayOrderModeOverviewType) o;
    return Objects.equals(this.topicResource, topicDisplayOrderModeOverviewType.topicResource) &&
        Objects.equals(this.orderMode, topicDisplayOrderModeOverviewType.orderMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicResource, orderMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicDisplayOrderModeOverviewType {\n");
    sb.append("    topicResource: ").append(toIndentedString(topicResource)).append("\n");
    sb.append("    orderMode: ").append(toIndentedString(orderMode)).append("\n");
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

