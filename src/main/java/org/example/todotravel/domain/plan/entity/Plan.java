package org.example.todotravel.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.example.todotravel.domain.user.entity.User;

@Entity
@Table(name = "plans")
@Getter
@Setter
@Builder(toBuilder = true)
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
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "status", nullable = false)
    private Boolean status;

    //Not Null -> Null로 변경
    //db에 반영 안 될 시 alter table plans modify column participants_count int null;
    @Column(name = "participants_count")
    private Integer participantsCount;

    @Column(name = "total_budget")
    private Long totalBudget;

    //생성자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User planUser;

    //각 플랜 유저
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlanUser> planUsers;

    //일정 받아오기
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    // 플랜 썸네일
    @Column(name = "plan_thumbnail_url")
    private  String planThumbnailUrl;

}
