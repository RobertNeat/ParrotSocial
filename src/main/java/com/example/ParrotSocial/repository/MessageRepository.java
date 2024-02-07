package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message,String> {
    List<Message> findByConversationId(String conversation_id);
    Optional<Message> findByImage(String image);
}
