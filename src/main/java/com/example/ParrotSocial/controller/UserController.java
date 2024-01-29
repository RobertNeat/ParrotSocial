package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.model.Role;
import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repository;
    private final MongoTemplate mongoTemplate;

    //authService
    //fileuploadProperties

    public UserController(UserRepository repository,MongoTemplate mongoTemplate)
    {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }



    //pobranie identyfikatora użytkownika na podstawie email i password
    @GetMapping
    public ResponseEntity<String> getIdentifier(@RequestBody User userCredentials){
        if(!userCredentials.getEmail().isEmpty() && !userCredentials.getPassword().isEmpty()) {
            Optional<User> userFromDatabase = repository.findByEmail(userCredentials.getEmail());
            if (userFromDatabase.isPresent()){
                return ResponseEntity.ok(userFromDatabase.get().getUser_id());
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body("No credentials attached");

    }


    //dodanie (aktualizacja) informacji do konta: name, surname, description, relationship_status, inhabitancy, provenance, education, work
    @Operation(summary = "Update a user", description = "Update a user based on user object and the valid token")
    @ApiResponses(value = {
            @ApiResponse(description = "User updated successfully",responseCode = "200"),
            @ApiResponse(description = "User not found",responseCode = "404"),
            @ApiResponse(description = "Unauthorized",responseCode = "403"),
    })
    @Parameters(
            value={
                    @Parameter(name = "name",example="Jan"),
                    @Parameter(name="surname",example="Kowalski"),
                    @Parameter(name="description",example="Profile text description"),
                    @Parameter(name="relationship_status",example="single"),
                    @Parameter(name="inhabitancy",example="Warsaw"),
                    @Parameter(name="provenance",example="Lublin"),
                    @Parameter(name="education",example="college"),
                    @Parameter(name="work",example="it_company")
            }
    )
    @PutMapping("/{user_id}")
    public ResponseEntity<User> update(@PathVariable("user_id") String user_id,@RequestBody User updateUser){//NIE MAM ID???

            //trzeba sprawdzić czy updateUser.id istnieje w bazie danych
            Optional<User> userFromDatabase = repository.findById(user_id);

            if(userFromDatabase.isPresent()) {

                User saveUser = User.builder()
                        .user_id(user_id)
                        .email(userFromDatabase.get().getEmail())
                        .password(userFromDatabase.get().getPassword())
                        .name(updateUser.getName())
                        .surname(updateUser.getSurname())
                        .description(updateUser.getDescription())
                        .relationship_status(updateUser.getRelationship_status())
                        .inhabitancy(updateUser.getInhabitancy())
                        .provenance(updateUser.getProvenance())
                        .education(updateUser.getEducation())
                        .work(updateUser.getWork())
                        .role(Role.USER)
                        .build();

                return ResponseEntity.ok(repository.save(saveUser));
            }else{
                return ResponseEntity.notFound().build();
            }
    }

    //pobranie listy wszystkich użytkowników po frazie wyszukiwania (zawierających w username lub surname ciąg znakowy)
    @GetMapping("/list/{serach_phrase}")
    public ResponseEntity<?> getList(@PathVariable("serach_phrase") String serach_phrase){
        Query query = new Query();
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("name").regex(serach_phrase,"i"),
                Criteria.where("surname").regex(serach_phrase,"i")
        );
        query.addCriteria(criteria);
        List<User> userList = mongoTemplate.find(query, User.class);

        System.out.println(userList);

        if (userList.isEmpty()) {
            return ResponseEntity.notFound().build();//new ResponseEntity<>("No results found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    //pobranie użytkownika po user_id
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable("user_id") String user_id){
        Optional<User> searchedUser = repository.findById(user_id);
        if(searchedUser.isPresent()){
            return ResponseEntity.ok().body(searchedUser.get());
        }
        return ResponseEntity.notFound().build();
    }


    //wysłanie zdjęcia profilowego
    //wysłanie zdjęcia okładki profilu


    //usunięcie użytkownika (wyczyszczenie danych)
    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> clearUser(@PathVariable("user_id") String user_id){
        Optional<User> searchedUser = repository.findById(user_id);
        if(searchedUser.isPresent()){

            User clearedUser = User.builder()
                    .user_id(searchedUser.get().getUser_id())
                    .name(searchedUser.get().getName())
                    .email("")
                    .password("")
                    .surname(searchedUser.get().getSurname()+" [disabled]")
                    .build();

            repository.save(clearedUser);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }

}
