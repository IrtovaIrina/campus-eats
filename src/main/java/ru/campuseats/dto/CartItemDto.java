package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Позиция внутри корзины предзаказа")
public class CartItemDto {

    @Schema(description = "ID позиции в корзине", example = "1")
    private Long id;

    @Schema(description = "ID блюда", example = "10")
    private Long dishId;

    @Schema(description = "Название блюда", example = "Поке-боул с лососем, киноа и авокадо")
    private String dishName;

    @Schema(description = "URL фотографии блюда")
    private String dishImageUrl;

    @Schema(description = "Вес одной порции (г)", example = "310")
    private Integer weightGrams;

    @Schema(description = "Цена за 1 порцию (руб.)", example = "340.00")
    private BigDecimal unitPrice;

    @Schema(description = "Количество порций", example = "2")
    private Integer quantity;

    @Schema(description = "Итоговая стоимость позиции (руб.)", example = "680.00")
    private BigDecimal subtotalPrice;

    @Schema(description = "Калорийность 1 порции (ккал)", example = "420")
    private Integer caloriesPerUnit;

    @Schema(description = "Итоговая калорийность позиции (ккал)", example = "840")
    private Integer subtotalCalories;

    @Schema(description = "Комментарий/пожелание к позиции", example = "Соус подать отдельно")
    private String specialRequest;
}