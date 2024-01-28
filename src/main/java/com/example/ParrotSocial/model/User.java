package com.example.ParrotSocial.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String user_id;
    private String name="";
    private String surname="";
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
    private List<String> followers;
    private List<String> followings;
    private String profile_picture="";
    private String cover_picture="";
    private String description="";
    private City inhabitancy;
    private City provenance;
    private String relationship_status="";
    private String education="";
    private String work="";
    private Date last_login_logout;
    private Boolean isOnline=false;

}
