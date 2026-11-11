package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Экран трекинга заказа")
public class OrderTrackingDto {

    private Long orderId;
    private String orderNumber;
    private OrderStatus currentStatus;
    private String currentStatusDisplayName;
    private Integer progressPercent;
    private String statusHeadline;
    private String canteenName;
    private String canteenLocation;
    private String pickupTimeSlot;
    private Boolean isPaid;
    private Boolean canCancel;
    private List<TrackingStepDto> steps;
    private LocalDateTime updatedAt;
}