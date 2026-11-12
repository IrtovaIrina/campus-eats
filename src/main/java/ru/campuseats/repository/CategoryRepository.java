package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByDisplayOrderAsc();
}