//configuration class for making queries to mongodb
package com.example.ParrotSocial.config;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient){
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, "ParrotSocialDatabase"));
    }
}
