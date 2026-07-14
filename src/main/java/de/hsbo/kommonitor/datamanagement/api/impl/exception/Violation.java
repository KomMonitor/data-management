package de.hsbo.kommonitor.datamanagement.api.impl.exception;

public record Violation(
        String field,
        String message
) {
}
