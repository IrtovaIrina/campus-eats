package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на обновление настроек профиля в личном кабинете")
public class UpdateProfileRequestDto {

    @NotBlank(message = "Укажите ФИО")
    private String fullName;

    private String phone;

    private String studentGroup;
}