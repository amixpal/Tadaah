package com.tadaah.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserServiceException.class)
  public ResponseEntity<String> handleUserServiceException(UserServiceException e) {
    return new ResponseEntity<>(e.getMessage(), e.getStatus());
  }

  @ExceptionHandler(DocumentServiceException.class)
  public ResponseEntity<String> handleDocumentServiceException(DocumentServiceException e) {
    return new ResponseEntity<>(e.getMessage(), e.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
