package com.tadaah.models.Dto.request;

import com.tadaah.models.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Details about the document")
public class DocumentDto {

  @Schema(description = "The name of the document", example = "Passport")
  @NotNull(message = "Document name is required")
  private String name;

  @Schema(description = "The type of the document", example = "ID_VERIFICATION")
  @NotNull(message = "Document type is required")
  private DocumentType documentType;

  @Schema(description = "The username of the document owner", example = "john_doe")
  @NotNull(message = "Username is required")
  private String userName;

  @Schema(description = "The URL of the file associated with the document", example = "http://example.com/file.pdf")
  @NotNull(message = "File URL is required")
  @Pattern(
      regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*\\.(jpg|jpeg|png|pdf)$",
      message = "File URL must be a valid URL and end with .jpg, .jpeg, .png, or .pdf"
  )
  private String fileUrl;

  @Schema(description = "The expiry date of the document", example = "2024-12-31")
  @NotNull(message = "Expiry date is required")
  private LocalDate expiryDate;
}

