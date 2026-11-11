package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация об оформленном предзаказе")
public class OrderResponseDto {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String userFullName;
    private String userStudentGroup;
    private Long canteenId;
    private String canteenName;
    private String canteenBuilding;
    private String pickupTimeSlot;
    private BigDecimal totalAmount;
    private Integer totalCalories;
    private OrderStatus status;
    private String statusDisplayName;
    private Boolean isPaid;
    private String comment;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}