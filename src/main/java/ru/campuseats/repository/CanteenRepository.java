package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.Canteen;

import java.util.List;
import java.util.Optional;

public interface CanteenRepository extends JpaRepository<Canteen, Long> {
    List<Canteen> findByIsOpenTrueAndIsVerifiedTrueOrderByRatingDesc();
    List<Canteen> findByIsVerifiedTrueOrderByRatingDesc();
    Optional<Canteen> findFirstByManagerId(Long managerId);
}