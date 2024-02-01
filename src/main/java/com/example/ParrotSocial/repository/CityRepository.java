package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends MongoRepository<City,String> {
    Optional<City> findByName(String name);
}
