package com.tadaah.exceptions;

import org.springframework.http.HttpStatus;

public class DocumentServiceException extends RuntimeException {
  private final HttpStatus status;

  public DocumentServiceException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public DocumentServiceException(String message, HttpStatus status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
