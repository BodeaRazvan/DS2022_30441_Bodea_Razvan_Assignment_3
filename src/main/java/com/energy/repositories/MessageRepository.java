package com.energy.repositories;

import com.energy.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
