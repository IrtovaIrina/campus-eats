package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Экран трекинга заказа в реальном времени (Шаг 5 ключевого сценария)")
public class OrderTrackingDto {

    @Schema(description = "ID заказа", example = "1")
    private Long orderId;

    @Schema(description = "Короткий пин-код для получения в окне выдачи", example = "CE-101")
    private String orderNumber;

    @Schema(description = "Текущий статус заказа", example = "READY")
    private OrderStatus currentStatus;

    @Schema(description = "Отображаемое название статуса", example = "Готов к выдаче")
    private String currentStatusDisplayName;

    @Schema(description = "Процент выполнения заказа для прогресс-бара (0..100)", example = "85")
    private Integer progressPercent;

    @Schema(description = "Статусное сообщение для студента", example = "Заказ ждёт вас в окне экспресс-выдачи! Назовите пин-код CE-101")
    private String statusHeadline;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Корпус и этаж", example = "Корпус ИРИТ-РТФ, ул. Мира, 32 (1 этаж)")
    private String canteenLocation;

    @Schema(description = "Окно экспресс-выдачи", example = "Окно экспресс-выдачи №1 (Green Line)")
    private String pickupWindowCode;

    @Schema(description = "Ориентир окна выдачи", example = "Слева от главной кассы, стойка с QR-сканером")
    private String pickupLocationHint;

    @Schema(description = "Выбранный слот времени получения", example = "11:50 - 12:05 (Большая перемена)")
    private String pickupTimeSlot;

    @Schema(description = "Оплачен ли заказ", example = "true")
    private Boolean isPaid;

    @Schema(description = "Можно ли отменить заказ прямо сейчас (доступно только в статусе ACCEPTED)", example = "false")
    private Boolean canCancel;

    @Schema(description = "Пошаговая шкала прогресса (Принят → Готовится → Готов к выдаче → Выдан)")
    private List<TrackingStepDto> steps;

    @Schema(description = "Время последнего обновления статуса")
    private LocalDateTime updatedAt;
}