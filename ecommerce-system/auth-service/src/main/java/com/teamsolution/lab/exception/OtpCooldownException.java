package com.teamsolution.lab.exception;

public class OtpCooldownException extends RuntimeException {
  public OtpCooldownException(String message) {
    super(message);
  }

  public OtpCooldownException(String message, Throwable cause) {
    super(message, cause);
  }
}
