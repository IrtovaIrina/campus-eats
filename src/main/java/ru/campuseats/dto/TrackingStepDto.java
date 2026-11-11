package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Этап выполнения заказа")
public class TrackingStepDto {

    private OrderStatus status;
    private String title;
    private String description;
    private boolean completed;
    private boolean current;
}