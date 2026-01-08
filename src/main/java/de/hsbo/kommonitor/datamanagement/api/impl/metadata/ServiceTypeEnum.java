package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceTypeEnum {

    WMS("wms");

    private final String value;

    ServiceTypeEnum(String value) {
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
    public static ServiceTypeEnum fromValue(String value) {
        for (ServiceTypeEnum b : ServiceTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
