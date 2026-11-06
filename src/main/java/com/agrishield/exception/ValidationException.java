package com.agrishield.exception;

/** Thrown when user input is invalid. */
public class ValidationException extends AgriShieldException {
    public ValidationException(String message) {
        super(message, 400);
    }
}
