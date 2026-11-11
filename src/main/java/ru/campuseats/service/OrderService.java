package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.*;
import ru.campuseats.entity.*;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.mapper.CartMapper;
import ru.campuseats.mapper.OrderMapper;
import ru.campuseats.repository.CartItemRepository;
import ru.campuseats.repository.FoodOrderRepository;
import ru.campuseats.repository.PickupPointRepository;
import ru.campuseats.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final PickupPointRepository pickupPointRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    private static final List<OrderStatus> ACTIVE_STATUSES = List.of(
            OrderStatus.ACCEPTED, OrderStatus.COOKING, OrderStatus.READY
    );

    @Transactional
    public OrderResponseDto checkoutFromCart(CheckoutRequestDto request) {
        User user = findUserOrThrow(request.getUserId());

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

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getUserOrderHistory(Long userId, boolean activeOnly) {
        findUserOrThrow(userId);
        List<FoodOrder> orders = activeOnly
                ? foodOrderRepository.findByUserIdAndStatusInOrderByCreatedAtDesc(userId, ACTIVE_STATUSES)
                : foodOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orderMapper.toResponseDtoList(orders);
    }

    @Transactional(readOnly = true)
    public OrderTrackingDto trackOrder(Long orderId) {
        FoodOrder order = findOrderOrThrow(orderId);
        OrderStatus current = order.getStatus();

        int progressPercent = switch (current) {
            case ACCEPTED -> 25;
            case COOKING -> 55;
            case READY -> 85;
            case COMPLETED -> 100;
            case CANCELLED -> 0;
        };

        String headline = switch (current) {
            case ACCEPTED -> "Заказ #" + order.getOrderNumber() + " принят столовой и ожидает начала приготовления";
            case COOKING -> "Повар готовит ваш заказ #" + order.getOrderNumber() + " к слоту " + order.getPickupTimeSlot();
            case READY -> "Заказ #" + order.getOrderNumber() + " готов! Заберите без очереди в окне «" + order.getPickupPoint().getWindowCode() + "»";
            case COMPLETED -> "Заказ #" + order.getOrderNumber() + " выдан. Вы можете повторить его в 1 клик или оставить отзыв";
            case CANCELLED -> "Заказ #" + order.getOrderNumber() + " был отменён";
        };

        int stageRank = switch (current) {
            case ACCEPTED -> 1;
            case COOKING -> 2;
            case READY -> 3;
            case COMPLETED -> 4;
            case CANCELLED -> 0;
        };

        List<TrackingStepDto> steps = List.of(
                orderMapper.toTrackingStepDto(
                        OrderStatus.ACCEPTED,
                        "Заказ зарегистрирован в очереди столовой",
                        stageRank >= 1,
                        current == OrderStatus.ACCEPTED
                ),
                orderMapper.toTrackingStepDto(
                        OrderStatus.COOKING,
                        "Повар столовой готовит и упаковывает блюда",
                        stageRank >= 2,
                        current == OrderStatus.COOKING
                ),
                orderMapper.toTrackingStepDto(
                        OrderStatus.READY,
                        "Ожидает в окне экспресс-выдачи без очереди",
                        stageRank >= 3,
                        current == OrderStatus.READY
                ),
                orderMapper.toTrackingStepDto(
                        OrderStatus.COMPLETED,
                        "Заказ получен студентом",
                        stageRank >= 4,
                        current == OrderStatus.COMPLETED
                )
        );

        return orderMapper.toTrackingDto(order, progressPercent, headline, steps);
    }

    @Transactional
    public CartSummaryDto repeatOrderToCart(Long orderId) {
        FoodOrder previousOrder = findOrderOrThrow(orderId);
        User user = previousOrder.getUser();

        // Очищаем текущую корзину студента перед наполнением блюдами из повторяемого заказа
        cartItemRepository.deleteByUserId(user.getId());
        cartItemRepository.flush();

        int copiedCount = 0;
        for (OrderItem oldItem : previousOrder.getItems()) {
            Dish dish = oldItem.getDish();
            if (dish != null && Boolean.TRUE.equals(dish.getIsAvailable())) {
                CartItem repeatedCartItem = cartMapper.fromOrderItem(oldItem, user);
                cartItemRepository.save(repeatedCartItem);
                copiedCount++;
            }
        }

        if (copiedCount == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Все блюда из этого заказа сейчас находятся в стоп-листе и недоступны для повтора"
            );
        }

        return cartService.getUserCart(user.getId());
    }

    @Transactional
    public OrderResponseDto cancelOrderByStudent(Long orderId, Long userId) {
        FoodOrder order = findOrderOrThrow(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Вы можете отменять только собственные заказы"
            );
        }

        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Заказ уже передан на кухню (статус: " + order.getStatus().getDisplayName() + ") и не может быть отменён"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderMapper.toResponseDto(foodOrderRepository.save(order));
    }

    public FoodOrder findOrderOrThrow(Long orderId) {
        return foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Заказ с ID " + orderId + " не найден"
                ));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден"
                ));
    }

    private String generateOrderNumber() {
        long nextSeq = foodOrderRepository.count() + 101;
        return "CE-" + nextSeq;
    }
}