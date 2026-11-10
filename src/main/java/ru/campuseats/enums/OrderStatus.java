package ru.campuseats.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ACCEPTED("Принят"),
    COOKING("Готовится"),
    READY("Готов к выдаче"),
    COMPLETED("Выдан"),
    CANCELLED("Отменён");

    private final String displayName;
}