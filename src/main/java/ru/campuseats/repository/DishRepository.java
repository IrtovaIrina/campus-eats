package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.campuseats.entity.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long>, JpaSpecificationExecutor<Dish> {
    List<Dish> findByCanteenIdOrderByCategoryDisplayOrderAscNameAsc(Long canteenId);
}