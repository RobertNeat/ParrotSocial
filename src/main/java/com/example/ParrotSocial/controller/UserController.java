package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.config.FileUploadProperties;
import com.example.ParrotSocial.model.Role;
import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin("http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private FileUploadProperties fileUploadProperties;

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
    public ResponseEntity<User> update(@PathVariable("user_id") String user_id,@RequestBody User updateUser){

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


    //wysłanie zdjęcia profilowego/okładki
    @PostMapping(path="/picture/{type}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPicture(@RequestParam("user_id") String user_id, @RequestParam("picture")MultipartFile file, @PathVariable("type") String type){
        Optional<User> query_user = repository.findById(user_id);
        if(query_user.isPresent()){
            User user = query_user.get();
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath().toString();

            String originalFilename = file.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            String randomFilename = UUID.randomUUID().toString()+"."+fileExtension;

            try{
                file.transferTo(new File(filePath+randomFilename));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

            if(type.equals("profile".toString())){
                user.setProfilepicture(randomFilename);
            }else{//cover
                user.setCoverpicture(randomFilename);
            }

            User saved_user = repository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(saved_user);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //pobranie zdjęcia profilowego/okładki
    @GetMapping(path="/picture/{type}/{picture_name}", produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> downloadPicture(@PathVariable("type") String type, @PathVariable("picture_name") String picture_name){
        Optional<User> user_query = (type.equals("profile"))//(type=="profile")
                ? repository.findByProfilepicture(picture_name)
                : repository.findByCoverpicture(picture_name);

        if(user_query.isPresent()){
            String filePath = (type.equals("profile".toString()))
                    ? Paths.get(fileUploadProperties.getPath()).toAbsolutePath()+user_query.get().getProfilepicture()
                    : Paths.get(fileUploadProperties.getPath()).toAbsolutePath()+user_query.get().getCoverpicture();

            try{
                byte[] image = Files.readAllBytes(new File(filePath).toPath());
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpg")).body(image);
            }catch(Exception e) {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Image reading error");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



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
