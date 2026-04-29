package com.diacono.diacono.infrastructure.exceptions;

public class TimeInvalidException extends RuntimeException{
    public TimeInvalidException(String message) {
        super(message);
    }
}
