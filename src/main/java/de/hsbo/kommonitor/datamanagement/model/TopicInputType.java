package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.TopicResourceEnum;
import de.hsbo.kommonitor.datamanagement.model.TopicTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TopicInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class TopicInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid TopicInputType> subTopics;

  private String topicDescription;

  private String topicId;

  private String topicName;

  private TopicResourceEnum topicResource;

  private TopicTypeEnum topicType;

  public TopicInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TopicInputType(String topicDescription, String topicName, TopicTypeEnum topicType) {
    this.topicDescription = topicDescription;
    this.topicName = topicName;
    this.topicType = topicType;
  }

  public TopicInputType subTopics(List<@Valid TopicInputType> subTopics) {
    this.subTopics = subTopics;
    return this;
  }

  public TopicInputType addSubTopicsItem(TopicInputType subTopicsItem) {
    if (this.subTopics == null) {
      this.subTopics = new ArrayList<>();
    }
    this.subTopics.add(subTopicsItem);
    return this;
  }

  /**
   * optional list of subTopics
   * @return subTopics
  */
  @Valid 
  @Schema(name = "subTopics", description = "optional list of subTopics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subTopics")
  public List<@Valid TopicInputType> getSubTopics() {
    return subTopics;
  }

  public void setSubTopics(List<@Valid TopicInputType> subTopics) {
    this.subTopics = subTopics;
  }

  public TopicInputType topicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
    return this;
  }

  /**
   * short description of the topic
   * @return topicDescription
  */
  @NotNull 
  @Schema(name = "topicDescription", description = "short description of the topic", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicDescription")
  public String getTopicDescription() {
    return topicDescription;
  }

  public void setTopicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
  }

  public TopicInputType topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

  /**
   * the topic identifier
   * @return topicId
  */
  
  @Schema(name = "topicId", description = "the topic identifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topicId")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public TopicInputType topicName(String topicName) {
    this.topicName = topicName;
    return this;
  }

  /**
   * the topic name
   * @return topicName
  */
  @NotNull 
  @Schema(name = "topicName", description = "the topic name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicName")
  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public TopicInputType topicResource(TopicResourceEnum topicResource) {
    this.topicResource = topicResource;
    return this;
  }

  /**
   * Get topicResource
   * @return topicResource
  */
  @Valid 
  @Schema(name = "topicResource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topicResource")
  public TopicResourceEnum getTopicResource() {
    return topicResource;
  }

  public void setTopicResource(TopicResourceEnum topicResource) {
    this.topicResource = topicResource;
  }

  public TopicInputType topicType(TopicTypeEnum topicType) {
    this.topicType = topicType;
    return this;
  }

  /**
   * Get topicType
   * @return topicType
  */
  @NotNull @Valid 
  @Schema(name = "topicType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicType")
  public TopicTypeEnum getTopicType() {
    return topicType;
  }

  public void setTopicType(TopicTypeEnum topicType) {
    this.topicType = topicType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopicInputType topicInputType = (TopicInputType) o;
    return Objects.equals(this.subTopics, topicInputType.subTopics) &&
        Objects.equals(this.topicDescription, topicInputType.topicDescription) &&
        Objects.equals(this.topicId, topicInputType.topicId) &&
        Objects.equals(this.topicName, topicInputType.topicName) &&
        Objects.equals(this.topicResource, topicInputType.topicResource) &&
        Objects.equals(this.topicType, topicInputType.topicType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subTopics, topicDescription, topicId, topicName, topicResource, topicType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicInputType {\n");
    sb.append("    subTopics: ").append(toIndentedString(subTopics)).append("\n");
    sb.append("    topicDescription: ").append(toIndentedString(topicDescription)).append("\n");
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    topicResource: ").append(toIndentedString(topicResource)).append("\n");
    sb.append("    topicType: ").append(toIndentedString(topicType)).append("\n");
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

