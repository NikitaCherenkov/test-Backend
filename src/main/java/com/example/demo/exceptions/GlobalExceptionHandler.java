package com.example.demo.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Validation Failed", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        String message = "Invalid request body";
        Map<String, String> errors = new HashMap<>();

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            Object value = ife.getValue();
            Class<?> targetType = ife.getTargetType();

            String fieldName = ife.getPath().stream()
                    .reduce((first, second) -> second)
                    .map(JsonMappingException.Reference::getFieldName)
                    .orElse("unknown");

            if (targetType != null && targetType.isEnum()) {
                String allowedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                message = String.format("Invalid value '%s' for field '%s'", value, fieldName);
                errors.put(fieldName,
                        String.format("Allowed values: [%s]", allowedValues));
            } else if (targetType != null &&
                    (targetType.equals(LocalDateTime.class) ||
                            targetType.equals(java.time.LocalDate.class) ||
                            targetType.equals(java.time.LocalTime.class))) {

                String expectedFormat = getExpectedFormat(targetType);
                message = String.format("Invalid date format for field '%s'", fieldName);
                errors.put(fieldName,
                        String.format("Expected format: %s. Received: '%s'", expectedFormat, value));
            }
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String fieldName = ex.getName();
        Object value = ex.getValue();
        String message = String.format("Invalid value '%s' for parameter '%s'", value, fieldName);

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String allowedValues = Arrays.stream(ex.getRequiredType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            message = String.format("Invalid value '%s' for %s. Allowed values: [%s]",
                    value, fieldName, allowedValues);
        }

        Map<String, String> errors = new HashMap<>();
        errors.put(fieldName, message);

        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, errors);
    }

    private String getExpectedFormat(Class<?> type) {
        if (type.equals(LocalDateTime.class)) {
            return "yyyy-MM-ddTHH:mm:ss (e.g., 2026-03-20T15:30:00)";
        } else if (type.equals(java.time.LocalDate.class)) {
            return "yyyy-MM-dd (e.g., 2026-03-20)";
        } else if (type.equals(java.time.LocalTime.class)) {
            return "HH:mm:ss (e.g., 15:30:00)";
        }
        return "ISO date format";
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String error,
            String message
    ) {
        return buildResponse(status, error, message, null);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String error,
            String message,
            Map<String, String> errors
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        if (errors != null && !errors.isEmpty()) {
            response.put("errors", errors);
        }
        return ResponseEntity.status(status).body(response);
    }
}