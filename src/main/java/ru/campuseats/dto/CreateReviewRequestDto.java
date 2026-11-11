package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на добавление отзыва")
public class CreateReviewRequestDto {

    @NotNull(message = "Укажите ID пользователя")
    private Long userId;

    @NotNull(message = "Укажите ID блюда")
    private Long dishId;

    private Long orderId;

    @NotNull(message = "Поставьте оценку от 1 до 5")
    @Min(value = 1, message = "Минимальная оценка — 1")
    @Max(value = 5, message = "Максимальная оценка — 5")
    private Integer rating;

    @NotBlank(message = "Напишите текст отзыва")
    @Size(min = 5, max = 500, message = "Отзыв должен содержать от 5 до 500 символов")
    private String comment;
}