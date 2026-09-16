package de.hsbo.kommonitor.datamanagement.api.impl.exception;

import java.util.List;

/**
 * Thrown when a request payload is semantically invalid. It is translated into a
 * {@link ValidationErrorResponse} (HTTP 400) by the {@code GlobalExceptionHandler}.
 */
public class ValidationException extends RuntimeException {

    private final transient List<Violation> violations;

    public ValidationException(String message, List<Violation> violations) {
        super(message);
        this.violations = violations;
    }

    public ValidationException(String field, String message) {
        this(message, List.of(new Violation(field, message)));
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
