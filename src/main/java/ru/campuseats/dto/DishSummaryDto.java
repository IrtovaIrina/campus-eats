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
@Schema(description = "Краткая карточка блюда в каталоге")
public class DishSummaryDto {

    private Long id;
    private Long canteenId;
    private String canteenName;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String composition;
    private Integer weightGrams;
    private Integer calories;
    private Double proteins;
    private Double fats;
    private Double carbs;
    private BigDecimal price;
    private String imageUrl;
    private Integer prepTimeMinutes;
    private Boolean isAvailable;
    private Double rating;
    private List<DietaryTagDto> dietaryTags;
}