package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Профиль пользователя")
public class UserDto {

    @Schema(description = "ID пользователя")
    private Long id;

    @Schema(description = "ФИО пользователя")
    private String fullName;

    @Schema(description = "Корпоративный email")
    private String email;

    @Schema(description = "Контактный телефон")
    private String phone;

    @Schema(description = "Роль в системе")
    private Role role;

    @Schema(description = "Учебная группа или подразделение")
    private String studentGroup;

    @Schema(description = "Бонусный баланс кампусной карты")
    private BigDecimal bonusBalance;

    @Schema(description = "Дата регистрации")
    private LocalDateTime createdAt;
}
