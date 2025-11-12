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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-12T17:40:10.146905100+01:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class TopicPATCHDisplayOrderInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer displayOrder;

  private @Nullable String indicatorId;

  public TopicPATCHDisplayOrderInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TopicPATCHDisplayOrderInputType(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public TopicPATCHDisplayOrderInputType displayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * the new displayOrder value
   * @return displayOrder
   */
  @NotNull 
  @Schema(name = "displayOrder", example = "0", description = "the new displayOrder value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("displayOrder")
  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  public TopicPATCHDisplayOrderInputType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

  /**
   * unique ID of the associated sub topic
   * @return indicatorId
   */
  
  @Schema(name = "indicatorId", description = "unique ID of the associated sub topic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorId")
  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
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
        Objects.equals(this.indicatorId, topicPATCHDisplayOrderInputType.indicatorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(displayOrder, indicatorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicPATCHDisplayOrderInputType {\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    indicatorId: ").append(toIndentedString(indicatorId)).append("\n");
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

