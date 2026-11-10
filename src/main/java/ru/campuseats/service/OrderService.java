package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.CheckoutRequestDto;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.entity.*;
import ru.campuseats.mapper.OrderMapper;
import ru.campuseats.repository.CartItemRepository;
import ru.campuseats.repository.FoodOrderRepository;
import ru.campuseats.repository.PickupPointRepository;
import ru.campuseats.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final PickupPointRepository pickupPointRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto checkoutFromCart(CheckoutRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + request.getUserId() + " не найден"
                ));

        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByAddedAtAsc(user.getId());
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Корзина пуста. Добавьте блюда перед оформлением предзаказа"
            );
        }

        Canteen canteen = cartItems.get(0).getDish().getCanteen();

        PickupPoint pickupPoint = pickupPointRepository.findById(request.getPickupPointId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Окно выдачи с ID " + request.getPickupPointId() + " не найдено"
                ));

        if (!pickupPoint.getCanteen().getId().equals(canteen.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Выбранное окно выдачи не принадлежит точке питания «" + canteen.getName() + "»"
            );
        }

        if (!Boolean.TRUE.equals(pickupPoint.getIsActive())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Выбранное окно выдачи временно закрыто"
            );
        }

        FoodOrder order = orderMapper.toOrderEntity(
                request, user, canteen, pickupPoint, generateOrderNumber()
        );

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCalories = 0;

        for (CartItem cartItem : cartItems) {
            Dish dish = cartItem.getDish();
            int qty = cartItem.getQuantity();

            totalAmount = totalAmount.add(dish.getPrice().multiply(BigDecimal.valueOf(qty)));
            totalCalories += dish.getCalories() * qty;

            OrderItem orderItem = orderMapper.toOrderItemEntity(cartItem);
            order.addItem(orderItem);
        }

        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        order.setTotalCalories(totalCalories);

        FoodOrder savedOrder = foodOrderRepository.save(order);
        cartItemRepository.deleteByUserId(user.getId());

        return orderMapper.toResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {
        FoodOrder order = findOrderOrThrow(orderId);
        return orderMapper.toResponseDto(order);
    }

    public FoodOrder findOrderOrThrow(Long orderId) {
        return foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Заказ с ID " + orderId + " не найден"
                ));
    }

    private String generateOrderNumber() {
        long nextSeq = foodOrderRepository.count() + 101;
        return "CE-" + nextSeq;
    }
}