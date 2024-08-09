package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Represents an error response for the API.
 * Contains details about the error status, message, and a list of specific error details.
 */
@Data
@Schema(description = "Details about an error response from the API.")
public class ApiError {

  @Schema(description = "The HTTP status of the error", example = "400")
  private HttpStatus status;

  @Schema(description = "The message describing the error", example = "Invalid request format")
  private String message;

  @Schema(description = "A list of specific error details", example = "[\"Field 'name' is required\", \"Invalid value for 'age'\"]")
  private List<String> errors;

  /**
   * Constructs a new ApiError with a list of errors.
   *
   * @param status The HTTP status of the error.
   * @param message The message describing the error.
   * @param errors A list of specific error details.
   */
  public ApiError(HttpStatus status, String message, List<String> errors) {
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

  /**
   * Constructs a new ApiError with a single error message.
   *
   * @param status The HTTP status of the error.
   * @param message The message describing the error.
   * @param error A single specific error detail.
   */
  public ApiError(HttpStatus status, String message, String error) {
    this.status = status;
    this.message = message;
    this.errors = Arrays.asList(error);
  }
}
