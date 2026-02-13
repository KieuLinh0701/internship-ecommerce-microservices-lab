package com.teamsolution.lab.exception;

public class NoRecordsFoundException extends RuntimeException {
  public NoRecordsFoundException(String message) {
    super(message);
  }

  public NoRecordsFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
