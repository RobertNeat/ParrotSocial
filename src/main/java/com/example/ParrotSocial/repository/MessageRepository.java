package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Conversation;
import com.example.ParrotSocial.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //?? optional IDK does id do anything
public interface MessageRepository extends MongoRepository<Message,String> {

    List<Message> findByConversationid(String conversation_id);
    Optional<Message> findByImage(String image);

}
