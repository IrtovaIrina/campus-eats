package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDishIdOrderByCreatedAtDesc(Long dishId);
    List<Review> findByDishCanteenIdOrderByCreatedAtDesc(Long canteenId);
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndDishIdAndOrderId(Long userId, Long dishId, Long orderId);
}