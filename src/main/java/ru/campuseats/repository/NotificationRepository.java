package ru.campuseats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.campuseats.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndIsReadFalse(Long userId);
}