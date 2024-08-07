package com.tadaah.models.Dto.request;

import lombok.Data;

/**
 * Data Transfer Object for filtering documents based on various criteria.
 */
@Data
public class DocumentFilterRequestDto {
  private String documentType;
  private String user;
  private Boolean verified;
  private int page = 0;
  private int size = 10;
}
