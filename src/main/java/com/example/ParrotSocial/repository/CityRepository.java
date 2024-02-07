package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CityRepository extends MongoRepository<City,String> {
    Optional<City> findByName(String name);
}
