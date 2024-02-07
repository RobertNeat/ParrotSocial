package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Conversation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation,String> {
    @Query("{'members': {$all: ?0}}")
    Optional<Conversation> findByMembersInAnyOrder(List<String> members);
    @Query("{'members': ?0}")
    List<Conversation> findByUserIdInMembers(String userId);

}
