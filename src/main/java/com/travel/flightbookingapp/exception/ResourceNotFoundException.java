package com.travel.flightbookingapp.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String id) {
        super("Could not find "+  resource + ": " + id);
    }
}
