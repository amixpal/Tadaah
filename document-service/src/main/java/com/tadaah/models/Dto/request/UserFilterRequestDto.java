package com.tadaah.models.Dto.request;

import lombok.Data;

@Data
public class UserFilterRequestDto {
  private String userName;
  private int page = 0;
  private int size = 10;
}
