package com.agrishield.exception;

/** Base class of all custom exceptions. Carries the HTTP status the API should answer with. */
public class AgriShieldException extends RuntimeException {
    private final int status;

    public AgriShieldException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
