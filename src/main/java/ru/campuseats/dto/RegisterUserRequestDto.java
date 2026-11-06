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
@Schema(description = "Запрос на регистрацию нового пользователя")
public class RegisterUserRequestDto {

    @NotBlank(message = "Укажите ФИО")
    @Schema(description = "Полное имя")
    private String fullName;

    @NotBlank(message = "Укажите email")
    @Email(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Некорректный формат email (пример: a.d.sokolov@campus.ru)"
    )
    @Schema(description = "Email")
    private String email;

    @NotBlank(message = "Укажите телефон")
    @Pattern(
            regexp = "^(\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Некорректный формат телефона. Пример: +7 (922) 114-55-09 или 89221145509"
    )
    @Schema(description = "Телефон")
    private String phone;

    @NotBlank(message = "Укажите пароль")
    @Schema(description = "Пароль")
    private String password;

    @NotNull(message = "Выберите роль")
    @Schema(description = "Роль пользователя")
    private Role role;

    @Schema(description = "Академическая группа (для студентов)")
    private String studentGroup;
}
