package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    String reason;

    public NotFoundException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}