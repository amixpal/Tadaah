package com.tadaah.utils;

import com.tadaah.exceptions.DocumentServiceException;
import com.tadaah.models.Dto.request.DocumentDto;
import com.tadaah.models.Users;
import com.tadaah.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;

@Slf4j
public class DocumentValidationUtil {

  // Private constructor to prevent instantiation
  private DocumentValidationUtil() {}

  /**
   * Validates the document based on business rules.
   *
   * @param document The document to be validated.
   * @param userRepository The user repository to fetch user details.
   */
  public static void validateDocument(DocumentDto document, UserRepository userRepository) {
    Users user = userRepository.findById(document.getUserName())
        .orElseThrow(() -> {
          log.error("User not found: {}", document.getUserName());
          return new DocumentServiceException("User not found: " + document.getUserName(), HttpStatus.BAD_REQUEST);
        });

    if (document.getName() == null || !document.getName().startsWith(user.getUserName())) {
      log.warn("Document name does not start with the owner's username: {}", document.getName());
      throw new DocumentServiceException("Document name must start with the owner's username.", HttpStatus.BAD_REQUEST);
    }

    if (document.getExpiryDate() == null || document.getExpiryDate().isBefore(LocalDate.now().plusDays(60))) {
      log.warn("Document expiry date is less than 60 days in the future: {}", document.getExpiryDate());
      throw new DocumentServiceException("Document expiry date must be at least 60 days in the future.", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Automatically verifies the document based on validation.
   *
   * @param document The document to be verified.
   * @param userRepository The user repository to fetch user details.
   * @return True if the document is valid, otherwise false.
   */
  public static boolean verifyDocument(DocumentDto document, UserRepository userRepository) {
    validateDocument(document, userRepository);
    log.info("Document is valid and verified: {}", document);
    return true;
  }
}
