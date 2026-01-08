package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceResourceEnum {

    INDICATOR("indicator"),

    GEORESOURCE("georesource");

    private final String value;

    ServiceResourceEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ServiceResourceEnum fromValue(String value) {
        for (ServiceResourceEnum b : ServiceResourceEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
