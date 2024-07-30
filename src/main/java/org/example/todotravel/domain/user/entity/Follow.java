package org.example.todotravel.domain.user.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "follows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", nullable = false)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Column(name = "following_id", nullable = false)
    private User followingUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Column(name = "follower_id", nullable = false)
    private User followerUser;
}
