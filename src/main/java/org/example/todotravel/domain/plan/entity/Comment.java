package org.example.todotravel.domain.plan.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

//    @Column(name = "user_id", nullable = false)
//    private Long userId;

//    @Column(name = "plan_id", nullable = false)
//    private Long planId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "before_travel", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean beforeTravel;
}
