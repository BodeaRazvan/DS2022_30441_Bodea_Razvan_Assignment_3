package com.energy.controllers;

import com.energy.dto.MessageDTO;
import com.energy.entities.Message;
import com.energy.entities.User;
import com.energy.services.MeasurementService;
import com.energy.services.MessageService;
import com.energy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
@RestController
@CrossOrigin
public class MessageController {

    @Autowired
    SimpMessagingTemplate template;
    private final UserService userService;
    private final MessageService messageService;
    private final MeasurementService measurementService;

    @Autowired
    public MessageController(UserService userService, MessageService messageService, MeasurementService measurementService) {
        this.userService = userService;
        this.messageService = messageService;
        this.measurementService = measurementService;
    }

    @PostMapping("/sendMessage2")
    public ResponseEntity<String> sendMessage(Message message){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");

        String response = "{";
        response += "\"from\":" + '"' + message.getSenderId() + '"' + ",";
        response += "\"message\":" + '"' + message.getMessage() + '"' + ",";
        response += "\"to\":" + message.getReceiverId();
        response += "}";
        template.convertAndSend("/message", response);
        return ResponseEntity.ok().headers(responseHeaders).body(null);
    }

    @GetMapping("/notifyIsTyping/{userId}")
    public ResponseEntity<String> notifyIsTyping(@PathVariable Long userId){
        User currentUser = LoginController.getCurrentUser();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");

        String response = "{";
        response += "\"isTyping\":" + '"' + true + '"' + ",";
        response += "\"from\":" + '"' + currentUser.getUsername() + '"' + ",";
        response += "\"userId\":" + userId;
        response += "}";
        template.convertAndSend("/message", response);
        return ResponseEntity.ok().headers(responseHeaders).body(null);
    }

    @GetMapping("/notifyHasSeen/{userId}")
    public ResponseEntity<String> notifyHasSeen(@PathVariable Long userId){
        User currentUser = LoginController.getCurrentUser();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");

        String response = "{";
        response += "\"hasSeen\":" + '"' + true + '"' + ",";
        response += "\"from\":" + '"' + currentUser.getUsername() + '"' + ",";
        response += "\"userId\":" + userId;
        response += "}";
        template.convertAndSend("/message", response);
        return ResponseEntity.ok().headers(responseHeaders).body(null);
    }


    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) throws IOException, TimeoutException {
       User currentUser = LoginController.getCurrentUser();
       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       Message sentMessage = new Message();
       sentMessage.setSenderId(currentUser.getId());
       sentMessage.setReceiverId(messageDTO.getUserId());
       sentMessage.setMessage(messageDTO.getMessage());
       sentMessage.setTimestamp(timestamp);
       messageService.save(sentMessage);
       sendMessage(sentMessage);
       return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/getMessages/{userId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long userId){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");
        List<Message> messageList = messageService.findAllMessagesBetween(userId, LoginController.getCurrentUser().getId());
        return ResponseEntity.ok().headers(responseHeaders).body(messageList);
    }
}
