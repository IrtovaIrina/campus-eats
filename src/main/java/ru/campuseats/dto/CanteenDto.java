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

    private Long id;
    private String name;
    private String building;
    private Integer floor;
    private String openingHours;
    private String description;
    private String imageUrl;
    private Double rating;
    private Integer reviewsCount;
    private Boolean isOpen;
    private Boolean isVerified;
    private Long managerId;
    private String managerName;
}