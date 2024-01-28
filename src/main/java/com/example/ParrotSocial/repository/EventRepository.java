package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event,String> {
}
