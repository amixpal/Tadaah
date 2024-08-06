package com.tadaah.services.impl;

import com.tadaah.models.Documents;
import com.tadaah.repositories.DocumentRepository;
import com.tadaah.services.DocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DocumentServiceImpl implements DocumentService {
  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private String notificationServiceUrl;

  @Override
  public Documents createDocument(Documents document) {
    validateDocument(document);
    document.setVerified(verifyDocument(document));
    Documents savedDocument = documentRepository.save(document);
    sendNotification(document.getUser(), savedDocument.getId(), "CREATED");
    return savedDocument;
  }

  @Override
  public Documents updateDocument(String id, Documents document) {
    validateDocument(document);
    document.setId(id);
    Documents updatedDocument = documentRepository.save(document);
    sendNotification(document.getUser(), updatedDocument.getId(), "UPDATED");
    return updatedDocument;
  }

  @Override
  public void deleteDocument(String id) {
    documentRepository.deleteById(id);
    sendNotification("admin", id, "DELETED");
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

  private void sendNotification(String receiver, String documentId, String action) {
    try {
      NotificationRequest request = new NotificationRequest(receiver, documentId, action);
      restTemplate.postForEntity(notificationServiceUrl, request, Void.class);
    } catch (Exception e) {
      // Handle exceptions
    }
  }

  public static class NotificationRequest {
    private String receiver;
    private String documentId;
    private String action;

    public NotificationRequest(String receiver, String documentId, String action) {
      this.receiver = receiver;
      this.documentId = documentId;
      this.action = action;
    }
  }
}
