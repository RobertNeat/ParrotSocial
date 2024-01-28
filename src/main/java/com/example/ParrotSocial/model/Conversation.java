package com.example.ParrotSocial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection="conversation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    private String conversation_id;
    private List<User> members;
    private Date creationDate;
}
