package com.tadaah.document.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tadaah.exceptions.DocumentServiceException;
import com.tadaah.models.DocumentType;
import com.tadaah.models.Documents;
import com.tadaah.models.Dto.request.DocumentDto;
import com.tadaah.models.Users;
import com.tadaah.repositories.DocumentRepository;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.impl.DocumentServiceImpl;
import java.time.LocalDate;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.client.RestTemplate;

class DocumentServiceImplTest {

  @Mock
  private DocumentRepository documentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private CacheManager cacheManager;

  @Mock
  private Cache cache;

  @InjectMocks
  private DocumentServiceImpl documentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(cacheManager.getCache(anyString())).thenReturn(cache);
  }

  @Test
  void createDocument_Success() {
    // Arrange
    DocumentDto documentDto = new DocumentDto();
    documentDto.setName("john_doe_Passport");
    documentDto.setDocumentType(DocumentType.ID_VERIFICATION);
    documentDto.setUserName("john_doe");
    documentDto.setFileUrl("http://example.com/file.pdf");
    documentDto.setExpiryDate(LocalDate.now().plusDays(90));

    Users user = new Users();
    user.setUserName("john_doe");

    // Create a mock document with an ID set, which mimics what the repository would return after saving
    Documents document = new Documents();
    document.setId(new ObjectId());
    document.setName(documentDto.getName());
    document.setDocumentType(documentDto.getDocumentType());
    document.setUserName(documentDto.getUserName());
    document.setFileUrl(documentDto.getFileUrl());
    document.setExpiryDate(documentDto.getExpiryDate());
    document.setVerified(true);

    when(userRepository.findById("john_doe")).thenReturn(Optional.of(user));
    when(documentRepository.save(any(Documents.class))).thenReturn(document);

    // Act
    Documents result = documentService.createDocument(documentDto);

    // Assert
    assertEquals(documentDto.getName(), result.getName());
    assertEquals(document.getId(), result.getId());
    verify(documentRepository, times(1)).save(any(Documents.class));
    verify(cacheManager, times(1)).getCache("documentsCache");
    verify(cache, times(3)).put(any(), any()); // assuming you're caching by ID, name, and username
  }


  @Test
  void createDocument_DocumentNameFailure() {
    // Arrange
    DocumentDto documentDto = new DocumentDto();
    documentDto.setName("incorrect_name"); // This name does not start with the user's username
    documentDto.setDocumentType(DocumentType.ID_VERIFICATION);
    documentDto.setUserName("john_doe");
    documentDto.setFileUrl("http://example.com/file.pdf");
    documentDto.setExpiryDate(LocalDate.now().plusDays(90)); // This is valid, but the name will cause the failure

    Users user = new Users();
    user.setUserName("john_doe");

    when(userRepository.findById("john_doe")).thenReturn(Optional.of(user));

    // Act & Assert
    DocumentServiceException exception = assertThrows(DocumentServiceException.class, () -> {
      documentService.createDocument(documentDto);
    });

    assertEquals("Document name must start with the owner's username.", exception.getMessage());
    verify(documentRepository, times(0)).save(any(Documents.class)); // Ensure that the save operation is never called
    verify(cacheManager, times(0)).getCache(anyString()); // Ensure that caching operations are never called
  }

  @Test
  void createDocument_ExpiryDateValidationFailure() {
    // Arrange
    DocumentDto documentDto = new DocumentDto();
    documentDto.setName("john_doe_Passport"); // This name is valid and starts with the user's username
    documentDto.setDocumentType(DocumentType.ID_VERIFICATION);
    documentDto.setUserName("john_doe");
    documentDto.setFileUrl("http://example.com/file.pdf");
    documentDto.setExpiryDate(LocalDate.now().plusDays(30)); // Less than 60 days in the future, should trigger validation failure

    Users user = new Users();
    user.setUserName("john_doe");

    when(userRepository.findById("john_doe")).thenReturn(Optional.of(user));

    // Act & Assert
    DocumentServiceException exception = assertThrows(DocumentServiceException.class, () -> {
      documentService.createDocument(documentDto);
    });

    assertEquals("Document expiry date must be at least 60 days in the future.", exception.getMessage());
    verify(documentRepository, times(0)).save(any(Documents.class)); // Ensure that the save operation is never called
    verify(cacheManager, times(0)).getCache(anyString()); // Ensure that caching operations are never called
  }

  @Test
  void updateDocument_Success() {
    // Arrange
    ObjectId objectId = new ObjectId();  // Create a new ObjectId
    String documentId = objectId.toHexString();  // Convert ObjectId to its hex string representation

    // Prepare the existing document in the repository
    Documents existingDocument = new Documents();
    existingDocument.setId(objectId);  // Set the ObjectId directly
    existingDocument.setName("john_doe_Passport");
    existingDocument.setDocumentType(DocumentType.ID_VERIFICATION);
    existingDocument.setUserName("john_doe");
    existingDocument.setFileUrl("http://example.com/file.pdf");
    existingDocument.setExpiryDate(LocalDate.now().plusDays(90));  // Expiry date 90 days in the future
    existingDocument.setVerified(true);

    // Prepare the incoming DocumentDto with updated information
    DocumentDto incomingDocumentDto = new DocumentDto();
    incomingDocumentDto.setName("john_doe_Passport_Updated");
    incomingDocumentDto.setDocumentType(DocumentType.ID_VERIFICATION);
    incomingDocumentDto.setUserName("john_doe");
    incomingDocumentDto.setFileUrl("http://example.com/file_updated.pdf");
    incomingDocumentDto.setExpiryDate(LocalDate.now().plusDays(120)); // New valid expiry date

    // Mock the repositories
    Users user = new Users();
    user.setUserName("john_doe");

    when(documentRepository.findById(documentId)).thenReturn(Optional.of(existingDocument));
    when(documentRepository.save(any(Documents.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(userRepository.findById("john_doe")).thenReturn(Optional.of(user));  // Mock user retrieval

    // Act
    Documents result = documentService.updateDocument(documentId, incomingDocumentDto);

    // Assert
    assertEquals(incomingDocumentDto.getName(), result.getName());
    assertEquals(incomingDocumentDto.getFileUrl(), result.getFileUrl());
    assertEquals(incomingDocumentDto.getExpiryDate(), result.getExpiryDate());
    verify(documentRepository, times(1)).save(any(Documents.class));  // Ensure save is called once
    verify(cacheManager, times(1)).getCache("documentsCache");
    verify(cache, times(3)).put(any(), any()); // Assuming caching by ID, name, and username
  }

  @Test
  void deleteDocument_Success() {
    // Arrange
    ObjectId objectId = new ObjectId();  // Create a new ObjectId
    String documentId = objectId.toHexString();  // Convert ObjectId to its hex string representation

    // Prepare the existing document in the repository
    Documents existingDocument = new Documents();
    existingDocument.setId(objectId);  // Set the ObjectId directly
    existingDocument.setName("john_doe_Passport");
    existingDocument.setDocumentType(DocumentType.ID_VERIFICATION);
    existingDocument.setUserName("john_doe");
    existingDocument.setFileUrl("http://example.com/file.pdf");
    existingDocument.setExpiryDate(LocalDate.now().plusDays(90));  // Expiry date 90 days in the future
    existingDocument.setVerified(true);

    // Mock the repository to return the existing document when findById is called
    when(documentRepository.findById(documentId)).thenReturn(Optional.of(existingDocument));

    // Act
    documentService.deleteDocument(documentId);

    // Assert
    verify(documentRepository, times(1)).findById(documentId);  // Ensure findById is called once
    verify(documentRepository, times(1)).deleteById(documentId);  // Ensure deleteById is called once
  }


  @Test
  void deleteDocument_DocumentNotFound() {
    // Arrange
    ObjectId objectId = new ObjectId();  // Create a new ObjectId
    String documentId = objectId.toHexString();  // Convert ObjectId to its hex string representation

    // Mock the repository to return empty when the document is not found
    when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

    // Act & Assert
    DocumentServiceException exception = assertThrows(DocumentServiceException.class, () -> {
      documentService.deleteDocument(documentId);
    });

    assertEquals("Document not found with ID: " + documentId, exception.getMessage());
    verify(documentRepository, times(1)).findById(documentId);  // Ensure findById is called once
    verify(documentRepository, times(0)).deleteById(documentId);  // Ensure deleteById is never called
    verify(restTemplate, times(0)).postForEntity(anyString(), any(), eq(Void.class));  // Ensure no notification is sent
  }










}
