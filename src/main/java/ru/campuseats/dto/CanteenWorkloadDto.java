package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Статистика загруженности и выручки конкретной точки питания кампуса")
public class CanteenWorkloadDto {

    @Schema(description = "ID точки питания", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Корпус", example = "Корпус ИРИТ-РТФ, ул. Мира, 32")
    private String building;

    @Schema(description = "Всего заказов за период", example = "18")
    private Integer totalOrdersCount;

    @Schema(description = "Активных заказов в очереди прямо сейчас (ACCEPTED + COOKING + READY)", example = "4")
    private Integer activeQueueCount;

    @Schema(description = "Суммарная пропускная способность окон выдачи за слот (заказов / 15 мин)", example = "30")
    private Integer slotCapacity;

    @Schema(description = "Текущий процент загрузки линии выдачи (0..100%)", example = "45")
    private Integer workloadPercent;

    @Schema(description = "Уровень загрузки (LOW — Свободно, MODERATE — Средняя загрузка, HIGH_PEAK — Час пик)", example = "MODERATE")
    private String workloadLevel;

    @Schema(description = "Выручка точки питания (руб.)", example = "8450.00")
    private BigDecimal revenue;

    @Schema(description = "Средний рейтинг точки питания", example = "4.9")
    private Double rating;
}