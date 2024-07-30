package org.example.todotravel.domain.chat.entity;

import lombok.*;

import jakarta.persistence.*;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User chatUser;
}
