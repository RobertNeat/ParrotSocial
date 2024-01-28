package com.example.ParrotSocial.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "city")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    private String city_id;
    private String name;
}
