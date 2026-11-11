package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Отзыв о блюде")
public class ReviewDto {

    private Long id;
    private Long userId;
    private String userFullName;
    private String userStudentGroup;
    private Long dishId;
    private String dishName;
    private Long canteenId;
    private String canteenName;
    private String orderNumber;
    private Integer rating;
    private String comment;
    private String vendorReply;
    private LocalDateTime createdAt;
}