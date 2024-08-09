package com.tadaah.exception;

import com.tadaah.models.ApiError;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for field validation errors.
 */
@RestControllerAdvice
public class FieldValidationException {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    // Extract field errors
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .collect(Collectors.toList());

    // Create ApiError instance
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", errors);

    // Return ResponseEntity with ApiError
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
