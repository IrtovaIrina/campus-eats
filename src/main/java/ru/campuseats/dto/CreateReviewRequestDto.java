package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на добавление отзыва и оценки блюду")
public class CreateReviewRequestDto {

    @NotNull(message = "Укажите ID пользователя")
    @Schema(description = "ID студента", example = "1")
    private Long userId;

    @NotNull(message = "Укажите ID блюда")
    @Schema(description = "ID оцениваемого блюда", example = "10")
    private Long dishId;

    @Schema(description = "ID выполненного заказа (необязательно)", example = "1")
    private Long orderId;

    @NotNull(message = "Поставьте оценку от 1 до 5")
    @Min(value = 1, message = "Минимальная оценка — 1 звезда")
    @Max(value = 5, message = "Максимальная оценка — 5 звёзд")
    @Schema(description = "Оценка блюда по 5-балльной шкале", example = "5")
    private Integer rating;

    @NotBlank(message = "Напишите текст отзыва")
    @Size(min = 5, max = 500, message = "Отзыв должен содержать от 5 до 500 символов")
    @Schema(description = "Впечатления о блюде и скорости выдачи", example = "Очень свежий лосось и тёплая киноа! Забрал в окне №1 ровно на большой перемене без очереди.")
    private String comment;
}