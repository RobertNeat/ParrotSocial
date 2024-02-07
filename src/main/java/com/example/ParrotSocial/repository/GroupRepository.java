package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Group;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group,String> {
    Optional<Group> findByCoverPicture(String picture);
    @Query("{'posts.postId': ?0}")
    Optional<Group> findByPostId(String post_id);
}
