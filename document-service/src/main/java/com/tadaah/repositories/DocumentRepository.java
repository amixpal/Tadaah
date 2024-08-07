package com.tadaah.repositories;

import com.tadaah.models.Documents;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DocumentRepository extends MongoRepository<Documents, String> {
  List<Documents> findByUserName(String userName);
  List<Documents> findByDocumentTypeAndUserNameAndVerified(String documentType, String userName, boolean verified);

  @Query("{ $and: [ { $or: [ { 'documentType': ?0 }, { 'userName': ?1 }, { 'verified': ?2 } ] } ] }")
  Page<Documents> findByFilter(String documentType, String userName, Boolean verified, Pageable pageable);
}
