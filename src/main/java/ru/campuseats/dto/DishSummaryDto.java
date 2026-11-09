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
@Schema(description = "Карточка блюда в каталоге меню")
public class DishSummaryDto {

    @Schema(description = "ID блюда", example = "10")
    private Long id;

    @Schema(description = "ID точки питания", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "ID категории", example = "2")
    private Long categoryId;

    @Schema(description = "Название категории", example = "Боулы и ЗОЖ")
    private String categoryName;

    @Schema(description = "Название блюда", example = "Поке-боул с лососем, киноа и авокадо")
    private String name;

    @Schema(description = "Состав и ингредиенты", example = "Филе лосося слабосолёное, киноа отварная, авокадо Хасс, бобы эдамаме, огурец, кунжутный соус")
    private String composition;

    @Schema(description = "Вес порции (г)", example = "310")
    private Integer weightGrams;

    @Schema(description = "Калорийность порции (ккал)", example = "420")
    private Integer calories;

    @Schema(description = "Белки (г)", example = "24.5")
    private Double proteins;

    @Schema(description = "Жиры (г)", example = "18.2")
    private Double fats;

    @Schema(description = "Углеводы (г)", example = "38.0")
    private Double carbs;

    @Schema(description = "Цена (руб.)", example = "340.00")
    private BigDecimal price;

    @Schema(description = "URL фотографии блюда")
    private String imageUrl;

    @Schema(description = "Время приготовления (мин.)", example = "7")
    private Integer prepTimeMinutes;

    @Schema(description = "Доступно для заказа", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Средний рейтинг блюда по отзывам", example = "4.9")
    private Double rating;

    @Schema(description = "Диетические особенности (вегетарианское, без глютена и т.д.)")
    private List<DietaryTagDto> dietaryTags;
}