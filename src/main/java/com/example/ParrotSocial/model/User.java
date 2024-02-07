package com.example.ParrotSocial.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Collection;
import java.time.LocalDateTime;

@Document(collection = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails { //implementation of the spring security for authentication (jwt) and authorization
    @Id
    private String userId;
    private String name="";
    private String surname="";
    @NonNull
    private String email;
    @NonNull
    private String password;
    private List<String> followers;
    private List<String> followings;
    private String profilePicture="";
    private String coverPicture="";
    private String description="";
    @SuppressWarnings("SpellCheckingInspection")
    private String inhabitancy;
    private String provenance;
    private String relationshipStatus="";
    private String education="";
    private String work="";
    private LocalDateTime lastLoginLogout;
    private Boolean isOnline=false;

    //fields that spring security needs:
    private Role role;

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
