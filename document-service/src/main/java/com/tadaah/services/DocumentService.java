package com.tadaah.services;

import com.tadaah.models.Documents;
import com.tadaah.models.Dto.request.DocumentDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
  Documents createDocument(DocumentDto documentDto);
  Documents updateDocument(String id, DocumentDto documentDto);
  void deleteDocument(String id);
  PaginatedResponseDto<Documents> getDocuments(String documentType, String user, Boolean verified, Pageable pageable);
}
