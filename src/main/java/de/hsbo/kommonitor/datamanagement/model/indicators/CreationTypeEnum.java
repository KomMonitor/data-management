package de.hsbo.kommonitor.datamanagement.model.indicators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * indicates if the data was simply inserted (INSERTED), computed by an automated script (COMPUTED) or automatically aggregated by a script (AGGREGATED)
 */
public enum CreationTypeEnum {
  INSERTED("INSERTED"),
  
  COMPUTED("COMPUTED"),
  
  AGGREGATED("AGGREGATED");

  private String value;

  CreationTypeEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CreationTypeEnum fromValue(String text) {
    for (CreationTypeEnum b : CreationTypeEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
