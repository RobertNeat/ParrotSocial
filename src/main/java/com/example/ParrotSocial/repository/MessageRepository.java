package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Conversation,String> {
}
