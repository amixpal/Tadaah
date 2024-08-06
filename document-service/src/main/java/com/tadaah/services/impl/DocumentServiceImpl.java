package com.tadaah.services.impl;

import com.tadaah.models.Documents;
import com.tadaah.repositories.DocumentRepository;
import com.tadaah.services.DocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {
  @Autowired
  private DocumentRepository documentRepository;

  @Override
  public Documents createDocument(Documents document) {
    validateDocument(document);
    document.setVerified(verifyDocument(document));
    return documentRepository.save(document);
  }

  @Override
  public Documents updateDocument(String id, Documents document) {
    validateDocument(document);
    document.setId(id);
    return documentRepository.save(document);
  }

  @Override
  public void deleteDocument(String id) {
    documentRepository.deleteById(id);
  }

  @Override
  public List<Documents> getAllDocuments() {
    return documentRepository.findAll();
  }

  private void validateDocument(Documents document) {
    // Implement validation logic
  }

  private boolean verifyDocument(Documents document) {
    // Implement verification logic
    return true; // Placeholder
  }
}
