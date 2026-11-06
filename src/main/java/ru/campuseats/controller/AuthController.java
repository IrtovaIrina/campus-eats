package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.enums.Role;
import ru.campuseats.dto.LoginRequestDto;
import ru.campuseats.dto.RegisterUserRequestDto;
import ru.campuseats.dto.UserDto;
import ru.campuseats.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация и Роли пользователей", description = "Аутентификация, регистрация и переключение между ролями")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Вход пользователя по email и паролю")
    public UserDto login(@Valid @RequestBody LoginRequestDto request) {
        return userService.authenticate(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация нового пользователя")
    public UserDto register(@Valid @RequestBody RegisterUserRequestDto request) {
        return userService.register(request);
    }

    @GetMapping("/users")
    @Operation(summary = "Список пользователей с фильтрацией по роли (для переключения сессий)")
    public List<UserDto> getUsers(@RequestParam(required = false) Role role) {
        return userService.getUsers(role);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Получение профиля пользователя по ID")
    public UserDto getUserProfile(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}