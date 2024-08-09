package com.tadaah.models.Dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for paginated filtering response.
 *
 * @param <T> The type of the content being returned in the response.
 */
@Data
@Schema(description = "Paginated response for filtering results")
public class PaginatedResponseDto<T> {

  @Schema(description = "Total number of elements available", example = "100")
  private long totalElements;

  @Schema(description = "Total number of pages", example = "10")
  private int totalPages;

  @Schema(description = "Size of the current page", example = "20")
  private int size;

  @Schema(description = "List of content items on the current page")
  private List<T> content;

  @Schema(description = "Indicates if this is the first page", example = "true")
  private boolean first;

  @Schema(description = "Indicates if this is the last page", example = "false")
  private boolean last;

  @Schema(description = "Indicates if the content list is empty", example = "false")
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
