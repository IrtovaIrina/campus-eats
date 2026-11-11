package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Статистика продаж блюда")
public class PopularDishStatDto {

    private Long dishId;
    private String dishName;
    private String canteenName;
    private String categoryName;
    private Integer totalPortionsSold;
    private BigDecimal totalRevenue;
    private Double rating;
}