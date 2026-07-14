package de.hsbo.kommonitor.datamanagement.api.impl.exception;

import de.hsbo.kommonitor.datamanagement.msg.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.exc.ValueInstantiationException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MSG_INVALID_PAYLOAD_ERROR = "invalid_payload_error";
    private static final String MSG_SCHEMA_VALIDATION_ERROR = "schema_validation_error";

    @Autowired
    private MessageResolver messageResolver;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<Violation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                messageResolver.getMessage(MSG_SCHEMA_VALIDATION_ERROR),
                request.getRequestURI(),
                violations
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    //Handles Jackson deserialization issues
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String fieldName = "Unknown";
        String message = "Malformed JSON request or invalid fields";

        Throwable cause = ex.getCause();

        if (cause instanceof ValueInstantiationException instantiationException) {
            // Extract the JSON field path
            fieldName = instantiationException.getPath().stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.joining("."));

            // Extract the root cause message
            Throwable rootCause = instantiationException.getCause();
            if (rootCause != null) {
                message = rootCause.getMessage();
            } else {
                message = instantiationException.getOriginalMessage();
            }

        } else if (cause instanceof InvalidFormatException formatException) {
            // General format/type issues (numbers vs strings, standard enums)
            fieldName = formatException.getPath().stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .collect(Collectors.joining("."));
            message = String.format("Value '%s' is not valid for type '%s'",
                    formatException.getValue(), formatException.getTargetType().getSimpleName());
        } else if (cause instanceof DatabindException databindException) {
            // Fallback: catches any other Jackson 3 binding/mapping issues
            fieldName = databindException.getPath().stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.joining("."));

            // Use the original message but keep it clean
            message = databindException.getOriginalMessage();
        }

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                messageResolver.getMessage(MSG_INVALID_PAYLOAD_ERROR),
                request.getRequestURI(),
                List.of(new Violation(fieldName, message))
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

}



