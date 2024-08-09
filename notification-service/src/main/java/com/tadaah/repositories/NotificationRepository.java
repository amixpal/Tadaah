package com.tadaah.repositories;

import com.tadaah.models.Notifications;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationRepository extends ReactiveMongoRepository<Notifications, String> { }

