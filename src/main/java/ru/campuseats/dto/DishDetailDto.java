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
@Schema(description = "Детальная карточка блюда (Шаг 2 ключевого сценария)")
public class DishDetailDto {

    @Schema(description = "ID блюда", example = "10")
    private Long id;

    @Schema(description = "ID точки питания", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Корпус и адрес точки питания", example = "Корпус ИРИТ-РТФ, ул. Мира, 32")
    private String canteenBuilding;

    @Schema(description = "Этаж", example = "1")
    private Integer canteenFloor;

    @Schema(description = "Часы работы точки питания", example = "08:30 - 18:30")
    private String canteenOpeningHours;

    @Schema(description = "ID категории", example = "2")
    private Long categoryId;

    @Schema(description = "Название категории", example = "Боулы и ЗОЖ")
    private String categoryName;

    @Schema(description = "Название блюда", example = "Поке-боул с лососем, киноа и авокадо")
    private String name;

    @Schema(description = "Полный состав и аллергены", example = "Филе лосося слабосолёное, киноа отварная, авокадо Хасс, бобы эдамаме, свежий огурец, водоросли чука, кунжутный соус, семена белого и чёрного кунжута")
    private String composition;

    @Schema(description = "Вес порции (г)", example = "310")
    private Integer weightGrams;

    @Schema(description = "Калорийность всей порции (ккал)", example = "420")
    private Integer calories;

    @Schema(description = "Калорийность на 100 г (ккал)", example = "135")
    private Integer caloriesPer100g;

    @Schema(description = "Белки на порцию (г)", example = "24.5")
    private Double proteins;

    @Schema(description = "Жиры на порцию (г)", example = "18.2")
    private Double fats;

    @Schema(description = "Углеводы на порцию (г)", example = "38.0")
    private Double carbs;

    @Schema(description = "Цена (руб.)", example = "340.00")
    private BigDecimal price;

    @Schema(description = "URL фотографии блюда")
    private String imageUrl;

    @Schema(description = "Среднее время сборки/приготовления заказа (мин.)", example = "7")
    private Integer prepTimeMinutes;

    @Schema(description = "Доступно для заказа прямо сейчас", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Средний рейтинг блюда", example = "4.9")
    private Double rating;

    @Schema(description = "Диетические теги (Вегетарианское, Без глютена и т.д.)")
    private List<DietaryTagDto> dietaryTags;
}