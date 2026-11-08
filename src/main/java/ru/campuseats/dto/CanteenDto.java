package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация о точке питания кампуса")
public class CanteenDto {

    @Schema(description = "ID точки питания", example = "1")
    private Long id;

    @Schema(description = "Название столовой или кафе", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String name;

    @Schema(description = "Учебный корпус и адрес", example = "Корпус ИРИТ-РТФ, ул. Мира, 32")
    private String building;

    @Schema(description = "Этаж", example = "1")
    private Integer floor;

    @Schema(description = "Часы работы", example = "08:30 - 18:30")
    private String openingHours;

    @Schema(description = "Краткое описание", example = "Горячие обеды, боулы, кофе и экспресс-выдача к большой перемене")
    private String description;

    @Schema(description = "URL фотографии точки питания")
    private String imageUrl;

    @Schema(description = "Средний рейтинг точки питания", example = "4.8")
    private Double rating;

    @Schema(description = "Количество отзывов", example = "42")
    private Integer reviewsCount;

    @Schema(description = "Открыта ли точка для предзаказа", example = "true")
    private Boolean isOpen;

    @Schema(description = "ID менеджера точки питания (роль VENDOR)", example = "2")
    private Long managerId;

    @Schema(description = "ФИО менеджера точки питания", example = "Карпов Сергей Викторович")
    private String managerName;
}