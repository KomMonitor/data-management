package de.hsbo.kommonitor.datamanagement.features.management;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets updateInterval
 */
public enum ResourceTypeEnum {
  SPATIAL_UNIT("SPATIAL_UNIT"),
  
  GEORESOURCE("GEORESOURCE"),
  
  INDICATOR("INDICATOR");

  private String value;

  ResourceTypeEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ResourceTypeEnum fromValue(String text) {
    for (ResourceTypeEnum b : ResourceTypeEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
