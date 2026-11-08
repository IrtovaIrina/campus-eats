package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.PickupPoint;

import java.util.List;

public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {
    List<PickupPoint> findByCanteenIdAndIsActiveTrueOrderByIdAsc(Long canteenId);
}