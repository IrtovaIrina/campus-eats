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
@Schema(description = "Сводная корзина предзаказа с расчётом суммы, КБЖУ и доступными окнами выдачи (Шаг 3 ключевого сценария)")
public class CartSummaryDto {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "ID точки питания, к которой привязана корзина", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Корпус точки питания", example = "Корпус ИРИТ-РТФ, ул. Мира, 32")
    private String canteenBuilding;

    @Schema(description = "Список позиций в корзине")
    private List<CartItemDto> items;

    @Schema(description = "Общее количество порций в корзине", example = "3")
    private Integer totalQuantity;

    @Schema(description = "Итоговая сумма к оплате (руб.)", example = "890.00")
    private BigDecimal totalAmount;

    @Schema(description = "Суммарная калорийность заказа (ккал)", example = "1150")
    private Integer totalCalories;

    @Schema(description = "Суммарные белки (г)", example = "58.4")
    private Double totalProteins;

    @Schema(description = "Суммарные жиры (г)", example = "36.0")
    private Double totalFats;

    @Schema(description = "Суммарные углеводы (г)", example = "92.5")
    private Double totalCarbs;

    @Schema(description = "Ориентировочное время приготовления заказа (мин.)", example = "10")
    private Integer estimatedPrepMinutes;

    @Schema(description = "Доступные окна экспресс-выдачи в этой столовой")
    private List<PickupPointDto> availablePickupPoints;

    @Schema(description = "Доступные тайм-слоты получения заказа без очереди")
    private List<String> availableTimeSlots;
}