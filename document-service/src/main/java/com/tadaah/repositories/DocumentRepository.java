package com.tadaah.repositories;

import com.tadaah.models.Documents;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Documents, String> {
}
