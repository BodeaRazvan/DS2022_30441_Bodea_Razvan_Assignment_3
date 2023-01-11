package com.energy.services;

import com.energy.entities.Message;
import com.energy.logger.MyLogger;
import com.energy.repositories.MessageRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {
    private static final Logger LOGGER = MyLogger.getInstance();
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAllMessagesBetween(Long senderId, Long receiverId) {
        LOGGER.info("Finding all messages between {} and {}", senderId, receiverId);
        List<Message> foundMessages = messageRepository.findAllBySenderIdAndReceiverId(senderId, receiverId);
        foundMessages.addAll(messageRepository.findAllBySenderIdAndReceiverId(receiverId, senderId));
        return foundMessages;
    }

    public void save(Message message){
        messageRepository.save(message);
    }

}
