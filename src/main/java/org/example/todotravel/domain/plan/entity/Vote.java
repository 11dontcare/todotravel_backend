package org.example.todotravel.domain.plan.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "votes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    @Id
    @Column(name = "vote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_participant_id", nullable = false)
    private PlanUser planUser;  //해당 plan에 있는 user

    @Column(nullable = false, name = "vote_count")
    private int voteCount;

    @Column(nullable = false, name = "start_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(nullable = false, name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteLog> voteLogs = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public enum Category {
        BREAKFAST, LUNCH, DINNER, //식사
        ACTIVITY, //주요 활동
        TRANSPORTATION, //이동 수단
        ACCOMMODATION, //숙소
        BREAK //휴식 (ex. 카페, 쇼핑몰, 공원 등 부가적인 활동)
    }

}
