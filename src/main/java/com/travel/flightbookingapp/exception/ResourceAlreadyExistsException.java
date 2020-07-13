package com.travel.flightbookingapp.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource) {
        super(resource + " already exists");
    }
}
