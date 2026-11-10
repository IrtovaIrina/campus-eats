package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.NotificationDto;
import ru.campuseats.entity.Notification;
import ru.campuseats.mapper.NotificationMapper;
import ru.campuseats.repository.NotificationRepository;
import ru.campuseats.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден"
                ));
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notificationMapper.toDtoList(list);
    }

    @Transactional
    public NotificationDto markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Уведомление с ID " + notificationId + " не найдено"
                ));
        notification.setIsRead(true);
        return notificationMapper.toDto(notificationRepository.save(notification));
    }
}
