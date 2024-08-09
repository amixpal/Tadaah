package com.tadaah.models.Dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Criteria for filtering users")
public class UserFilterRequestDto {

  @Schema(description = "The username of the user", example = "john_doe")
  private String userName;

  @Schema(description = "The page number for pagination", example = "0")
  private int page = 0;

  @Schema(description = "The size of each page for pagination", example = "10")
  private int size = 10;
}
