package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post,String> {
    List<Post> findByGroupId(String group_id);
    //List<Post> findByUserId(String user_id); - not used
    List<Post> findByGroupIdIsNullAndUserId(String user_id);
    Optional<Post> findByImage(String image);
}
