package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.config.FileUploadProperties;
import com.example.ParrotSocial.model.Event;
import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin("http://127.0.0.1:3000")//5500
@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    public EventController(EventRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    //dodanie nowego eventu
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Event event){
        Event saveEvent = Event.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .members(Collections.emptyList())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();

        return ResponseEntity.ok(repository.save(event));
    }


    //pobranie listy eventów
    @GetMapping
    public ResponseEntity<?> getList(){
        List<Event> eventList = repository.findAll();
        if(!eventList.isEmpty()){
            LocalDateTime currentDate = LocalDateTime.now();
            Iterator<Event> iterator = eventList.iterator();

            while (iterator.hasNext()) {
                Event event = iterator.next();
                if (event.getEndDate().isBefore(currentDate)) {
                    iterator.remove();
                    repository.delete(event);
                }
            }
            return ResponseEntity.ok(eventList);
        }
        return ResponseEntity.notFound().build();
    }


    //wysłanie zdjęcia eventu
    @PostMapping(path="/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("event_id") String event_id, @RequestParam("image")MultipartFile image){
        Optional<Event> query_event = repository.findById(event_id);
        if(query_event.isPresent()){
            Event event = query_event.get();
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath().toString();

            String originalFilename = image.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            String randomFilename = UUID.randomUUID().toString()+"."+fileExtension;

            try{
                image.transferTo(new File(filePath+randomFilename));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            event.setImage(randomFilename);
            Event saved_event = repository.save(event);
            return ResponseEntity.status(HttpStatus.OK).body(saved_event);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //pobranie zdjęcia eventu na podstawie event_id
    @GetMapping(path="/image/{image_name}",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> downloadImage(@PathVariable("image_name") String image_name){
        Optional<Event> event_query = repository.findByImage(image_name);
        if(event_query.isPresent()){
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath()+event_query.get().getImage();

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

    //dodanie zainteresowania
    @PutMapping("/interest/{event_id}/{user_id}")
    public ResponseEntity<Event> interest(@PathVariable("event_id") String event_id, @PathVariable("user_id") String user_id){
        Optional<Event> query_event = repository.findById(event_id);
        if(query_event.isPresent()){
            Event event = query_event.get();
            List<String> members = event.getMembers();
            if (members == null){
                members = new ArrayList<>();
            }

            if(!members.contains(user_id)){
                members.add(user_id);
                event.setMembers(members);
                return ResponseEntity.ok(repository.save(event));
            }else{
                return ResponseEntity.ok(event);
            }

        }
        return ResponseEntity.notFound().build();
    }


    //usunięcie zainteresowania
    @PutMapping("/uninterest/{event_id}/{user_id}")
    public ResponseEntity<Event> uninterest(@PathVariable("event_id") String event_id, @PathVariable("user_id") String user_id){
        Optional<Event> query_event = repository.findById(event_id);
        if(query_event.isPresent()){
            Event event = query_event.get();
            List<String> members = event.getMembers();
            if (members == null){
                members = new ArrayList<>();
            }

            if(members.contains(user_id)){
                members.remove(user_id);
                event.setMembers(members);
                return ResponseEntity.ok(repository.save(event));
            }else{
                return ResponseEntity.ok(event);
            }

        }
        return ResponseEntity.notFound().build();
    }
}
