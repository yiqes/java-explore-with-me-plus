package ru.practicum.exception;

public class ConflictException extends RuntimeException {

    String reason;

    public ConflictException(final String message, final String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
