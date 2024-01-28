package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repository;

    //authService
    //fileuploadProperties

    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from user endpoint (secured)");
    }


    //rejestracja początkowa: username, email, password, NIE TRZEBA --- ROIONA PRZEZ SPRING SECURITY
    /*
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody User newUser){
        String raw_password = newUser.getPassword();
        String encoded_password = DigestUtils.sha256Hex(raw_password);
        newUser.setPassword(encoded_password);
        return ResponseEntity.ok(repository.save(newUser));
    }
    */


    //dodanie (aktualizacja) informacji do konta: name, surname, description, relationship_status, inhabitancy, provenance, education, work

    //pobranie listy wszystkich użytkowników po frazie wyszukiwania (zawierających w username lub surname ciąg znakowy)
    //pobranie użytkownika po user_id

    //wysłanie zdjęcia profilowego
    //wysłanie zdjęcia okładki profilu


    //usunięcie użytkownika (wyczyszczenie danych)
}
