package com.tadaah.models.Dto.request;

import lombok.Data;

@Data
public class DocumentFilterDto {
  private String documentType;
  private String user;
  private Boolean verified;
  private int page = 0;
  private int size = 10;
}
