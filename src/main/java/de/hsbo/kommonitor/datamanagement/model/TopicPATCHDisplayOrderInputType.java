package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TopicPATCHDisplayOrderInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-14T13:40:25.807350800+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class TopicPATCHDisplayOrderInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer displayOrder;

  private String topicId;

  public TopicPATCHDisplayOrderInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TopicPATCHDisplayOrderInputType(Integer displayOrder, String topicId) {
    this.displayOrder = displayOrder;
    this.topicId = topicId;
  }

  public TopicPATCHDisplayOrderInputType displayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * the new display order value
   * @return displayOrder
   */
  @NotNull 
  @Schema(name = "displayOrder", example = "0", description = "the new display order value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("displayOrder")
  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public TopicPATCHDisplayOrderInputType topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

  /**
   * unique ID of the associated sub topic
   * @return topicId
   */
  @NotNull 
  @Schema(name = "topicId", description = "unique ID of the associated sub topic", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicId")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopicPATCHDisplayOrderInputType topicPATCHDisplayOrderInputType = (TopicPATCHDisplayOrderInputType) o;
    return Objects.equals(this.displayOrder, topicPATCHDisplayOrderInputType.displayOrder) &&
        Objects.equals(this.topicId, topicPATCHDisplayOrderInputType.topicId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(displayOrder, topicId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicPATCHDisplayOrderInputType {\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
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

