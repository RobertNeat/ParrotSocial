package com.example.ParrotSocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String message_id;
    private String conversation_id;
    private String sender_id;
    private String text;
    private String image;
    private LocalDateTime send_date;
}
