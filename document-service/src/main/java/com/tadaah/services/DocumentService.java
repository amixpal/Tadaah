package com.tadaah.services;

import com.tadaah.models.Documents;
import com.tadaah.models.Dto.response.DocumentFilterDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
  Documents createDocument(Documents document);
  Documents updateDocument(String id, Documents document);
  void deleteDocument(String id);
  DocumentFilterDto<Documents> getDocuments(String documentType, String user, Boolean verified, Pageable pageable);
}
