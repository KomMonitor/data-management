package de.hsbo.kommonitor.datamanagement.model.topics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * topic resource indicating if the topic object corresponds to an indicator or to a georesource
 */
public enum TopicResourceEnum {
  INDICATOR("indicator"),
  
  GEORESOURCE("georesource");

  private String value;

  TopicResourceEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TopicResourceEnum fromValue(String text) {
    for (TopicResourceEnum b : TopicResourceEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}