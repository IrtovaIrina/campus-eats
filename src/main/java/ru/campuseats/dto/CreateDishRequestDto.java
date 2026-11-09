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
@Schema(description = "Запрос на добавление нового блюда в меню (для ролей ADMIN и VENDOR)")
public class CreateDishRequestDto {

    @NotNull(message = "Укажите ID точки питания")
    @Schema(description = "ID столовой/кафе", example = "1")
    private Long canteenId;

    @NotNull(message = "Укажите ID категории меню")
    @Schema(description = "ID категории", example = "2")
    private Long categoryId;

    @NotBlank(message = "Укажите название блюда")
    @Size(max = 150, message = "Название блюда не должно превышать 150 символов")
    @Schema(description = "Название блюда", example = "Сырники из фермерского творога без глютена на рисовой муке")
    private String name;

    @NotBlank(message = "Укажите состав блюда")
    @Size(max = 600, message = "Состав не должен превышать 600 символов")
    @Schema(description = "Полный состав", example = "Творог 5%, мука рисовая цельнозерновая, яйцо куриное С0, эритрит, ваниль натуральная, ягодный конфитюр без сахара")
    private String composition;

    @NotNull(message = "Укажите вес порции в граммах")
    @Min(value = 20, message = "Вес порции должен быть не менее 20 г")
    @Schema(description = "Вес порции (г)", example = "220")
    private Integer weightGrams;

    @NotNull(message = "Укажите калорийность")
    @Min(value = 1, message = "Калорийность должна быть больше 0")
    @Schema(description = "Калорийность порции (ккал)", example = "310")
    private Integer calories;

    @NotNull(message = "Укажите количество белков")
    @PositiveOrZero(message = "Белки не могут быть отрицательными")
    @Schema(description = "Белки (г)", example = "26.4")
    private Double proteins;

    @NotNull(message = "Укажите количество жиров")
    @PositiveOrZero(message = "Жиры не могут быть отрицательными")
    @Schema(description = "Жиры (г)", example = "12.0")
    private Double fats;

    @NotNull(message = "Укажите количество углеводов")
    @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
    @Schema(description = "Углеводы (г)", example = "24.2")
    private Double carbs;

    @NotNull(message = "Укажите цену блюда")
    @DecimalMin(value = "10.00", message = "Цена должна быть не менее 10 руб.")
    @Schema(description = "Цена (руб.)", example = "210.00")
    private BigDecimal price;

    @NotBlank(message = "Укажите ссылку на фото блюда")
    @Schema(description = "URL фотографии блюда", example = "https://images.unsplash.com/photo-1565299543923-37dd37887442?auto=format&fit=crop&w=900&q=80")
    private String imageUrl;

    @Min(value = 1, message = "Время приготовления должно быть не менее 1 минуты")
    @Schema(description = "Время приготовления (мин.)", example = "8")
    private Integer prepTimeMinutes;

    @Schema(description = "Коды диетических тегов (например: GLUTEN_FREE, SUGAR_FREE, HIGH_PROTEIN)", example = "[\"GLUTEN_FREE\", \"HIGH_PROTEIN\"]")
    private List<String> dietaryTagCodes;
}