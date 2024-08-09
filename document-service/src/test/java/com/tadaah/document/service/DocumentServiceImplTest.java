package com.tadaah.document.service;

import com.tadaah.exceptions.DocumentServiceException;
import com.tadaah.models.Documents;
import com.tadaah.models.Dto.request.DocumentDto;
import com.tadaah.models.Users;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.repositories.DocumentRepository;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.impl.DocumentServiceImpl;
import com.tadaah.utils.DocumentValidationUtil;
import com.tadaah.utils.NotificationUtil;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentServiceImplTest {

  @InjectMocks
  private DocumentServiceImpl documentService;

  @Mock
  private DocumentRepository documentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateDocument_Success() {
    DocumentDto documentDto = new DocumentDto();
    documentDto.setUserName("user1");
    documentDto.setName("user1_document");
    documentDto.setExpiryDate(LocalDate.now().plusDays(90));

    Users user = new Users();
    user.setUserName("user1");

    Documents savedDocument = new Documents();
    savedDocument.setUserName("user1");
    savedDocument.setName("user1_document");
    savedDocument.setExpiryDate(LocalDate.now().plusDays(90));

    // Mocking
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(documentRepository.save(any(Documents.class))).thenReturn(savedDocument);

    // Method under test
    Documents createdDocument = documentService.createDocument(documentDto);

    // Assertions
    assertNotNull(createdDocument);
    assertEquals("user1", createdDocument.getUserName());
    assertEquals("user1_document", createdDocument.getName());
    assertEquals(LocalDate.now().plusDays(90), createdDocument.getExpiryDate());

    // Verifications
    verify(documentRepository, times(1)).save(any(Documents.class));
    verify(userRepository, times(1)).findById(anyString());
  }


  @Test
  public void testCreateDocument_ValidationError() {
    DocumentDto documentDto = new DocumentDto();
    documentDto.setUserName("user1");

    // Mocking validation exception
    doThrow(new DocumentServiceException("Validation error", HttpStatus.BAD_REQUEST))
        .when(userRepository).findById(anyString());

    // Method under test
    DocumentServiceException exception = assertThrows(DocumentServiceException.class, () -> {
      documentService.createDocument(documentDto);
    });

    // Assertions
    assertEquals("Validation error", exception.getMessage());

    // Verifications
    verify(documentRepository, never()).save(any(Documents.class));
    verify(userRepository, times(1)).findById(anyString());
  }


  @Test
  public void testUpdateDocument_Success() {
    String id = "doc1";

    DocumentDto documentDto = new DocumentDto();
    documentDto.setUserName("user2");

    Documents existingDocument = new Documents();
    existingDocument.setUserName("user1");

    Documents updatedDocument = new Documents();
    updatedDocument.setUserName("user2");

    // Mock repository behavior
    when(documentRepository.findById(id)).thenReturn(Optional.of(existingDocument));
    when(documentRepository.save(any(Documents.class))).thenReturn(updatedDocument);

    Documents result = documentService.updateDocument(id, documentDto);

    // Assertions
    assertNotNull(result);
    assertEquals("user2", result.getUserName());

    // Verifications
    verify(documentRepository, times(1)).findById(id);
    verify(documentRepository, times(1)).save(any(Documents.class));
  }


  @Test
  public void testUpdateDocument_DocumentNotFound() {
    String id = "doc1";
    DocumentDto documentDto = new DocumentDto();

    when(documentRepository.findById(id)).thenReturn(Optional.empty());

    DocumentServiceException exception = assertThrows(DocumentServiceException.class, () -> {
      documentService.updateDocument(id, documentDto);
    });

    // Assertions
    assertEquals("Document not found with ID: " + id, exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

    // Verifications
    verify(documentRepository, times(1)).findById(id);
    verify(documentRepository, never()).save(any(Documents.class));
  }


  @Test
  public void testDeleteDocument_Success() {
    String id = "doc1";

    when(documentRepository.existsById(id)).thenReturn(true);
    doNothing().when(documentRepository).deleteById(id);

    assertDoesNotThrow(() -> documentService.deleteDocument(id));
    verify(documentRepository, times(1)).existsById(id);
    verify(documentRepository, times(1)).deleteById(id);
  }

  @Test
  public void testDeleteDocument_DocumentNotFound() {
    String id = "doc1";

    when(documentRepository.existsById(id)).thenReturn(false);

    DocumentServiceException exception = assertThrows(DocumentServiceException.class, () -> {
      documentService.deleteDocument(id);
    });

    assertEquals("Document not found with ID: " + id, exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    verify(documentRepository, times(1)).existsById(id);
    verify(documentRepository, never()).deleteById(id);
  }

//  @Test
//  public void testGetDocuments_Success() {
//    Documents document1 = new Documents();
//    document1.setUserName("user1");
//
//    Documents document2 = new Documents();
//    document2.setUserName("user2");
//
//    List<Documents> documentList = Arrays.asList(document1, document2);
//    Page<Documents> documentPage = new PageImpl<>(documentList);
//    Pageable pageable = PageRequest.of(0, 10);
//
//    when(documentRepository.findByFilter("type", "user", true, pageable)).thenReturn(documentPage);
//
//    PaginatedResponseDto<Documents> result = documentService.getDocuments("type", "user", true, pageable);
//
//    assertNotNull(result);
//    assertEquals(2, result.getTotalElements());
//    verify(documentRepository, times(1)).findByFilter("type", "user", true, pageable);
//  }
}
