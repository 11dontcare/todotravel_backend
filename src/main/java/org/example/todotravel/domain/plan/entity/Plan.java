package org.example.todotravel.domain.plan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    private Long planId;

//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "location", nullable = false, length = 50)
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "participants_count", nullable = false)
    private Integer participantsCount;

    @Column(name = "total_budget")
    private Long totalBudget;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User planUser;

}
