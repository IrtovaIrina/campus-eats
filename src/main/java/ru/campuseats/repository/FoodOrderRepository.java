package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.enums.OrderStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    Optional<FoodOrder> findByOrderNumberIgnoreCase(String orderNumber);
    List<FoodOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<FoodOrder> findByUserIdAndStatusInOrderByCreatedAtDesc(Long userId, Collection<OrderStatus> statuses);
    List<FoodOrder> findByCanteenIdOrderByCreatedAtDesc(Long canteenId);
    List<FoodOrder> findByCanteenIdAndStatusOrderByCreatedAtAsc(Long canteenId, OrderStatus status);
}