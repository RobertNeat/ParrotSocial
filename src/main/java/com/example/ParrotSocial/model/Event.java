package com.example.ParrotSocial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(collection = "event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    private String event_id;
    private String title;
    private String description="";
    private String image="";
    private List<String> members;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
