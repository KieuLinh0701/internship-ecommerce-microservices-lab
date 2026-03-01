package com.teamsolution.lab.exception;

public class InvalidAccountStatusException extends RuntimeException {
    public InvalidAccountStatusException(String message) {
        super(message);
    }
}
