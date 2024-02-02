package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //?? optional IDK does id do anything
public interface ConversationRepository extends MongoRepository<Conversation,String> {
    @Query("{'members': {$all: ?0}}")
    Optional<Conversation> findByMembersInAnyOrder(List<String> members);

    @Query("{'members': ?0}")
    List<Conversation> findByUserIdInMembers(String userId);

}
