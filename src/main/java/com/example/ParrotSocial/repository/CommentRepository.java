package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment,String> {
}
