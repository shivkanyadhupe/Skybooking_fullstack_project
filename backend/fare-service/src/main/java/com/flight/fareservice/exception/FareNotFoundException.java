package com.flight.fareservice.exception;

public class FareNotFoundException extends RuntimeException {

    public FareNotFoundException(String message) {
        super(message);
    }
}
