package com.energy.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name= "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "senderId")
    private Long senderId;

    @Column(name="receiverId")
    private Long receiverId;

    @Column(name="timestamp")
    private Timestamp timestamp;

}
