package com.tadaah.services.impl;

import com.tadaah.exceptions.DocumentServiceException;
import com.tadaah.models.Documents;
import com.tadaah.models.Dto.response.DocumentFilterDto;
import com.tadaah.repositories.DocumentRepository;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.DocumentService;
import com.tadaah.utils.DocumentValidationUtil;
import com.tadaah.utils.NotificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

  private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${notification.service.url}")
  private String notificationServiceUrl;

  /**
   * Creates a new document.
   *
   * @param document The document to be created.
   * @return The created document.
   */
  @Override
  @CacheEvict(value = {"documents", "filteredDocuments"}, allEntries = true)
  public Documents createDocument(Documents document) {
    try {
      document.setVerified(DocumentValidationUtil.verifyDocument(document, userRepository));
      Documents savedDocument = documentRepository.save(document);

      // Sending Notification
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, document.getUserName(), savedDocument.getId(), "CREATED");
      } catch (Exception e) {
        logger.error("Notification error while creating document for user: {}", document.getUserName(), e);
        savedDocument.setNotificationError("Notification failed: " + e.getMessage());
        documentRepository.save(savedDocument);  // Update the document with the notification error
      }
      return savedDocument;
    } catch (DocumentServiceException e) {
      logger.error("Validation error while creating document for user: {}", document.getUserName(), e);
      throw e;
    } catch (RuntimeException e) {
      logger.error("Unexpected error while creating document for user: {}", document.getUserName(), e);
      throw new DocumentServiceException("Unexpected error while creating document for user: " + document.getUserName(), HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Updates an existing document.
   *
   * @param id       The ID of the document to be updated.
   * @param document The document details to be updated.
   * @return The updated document.
   */
  @Override
  @CachePut(value = "documents", key = "#id")
  @CacheEvict(value = "filteredDocuments", allEntries = true)
  public Documents updateDocument(String id, Documents document) {
    try {
      Documents existingDocument = documentRepository.findById(id)
          .orElseThrow(() -> {
            logger.warn("Document not found with ID: {}", id);
            return new DocumentServiceException("Document not found with ID: " + id, HttpStatus.NOT_FOUND);
          });

      // Merge fields
      if (document.getName() != null) {
        existingDocument.setName(document.getName());
      }
      if (document.getDocumentType() != null) {
        existingDocument.setDocumentType(document.getDocumentType());
      }
      if (document.getUserName() != null) {
        existingDocument.setUserName(document.getUserName());
      }
      if (document.getFileUrl() != null) {
        existingDocument.setFileUrl(document.getFileUrl());
      }
      if (document.getExpiryDate() != null) {
        existingDocument.setExpiryDate(document.getExpiryDate());
      }
      existingDocument.setVerified(DocumentValidationUtil.verifyDocument(existingDocument, userRepository));

      Documents updatedDocument = documentRepository.save(existingDocument);

      // Sending Notification
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, existingDocument.getUserName(), updatedDocument.getId(), "UPDATED");
      } catch (Exception e) {
        logger.error("Notification error while updating document for user: {}", existingDocument.getUserName(), e);
        updatedDocument.setNotificationError("Notification failed: " + e.getMessage());
        documentRepository.save(updatedDocument);  // Update the document with the notification error
      }

      return updatedDocument;
    } catch (DocumentServiceException e) {
      logger.error("Validation error while updating document with ID: {}", id, e);
      throw e;
    } catch (RuntimeException e) {
      logger.error("Unexpected error while updating document with ID: {}", id, e);
      throw new DocumentServiceException("Unexpected error while updating document with ID: " + id, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }


  /**
   * Deletes a document.
   *
   * @param id The ID of the document to be deleted.
   */
  @Override
  @CacheEvict(value = {"documents", "filteredDocuments"}, allEntries = true)
  public void deleteDocument(String id) {
    try {
      if (!documentRepository.existsById(id)) {
        logger.warn("Document not found with ID: {}", id);
        throw new DocumentServiceException("Document not found with ID: " + id,
            HttpStatus.NOT_FOUND);
      }
      documentRepository.deleteById(id);

      // Sending Notification
      NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, "admin", id,
          "DELETED");

    } catch (RuntimeException e) {
      logger.error("Unexpected error while deleting document with ID: {}", id, e);
      throw new DocumentServiceException("Unexpected error while deleting document with ID: " + id,
          HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Retrieves documents with optional filtering and sorting.
   *
   * @param documentType The type of the document to filter by.
   * @param user         The user to filter by.
   * @param verified     The verification status to filter by.
   * @return A list of documents that match the filter criteria.
   */
  @Override
  @Cacheable(value = "filteredDocuments", key = "{#documentType, #user, #verified, #pageable.pageNumber, #pageable.pageSize}")
  public DocumentFilterDto<Documents> getDocuments(String documentType, String user, Boolean verified, Pageable pageable) {
    try {
      logger.info("Fetching documents with filters - documentType: {}, user: {}, verified: {}", documentType, user, verified);
      Page<Documents> documentsPage = documentRepository.findByFilter(documentType, user, verified, pageable);
      return new DocumentFilterDto<>(documentsPage);
    } catch (RuntimeException e) {
      logger.error("Error fetching documents with filters - documentType: {}, user: {}, verified: {}", documentType, user, verified, e);
      throw new DocumentServiceException("Error fetching documents with filters", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }
}
