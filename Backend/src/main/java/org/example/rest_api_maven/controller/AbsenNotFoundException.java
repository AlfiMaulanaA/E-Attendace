package org.example.rest_api_maven.controller;

public class AbsenNotFoundException extends RuntimeException {
    public AbsenNotFoundException(String message) {
        super(message);
    }
}
