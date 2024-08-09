package com.tadaah.models.Dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Details about the user")
public class UserDto {

  @Schema(description = "The username of the user", example = "john_doe")
  @NotNull(message = "Username is required")
  private String userName;

  @Schema(description = "The first name of the user", example = "John")
  @NotNull(message = "First name is required")
  private String firstName;

  @Schema(description = "The last name of the user", example = "Doe")
  @NotNull(message = "Last name is required")
  private String lastName;
}
