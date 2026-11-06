package com.agrishield.exception;

/** Thrown when a requested record does not exist. */
public class NotFoundException extends AgriShieldException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
