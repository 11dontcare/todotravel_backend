package org.example.todotravel.domain.chat.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetail {

    @Id
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "room_name", nullable = false, length = 50)
    private String roomName;

    @Column(name = "room_date", nullable = false)
    private LocalDateTime roomDate;
}
