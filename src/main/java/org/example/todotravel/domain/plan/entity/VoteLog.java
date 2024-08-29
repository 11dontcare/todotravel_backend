package org.example.todotravel.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "vote_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteLog {
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "is_voted")
    private boolean isVoted;
}