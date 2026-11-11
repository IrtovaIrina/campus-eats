package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Позиция внутри заказа")
public class OrderItemDto {

    private Long id;
    private Long dishId;
    private String dishNameSnapshot;
    private String dishImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotalPrice;
    private Integer caloriesPerUnit;
    private String specialRequest;
}