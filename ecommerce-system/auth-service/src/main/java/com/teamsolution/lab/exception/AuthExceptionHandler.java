package com.teamsolution.lab.exception;

import com.teamsolution.lab.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler extends GlobalExceptionHandler {

  @ExceptionHandler(AccountNotAccessibleException.class)
  public ResponseEntity<ApiResponse> handleAccountNotAccessible(AccountNotAccessibleException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ApiResponse> handleInvalidToken(InvalidTokenException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ApiResponse> handleDisabled(DisabledException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(SameRoleException.class)
  public ResponseEntity<ApiResponse> handleSameRole(SameRoleException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(InvalidAccountStatusException.class)
  public ResponseEntity<ApiResponse> handleInvalidStatusAccount(InvalidAccountStatusException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<ApiResponse> handleRoleNotFound(RoleNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ApiResponse> handleLocked(LockedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.failure(ex.getMessage()));
  }
}
