package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * TopicOverviewType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

@Entity(name = "Topics")
public class TopicOverviewType   {
	
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @JsonProperty("topicId")
  private String topicId = null;

  @JsonProperty("topicName")
  private String topicName = null;

  @JsonProperty("topicDescription")
  private String topicDescription = null;

//  public TopicOverviewType topicId(String topicId) {
//    this.topicId = topicId;
//    return this;
//  }

   /**
   * the identifier of the topic
   * @return topicId
  **/
  @ApiModelProperty(required = true, value = "the identifier of the topic")
  public String getTopicId() {
    return topicId;
  }

//  public void setTopicId(String topicId) {
//    this.topicId = topicId;
//  }

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
        Objects.equals(this.topicDescription, topicOverviewType.topicDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicId, topicName, topicDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicOverviewType {\n");
    
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    topicDescription: ").append(toIndentedString(topicDescription)).append("\n");
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

