package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
   * the poi marker type, either text or symbol
   */
  public enum PoiMarkerStyleEnum {
    TEXT("text"),
    
    SYMBOL("symbol");

    private String value;

    PoiMarkerStyleEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PoiMarkerStyleEnum fromValue(String text) {
      for (PoiMarkerStyleEnum b : PoiMarkerStyleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }