package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Permission Levels for CRUD operations.
 * "crud" = create, read, update, delete
 * "cru" = create, read, update
 * "ru" = read, update
 * "r" = read
 */
public enum PermissionLevelType {

    CREATOR("creator"),

    PUBLISHER("publisher"),

    EDITOR("editor"),

    VIEWER("viewer");

    private String value;

    PermissionLevelType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static PermissionLevelType fromValue(String text) {
        for (PermissionLevelType b : PermissionLevelType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

