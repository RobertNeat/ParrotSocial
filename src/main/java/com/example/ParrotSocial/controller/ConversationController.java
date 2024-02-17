package com.example.ParrotSocial.controller;

import com.example.ParrotSocial.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ParrotSocial.model.Conversation;
import com.example.ParrotSocial.repository.MessageRepository;
import com.example.ParrotSocial.repository.ConversationRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@CrossOrigin("http://127.0.0.1:3000")//5500
@RestController
@RequestMapping("/api/conversation")
public class ConversationController {
    private final ConversationRepository repository;
    private final MessageRepository messageRepository;

    public ConversationController(ConversationRepository repository, MessageRepository messageRepository) {
        this.repository = repository;
        this.messageRepository = messageRepository;
    }

    //pobranie pojedynczej konwersacji
    @GetMapping("/{conversation_id}")
    public ResponseEntity<?> getConversation(@PathVariable("conversation_id") String conversation_id){
        Optional<Conversation> query_conversation = repository.findById(conversation_id);
        if(query_conversation.isPresent()){
            return ResponseEntity.ok(query_conversation.get());
        }
        return ResponseEntity.notFound().build();
    }

    //pobranie listy konwersacji dla użytkownika o danym user_id
    @GetMapping("/list/{user_id}")
    public ResponseEntity<?> getListForUser(@PathVariable("user_id")String user_id){
        List<Conversation> conversation_list = repository.findByUserIdInMembers(user_id);
        if(!conversation_list.isEmpty()){
            return ResponseEntity.ok(conversation_list);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createConversation(@RequestBody Conversation conversation){
        if(!conversation.getMembers().isEmpty() && conversation.getMembers().size()>=2) {
            //jeżeli konwersacja z ludźmi z listy istnieje nie tworzyć nowej konwersacji
            Optional<Conversation> query_conversation = repository.findByMembersInAnyOrder(conversation.getMembers());
            if(query_conversation.isEmpty()){
                LocalDateTime currentDate = LocalDateTime.now();
                Conversation save_conversation = Conversation.builder()
                    .members(conversation.getMembers())
                    .creationDate(currentDate)
                    .build();
                return ResponseEntity.ok(repository.save(save_conversation));
            }
            return ResponseEntity.ok("Conversation already exists");
        }
        return ResponseEntity.badRequest().build();
    }


    //usunięcie konwersacji (+ wszystkich wiadomości w danej konwersacji)
    @DeleteMapping("/{conversation_id}")
    public ResponseEntity<?> deleteConversation(@PathVariable("conversation_id") String conversation_id){
        Optional<Conversation> query_conversation = repository.findById(conversation_id);
        if(query_conversation.isPresent()){
            List<Message> conversation_list  = messageRepository.findByConversationId(query_conversation.get().getConversationId());
            for (Message current_message:
                 conversation_list) {
                messageRepository.deleteById(current_message.getMessageId());
            }
            repository.delete(query_conversation.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
