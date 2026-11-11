package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание или обновление точки питания")
public class CreateCanteenRequestDto {

    @NotBlank(message = "Укажите название точки питания")
    private String name;

    @NotBlank(message = "Укажите учебный корпус")
    private String building;

    @NotNull(message = "Укажите этаж")
    @Min(value = 1, message = "Этаж должен быть не меньше 1")
    private Integer floor;

    @NotBlank(message = "Укажите часы работы")
    private String openingHours;

    private String description;

    @NotBlank(message = "Укажите ссылку на изображение")
    private String imageUrl;

    private Long managerId;
}