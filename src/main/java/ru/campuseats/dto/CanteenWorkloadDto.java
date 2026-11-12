package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Статистика загруженности и выручки буфета")
public class CanteenWorkloadDto {

    private Long canteenId;
    private String canteenName;
    private String building;
    private Integer activeQueueCount;
    private Integer workloadPercent;
    private String workloadLevel;
    private BigDecimal revenue;
    private Double rating;
}