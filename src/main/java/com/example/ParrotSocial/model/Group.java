package com.example.ParrotSocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "group")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    private String group_id;
    private String owner_id;
    private String name;
    private String description;
    private String cover_picture;
    private List<String> members;
    private List<Post> posts;
    private LocalDateTime created;
}
