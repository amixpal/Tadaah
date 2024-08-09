package com.tadaah.controllers;

import com.tadaah.models.ApiError;
import com.tadaah.models.Documents;
import com.tadaah.models.Dto.request.DocumentDto;
import com.tadaah.models.Dto.request.DocumentFilterRequestDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Dto.response.ResponseDto;
import com.tadaah.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/documents")
@Tag(name = "Documents", description = "Operations related to documents")
public class DocumentController {

  private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

  @Autowired
  private DocumentService documentService;

  @PostMapping
  @Operation(
      summary = "Create a new document",
      description = "Creates a new document and returns the created document.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully created the document",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Documents.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": { \"id\": \"12345\", \"name\": \"Passport\", \"documentType\": \"ID_VERIFICATION\", \"userName\": \"john_doe\", \"fileUrl\": \"http://example.com/document.pdf\", \"expiryDate\": \"2024-12-31\", \"verified\": true, \"notificationError\": null }, \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid input",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"status\": \"BAD_REQUEST\", \"message\": \"Invalid input\", \"errors\": [\"Document name is required\"] }"
                  )
              )
          )
      }
  )
  public ResponseDto<Documents> createDocument(
      @Parameter(
          description = "Details of the document to be created",
          required = true
      )
      @RequestBody @Valid DocumentDto documentDto) {
    logger.info("Creating API called with document: {}", documentDto);
    Documents createdDocument = documentService.createDocument(documentDto);
    return ResponseDto.success(createdDocument);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing document",
      description = "Updates an existing document with the provided ID and returns the updated document.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully updated the document",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Documents.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": { \"id\": \"12345\", \"name\": \"Updated Passport\", \"documentType\": \"ID_VERIFICATION\", \"userName\": \"john_doe\", \"fileUrl\": \"http://example.com/updated_document.pdf\", \"expiryDate\": \"2025-12-31\", \"verified\": true, \"notificationError\": null }, \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid input",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"status\": \"BAD_REQUEST\", \"message\": \"Invalid input\", \"errors\": [\"Invalid document ID\"] }"
                  )
              )
          )
      }
  )
  public ResponseDto<Documents> updateDocument(
      @Parameter(
          description = "ID of the document to be updated",
          required = true
      )
      @PathVariable String id,
      @Parameter(
          description = "Updated details of the document",
          required = true
      )
      @RequestBody @Valid DocumentDto documentDto) {
    logger.info("updateDocument API called with ID: {} and document: {}", id, documentDto);
    Documents updatedDocument = documentService.updateDocument(id, documentDto);
    return ResponseDto.success(updatedDocument);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a document",
      description = "Deletes the document with the specified ID.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully deleted the document",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ResponseDto.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": \"Document deleted successfully\", \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Document not found",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"status\": \"NOT_FOUND\", \"message\": \"Document not found\", \"errors\": [\"Document with ID not found\"] }"
                  )
              )
          )
      }
  )
  public ResponseDto<String> deleteDocument(
      @Parameter(
          description = "ID of the document to be deleted",
          required = true
      )
      @PathVariable String id) {
    logger.info("deleteDocument API called with ID: {}", id);
    documentService.deleteDocument(id);
    return ResponseDto.success("Document deleted successfully");
  }

  @PostMapping("/filter")
  @Operation(
      summary = "Retrieve documents based on filters",
      description = "Retrieves a list of documents based on the provided filter criteria.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved the documents",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = PaginatedResponseDto.class),
                  examples = @ExampleObject(
                      name = "Success Example",
                      value = "{ \"success\": true, \"data\": { \"items\": [{ \"id\": \"12345\", \"name\": \"Passport\", \"documentType\": \"ID_VERIFICATION\", \"userName\": \"john_doe\", \"fileUrl\": \"http://example.com/document.pdf\", \"expiryDate\": \"2024-12-31\", \"verified\": true, \"notificationError\": null }], \"totalElements\": 1 }, \"error\": null }"
                  )
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid filter criteria",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiError.class),
                  examples = @ExampleObject(
                      name = "Error Example",
                      value = "{ \"status\": \"BAD_REQUEST\", \"message\": \"Invalid filter criteria\", \"errors\": [\"Document type is required\"] }"
                  )
              )
          )
      }
  )
  public ResponseDto<PaginatedResponseDto<Documents>> getDocuments(
      @Parameter(
          description = "Filter criteria for retrieving documents",
          required = true
      )
      @RequestBody DocumentFilterRequestDto filter) {
    logger.info("getDocuments API called with filters - documentType: {}, user: {}, verified: {}", filter.getDocumentType(), filter.getUser(), filter.getVerified());

    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

    PaginatedResponseDto<Documents> response = documentService.getDocuments(
        filter.getDocumentType(),
        filter.getUser(),
        filter.getVerified(),
        filter.getIsNotificationFailed(),
        pageable
    );

    return ResponseDto.success(response);
  }
}
