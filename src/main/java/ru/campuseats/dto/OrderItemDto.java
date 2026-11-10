package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Позиция внутри оформленного заказа")
public class OrderItemDto {

    @Schema(description = "ID позиции заказа", example = "1")
    private Long id;

    @Schema(description = "ID блюда", example = "10")
    private Long dishId;

    @Schema(description = "Название блюда на момент заказа", example = "Поке-боул с лососем, киноа и авокадо")
    private String dishNameSnapshot;

    @Schema(description = "URL фото блюда")
    private String dishImageUrl;

    @Schema(description = "Количество порций", example = "2")
    private Integer quantity;

    @Schema(description = "Цена за 1 порцию (руб.)", example = "340.00")
    private BigDecimal unitPrice;

    @Schema(description = "Стоимость позиции (руб.)", example = "680.00")
    private BigDecimal subtotalPrice;

    @Schema(description = "Калорийность за 1 порцию (ккал)", example = "420")
    private Integer caloriesPerUnit;

    @Schema(description = "Пожелание повару по позиции", example = "Соус подать отдельно")
    private String specialRequest;
}