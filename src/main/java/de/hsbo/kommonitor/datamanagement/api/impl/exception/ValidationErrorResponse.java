package de.hsbo.kommonitor.datamanagement.api.impl.exception;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.Violation;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String path,
        List<Violation> violations
) {}