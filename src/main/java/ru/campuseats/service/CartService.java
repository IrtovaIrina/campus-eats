package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.AddToCartRequestDto;
import ru.campuseats.dto.CartItemDto;
import ru.campuseats.dto.CartSummaryDto;
import ru.campuseats.dto.PickupPointDto;
import ru.campuseats.entity.Canteen;
import ru.campuseats.entity.CartItem;
import ru.campuseats.entity.Dish;
import ru.campuseats.entity.User;
import ru.campuseats.mapper.CartMapper;
import ru.campuseats.mapper.PickupPointMapper;
import ru.campuseats.repository.CartItemRepository;
import ru.campuseats.repository.DishRepository;
import ru.campuseats.repository.PickupPointRepository;
import ru.campuseats.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final PickupPointRepository pickupPointRepository;
    private final CartMapper cartMapper;
    private final PickupPointMapper pickupPointMapper;

    private static final List<String> DEFAULT_PICKUP_SLOTS = List.of(
            "10:15 - 10:30 (Перемена)",
            "11:50 - 12:05 (Большая перемена)",
            "12:15 - 12:30 (Большая перемена)",
            "13:45 - 14:00 (Перемена)",
            "15:25 - 15:40 (Перемена)",
            "16:00 - 16:15",
            "Как можно скорее (по готовности)"
    );

    @Transactional(readOnly = true)
    public CartSummaryDto getUserCart(Long userId) {
        findUserOrThrow(userId);
        List<CartItem> items = cartItemRepository.findByUserIdOrderByAddedAtAsc(userId);
        return buildCartSummary(userId, items);
    }

    @Transactional
    public CartSummaryDto addItemToCart(AddToCartRequestDto request) {
        User user = findUserOrThrow(request.getUserId());
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Блюдо с ID " + request.getDishId() + " не найдено"
                ));

        if (!Boolean.TRUE.equals(dish.getIsAvailable())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Блюдо «" + dish.getName() + "» временно в стоп-листе и недоступно для заказа"
            );
        }

        List<CartItem> currentItems = cartItemRepository.findByUserIdOrderByAddedAtAsc(user.getId());

        if (!currentItems.isEmpty()) {
            Canteen existingCanteen = currentItems.get(0).getDish().getCanteen();
            if (!existingCanteen.getId().equals(dish.getCanteen().getId())) {
                if (Boolean.TRUE.equals(request.getClearIfDifferentCanteen())) {
                    cartItemRepository.deleteByUserId(user.getId());
                    cartItemRepository.flush();
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "В корзине уже есть блюда из точки «" + existingCanteen.getName()
                                    + "». Оформите текущий заказ или очистите корзину перед добавлением блюд из «"
                                    + dish.getCanteen().getName() + "»."
                    );
                }
            }
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndDishId(user.getId(), dish.getId());

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            if (request.getSpecialRequest() != null && !request.getSpecialRequest().isBlank()) {
                existingItem.setSpecialRequest(request.getSpecialRequest().trim());
            }
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .user(user)
                    .dish(dish)
                    .quantity(request.getQuantity())
                    .specialRequest(request.getSpecialRequest() != null ? request.getSpecialRequest().trim() : null)
                    .addedAt(LocalDateTime.now())
                    .build();
            cartItemRepository.save(newItem);
        }

        List<CartItem> updatedItems = cartItemRepository.findByUserIdOrderByAddedAtAsc(user.getId());
        return buildCartSummary(user.getId(), updatedItems);
    }

    @Transactional
    public CartSummaryDto updateItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Позиция корзины с ID " + cartItemId + " не найдена"
                ));

        Long userId = cartItem.getUser().getId();

        if (quantity == null || quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        List<CartItem> updatedItems = cartItemRepository.findByUserIdOrderByAddedAtAsc(userId);
        return buildCartSummary(userId, updatedItems);
    }

    @Transactional
    public CartSummaryDto removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Позиция корзины с ID " + cartItemId + " не найдена"
                ));
        Long userId = cartItem.getUser().getId();
        cartItemRepository.delete(cartItem);

        List<CartItem> updatedItems = cartItemRepository.findByUserIdOrderByAddedAtAsc(userId);
        return buildCartSummary(userId, updatedItems);
    }

    @Transactional
    public CartSummaryDto clearCart(Long userId) {
        findUserOrThrow(userId);
        cartItemRepository.deleteByUserId(userId);
        return buildCartSummary(userId, Collections.emptyList());
    }

    private CartSummaryDto buildCartSummary(Long userId, List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return CartSummaryDto.builder()
                    .userId(userId)
                    .items(Collections.emptyList())
                    .totalQuantity(0)
                    .totalAmount(BigDecimal.ZERO)
                    .totalCalories(0)
                    .totalProteins(0.0)
                    .totalFats(0.0)
                    .totalCarbs(0.0)
                    .estimatedPrepMinutes(0)
                    .availablePickupPoints(Collections.emptyList())
                    .availableTimeSlots(DEFAULT_PICKUP_SLOTS)
                    .build();
        }

        Canteen canteen = items.get(0).getDish().getCanteen();
        List<CartItemDto> itemDtos = cartMapper.toItemDtoList(items);

        int totalQty = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCalories = 0;
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbs = 0.0;
        int maxPrepTime = 5;

        for (CartItem item : items) {
            int qty = item.getQuantity();
            Dish d = item.getDish();

            totalQty += qty;
            totalAmount = totalAmount.add(d.getPrice().multiply(BigDecimal.valueOf(qty)));
            totalCalories += d.getCalories() * qty;
            totalProteins += d.getProteins() * qty;
            totalFats += d.getFats() * qty;
            totalCarbs += d.getCarbs() * qty;

            if (d.getPrepTimeMinutes() != null && d.getPrepTimeMinutes() > maxPrepTime) {
                maxPrepTime = d.getPrepTimeMinutes();
            }
        }

        List<PickupPointDto> pickupPoints = pickupPointMapper.toDtoList(
                pickupPointRepository.findByCanteenIdAndIsActiveTrueOrderByIdAsc(canteen.getId())
        );

        return CartSummaryDto.builder()
                .userId(userId)
                .canteenId(canteen.getId())
                .canteenName(canteen.getName())
                .canteenBuilding(canteen.getBuilding())
                .items(itemDtos)
                .totalQuantity(totalQty)
                .totalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP))
                .totalCalories(totalCalories)
                .totalProteins(roundMacro(totalProteins))
                .totalFats(roundMacro(totalFats))
                .totalCarbs(roundMacro(totalCarbs))
                .estimatedPrepMinutes(maxPrepTime)
                .availablePickupPoints(pickupPoints)
                .availableTimeSlots(DEFAULT_PICKUP_SLOTS)
                .build();
    }

    private double roundMacro(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден"
                ));
    }
}