package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.Canteen;

import java.util.List;

public interface CanteenRepository extends JpaRepository<Canteen, Long> {
    List<Canteen> findByIsOpenTrueOrderByRatingDesc();
    List<Canteen> findByManagerId(Long managerId);
}