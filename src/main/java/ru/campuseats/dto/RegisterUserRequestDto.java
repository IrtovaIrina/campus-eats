package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.campuseats.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на регистрацию пользователя")
public class RegisterUserRequestDto {

    @NotBlank(message = "Укажите ФИО")
    private String fullName;

    @NotBlank(message = "Укажите email")
    @Email(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Некорректный формат email (пример: a.d.sokolov@campus.edu)"
    )
    private String email;

    @NotBlank(message = "Укажите телефон")
    @Pattern(
            regexp = "^(\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Некорректный формат телефона. Пример: +7 (922) 114-55-09"
    )
    private String phone;

    @NotBlank(message = "Укажите пароль")
    private String password;

    @NotNull(message = "Выберите роль")
    private Role role;

    private String studentGroup;
    private String canteenName;
    private String canteenBuilding;
    private Integer canteenFloor;
    private String canteenOpeningHours;
    private String canteenDescription;
}