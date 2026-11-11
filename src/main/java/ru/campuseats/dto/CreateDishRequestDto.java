package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание или редактирование блюда")
public class CreateDishRequestDto {

    @NotNull(message = "Укажите ID точки питания")
    private Long canteenId;

    @NotNull(message = "Укажите ID категории меню")
    private Long categoryId;

    @NotBlank(message = "Укажите название блюда")
    @Size(max = 150, message = "Название блюда не должно превышать 150 символов")
    private String name;

    @NotBlank(message = "Укажите состав блюда")
    @Size(max = 600, message = "Состав не должен превышать 600 символов")
    private String composition;

    @NotNull(message = "Укажите вес порции в граммах")
    @Min(value = 20, message = "Вес порции должен быть не менее 20 г")
    private Integer weightGrams;

    @NotNull(message = "Укажите калорийность")
    @Min(value = 1, message = "Калорийность должна быть больше 0")
    private Integer calories;

    @NotNull(message = "Укажите количество белков")
    @PositiveOrZero(message = "Белки не могут быть отрицательными")
    private Double proteins;

    @NotNull(message = "Укажите количество жиров")
    @PositiveOrZero(message = "Жиры не могут быть отрицательными")
    private Double fats;

    @NotNull(message = "Укажите количество углеводов")
    @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
    private Double carbs;

    @NotNull(message = "Укажите цену блюда")
    @DecimalMin(value = "10.00", message = "Цена должна быть не менее 10 руб.")
    private BigDecimal price;

    @NotBlank(message = "Укажите ссылку на фото блюда")
    private String imageUrl;

    @Min(value = 1, message = "Время приготовления должно быть не менее 1 минуты")
    private Integer prepTimeMinutes;

    private List<String> dietaryTagCodes;
}