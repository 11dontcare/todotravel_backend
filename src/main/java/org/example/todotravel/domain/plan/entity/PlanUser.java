package org.example.todotravel.domain.plan.entity;

import lombok.*;

import jakarta.persistence.*;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "plan_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_particiapant_id", nullable = false)
    private Long planParticipantId;

    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    public enum StatusType {
        PENDING, ACCEPTED, REJECTED
    }

}
