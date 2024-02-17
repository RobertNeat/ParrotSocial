package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.repository.CityRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://127.0.0.1:3000")//5500
@RestController
@RequestMapping("/api/city")
public class CityController {

    private final CityRepository repository;
    private final MongoTemplate mongoTemplate;

    public CityController(CityRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(repository.findAll());
    }
}
