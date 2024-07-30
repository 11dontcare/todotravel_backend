package org.example.todotravel.domain.chat.entity;

import lombok.*;

import jakarta.persistence.*;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User chatUser;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
