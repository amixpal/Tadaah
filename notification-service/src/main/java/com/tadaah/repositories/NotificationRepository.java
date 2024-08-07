package com.tadaah.repositories;

import com.tadaah.models.Notifications;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends ReactiveMongoRepository<Notifications, String> {

  @Query("{ 'notificationType': ?0, 'userName': ?1, 'documentName': ?2 }")
  Flux<Notifications> findByFilters(String notificationType, String userName, String documentName);

  @Override
  Flux<Notifications> findAll();
}

