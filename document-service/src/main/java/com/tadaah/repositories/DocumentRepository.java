package com.tadaah.repositories;

import com.tadaah.models.DocumentType;
import com.tadaah.models.Documents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DocumentRepository extends MongoRepository<Documents, String> {

  List<Documents> findByUserName(String userName);

  List<Documents> findByDocumentTypeAndUserNameAndVerified(DocumentType documentType, String userName, boolean verified);

  @Query("{ $and: [ "
      + "{ $or: [ { 'documentType': ?0 }, { 'userName': ?1 }, { 'verified': ?2 } ] }, "
      + "{ $expr: { $cond: [ { $eq: [?3, true] }, { $ne: ['$notificationError', null] }, {} ] } } ] }")
  Page<Documents> findByFilter(DocumentType documentType, String user, Boolean verified, Boolean isNotificationFailed, Pageable pageable);
}
