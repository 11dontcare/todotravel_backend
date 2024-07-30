package org.example.todotravel.domain.plan.entity;

import lombok.*;

import jakarta.persistence.*;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User commentUser;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan planId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "before_travel", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean beforeTravel;
}
