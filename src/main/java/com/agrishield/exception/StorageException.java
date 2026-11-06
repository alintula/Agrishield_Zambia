package com.agrishield.exception;

/** Thrown when data cannot be saved to or read from the data file. */
public class StorageException extends AgriShieldException {
    public StorageException(String message) {
        super(message, 500);
    }
}
