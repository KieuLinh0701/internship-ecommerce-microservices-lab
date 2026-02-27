package com.teamsolution.lab.exception;

import com.teamsolution.lab.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({ResourceNotFoundException.class, NoRecordsFoundException.class})
  public ResponseEntity<ApiResponse> handleNotFoundException(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ApiResponse> handleDuplicateException(DuplicateResourceException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("Invalid input");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(errorMessage));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleOtherExceptions(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure("An unexpected error occurred: " + ex.getMessage()));
  }
}
