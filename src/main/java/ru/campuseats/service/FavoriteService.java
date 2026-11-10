package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.FavoriteDishDto;
import ru.campuseats.dto.FavoriteToggleResponseDto;
import ru.campuseats.entity.Dish;
import ru.campuseats.entity.FavoriteDish;
import ru.campuseats.entity.User;
import ru.campuseats.mapper.FavoriteDishMapper;
import ru.campuseats.repository.DishRepository;
import ru.campuseats.repository.FavoriteDishRepository;
import ru.campuseats.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteDishRepository favoriteDishRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final FavoriteDishMapper favoriteDishMapper;

    @Transactional(readOnly = true)
    public List<FavoriteDishDto> getUserFavorites(Long userId) {
        findUserOrThrow(userId);
        List<FavoriteDish> favorites = favoriteDishRepository.findByUserIdOrderByAddedAtDesc(userId);
        return favoriteDishMapper.toDtoList(favorites);
    }

    @Transactional
    public FavoriteToggleResponseDto toggleFavorite(Long userId, Long dishId) {
        User user = findUserOrThrow(userId);
        Dish dish = findDishOrThrow(dishId);

        Optional<FavoriteDish> existing = favoriteDishRepository.findByUserIdAndDishId(userId, dishId);

        if (existing.isPresent()) {
            favoriteDishRepository.delete(existing.get());
            String msg = "Блюдо «" + dish.getName() + "» удалено из избранного";
            return favoriteDishMapper.toToggleResponseDto(user, dish, false, msg);
        }

        FavoriteDish favoriteDish = favoriteDishMapper.toEntity(user, dish);
        favoriteDishRepository.save(favoriteDish);

        String msg = "Блюдо «" + dish.getName() + "» добавлено в избранное";
        return favoriteDishMapper.toToggleResponseDto(user, dish, true, msg);
    }

    @Transactional
    public void removeFavorite(Long userId, Long dishId) {
        FavoriteDish favorite = favoriteDishRepository.findByUserIdAndDishId(userId, dishId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Блюдо не найдено в списке избранного пользователя"
                ));
        favoriteDishRepository.delete(favorite);
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден"
                ));
    }

    private Dish findDishOrThrow(Long dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Блюдо с ID " + dishId + " не найдено"
                ));
    }
}