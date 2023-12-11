package de.hsbo.kommonitor.datamanagement.model.legacy.georesources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
   * If georesource is a POI then custom POI marker color can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)
   */
  public enum PoiSymbolColorEnum {
    WHITE("white"),
    
    RED("red"),
    
    ORANGE("orange"),
    
    BEIGE("beige"),
    
    GREEN("green"),
    
    BLUE("blue"),
    
    PURPLE("purple"),
    
    PINK("pink"),
    
    GRAY("gray"),
    
    BLACK("black");

    private String value;

    PoiSymbolColorEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PoiSymbolColorEnum fromValue(String text) {
      for (PoiSymbolColorEnum b : PoiSymbolColorEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }