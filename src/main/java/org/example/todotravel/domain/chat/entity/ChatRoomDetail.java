package org.example.todotravel.domain.chat.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(ChatRoomDetailId.class)
public class ChatRoomDetail {

    @Id
    @Column(name = "room_id", nullable = false)
    private Long roomId;

//    @Id
//    @Column(name = "plan_id", nullable = false)
//    private Long planId;

    @Column(name = "room_name", nullable = false, length = 50)
    private String roomName;

    @Column(name = "room_date", nullable = false)
    private LocalDateTime roomDate;
}
