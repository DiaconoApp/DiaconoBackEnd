package com.diacono.diacono.application.exceptions;

public class ObjectExistsException extends RuntimeException {
    public ObjectExistsException(String message) {
        super(message);
    }
}
