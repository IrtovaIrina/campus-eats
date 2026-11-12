package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.LoginRequestDto;
import ru.campuseats.dto.RegisterUserRequestDto;
import ru.campuseats.dto.UpdateProfileRequestDto;
import ru.campuseats.dto.UserDto;
import ru.campuseats.entity.Canteen;
import ru.campuseats.entity.User;
import ru.campuseats.enums.Role;
import ru.campuseats.mapper.UserMapper;
import ru.campuseats.repository.CanteenRepository;
import ru.campuseats.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CanteenRepository canteenRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto register(RegisterUserRequestDto request) {
        if (request.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Недопустимая роль для регистрации"
            );
        }

        if (userRepository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Пользователь с таким Email уже зарегистрирован"
            );
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(hashPassword(request.getPassword()));
        User saved = userRepository.save(user);

        if (saved.getRole() == Role.CANTEEN) {
            String cName = (request.getCanteenName() != null && !request.getCanteenName().isBlank())
                    ? request.getCanteenName().trim()
                    : "Буфет («" + saved.getFullName() + "»)";
            String cBuilding = (request.getCanteenBuilding() != null && !request.getCanteenBuilding().isBlank())
                    ? request.getCanteenBuilding().trim()
                    : "Главный корпус";
            Integer cFloor = (request.getCanteenFloor() != null && request.getCanteenFloor() >= 1)
                    ? request.getCanteenFloor()
                    : 1;
            String cHours = (request.getCanteenOpeningHours() != null && !request.getCanteenOpeningHours().isBlank())
                    ? request.getCanteenOpeningHours().trim()
                    : "08:30 - 18:00";
            String cDesc = (request.getCanteenDescription() != null && !request.getCanteenDescription().isBlank())
                    ? request.getCanteenDescription().trim()
                    : "Столовая кампуса";

            Canteen newCanteen = Canteen.builder()
                    .name(cName)
                    .building(cBuilding)
                    .floor(cFloor)
                    .openingHours(cHours)
                    .description(cDesc)
                    .imageUrl("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=900&q=80")
                    .rating(5.0)
                    .reviewsCount(0)
                    .isOpen(true)
                    .isVerified(false)
                    .manager(saved)
                    .build();

            canteenRepository.save(newCanteen);
        }

        return userMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public UserDto authenticate(LoginRequestDto request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Неверный email или пароль"
                ));

        String hashedInput = hashPassword(request.getPassword());
        if (!user.getPasswordHash().equals(hashedInput) && !user.getPasswordHash().equals(request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Неверный email или пароль"
            );
        }

        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateProfile(Long userId, UpdateProfileRequestDto request) {
        User user = findUserEntityOrThrow(userId);
        user.setFullName(request.getFullName().trim());
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        user.setStudentGroup(request.getStudentGroup() != null ? request.getStudentGroup().trim() : null);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return userMapper.toDto(findUserEntityOrThrow(id));
    }

    private User findUserEntityOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь не найден"
                ));
    }

    public static String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}