package de.hsbo.kommonitor.datamanagement.model.topics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * TopicInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2020-03-08T20:17:51.649+01:00")

public class TopicInputType   {
  @JsonProperty("topicId")
  private String topicId = null;

  @JsonProperty("topicName")
  private String topicName = null;

  @JsonProperty("topicDescription")
  private String topicDescription = null;

  @JsonProperty("topicType")
  private TopicTypeEnum topicType = null;

  @JsonProperty("topicResource")
  private TopicResourceEnum topicResource = null;

  @JsonProperty("subTopics")
  
  private List<TopicInputType> subTopics = null;

  public TopicInputType topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

   /**
   * the topic identifier
   * @return topicId
  **/
  @ApiModelProperty(value = "the topic identifier")
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
  **/
  @ApiModelProperty(required = true, value = "the topic name")
  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public TopicInputType topicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
    return this;
  }

   /**
   * short description of the topic
   * @return topicDescription
  **/
  @ApiModelProperty(required = true, value = "short description of the topic")
  public String getTopicDescription() {
    return topicDescription;
  }

  public void setTopicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
  }

  public TopicInputType topicType(TopicTypeEnum topicType) {
    this.topicType = topicType;
    return this;
  }

   /**
   * topic type indicating if the topic object is a subtopic or a main topic - only topics of type 'sub' shall be subTopics of topics with type 'main'
   * @return topicType
  **/
  @ApiModelProperty(required = true, value = "topic type indicating if the topic object is a subtopic or a main topic - only topics of type 'sub' shall be subTopics of topics with type 'main'")
  public TopicTypeEnum getTopicType() {
    return topicType;
  }

  public void setTopicType(TopicTypeEnum topicType) {
    this.topicType = topicType;
  }

  public TopicInputType topicResource(TopicResourceEnum topicResource) {
    this.topicResource = topicResource;
    return this;
  }

   /**
   * topic resource indicating if the topic object corresponds to an indicator or to a georesource
   * @return topicResource
  **/
  @ApiModelProperty(value = "topic resource indicating if the topic object corresponds to an indicator or to a georesource")
  public TopicResourceEnum getTopicResource() {
    return topicResource;
  }

  public void setTopicResource(TopicResourceEnum topicResource) {
    this.topicResource = topicResource;
  }

  public TopicInputType subTopics(List<TopicInputType> subTopics) {
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
  **/
  @ApiModelProperty(value = "optional list of subTopics")
  public List<TopicInputType> getSubTopics() {
    return subTopics;
  }

  public void setSubTopics(List<TopicInputType> subTopics) {
    this.subTopics = subTopics;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopicInputType topicInputType = (TopicInputType) o;
    return Objects.equals(this.topicId, topicInputType.topicId) &&
        Objects.equals(this.topicName, topicInputType.topicName) &&
        Objects.equals(this.topicDescription, topicInputType.topicDescription) &&
        Objects.equals(this.topicType, topicInputType.topicType) &&
        Objects.equals(this.topicResource, topicInputType.topicResource) &&
        Objects.equals(this.subTopics, topicInputType.subTopics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicId, topicName, topicDescription, topicType, topicResource, subTopics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicInputType {\n");
    
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    topicDescription: ").append(toIndentedString(topicDescription)).append("\n");
    sb.append("    topicType: ").append(toIndentedString(topicType)).append("\n");
    sb.append("    topicResource: ").append(toIndentedString(topicResource)).append("\n");
    sb.append("    subTopics: ").append(toIndentedString(subTopics)).append("\n");
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

