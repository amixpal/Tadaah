package com.tadaah.models;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Represents an error response for the API.
 * Contains details about the error status, message, and a list of specific error details.
 */
@Data
public class ApiError {
  private HttpStatus status;
  private String message;
  private List<String> errors;

  /**
   * Constructs a new ApiError with a list of errors.
   *
   * @param status  The HTTP status of the error.
   * @param message The message describing the error.
   * @param errors  A list of specific error details.
   */
  public ApiError(HttpStatus status, String message, List<String> errors) {
    super();
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

  /**
   * Constructs a new ApiError with a single error message.
   *
   * @param status  The HTTP status of the error.
   * @param message The message describing the error.
   * @param error   A single specific error detail.
   */
  public ApiError(HttpStatus status, String message, String error) {
    super();
    this.status = status;
    this.message = message;
    this.errors = Arrays.asList(error);
  }
}
