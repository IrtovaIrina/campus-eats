package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Отзыв студента о блюде с ответом точки питания")
public class ReviewDto {

    @Schema(description = "ID отзыва", example = "1")
    private Long id;

    @Schema(description = "ID автора отзыва", example = "1")
    private Long userId;

    @Schema(description = "ФИО автора отзыва", example = "Соколов Алексей Дмитриевич")
    private String userFullName;

    @Schema(description = "Учебная группа автора", example = "РИ-320941")
    private String userStudentGroup;

    @Schema(description = "ID блюда", example = "10")
    private Long dishId;

    @Schema(description = "Название блюда", example = "Поке-боул с лососем, киноа и авокадо")
    private String dishName;

    @Schema(description = "ID точки питания", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Номер заказа (если отзыв привязан к заказу)", example = "CE-101")
    private String orderNumber;

    @Schema(description = "Оценка (1..5)", example = "5")
    private Integer rating;

    @Schema(description = "Текст отзыва студента", example = "Очень свежий лосось и тёплая киноа!")
    private String comment;

    @Schema(description = "Ответ менеджера столовой (если есть)", example = "Спасибо за отзыв, Алексей! Ждём вас снова на большой перемене!")
    private String vendorReply;

    @Schema(description = "Дата и время публикации отзыва")
    private LocalDateTime createdAt;
}