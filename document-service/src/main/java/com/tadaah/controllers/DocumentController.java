package com.tadaah.controllers;

import com.tadaah.models.Documents;
import com.tadaah.models.Dto.request.DocumentFilterRequestDto;
import com.tadaah.models.Dto.response.DocumentFilterResponseDto;
import com.tadaah.models.Dto.response.ResponseDto;
import com.tadaah.services.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/documents")
public class DocumentController {
  private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

  @Autowired
  private DocumentService documentService;

  /**
   * Create a new document.
   *
   * @param document The document to be created.
   * @return The created document.
   */
  @PostMapping
  public ResponseDto<Documents> createDocument(@RequestBody Documents document) {
    logger.info("Creating a new document: {}", document);
    Documents createdDocument = documentService.createDocument(document);
    return ResponseDto.success(createdDocument);
  }

  /**
   * Update an existing document.
   *
   * @param id The ID of the document to be updated.z
   * @param document The document details to be updated.
   * @return The updated document.
   */
  @PutMapping("/{id}")
  public ResponseDto<Documents> updateDocument(@PathVariable String id, @RequestBody Documents document) {
    logger.info("Updating document with ID: {}", id);
    Documents updatedDocument = documentService.updateDocument(id, document);
    return ResponseDto.success(updatedDocument);
  }

  /**
   * Delete a document.
   *
   * @param id The ID of the document to be deleted.
   * @return No content.
   */
  @DeleteMapping("/{id}")
  public ResponseDto<Void> deleteDocument(@PathVariable String id) {
    logger.info("Deleting document with ID: {}", id);
    documentService.deleteDocument(id);
    return ResponseDto.success(null);
  }

  /**
   * Retrieves documents based on the provided filters.
   *
   * @param filter The filter criteria including documentType, user, verified status, page, and size.
   * @return A ResponseDto containing a DocumentFilterResponseDto with the filtered documents and pagination details.
   */
  @PostMapping("/filter")
  public ResponseDto<DocumentFilterResponseDto<Documents>> getDocuments(@RequestBody DocumentFilterRequestDto filter) {
    logger.info("Fetching documents with filters - documentType: {}, user: {}, verified: {}", filter.getDocumentType(), filter.getUser(), filter.getVerified());
    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
    DocumentFilterResponseDto<Documents> response = documentService.getDocuments(filter.getDocumentType(), filter.getUser(), filter.getVerified(), pageable);
    return ResponseDto.success(response);
  }
}
