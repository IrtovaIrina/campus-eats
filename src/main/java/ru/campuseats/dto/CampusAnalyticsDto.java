package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сводная аналитика по заказам и точкам питания")
public class CampusAnalyticsDto {

    private Integer totalOrders;
    private Integer activeOrders;
    private Integer completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderCheck;
    private Long totalUsers;
    private Long totalDishes;
    private Map<String, Long> ordersByStatus;
    private Map<String, Long> ordersByTimeSlot;
    private List<CanteenWorkloadDto> canteenWorkloads;
    private List<PopularDishStatDto> topDishes;
}