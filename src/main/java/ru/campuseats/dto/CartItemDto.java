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

    private Long id;
    private Long dishId;
    private String dishName;
    private String dishImageUrl;
    private Integer weightGrams;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotalPrice;
    private Integer caloriesPerUnit;
    private Integer subtotalCalories;
    private String specialRequest;
    private Boolean isAvailable;
}