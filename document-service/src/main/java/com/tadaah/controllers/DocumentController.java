package com.tadaah.controllers;

import com.tadaah.models.Documents;
import com.tadaah.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
  @Autowired
  private DocumentService documentService;

  @PostMapping
  public ResponseEntity<Documents> createDocument(@RequestBody Documents document) {
    Documents createdDocument = documentService.createDocument(document);
    return ResponseEntity.status(201).body(createdDocument);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Documents> updateDocument(@PathVariable String id, @RequestBody Documents document) {
    Documents updatedDocument = documentService.updateDocument(id, document);
    return ResponseEntity.ok(updatedDocument);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
    documentService.deleteDocument(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<Documents>> getAllDocuments() {
    List<Documents> documents = documentService.getAllDocuments();
    return ResponseEntity.ok(documents);
  }
}
