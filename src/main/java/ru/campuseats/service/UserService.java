package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.entity.User;
import ru.campuseats.enums.Role;
import ru.campuseats.dto.LoginRequestDto;
import ru.campuseats.dto.RegisterUserRequestDto;
import ru.campuseats.dto.UserDto;
import ru.campuseats.mapper.UserMapper;
import ru.campuseats.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDto authenticate(LoginRequestDto request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Пользователь с таким email не найден"
                ));

        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный пароль");
        }

        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto register(RegisterUserRequestDto request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Пользователь с таким email уже зарегистрирован"
            );
        }
        User savedUser = userRepository.save(userMapper.toEntity(request));
        return userMapper.toDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(Role role) {
        List<User> users = (role != null)
                ? userRepository.findByRoleOrderByIdAsc(role)
                : userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + id + " не найден"
                ));
        return userMapper.toDto(user);
    }
}
