package com.teamsolution.lab.exception;

public class MaxResendOtpExceededException
        extends RuntimeException {
  public MaxResendOtpExceededException(String message) {
    super(message);
  }

  public MaxResendOtpExceededException(String message, Throwable cause) {
    super(message, cause);
  }
}
