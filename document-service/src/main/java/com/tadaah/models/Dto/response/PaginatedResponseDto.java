package com.tadaah.models.Dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Data Transfer Object for paginated filtering response.
 *
 * @param <T> The type of the content being returned in the response.
 */
@Data
public class PaginatedResponseDto<T> {
  private long totalElements;
  private int totalPages;
  private int size;
  private List<T> content;
  private boolean first;
  private boolean last;
  private boolean empty;

  public PaginatedResponseDto(Page<T> page) {
    this.totalElements = page.getTotalElements();
    this.totalPages = page.getTotalPages();
    this.size = page.getSize();
    this.content = page.getContent();
    this.first = page.isFirst();
    this.last = page.isLast();
    this.empty = page.isEmpty();
  }
}
