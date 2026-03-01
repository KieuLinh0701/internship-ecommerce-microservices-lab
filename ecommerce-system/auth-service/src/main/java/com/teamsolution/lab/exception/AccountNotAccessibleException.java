package com.teamsolution.lab.exception;

public class AccountNotAccessibleException extends RuntimeException {
    public AccountNotAccessibleException(String message) {
        super(message);
    }
}
