package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an error response for the API.
 * Contains details about the error status, message, and a list of specific error details.
 */
@Data
@Schema(description = "Represents an error response for the API, including status, message, and error details.")
public class ApiError {

  @Schema(description = "HTTP status code of the error", example = "400")
  private HttpStatus status;

  @Schema(description = "A message describing the error", example = "Invalid input data")
  private String message;

  @Schema(description = "A list of specific error details", example = "[\"Field 'name' cannot be empty\", \"Field 'age' must be a positive number\"]")
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
