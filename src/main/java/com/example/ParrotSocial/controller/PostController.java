package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.config.FileUploadProperties;
import com.example.ParrotSocial.model.Post;
import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.GroupRepository;
import com.example.ParrotSocial.repository.PostRepository;
import com.example.ParrotSocial.repository.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin("http://127.0.0.1:3000")//5500
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostRepository repository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private FileUploadProperties fileUploadProperties;

    public PostController(PostRepository repository,
                          GroupRepository groupRepository,
                          UserRepository userRepository,
                          MongoTemplate mongoTemplate,
                          FileUploadProperties fileUploadProperties) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.fileUploadProperties = fileUploadProperties;
    }

    //pobranie listy postów dla grupy (group_id)
    @GetMapping("/by_group/{group_id}")
    public ResponseEntity<?> getPostsByGroup(@PathVariable("group_id") String group_id){
        List<Post> query_post = repository.findByGroupId(group_id);
        if(!query_post.isEmpty()){
            return ResponseEntity.ok(query_post);
        }
        return ResponseEntity.notFound().build();
    }

    //pobranie listy postów dla użytkownika (user_id)
    @GetMapping("/by_user/{user_id}")
    public ResponseEntity<?> getPostsByUser(@PathVariable("user_id") String user_id){
        List<Post> query_post = repository.findByGroupIdIsNullAndUserId(user_id);
        if(!query_post.isEmpty()){
            return ResponseEntity.ok(query_post);
        }
        return ResponseEntity.notFound().build();
    }

    //pobranie posta na podstawie post_id
    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable("post_id") String post_id){
        Optional<Post> post_query = repository.findById(post_id);
        if(post_query.isPresent()){
            return ResponseEntity.ok(post_query.get());
        }
        return ResponseEntity.notFound().build();
    }

    //utworzenie posta (w groupie i bez grupy)
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post){
        Optional<User> user_query = userRepository.findById(post.getUserId());
        if(user_query.isPresent()) {

            String group_id = post.getGroupId();
            LocalDateTime currentDate = LocalDateTime.now();
            Post savePost = Post.builder()
                    .userId(post.getUserId())
                    .description(post.getDescription())
                    .created(currentDate)
                    .build();

            if(post.getGroupId() == null){
                return ResponseEntity.ok(repository.save(savePost));
            }

            if(!post.getGroupId().isEmpty() && groupRepository.findById(post.getGroupId()).isPresent()){
                savePost.setGroupId(group_id);
                return ResponseEntity.ok(repository.save(savePost));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no group with specified ID");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with specified ID");
    }

    //edycja posta na podstawie post_id
    @PutMapping
    public ResponseEntity<?> editPost(@RequestBody Post post){
        Optional<Post> post_query = repository.findById(post.getPostId());
        if(post_query.isPresent()){
            Post save_post = post_query.get();
            if(post.getDescription() != null){save_post.setDescription(post.getDescription());}
            return ResponseEntity.ok(repository.save(save_post));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie posta na podstawie post_id
    @DeleteMapping("/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable("post_id") String post_id){
        Optional<Post> post_query = repository.findById(post_id);
        if(post_query.isPresent()){
            repository.deleteById(post_id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //dodanie zdjęcia do posta
    @PostMapping(path="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("post_id") String post_id, @RequestParam("image") MultipartFile image){
        Optional<Post> query_post = repository.findById(post_id);
        if(query_post.isPresent()){
            Post post = query_post.get();
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath().toString();

            String originalFilename = image.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            String randomFilename = UUID.randomUUID().toString()+"."+fileExtension;

            try{
                image.transferTo(new File(filePath+randomFilename));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            post.setImage(randomFilename);
            Post saved_post = repository.save(post);
            return ResponseEntity.status(HttpStatus.OK).body(saved_post);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //pobranie zdjęcia posta
    @GetMapping(path="/image/{image_name}",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> downloadImage(@PathVariable("image_name") String image_name){
        Optional<Post> post_query = repository.findByImage(image_name);
        if(post_query.isPresent()){
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath()+post_query.get().getImage();

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

    //dodanie lajka do posta
    @PutMapping("/like/{post_id}/{user_id}")
    public  ResponseEntity<?> likePost(@PathVariable("post_id") String post_id, @PathVariable("user_id") String user_id){
        Optional<Post> post_query = repository.findById(post_id);
        Optional<User> user_query = userRepository.findById(user_id);
        if(post_query.isPresent() && user_query.isPresent()){
            Post save_post = post_query.get();
            List<String> likes_array = save_post.getLikes();
            if(likes_array == null){
                likes_array = new ArrayList<>();
            }
            if(!likes_array.contains(user_id)) {
                likes_array.add(user_id);
            }else{
                return ResponseEntity.badRequest().build();
            }
            save_post.setLikes(likes_array);
            return ResponseEntity.ok(repository.save(save_post));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie lajka posta
    @PutMapping("/dislike/{post_id}/{user_id}")
    public ResponseEntity<?> dislikePost(@PathVariable("post_id") String post_id, @PathVariable("user_id") String user_id){
        Optional<Post> post_query = repository.findById(post_id);
        if(post_query.isPresent() && post_query.get().getLikes().contains(user_id)){
            Post update_post = post_query.get();
            List<String> likes_array = update_post.getLikes();
            if(likes_array != null){
                likes_array.remove(user_id);
            }
            update_post.setLikes(likes_array);
            return ResponseEntity.ok(repository.save(update_post));
        }
        return ResponseEntity.notFound().build();
    }

}
