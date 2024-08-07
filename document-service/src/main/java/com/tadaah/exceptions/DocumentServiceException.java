package com.tadaah.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling document service-related errors.
 */
public class DocumentServiceException extends RuntimeException {
  private final HttpStatus status;

  /**
   * Constructor for DocumentServiceException with message and status.
   *
   * @param message The error message.
   * @param status  The HTTP status code.
   */
  public DocumentServiceException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  /**
   * Constructor for DocumentServiceException with message, status, and cause.
   *
   * @param message The error message.
   * @param status  The HTTP status code.
   * @param cause   The cause of the exception.
   */
  public DocumentServiceException(String message, HttpStatus status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  /**
   * Get the HTTP status code associated with this exception.
   *
   * @return The HTTP status code.
   */
  public HttpStatus getStatus() {
    return status;
  }
}
