package org.example.todotravel.domain.plan.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "vehicle", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicle;

    public enum VehicleType {
        CAR, AIRPLANE, TRAIN, BUS, BIKE, WALK, TAXI
    }
}
