package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.NotificationDto;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.entity.Notification;
import ru.campuseats.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderNumber", expression = "java(notification.getOrder() != null ? notification.getOrder().getOrderNumber() : null)")
    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDtoList(List<Notification> notifications);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "isRead", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Notification toEntity(User user, FoodOrder order, String title, String message);
}