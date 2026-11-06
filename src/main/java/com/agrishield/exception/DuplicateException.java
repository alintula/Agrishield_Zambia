package com.agrishield.exception;

/** Thrown when a record already exists, for example a phone number. */
public class DuplicateException extends AgriShieldException {
    public DuplicateException(String message) {
        super(message, 409);
    }
}
