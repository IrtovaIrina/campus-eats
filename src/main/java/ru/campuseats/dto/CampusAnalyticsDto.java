package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сводная аналитика по заказам и буфетам")
public class CampusAnalyticsDto {

    private Integer totalOrders;
    private Integer activeOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderCheck;
    private Long totalDishes;
    private List<CanteenWorkloadDto> canteenWorkloads;
    private List<PopularDishStatDto> topDishes;
}