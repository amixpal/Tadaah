package com.tadaah.exception;

import com.tadaah.models.ApiError;
import java.util.Arrays;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for handling various types of exceptions across the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handle common runtime exceptions and provide appropriate responses.
   *
   * @param ex      The exception that was thrown.
   * @param request The web request during which the exception was thrown.
   * @return A ResponseEntity containing the error details.
   */
  @ExceptionHandler(value = {IllegalArgumentException.class, NullPointerException.class, IllegalStateException.class,
      RuntimeException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    LOG.error("Exception: ", ex);
    ApiError apiError;

    if (ex instanceof IllegalStateException) {
      apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), Collections.singletonList(ex.getMessage()));
    } else if (ex instanceof IllegalArgumentException) {
      apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.singletonList(ex.getMessage()));
    } else {
      apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex.toString());
    }

    return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
  }


  /**
   * Handle NotificationServiceException and provide appropriate responses.
   *
   * @param e       The NotificationServiceException that was thrown.
   * @param request The web request during which the exception was thrown.
   * @return A ResponseEntity containing the error details.
   */
  @ExceptionHandler(NotificationServiceException.class)
  public ResponseEntity<Object> handleNotificationServiceException(NotificationServiceException e, WebRequest request) {
    ApiError apiError = new ApiError(e.getStatus(), e.getMessage(), Arrays.asList(e.getMessage()));
    return handleExceptionInternal(e, apiError, new HttpHeaders(), e.getStatus(), request);
  }
}
