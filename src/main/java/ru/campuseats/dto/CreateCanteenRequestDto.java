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
@Schema(description = "Запрос на добавление новой точки питания кампуса")
public class CreateCanteenRequestDto {

    @NotBlank(message = "Укажите название точки питания")
    @Schema(description = "Название столовой или буфета", example = "Столовая Главного учебного корпуса")
    private String name;

    @NotBlank(message = "Укажите учебный корпус")
    @Schema(description = "Корпус кампуса", example = "Главный учебный корпус, ул. Мира, 19")
    private String building;

    @NotNull(message = "Укажите этаж")
    @Min(value = 1, message = "Этаж должен быть не меньше 1")
    @Schema(description = "Этаж", example = "1")
    private Integer floor;

    @NotBlank(message = "Укажите часы работы")
    @Schema(description = "Режим работы", example = "08:00 - 19:00")
    private String openingHours;

    @Schema(description = "Описание точки питания", example = "Комплексные обеды, диетическая линия и свежая выпечка")
    private String description;

    @NotBlank(message = "Укажите ссылку на изображение")
    @Schema(description = "URL фото", example = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=900&q=80")
    private String imageUrl;

    @Schema(description = "ID менеджера точки питания (необязательно)", example = "2")
    private Long managerId;
}