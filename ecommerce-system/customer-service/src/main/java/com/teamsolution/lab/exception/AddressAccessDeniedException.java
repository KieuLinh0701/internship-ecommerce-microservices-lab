package com.teamsolution.lab.exception;

public class AddressAccessDeniedException extends RuntimeException {
  public AddressAccessDeniedException(String message) {
    super(message);
  }
}
