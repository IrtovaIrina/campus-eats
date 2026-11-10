package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserIdOrderByAddedAtAsc(Long userId);
    Optional<CartItem> findByUserIdAndDishId(Long userId, Long dishId);
    void deleteByUserId(Long userId);
}