package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.DefaultResourcePermissionType;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * association Object for connecting topicIds with defaultPermissions
 */

@Schema(name = "TopicDefaultPermissionType", description = "association Object for connecting topicIds with defaultPermissions")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-03-08T11:42:46.348441096+01:00[Europe/Berlin]")
public class TopicDefaultPermissionType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String topicId;

  private DefaultResourcePermissionType defaultPermissions;

  public TopicDefaultPermissionType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TopicDefaultPermissionType(DefaultResourcePermissionType defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
  }

  public TopicDefaultPermissionType topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

  /**
   * uuid of the topic
   * @return topicId
  */
  
  @Schema(name = "topicId", description = "uuid of the topic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topicId")
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public TopicDefaultPermissionType defaultPermissions(DefaultResourcePermissionType defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
    return this;
  }

  /**
   * Get defaultPermissions
   * @return defaultPermissions
  */
  @NotNull @Valid 
  @Schema(name = "defaultPermissions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defaultPermissions")
  public DefaultResourcePermissionType getDefaultPermissions() {
    return defaultPermissions;
  }

  public void setDefaultPermissions(DefaultResourcePermissionType defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopicDefaultPermissionType topicDefaultPermissionType = (TopicDefaultPermissionType) o;
    return Objects.equals(this.topicId, topicDefaultPermissionType.topicId) &&
        Objects.equals(this.defaultPermissions, topicDefaultPermissionType.defaultPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicId, defaultPermissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicDefaultPermissionType {\n");
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    defaultPermissions: ").append(toIndentedString(defaultPermissions)).append("\n");
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

