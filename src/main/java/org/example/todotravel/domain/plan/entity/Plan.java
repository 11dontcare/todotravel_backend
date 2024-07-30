package org.example.todotravel.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    private Long planId;

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

    //생성자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User planUser;

    //각 플랜 유저
    @OneToMany(mappedBy = "plan_users")
    private Set<PlanUser> planUsers;

    //일정 받아오기
    @OneToMany(mappedBy = "schedules")
    private List<Schedule> schedules;

}
