package org.example.todotravel.domain.plan.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(LikeId.class)
public class Like {

    @Id
    @Column(name = "like_id", nullable = false, length = 255)
    private String likeId;

//    @Id
//    @Column(name = "plan_id", nullable = false)
//    private Long planId;
//
//    @Id
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
}
