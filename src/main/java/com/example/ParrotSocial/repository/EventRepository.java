package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //?? optional IDK does id do anything
public interface EventRepository extends MongoRepository<Event,String> {

    Optional<Event> findByImage(String image);
}
