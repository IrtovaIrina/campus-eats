package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на вход в систему")
public class LoginRequestDto {

    @NotBlank(message = "Email не может быть пустым")
    @Email(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Некорректный формат email (пример: a.d.sokolov@campus.edu)"
    )
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}