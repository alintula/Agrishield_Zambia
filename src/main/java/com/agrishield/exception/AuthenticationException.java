package com.agrishield.exception;

/** Thrown when login fails or the user is not logged in. */
public class AuthenticationException extends AgriShieldException {
    public AuthenticationException(String message) {
        super(message, 401);
    }
}
