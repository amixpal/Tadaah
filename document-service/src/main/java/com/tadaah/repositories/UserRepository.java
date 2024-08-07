package com.tadaah.repositories;

import com.tadaah.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<Users, String> {

  @Query("{ $and: [ { 'userName': { $regex: ?0, $options: 'i' } }] }")
  Page<Users> findByUsername(String username, Pageable pageable);

}
