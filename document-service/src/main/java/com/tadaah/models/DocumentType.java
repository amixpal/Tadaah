package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of documents")
public enum DocumentType {
  @Schema(description = "Financial Document")
  FINANCIAL_DOCUMENT,

  @Schema(description = "Identification Verification")
  ID_VERIFICATION,

  @Schema(description = "Legal Document")
  LEGAL_DOCUMENT,

  @Schema(description = "Other")
  OTHER
}
