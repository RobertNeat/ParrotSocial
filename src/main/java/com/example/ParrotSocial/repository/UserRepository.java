package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByEmail(String email);
}
