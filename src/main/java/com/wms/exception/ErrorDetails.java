package com.wms.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorDetails {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    public ErrorDetails() {}

    public ErrorDetails(LocalDateTime timestamp, int status, String error, String message, String path, List<FieldError> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public List<FieldError> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldError> fieldErrors) { this.fieldErrors = fieldErrors; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private List<FieldError> fieldErrors;

        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder status(int status) { this.status = status; return this; }
        public Builder error(String error) { this.error = error; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder path(String path) { this.path = path; return this; }
        public Builder fieldErrors(List<FieldError> fieldErrors) { this.fieldErrors = fieldErrors; return this; }

        public ErrorDetails build() {
            return new ErrorDetails(timestamp, status, error, message, path, fieldErrors);
        }
    }

    // FieldError inner class
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;

        public FieldError() {}

        public FieldError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        // Getters and Setters
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getRejectedValue() { return rejectedValue; }
        public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }

        // Builder
        public static FieldErrorBuilder builder() { return new FieldErrorBuilder(); }

        public static class FieldErrorBuilder {
            private String field;
            private String message;
            private Object rejectedValue;

            public FieldErrorBuilder field(String field) { this.field = field; return this; }
            public FieldErrorBuilder message(String message) { this.message = message; return this; }
            public FieldErrorBuilder rejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; return this; }

            public FieldError build() {
                return new FieldError(field, message, rejectedValue);
            }
        }
    }
}