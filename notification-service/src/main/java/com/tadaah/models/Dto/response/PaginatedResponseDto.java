package com.tadaah.models.Dto.response;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * Data Transfer Object for paginated filtering response.
 *
 * @param <T> The type of the content being returned in the response.
 */
import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponseDto<T> {
  private long totalElements;
  private int totalPages;
  private int size;
  private List<T> content;
  private boolean first;
  private boolean last;
  private boolean empty;

  public PaginatedResponseDto(List<T> content, long totalElements, int totalPages, int size, boolean first, boolean last, boolean empty) {
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.size = size;
    this.content = content;
    this.first = first;
    this.last = last;
    this.empty = empty;
  }
}

