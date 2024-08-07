package com.tadaah.services;

import com.tadaah.models.Documents;
import com.tadaah.models.Dto.response.DocumentFilterResponseDto;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
  Documents createDocument(Documents document);
  Documents updateDocument(String id, Documents document);
  void deleteDocument(String id);
  DocumentFilterResponseDto<Documents> getDocuments(String documentType, String user, Boolean verified, Pageable pageable);
}
