package com.example.ParrotSocial.controller;


import com.example.ParrotSocial.config.FileUploadProperties;
import com.example.ParrotSocial.model.Conversation;
import com.example.ParrotSocial.model.Event;
import com.example.ParrotSocial.model.Message;
import com.example.ParrotSocial.repository.ConversationRepository;
import com.example.ParrotSocial.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin("http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageRepository repository;
    private final ConversationRepository conversationRepository;
    @Autowired
    private FileUploadProperties fileUploadProperties;
    public MessageController(MessageRepository repository, ConversationRepository conversationRepository) {
        this.repository = repository;
        this.conversationRepository = conversationRepository;
    }


    //pobranie pojedynczej wiadomości po message_id
    @GetMapping("/{message_id}")
    public ResponseEntity<?> getMessageById(@PathVariable("message_id") String message_id){
        Optional<Message> query_message = repository.findById(message_id);
        if(query_message.isPresent()){
            return ResponseEntity.ok(query_message.get());
        }
        return ResponseEntity.notFound().build();
    }

    //pobranie listy wiadomości na podstawie conversation_id
    @GetMapping("/list/{conversation_id}")
    public ResponseEntity<?> getMessagesForConvo(@PathVariable("conversation_id") String conversation_id){
        Optional<Conversation> query_conversation = conversationRepository.findById(conversation_id);
        if(query_conversation.isPresent()){
            List<Message> message_list = repository.findByConversationid(conversation_id);
            return ResponseEntity.ok(message_list);
        }
        return ResponseEntity.notFound().build();
    }

    //wysłanie wiadomości na podstawie conversation_id
    @PostMapping("/{conversation_id}")
    public ResponseEntity<?> sendMessage(@PathVariable("conversation_id")String conversation_id, @RequestBody Message message){
        Optional<Conversation> query_conversation = conversationRepository.findById(conversation_id);
        if(query_conversation.isPresent()){
            LocalDateTime currentDate = LocalDateTime.now();
            Message save_message = Message.builder()
                    .conversationid(conversation_id)
                    .senderid(message.getSenderid())
                    .text(message.getText())
                    .image(message.getImage())
                    .send_date(currentDate)
                    .build();
            return ResponseEntity.ok(repository.save(save_message));
        }
        return ResponseEntity.notFound().build();
    }

    //wysłanie zdjęcia wiadomości
    @PostMapping(path="/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("message_id") String message_id, @RequestParam("image") MultipartFile image){
        Optional<Message> query_message = repository.findById(message_id);
        if(query_message.isPresent()){
            Message message = query_message.get();
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath().toString();

            String originalFilename = image.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            String randomFilename = UUID.randomUUID().toString()+"."+fileExtension;

            try{
                image.transferTo(new File(filePath+randomFilename));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            message.setImage(randomFilename);
            Message saved_message = repository.save(message);
            return ResponseEntity.status(HttpStatus.OK).body(saved_message);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //pobranie zdjęcia wiadomości
    @GetMapping(path="/image/{image_name}",produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> downloadImage(@PathVariable("image_name") String image_name){
        Optional<Message> message_query = repository.findByImage(image_name);
        if(message_query.isPresent()){
            String filePath = Paths.get(fileUploadProperties.getPath()).toAbsolutePath()+message_query.get().getImage();

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

    //edycja wiadomości na podstawie message_id
    @PutMapping("/{message_id}")
    public ResponseEntity<?> editMessage(@PathVariable("message_id")String message_id, @RequestBody Message message){
        Optional<Message> query_message = repository.findById(message_id);
        if(query_message.isPresent()){

            Message save_message = query_message.get();
            if(message.getMessageid() != null)      {save_message.setMessageid(message.getMessageid());}
            if(message.getConversationid() != null) {save_message.setConversationid(message.getConversationid());}
            if(message.getSenderid() != null)       {save_message.setSenderid(message.getSenderid());}
            if(message.getText() != null)           {save_message.setText(message.getText());}
            if(message.getImage() != null)          {save_message.setImage(message.getImage());}

            return ResponseEntity.ok(repository.save(save_message));
        }
        return ResponseEntity.notFound().build();
    }

    //usunięcie wiadomości na podstawie message_id
    @DeleteMapping("/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") String message_id){
        Optional<Message> query_message = repository.findById(message_id);
        if(query_message.isPresent()){
            repository.deleteById(message_id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}