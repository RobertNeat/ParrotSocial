package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends MongoRepository<City,String> {
}
