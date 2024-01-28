package com.example.ParrotSocial.repository;

import com.example.ParrotSocial.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository //?? optional IDK does id do anything
public interface GroupRepository extends MongoRepository<Group,String> {
}
