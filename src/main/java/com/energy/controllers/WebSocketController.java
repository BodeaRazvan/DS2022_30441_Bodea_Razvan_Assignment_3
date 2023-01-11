package com.energy.controllers;

import com.energy.consumer.Consumer;
import com.energy.entities.Device;
import com.energy.entities.Message;
import com.energy.services.MeasurementService;
import com.energy.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Controller
@CrossOrigin
@RestController
public class WebSocketController {
    @Autowired
    SimpMessagingTemplate template;

    private final MeasurementService measurementService;
    private final MessageService messageService;

    @Autowired
    public WebSocketController(MeasurementService measurementService, MessageService messageService) throws IOException, TimeoutException {
        this.measurementService = measurementService;
        this.messageService = messageService;
        Consumer consumer = new Consumer();
        consumer.start(this,measurementService);
    }


    @PostMapping("/send")
    public ResponseEntity<String> send(Device device){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");

        String message = "{";
        message += "\"deviceName\":" + '"' + device.getName() + '"' + ",";
        message += "\"userId\":" + device.getUser().getId();
        message += "}";
        template.convertAndSend("/topic", message);
        return ResponseEntity.ok().headers(responseHeaders).body(null);
    }

    @MessageMapping("/application")
    public void recieveMessage(@Payload String message){
        System.out.println(message);
    }

    @SendTo("/")
    public String broadcastMessage(@Payload String message) {
        return message;
    }
}
