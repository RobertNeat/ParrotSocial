package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group,String> {
}
