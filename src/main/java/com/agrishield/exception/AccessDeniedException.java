package com.agrishield.exception;

/** Thrown when a user tries to do something their role does not allow. */
public class AccessDeniedException extends AgriShieldException {
    public AccessDeniedException(String message) {
        super(message, 403);
    }
}
