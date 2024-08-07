package com.tadaah.controllers;

import com.tadaah.models.Documents;
import com.tadaah.models.Dto.response.DocumentFilterDto;
import com.tadaah.services.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<Documents> createDocument(@RequestBody Documents document) {
    logger.info("Creating a new document: {}", document);
    Documents createdDocument = documentService.createDocument(document);
    return ResponseEntity.status(201).body(createdDocument);
  }

  /**
   * Update an existing document.
   *
   * @param id The ID of the document to be updated.z
   * @param document The document details to be updated.
   * @return The updated document.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Documents> updateDocument(@PathVariable String id, @RequestBody Documents document) {
    logger.info("Updating document with ID: {}", id);
    Documents updatedDocument = documentService.updateDocument(id, document);
    return ResponseEntity.ok(updatedDocument);
  }

  /**
   * Delete a document.
   *
   * @param id The ID of the document to be deleted.
   * @return No content.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
    logger.info("Deleting document with ID: {}", id);
    documentService.deleteDocument(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/filter")
  public ResponseEntity<DocumentFilterDto<Documents>> getDocuments(@RequestBody com.tadaah.models.Dto.request.DocumentFilterDto filter) {
    logger.info("Fetching documents with filters - documentType: {}, user: {}, verified: {}", filter.getDocumentType(), filter.getUser(), filter.getVerified());
    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
    DocumentFilterDto<Documents> response = documentService.getDocuments(filter.getDocumentType(), filter.getUser(), filter.getVerified(), pageable);
    return ResponseEntity.ok(response);
  }
}
