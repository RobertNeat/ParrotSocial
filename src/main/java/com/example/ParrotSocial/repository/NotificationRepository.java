package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {
}
