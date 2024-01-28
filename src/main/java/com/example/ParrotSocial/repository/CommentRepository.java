package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository //?? optional IDK does id do anything
public interface CommentRepository extends MongoRepository<Comment,String> {
}
