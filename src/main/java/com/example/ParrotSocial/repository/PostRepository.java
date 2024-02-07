package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {
}
