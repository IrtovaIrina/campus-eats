package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация об оформленном предзаказе")
public class OrderResponseDto {

    @Schema(description = "ID заказа", example = "1")
    private Long id;

    @Schema(description = "Короткий пин-код получения в окне экспресс-выдачи", example = "CE-101")
    private String orderNumber;

    @Schema(description = "ID заказчика (студента)", example = "1")
    private Long userId;

    @Schema(description = "ФИО заказчика", example = "Соколов Алексей Дмитриевич")
    private String userFullName;

    @Schema(description = "Учебная группа заказчика", example = "РИ-320941")
    private String userStudentGroup;

    @Schema(description = "ID точки питания", example = "1")
    private Long canteenId;

    @Schema(description = "Название точки питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Корпус точки питания", example = "Корпус ИРИТ-РТФ, ул. Мира, 32")
    private String canteenBuilding;

    @Schema(description = "ID окна выдачи", example = "1")
    private Long pickupPointId;

    @Schema(description = "Название окна экспресс-выдачи", example = "Окно экспресс-выдачи №1 (Green Line)")
    private String pickupWindowCode;

    @Schema(description = "Ориентир окна выдачи", example = "Слева от главной кассы, стойка с QR-сканером")
    private String pickupLocationHint;

    @Schema(description = "Выбранный слот времени получения", example = "11:50 - 12:05 (Большая перемена)")
    private String pickupTimeSlot;

    @Schema(description = "Итоговая сумма заказа (руб.)", example = "680.00")
    private BigDecimal totalAmount;

    @Schema(description = "Общая калорийность заказа (ккал)", example = "840")
    private Integer totalCalories;

    @Schema(description = "Текущий статус заказа (ACCEPTED, COOKING, READY, COMPLETED, CANCELLED)", example = "ACCEPTED")
    private OrderStatus status;

    @Schema(description = "Русское название статуса для интерфейса", example = "Принят")
    private String statusDisplayName;

    @Schema(description = "Оплачен ли заказ", example = "false")
    private Boolean isPaid;

    @Schema(description = "Комментарий к заказу", example = "Приборы на 2 персоны")
    private String comment;

    @Schema(description = "Список блюд в заказе")
    private List<OrderItemDto> items;

    @Schema(description = "Дата и время создания заказа")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время последнего обновления статуса")
    private LocalDateTime updatedAt;
}