package com.tadaah.models.Dto.request;

import com.tadaah.models.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Criteria for filtering documents")
public class DocumentFilterRequestDto {

  @Schema(description = "The type of the document", example = "FINANCIAL_DOCUMENT")
  private DocumentType documentType;

  @Schema(description = "The username of the document owner", example = "john_doe")
  private String user;

  @Schema(description = "Flag indicating if the document is verified")
  private Boolean verified;

  @Schema(description = "Flag indicating if the notification failed for the document")
  private Boolean isNotificationFailed;

  @Schema(description = "The page number for pagination", example = "0")
  private int page = 0;

  @Schema(description = "The size of each page for pagination", example = "10")
  private int size = 10;
}
