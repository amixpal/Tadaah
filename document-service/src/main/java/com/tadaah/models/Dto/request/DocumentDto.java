package com.tadaah.models.Dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DocumentDto {
  @NotBlank(message = "name is required")
  private String name;
  @NotBlank(message = "documentType is required")
  private String documentType;
  @NotBlank(message = "Username is required")
  private String userName;
  @NotBlank(message = "fileUrl is required")
  private String fileUrl;
  @NotBlank(message = "expiryDate is required")
  private LocalDate expiryDate;
}
