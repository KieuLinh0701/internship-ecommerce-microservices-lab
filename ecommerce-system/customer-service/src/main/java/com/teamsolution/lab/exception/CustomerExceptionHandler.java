package com.teamsolution.lab.exception;

import com.teamsolution.lab.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerExceptionHandler extends GlobalExceptionHandler {

  @ExceptionHandler(AddressAccessDeniedException.class)
  public ResponseEntity<ApiResponse> handleAddressAccessDenied(AddressAccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(DefaultAddressDeletionException.class)
  public ResponseEntity<ApiResponse> handleDefaultAddressDeletion(
      DefaultAddressDeletionException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }
}
