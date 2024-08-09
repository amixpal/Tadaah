package com.tadaah.models.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic Data Transfer Object for API responses.
 *
 * @param <T> The type of the data being returned in the response.
 */
@Data
@AllArgsConstructor
public class ResponseDto<T> {
  private Boolean success;
  private T data;
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
   * @param <T>     The type of the data.
   * @param error   The error message to be returned in the response.
   * @param message
   * @return A ResponseDto object representing an error response.
   */
  public static <T> ResponseDto<T> error(String error, String message) {
    return new ResponseDto<>(false, null, error);
  }
}
