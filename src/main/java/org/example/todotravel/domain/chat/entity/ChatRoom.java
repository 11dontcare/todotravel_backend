package org.example.todotravel.domain.chat.entity;

import lombok.*;

import jakarta.persistence.*;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "room_date", nullable = false)
    private LocalDateTime roomDate;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomUser> chatRoomUsers = new HashSet<>();

    public void addUser(User user) {
        if (this.chatRoomUsers == null) {
            this.chatRoomUsers = new HashSet<>();
        }
        ChatRoomUser chatRoomUser = new ChatRoomUser(this, user);
        this.chatRoomUsers.add(chatRoomUser);
    }
}
