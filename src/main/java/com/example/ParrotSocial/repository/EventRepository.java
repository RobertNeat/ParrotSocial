package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventRepository extends MongoRepository<Event,String> {
    Optional<Event> findByImage(String image);
}
