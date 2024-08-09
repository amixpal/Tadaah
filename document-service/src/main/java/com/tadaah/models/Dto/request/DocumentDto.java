package com.tadaah.models.Dto.request;

import com.tadaah.models.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Details about the document")
public class DocumentDto {

  @Schema(description = "The name of the document", example = "Passport")
  private String name;

  @Schema(description = "The type of the document", example = "ID_VERIFICATION")
  private DocumentType documentType;

  @Schema(description = "The username of the document owner", example = "john_doe")
  private String userName;

  @Schema(description = "The URL of the file associated with the document", example = "http://example.com/file.pdf")
  private String fileUrl;

  @Schema(description = "The expiry date of the document", example = "2024-12-31")
  private LocalDate expiryDate;
}
