package com.example.ParrotSocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String post_id;
    private String user_id;
    private String description;
    private String image;
    private List<String> likes;
    private LocalDateTime created;
    private List<Comment> comments;
}
