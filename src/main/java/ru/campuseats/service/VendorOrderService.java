package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.dto.UpdateOrderStatusRequestDto;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.entity.Notification;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.mapper.NotificationMapper;
import ru.campuseats.mapper.OrderMapper;
import ru.campuseats.repository.FoodOrderRepository;
import ru.campuseats.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorOrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final NotificationRepository notificationRepository;
    private final OrderMapper orderMapper;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getCanteenQueue(Long canteenId, OrderStatus status) {
        List<FoodOrder> orders = (status != null)
                ? foodOrderRepository.findByCanteenIdAndStatusOrderByCreatedAtAsc(canteenId, status)
                : foodOrderRepository.findByCanteenIdOrderByCreatedAtDesc(canteenId);
        return orderMapper.toResponseDtoList(orders);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto request) {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Заказ с ID " + orderId + " не найден"
                ));

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (oldStatus == newStatus) {
            return orderMapper.toResponseDto(order);
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        FoodOrder savedOrder = foodOrderRepository.save(order);

        // Генерация пуш-уведомления студенту
        generateNotificationForStatusChange(savedOrder, newStatus);

        return orderMapper.toResponseDto(savedOrder);
    }

    private void generateNotificationForStatusChange(FoodOrder order, OrderStatus status) {
        String title;
        String message;

        switch (status) {
            case COOKING -> {
                title = "Заказ готовится 🍳";
                message = "Повар столовой «" + order.getCanteen().getName() + "» начал приготовление вашего предзаказа #" + order.getOrderNumber() + ".";
            }
            case READY -> {
                title = "Заказ готов к выдаче! 🎉";
                message = "Ваш предзаказ #" + order.getOrderNumber() + " ожидает вас в окне «" + order.getPickupPoint().getWindowCode() + "». Приятного аппетита!";
            }
            case COMPLETED -> {
                title = "Заказ выдан ✓";
                message = "Предзаказ #" + order.getOrderNumber() + " успешно выдан. Будем рады вашей оценке в разделе отзывов!";
            }
            case CANCELLED -> {
                title = "Заказ отменён ❌";
                message = "К сожалению, предзаказ #" + order.getOrderNumber() + " был отменён столовой. Средства возвращены на ваш счёт.";
            }
            default -> {
                title = "Статус заказа обновлен";
                message = "Ваш предзаказ #" + order.getOrderNumber() + " переведен в статус: " + status.getDisplayName();
            }
        }

        Notification notification = notificationMapper.toEntity(
                order.getUser(), order, title, message
        );
        notificationRepository.save(notification);
    }
}