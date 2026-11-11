package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Профиль пользователя")
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private String studentGroup;
    private LocalDateTime createdAt;
}