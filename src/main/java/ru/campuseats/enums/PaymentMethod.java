package ru.campuseats.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    SBP("Система быстрых платежей (QR-код)"),
    CAMPUS_CARD("Кампусная карта студента / Профком"),
    BANK_CARD("Банковская карта (Мир / Visa / Mastercard)");

    private final String displayName;
}