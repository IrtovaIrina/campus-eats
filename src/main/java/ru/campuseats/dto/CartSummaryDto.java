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
@Schema(description = "Сводная корзина предзаказа")
public class CartSummaryDto {

    private Long userId;
    private Long canteenId;
    private String canteenName;
    private String canteenBuilding;
    private List<CartItemDto> items;
    private Integer totalQuantity;
    private Integer availableItemsCount;
    private BigDecimal totalAmount;
    private Integer totalCalories;
    private Double totalProteins;
    private Double totalFats;
    private Double totalCarbs;
    private Integer estimatedPrepMinutes;
    private List<String> availableTimeSlots;
}