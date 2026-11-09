package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.DietaryTag;

import java.util.List;
import java.util.Optional;

public interface DietaryTagRepository extends JpaRepository<DietaryTag, Long> {
    Optional<DietaryTag> findByCodeIgnoreCase(String code);
    List<DietaryTag> findAllByOrderByIdAsc();
}