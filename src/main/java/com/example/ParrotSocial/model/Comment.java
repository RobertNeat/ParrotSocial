package com.example.ParrotSocial.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;
import java.time.LocalDateTime;

@Document(collection = "comment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
    @Id
    private String commentId;
    private String userId;
    private String description;
    private List<String> likes;
    private LocalDateTime created;
    private String postId;
}
