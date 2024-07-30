package org.example.todotravel.domain.plan.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

//    @Column(name = "plan_id", nullable = false)
//    private Long planId;
//
//    @Column(name = "location_id", nullable = false)
//    private Long locationId;
//
//    @Column(name = "vehicle_id", nullable = false)
//    private Long vehicleId;
//
//    @Column(name = "budget_id", nullable = false, length = 255)
//    private String budgetId;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "travel_day_count", nullable = false)
    private Integer travelDayCount;

    @Column(name = "description")
    private String description;
}
