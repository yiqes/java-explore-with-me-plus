package ru.practicum.exception;

public class ValidationException extends RuntimeException {
    String reason;

    public ValidationException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}