package com.tadaah.repositories;

import com.tadaah.models.Notifications;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notifications, String> {
}
