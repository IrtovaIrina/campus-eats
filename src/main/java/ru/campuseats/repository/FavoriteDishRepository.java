package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.FavoriteDish;

import java.util.List;
import java.util.Optional;

public interface FavoriteDishRepository extends JpaRepository<FavoriteDish, Long> {
    List<FavoriteDish> findByUserIdOrderByAddedAtDesc(Long userId);
    Optional<FavoriteDish> findByUserIdAndDishId(Long userId, Long dishId);
    void deleteByDishId(Long dishId);
}
