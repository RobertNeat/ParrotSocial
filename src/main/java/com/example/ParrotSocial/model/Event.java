package com.example.ParrotSocial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    private String event_id;
    private String title;
    private String description="";
    private String image="";
    private List<String> members;
    private Date start_date;
    private Date end_date;
}
