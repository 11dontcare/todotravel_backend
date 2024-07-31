package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatitudeAndLongitude(double latitude, double longitude);
}
