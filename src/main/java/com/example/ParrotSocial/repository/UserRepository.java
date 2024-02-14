package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProfilePicture(String profile_picture);
    Optional<User> findByCoverPicture(String cover_picture);
    Optional<User> findByEmailAndPassword(String email, String password);
}
