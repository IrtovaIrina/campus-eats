package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Окно экспресс-выдачи предзаказов без очереди")
public class PickupPointDto {

    @Schema(description = "ID окна выдачи", example = "1")
    private Long id;

    @Schema(description = "ID точки питания", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Код/название окна выдачи", example = "Окно экспресс-выдачи №1 (Green Line)")
    private String windowCode;

    @Schema(description = "Ориентир внутри столовой", example = "Слева от главной кассы, стойка с QR-сканером")
    private String locationHint;

    @Schema(description = "Лимит заказов на 15-минутный слот", example = "15")
    private Integer maxOrdersPerSlot;

    @Schema(description = "Активно ли окно выдачи", example = "true")
    private Boolean isActive;
}