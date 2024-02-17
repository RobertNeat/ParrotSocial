package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.config.FileUploadProperties;
import com.example.ParrotSocial.model.Group;
import com.example.ParrotSocial.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.ParrotSocial.repository.GroupRepository;
import com.example.ParrotSocial.repository.PostRepository;
import com.example.ParrotSocial.repository.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Paths;

@CrossOrigin("http://127.0.0.1:3000")//5500
@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupRepository repository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    private final FileUploadProperties fileUploadProperties;

    public GroupController(GroupRepository repository,
                           UserRepository userRepository,
                           PostRepository postRepository,
                           MongoTemplate mongoTemplate,
                           FileUploadProperties fileUploadProperties) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.mongoTemplate = mongoTemplate;
        this.fileUploadProperties = fileUploadProperties;
    }

    //pobranie grupy
    @GetMapping("/{group_id}")
    public ResponseEntity<?> getGroup(@PathVariable("group_id") String group_id){
        Optional<Group> group_query = repository.findById(group_id);
        if(group_query.isPresent()){
            return ResponseEntity.ok(group_query.get());
        }
        return ResponseEntity.notFound().build();
    }


    //pobranie listy grup do których użytkownik należy
    @GetMapping("/list/{user_id}")
    public ResponseEntity<?> getGroupListForUser(@PathVariable("user_id")String user_id){
        List<Group> group_list = repository.findAll();
        List<Group> group_containing_user = new ArrayList<>();
        for (Group group:
             group_list) {
            List<String> group_members = group.getMembers();
            if(group_members==null){
                group_members=new ArrayList<>();
            }
            if(group_members.contains(user_id)) {
                group_containing_user.add(group);
            } else if (group.getOwnerId().equals(user_id)) {
                group_containing_user.add(group);
            }
        }
        return ResponseEntity.ok(group_containing_user);
    }


    //pobranie listy grup - do wyświetlenia listy grup istniejących
    @GetMapping("/list")
    public ResponseEntity<?> getGroupList(){
        return ResponseEntity.ok(repository.findAll());
    }

    //stworzenie nowej grupy jeżeli nie istnieje
    @PostMapping()
    public ResponseEntity<?> createGroup(@RequestBody Group group){
        Optional<User> owner_query = userRepository.findById(group.getOwnerId());
        if(owner_query.isPresent()){
            LocalDateTime currentDate = LocalDateTime.now();
            group.setCreated(currentDate);
            return ResponseEntity.ok(repository.save(group));
        }
        return ResponseEntity.badRequest().build();
    }

    //dodanie zdjęcia grupy
    @PostMapping(path="/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("group_id") String group_id, @RequestParam("image") MultipartFile image){
        Optional<Group> query_group = repository.findById(group_id);
        if(query_group.isPresent()){
            Group group = query_group.get();
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath().toString();

            String originalFilename = image.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            String randomFilename = UUID.randomUUID().toString()+"."+fileExtension;

            try{
                image.transferTo(new File(filePath+randomFilename));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            group.setCoverPicture(randomFilename);
            Group saved_group = repository.save(group);
            return ResponseEntity.status(HttpStatus.OK).body(saved_group);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //pobranie zdjęcia grupy
    @GetMapping(path="/image/{image_name}",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> downloadImage(@PathVariable("image_name") String image_name){
        Optional<Group> group_query = repository.findByCoverPicture(image_name);
        if(group_query.isPresent()){
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath()+group_query.get().getCoverPicture();

            try{
                byte[] image = Files.readAllBytes(new File(filePath).toPath());
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpg")).body(image);
            }catch (Exception e){
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Image reading error");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //edycja grupy
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Group group){
        Optional<Group> group_query = repository.findById(group.getGroupId());
        if(group_query.isPresent()){
            Group save_group = group_query.get();
            if(group.getOwnerId() != null){save_group.setOwnerId(group.getOwnerId());}
            if(group.getName() != null){save_group.setName(group.getName());}
            if(group.getDescription() != null){save_group.setDescription(group.getDescription());}
            return ResponseEntity.ok(repository.save(save_group));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie grupy
    @DeleteMapping("/{group_id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("group_id") String group_id){
        Optional<Group> group_query = repository.findById(group_id);
        if(group_query.isPresent()){
            repository.deleteById(group_id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //dodanie nowego członka do grupy
    @PutMapping("add_member/group/{group_id}/member/{user_id}")
    public ResponseEntity<?> addMember(@PathVariable("group_id") String group_id,@PathVariable("user_id")String user_id){
        Optional<Group> group_query = repository.findById(group_id);
        Optional<User> user_query = userRepository.findById(user_id);
        if(group_query.isPresent() && user_query.isPresent()){
            Group save_group = group_query.get();
            List<String> members = save_group.getMembers();
            if (members == null){
                members = new ArrayList<>();
            }
            members.add(user_id);
            save_group.setMembers(members);
            return ResponseEntity.ok(repository.save(save_group));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie członka grupy
    @PutMapping("remove_member/group/{group_id}/member/{user_id}")
    public ResponseEntity<?> removeMember(@PathVariable("group_id") String group_id,@PathVariable("user_id")String user_id){
        Optional<Group> group_query = repository.findById(group_id);
        if(group_query.isPresent() && group_query.get().getMembers().contains(user_id)){
            Group update_group = group_query.get();
            List<String> members = update_group.getMembers();
            if (members != null) {
                members.remove(user_id);
            }
            update_group.setMembers(members);
            return ResponseEntity.ok(repository.save(update_group));
        }
        return ResponseEntity.notFound().build();
    }

    //przetestować kontroller!!!

}

