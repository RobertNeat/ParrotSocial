package com.example.ParrotSocial.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document(collection = "user")
@Data
@Builder //??idk if i will use it
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //only non-null fields from object will be stored in mongodb
public class User implements UserDetails { //implementation of the spring security for authentication (jwt) and authorization
    @Id
    private String user_id;
    private String name="";
    private String surname="";
    @NonNull
    private String email;
    @NonNull
    private String password;
    private List<String> followers;
    private List<String> followings;
    private String profile_picture="";
    private String cover_picture="";
    private String description="";
    private String inhabitancy;
    private String provenance;
    private String relationship_status="";
    private String education="";
    private String work="";
    private Date last_login_logout;
    private Boolean isOnline=false;

    //fields that spring security needs:
    private Role role; //?? na filmiku typ miał dopisane, żeby baza sql-owa zapisywała enum jako string

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
