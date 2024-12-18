package ru.practicum.exception;

public class ForbiddenException extends RuntimeException {
    String reason;

    public ForbiddenException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}