package com.tadaah.services.impl;

import com.tadaah.exceptions.DocumentServiceException;
import com.tadaah.exceptions.NotificationServiceException;
import com.tadaah.models.Documents;
import com.tadaah.models.Dto.request.DocumentDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.NotificationType;
import com.tadaah.repositories.DocumentRepository;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.DocumentService;
import com.tadaah.utils.DocumentValidationUtil;
import com.tadaah.utils.GenericUtils;
import com.tadaah.utils.NotificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
   * @param documentDto The document to be created.
   * @return The created document with verification and possible notification error.
   * @throws DocumentServiceException if validation or unexpected errors occur during creation.
   */
  @Override
  @CacheEvict(value = {"documents", "filteredDocuments"}, allEntries = true)
  public Documents createDocument(DocumentDto documentDto) {
    try {
      // Validate the document using custom validation utility
      DocumentValidationUtil.validateDocument(documentDto, userRepository);

      // Convert DocumentDto to Documents entity using a generic utility method
      Documents document = new Documents();
      GenericUtils.mergeObjects(document, documentDto);

      // Verify the document based on business rules
      document.setVerified(DocumentValidationUtil.verifyDocument(documentDto, userRepository));
      Documents savedDocument = documentRepository.save(document);

      // Send a notification regarding the creation of the document
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, document, NotificationType.CREATE);
      } catch (NotificationServiceException e) {
        // If notification fails, log the error
        logger.error("Notification error while creating document for user: {}", savedDocument.getUserName(), e);
        savedDocument.setNotificationError("Notification failed: " + e.getMessage());
        documentRepository.save(savedDocument);  // Update the document with the notification error
      }

      return savedDocument;

    } catch (DocumentServiceException e) {
      logger.error("Validation error while creating document for user: {}", documentDto.getUserName(), e);
      throw e;

    } catch (RuntimeException e) {
      logger.error("Unexpected error while creating document for user: {}", documentDto.getUserName(), e);
      throw new DocumentServiceException("Unexpected error while creating document for user: " + documentDto.getUserName(), HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Updates an existing document.
   *
   * @param id The ID of the document to be updated.
   * @param documentDto The document details to be updated.
   * @return The updated document with verification and possible notification error.
   * @throws DocumentServiceException if validation or unexpected errors occur during updating.
   */
  @Override
  @CachePut(value = "documents", key = "#id")
  @CacheEvict(value = "filteredDocuments", allEntries = true)
  public Documents updateDocument(String id, DocumentDto documentDto) {
    try {
      // Retrieve existing document by ID or throw an exception if not found
      Documents existingDocument = documentRepository.findById(id)
          .orElseThrow(() -> new DocumentServiceException("Document not found with ID: " + id, HttpStatus.NOT_FOUND));

      // Verify the provided documentDto before applying changes
      boolean isVerified = DocumentValidationUtil.verifyDocument(documentDto, userRepository);
      if (!isVerified) {
        throw new DocumentServiceException("Document verification failed", HttpStatus.BAD_REQUEST);
      }

      // Merge fields from the validated documentDto into the existing document
      GenericUtils.mergeObjects(existingDocument, documentDto);

      // Save the updated document
      Documents updatedDocument = documentRepository.save(existingDocument);

      // Send a notification regarding the update of the document
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, updatedDocument, NotificationType.UPDATE);
      } catch (NotificationServiceException e) {
        // If notification fails, log the error
        logger.error("Notification error while updating document for user: {}", updatedDocument.getUserName(), e);
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
   * @throws DocumentServiceException if the document is not found or unexpected errors occur during deletion.
   */
  @Override
  @CacheEvict(value = {"documents", "filteredDocuments"}, allEntries = true)
  public void deleteDocument(String id) {
    try {
      // Check if the document exists by ID
      Documents document = documentRepository.findById(id)
          .orElseThrow(() -> new DocumentServiceException("Document not found with ID: " + id, HttpStatus.NOT_FOUND));

      // Delete the document from the database
      documentRepository.deleteById(id);

      // Send a notification regarding the deletion of the document
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, document, NotificationType.DELETE);
      } catch (NotificationServiceException e) {
        // If notification fails, log the error
        logger.error("Notification error while deleting document with ID: {}", id, e);
      }

    } catch (DocumentServiceException e) {
      logger.error("Error deleting document with ID: {}", id, e);
      throw e;
    } catch (RuntimeException e) {
      logger.error("Unexpected error while deleting document with ID: {}", id, e);
      throw new DocumentServiceException("Unexpected error while deleting document with ID: " + id, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Retrieves documents with optional filtering and sorting.
   *
   * @param documentType The type of the document to filter by.
   * @param user The user to filter by.
   * @param verified The verification status to filter by.
   * @param pageable The pagination information.
   * @return A paginated response DTO containing documents that match the filter criteria.
   * @throws DocumentServiceException if unexpected errors occur during retrieval.
   */
  @Override
  @Cacheable(value = "filteredDocuments", key = "{#documentType, #user, #verified, #pageable.pageNumber, #pageable.pageSize}")
  public PaginatedResponseDto<Documents> getDocuments(String documentType, String user, Boolean verified, Pageable pageable) {
    try {
      logger.info("Fetching documents with filters - documentType: {}, user: {}, verified: {}", documentType, user, verified);
      // Retrieve filtered documents from the repository
      Page<Documents> documentsPage = documentRepository.findByFilter(documentType, user, verified, pageable);
      // Wrap the result in a response DTO and return
      return new PaginatedResponseDto<>(documentsPage);
    } catch (RuntimeException e) {
      logger.error("Error fetching documents with filters - documentType: {}, user: {}, verified: {}", documentType, user, verified, e);
      throw new DocumentServiceException("Error fetching documents with filters", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }
}

