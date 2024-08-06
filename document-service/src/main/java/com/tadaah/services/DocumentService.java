package com.tadaah.services;

import com.tadaah.models.Documents;
import java.util.List;

public interface DocumentService {
  Documents createDocument(Documents document);
  Documents updateDocument(String id, Documents document);
  void deleteDocument(String id);
  List<Documents> getAllDocuments();
}
