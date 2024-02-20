package org.insurance.welcomeservice.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.insurance.welcomeservice.exception.NotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Error handling class for defining the HTTP error codes when an exception is thrown.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    log.error(e.getMessage());
    return getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
  }

  private ResponseEntity<ErrorResponse> getErrorResponse(HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(ErrorResponse.builder()
            .errorCode(String.valueOf(status.value()))
            .message(message)
            .build());
  }
}
