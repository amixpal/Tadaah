package com.tadaah.models.Dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic Data Transfer Object for API responses.
 *
 * @param <T> The type of the data being returned in the response.
 */
@Data
@AllArgsConstructor
@Schema(description = "Generic DTO for API responses")
public class ResponseDto<T> {

  @Schema(description = "Indicates whether the response is successful", example = "true")
  private Boolean success;

  @Schema(description = "The data returned in the response. Can be any type depending on the API endpoint.")
  private T data;

  @Schema(description = "The error message if the response indicates failure", example = "Invalid input")
  private String error;

  /**
   * Creates a successful response with the provided data.
   *
   * @param data The data to be returned in the response.
   * @param <T> The type of the data.
   * @return A ResponseDto object representing a successful response.
   */
  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>(true, data, null);
  }

  /**
   * Creates an error response with the provided error message.
   *
   * @param <T> The type of the data.
   * @param error The error message to be returned in the response.
   * @return A ResponseDto object representing an error response.
   */
  public static <T> ResponseDto<T> error(String error) {
    return new ResponseDto<>(false, null, error);
  }
}
