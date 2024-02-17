package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.model.Comment;
import com.example.ParrotSocial.model.Post;
import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.CommentRepository;
import com.example.ParrotSocial.repository.PostRepository;
import com.example.ParrotSocial.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://127.0.0.1:3000")//5500
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentController(CommentRepository repository, UserRepository userRepository, PostRepository postRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    //pobranie pojedynczego
    @GetMapping("/{post_id}")
    public ResponseEntity<?> getComment(@PathVariable("post_id") String post_id){
        Optional<Comment> comment_query = repository.findById(post_id);
        if(comment_query.isPresent()){
            return ResponseEntity.ok(comment_query.get());
        }
        return ResponseEntity.notFound().build();
    }

    //pobranie listy do posta
    @GetMapping("/to_post/{post_id}")
    public ResponseEntity<?> getPostComments(@PathVariable("post_id") String post_id){
        Optional<Post> post_query = postRepository.findById(post_id);
        if(post_query.isPresent()){
            List<Comment> comment_list = repository.findByPostId(post_id);
            if(comment_list == null || comment_list.size()<=0){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(comment_list);
        }
        return ResponseEntity.notFound().build();
    }

    //dodanie komentarza do posta
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment){
        Optional<User> query_user = userRepository.findById(comment.getUserId());
        Optional<Post> post_query = postRepository.findById(comment.getPostId());
        if(query_user.isPresent() && post_query.isPresent()){
            LocalDateTime currentDate = LocalDateTime.now();
            comment.setCreated(currentDate);
            return ResponseEntity.ok(repository.save(comment));
        }
        return ResponseEntity.notFound().build();
    }

    //edycja istniejącego komentarza
    @PutMapping
    public ResponseEntity<?> editComment(@RequestBody Comment comment){
        Optional<User> user_query = userRepository.findById(comment.getUserId());
        Optional<Comment> comment_query = repository.findById(comment.getCommentId());
        if(user_query.isPresent() && comment_query.isPresent() && user_query.get().getUserId().equals(comment_query.get().getUserId())){
            Comment save_comment = comment_query.get();
            if(comment.getDescription() != null){
                save_comment.setDescription(comment.getDescription());
            }
            return ResponseEntity.ok(repository.save(save_comment));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie komentarza
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") String comment_id){
        Optional<Comment> comment_query = repository.findById(comment_id);
        if(comment_query.isPresent()){
            repository.deleteById(comment_id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //dodanie polubienia komentarza
    @PutMapping("/like/{comment_id}/{user_id}")
    public ResponseEntity<?> likeComment(@PathVariable("comment_id") String comment_id, @PathVariable("user_id") String user_id){
        Optional<Comment> comment_query = repository.findById(comment_id);
        Optional<User> user_query = userRepository.findById(user_id);
        if(comment_query.isPresent() && user_query.isPresent()){
            Comment save_comment = comment_query.get();
            List<String> likes_array = save_comment.getLikes();
            if(likes_array == null){
                likes_array = new ArrayList<>();
            }
            if(!likes_array.contains(user_id)) {
                likes_array.add(user_id);
            }else{
                return ResponseEntity.badRequest().build();
            }
            save_comment.setLikes(likes_array);
            return ResponseEntity.ok(repository.save(save_comment));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie polubienia komentarza
    @PutMapping("/dislike/{comment_id}/{user_id}")
    public ResponseEntity<?> dislikeComment(@PathVariable("comment_id") String comment_id, @PathVariable("user_id") String user_id){
        Optional<Comment> comment_query = repository.findById(comment_id);
        if(comment_query.isPresent() && comment_query.get().getLikes().contains(user_id)){
            Comment update_comment = comment_query.get();
            List<String> likes_array = update_comment.getLikes();
            if(likes_array != null){
                likes_array.remove(user_id);
            }
            update_comment.setLikes(likes_array);
            return ResponseEntity.ok(repository.save(update_comment));
        }
        return ResponseEntity.notFound().build();
    }


}
