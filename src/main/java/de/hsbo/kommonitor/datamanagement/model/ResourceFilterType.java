package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ResourceFilterType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class ResourceFilterType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> topicIds = new ArrayList<>();

  @Valid
  private List<String> ids = new ArrayList<>();

  public ResourceFilterType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResourceFilterType(List<String> topicIds, List<String> ids) {
    this.topicIds = topicIds;
    this.ids = ids;
  }

  public ResourceFilterType topicIds(List<String> topicIds) {
    this.topicIds = topicIds;
    return this;
  }

  public ResourceFilterType addTopicIdsItem(String topicIdsItem) {
    if (this.topicIds == null) {
      this.topicIds = new ArrayList<>();
    }
    this.topicIds.add(topicIdsItem);
    return this;
  }

  /**
   * list of topics for which all resources should be filtered
   * @return topicIds
   */
  @NotNull 
  @Schema(name = "topicIds", description = "list of topics for which all resources should be filtered", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicIds")
  public List<String> getTopicIds() {
    return topicIds;
  }

  public void setTopicIds(List<String> topicIds) {
    this.topicIds = topicIds;
  }

  public ResourceFilterType ids(List<String> ids) {
    this.ids = ids;
    return this;
  }

  public ResourceFilterType addIdsItem(String idsItem) {
    if (this.ids == null) {
      this.ids = new ArrayList<>();
    }
    this.ids.add(idsItem);
    return this;
  }

  /**
   * list of resources that should be filtered
   * @return ids
   */
  @NotNull 
  @Schema(name = "ids", description = "list of resources that should be filtered", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ids")
  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceFilterType resourceFilterType = (ResourceFilterType) o;
    return Objects.equals(this.topicIds, resourceFilterType.topicIds) &&
        Objects.equals(this.ids, resourceFilterType.ids);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicIds, ids);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResourceFilterType {\n");
    sb.append("    topicIds: ").append(toIndentedString(topicIds)).append("\n");
    sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
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

