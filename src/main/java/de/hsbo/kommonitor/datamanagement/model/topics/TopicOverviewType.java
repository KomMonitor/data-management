package de.hsbo.kommonitor.datamanagement.model.topics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * TopicOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2020-01-04T23:51:22.008+01:00")

public class TopicOverviewType   {
  @JsonProperty("topicId")
  private String topicId = null;

  @JsonProperty("topicName")
  private String topicName = null;

  @JsonProperty("topicDescription")
  private String topicDescription = null;

  @JsonProperty("topicType")
  private TopicTypeEnum topicType = null;

  @JsonProperty("subTopics")
  
  private List<TopicOverviewType> subTopics = null;
  
  public TopicOverviewType(){}

  public TopicOverviewType(String topicId) {
	this.topicId = topicId;
}

public TopicOverviewType topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

   /**
   * the identifier of the topic
   * @return topicId
  **/
  @ApiModelProperty(required = true, value = "the identifier of the topic")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public TopicOverviewType topicName(String topicName) {
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

  public TopicOverviewType topicDescription(String topicDescription) {
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

  public TopicOverviewType topicType(TopicTypeEnum topicType) {
    this.topicType = topicType;
    return this;
  }

   /**
   * topic type indicating if the topic object is a subtopic or a main topic - only topics of type 'sub' shall be subTopics of topics with type 'main'
   * @return topicType
  **/
  @ApiModelProperty(value = "topic type indicating if the topic object is a subtopic or a main topic - only topics of type 'sub' shall be subTopics of topics with type 'main'")
  public TopicTypeEnum getTopicType() {
    return topicType;
  }

  public void setTopicType(TopicTypeEnum topicType) {
    this.topicType = topicType;
  }

  public TopicOverviewType subTopics(List<TopicOverviewType> subTopics) {
    this.subTopics = subTopics;
    return this;
  }

  public TopicOverviewType addSubTopicsItem(TopicOverviewType subTopicsItem) {
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
  public List<TopicOverviewType> getSubTopics() {
    return subTopics;
  }

  public void setSubTopics(List<TopicOverviewType> subTopics) {
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
    TopicOverviewType topicOverviewType = (TopicOverviewType) o;
    return Objects.equals(this.topicId, topicOverviewType.topicId) &&
        Objects.equals(this.topicName, topicOverviewType.topicName) &&
        Objects.equals(this.topicDescription, topicOverviewType.topicDescription) &&
        Objects.equals(this.topicType, topicOverviewType.topicType) &&
        Objects.equals(this.subTopics, topicOverviewType.subTopics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicId, topicName, topicDescription, topicType, subTopics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicOverviewType {\n");
    
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    topicDescription: ").append(toIndentedString(topicDescription)).append("\n");
    sb.append("    topicType: ").append(toIndentedString(topicType)).append("\n");
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

