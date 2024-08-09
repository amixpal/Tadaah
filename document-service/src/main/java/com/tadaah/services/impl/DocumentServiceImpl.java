package com.tadaah.services.impl;

import com.tadaah.exceptions.DocumentServiceException;
import com.tadaah.exceptions.NotificationServiceException;
import com.tadaah.models.DocumentType;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {


  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private CacheManager cacheManager;

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
  @CacheEvict(value = "filteredDocumentsCache", allEntries = true)
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

      // Manually cache the document under multiple keys
      cacheDocument(savedDocument);

      // Send a notification regarding the creation of the document
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, savedDocument,
            NotificationType.CREATE);
      } catch (NotificationServiceException e) {
        // If notification fails, log the error
        log.error("Notification error while creating document for user: {}",
            savedDocument.getUserName(), e);
        savedDocument.setNotificationError("Notification failed: " + e.getMessage());
        documentRepository.save(savedDocument);  // Update the document with the notification error
      }

      return savedDocument;

    } catch (DocumentServiceException e) {
      log.error("Validation error while creating document for user: {}", documentDto.getUserName(),
          e);
      throw e;

    } catch (RuntimeException e) {
      log.error("Unexpected error while creating document for user: {}", documentDto.getUserName(),
          e);
      throw new DocumentServiceException(
          "Unexpected error while creating document for user: " + documentDto.getUserName(),
          HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Updates an existing document.
   *
   * @param id          The ID of the document to be updated.
   * @param documentDto The document details to be updated.
   * @return The updated document with verification and possible notification error.
   * @throws DocumentServiceException if validation or unexpected errors occur during updating.
   */
  @Override
  @CacheEvict(value = "filteredDocumentsCache", allEntries = true)
  // Clears filtered cache since filtering criteria may change
  public Documents updateDocument(String id, DocumentDto documentDto) {
    try {
      // Retrieve existing document by ID or throw an exception if not found
      Documents existingDocument = documentRepository.findById(id)
          .orElseThrow(() -> new DocumentServiceException("Document not found with ID: " + id,
              HttpStatus.NOT_FOUND));

      // Create a new DocumentDto that merges data from both the existing document and the incoming DTO
      DocumentDto verificationDto = new DocumentDto();

      // Manually copy fields from existingDocument to verificationDto
      verificationDto.setName(existingDocument.getName());
      verificationDto.setDocumentType(existingDocument.getDocumentType());
      verificationDto.setUserName(existingDocument.getUserName());
      verificationDto.setFileUrl(existingDocument.getFileUrl());
      verificationDto.setExpiryDate(existingDocument.getExpiryDate());

      // Overlay the incoming documentDto fields (only non-null values will overwrite)
      if (documentDto.getName() != null) {
        verificationDto.setName(documentDto.getName());
      }
      if (documentDto.getDocumentType() != null) {
        verificationDto.setDocumentType(documentDto.getDocumentType());
      }
      if (documentDto.getUserName() != null) {
        verificationDto.setUserName(documentDto.getUserName());
      }
      if (documentDto.getFileUrl() != null) {
        verificationDto.setFileUrl(documentDto.getFileUrl());
      }
      if (documentDto.getExpiryDate() != null) {
        verificationDto.setExpiryDate(documentDto.getExpiryDate());
      }

      // Verify the combined data using the verification DTO
      boolean isVerified = DocumentValidationUtil.verifyDocument(verificationDto, userRepository);
      if (!isVerified) {
        throw new DocumentServiceException("Document verification failed", HttpStatus.BAD_REQUEST);
      }

      // Merge fields from the validated documentDto into the existing document
      GenericUtils.mergeObjects(existingDocument, verificationDto);

      // Save the updated document
      Documents updatedDocument = documentRepository.save(existingDocument);

      // Manually cache the document under multiple keys
      cacheDocument(updatedDocument);

      // Send a notification regarding the update of the document
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, updatedDocument,
            NotificationType.UPDATE);
      } catch (NotificationServiceException e) {
        // If notification fails, log the error
        log.error("Notification error while updating document for user: {}",
            updatedDocument.getUserName(), e);
        updatedDocument.setNotificationError("Notification failed: " + e.getMessage());
        documentRepository.save(
            updatedDocument);  // Update the document with the notification error
      }

      return updatedDocument;

    } catch (DocumentServiceException e) {
      log.error("Validation error while updating document with ID: {}", id, e);
      throw e;
    } catch (RuntimeException e) {
      log.error("Unexpected error while updating document with ID: {}", id, e);
      throw new DocumentServiceException("Unexpected error while updating document with ID: " + id,
          HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }


  /**
   * Manually cache a document under multiple keys.
   */
  private void cacheDocument(Documents document) {
    Cache cache = cacheManager.getCache("documentsCache");
    if (cache != null) {
      // Cache by document ID
      cache.put(document.getId(), document);

      // Cache by document name
      cache.put(document.getName(), document);

      // Cache by username
      cache.put(document.getUserName(), document);
    }
  }


  /**
   * Deletes a document.
   *
   * @param id The ID of the document to be deleted.
   * @throws DocumentServiceException if the document is not found or unexpected errors occur during
   *                                  deletion.
   */
  @Override
  @CacheEvict(value = {"documentsCache", "filteredDocumentsCache"}, allEntries = true)
  public void deleteDocument(String id) {
    try {
      // Check if the document exists by ID
      Documents document = documentRepository.findById(id)
          .orElseThrow(() -> new DocumentServiceException("Document not found with ID: " + id,
              HttpStatus.NOT_FOUND));

      // Delete the document from the database
      documentRepository.deleteById(id);

      // Send a notification regarding the deletion of the document
      try {
        NotificationUtil.sendNotification(restTemplate, notificationServiceUrl, document,
            NotificationType.DELETE);
      } catch (NotificationServiceException e) {
        // If notification fails, log the error
        log.error("Notification error while deleting document with ID: {}", id, e);
      }

    } catch (DocumentServiceException e) {
      log.error("Error deleting document with ID: {}", id, e);
      throw e;
    } catch (RuntimeException e) {
      log.error("Unexpected error while deleting document with ID: {}", id, e);
      throw new DocumentServiceException("Unexpected error while deleting document with ID: " + id,
          HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Retrieves documents with optional filtering and sorting.
   *
   * @param documentType         The type of the document to filter by.
   * @param user                 The user to filter by.
   * @param verified             The verification status to filter by.
   * @param isNotificationFailed Whether to filter documents with a notification error.
   * @param pageable             The pagination information.
   * @return A paginated response DTO containing documents that match the filter criteria.
   * @throws DocumentServiceException if unexpected errors occur during retrieval.
   */
  @Override
  @Cacheable(value = "filteredDocumentsCache", key = "{#documentType != null ? #documentType.name() : 'all', #user != null ? #user : 'all', #verified != null ? #verified : 'all', #isNotificationFailed != null ? #isNotificationFailed : 'all', #pageable.pageNumber, #pageable.pageSize}")
  public PaginatedResponseDto<Documents> getDocuments(DocumentType documentType, String user, Boolean verified, Boolean isNotificationFailed, Pageable pageable) {
    try {
      log.info("Fetching documents with filters - documentType: {}, user: {}, verified: {}, isNotificationFailed: {}", documentType, user, verified, isNotificationFailed);

      // Retrieve filtered documents from the repository
      Page<Documents> documentsPage = documentRepository.findByFilter(
          documentType,
          user,
          verified,
          isNotificationFailed != null && isNotificationFailed ? Boolean.TRUE : null,
          pageable
      );

      // Wrap the result in a response DTO and return
      return new PaginatedResponseDto<>(documentsPage);
    } catch (RuntimeException e) {
      log.error("Error fetching documents with filters - documentType: {}, user: {}, verified: {}, isNotificationFailed: {}", documentType, user, verified, isNotificationFailed, e);
      throw new DocumentServiceException("Error fetching documents with filters", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }
}

