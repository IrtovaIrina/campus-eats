package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.campuseats.dto.CampusAnalyticsDto;
import ru.campuseats.dto.CanteenWorkloadDto;
import ru.campuseats.dto.PopularDishStatDto;
import ru.campuseats.entity.*;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.mapper.AnalyticsMapper;
import ru.campuseats.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final FoodOrderRepository foodOrderRepository;
    private final CanteenRepository canteenRepository;
    private final PickupPointRepository pickupPointRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final AnalyticsMapper analyticsMapper;

    @Transactional(readOnly = true)
    public CampusAnalyticsDto getCampusDashboard(Long canteenIdFilter) {
        List<FoodOrder> allOrders = (canteenIdFilter != null)
                ? foodOrderRepository.findByCanteenIdOrderByCreatedAtDesc(canteenIdFilter)
                : foodOrderRepository.findAll();

        int totalOrders = allOrders.size();
        int activeOrders = (int) allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.ACCEPTED
                        || o.getStatus() == OrderStatus.COOKING
                        || o.getStatus() == OrderStatus.READY)
                .count();
        int completedOrders = (int) allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .count();

        List<FoodOrder> validOrders = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .toList();

        BigDecimal totalRevenue = validOrders.stream()
                .map(FoodOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal averageCheck = validOrders.isEmpty()
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(validOrders.size()), 2, RoundingMode.HALF_UP);

        Map<String, Long> ordersByStatus = new LinkedHashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            long count = allOrders.stream().filter(o -> o.getStatus() == status).count();
            ordersByStatus.put(status.name(), count);
        }

        Map<String, Long> ordersByTimeSlot = allOrders.stream()
                .collect(Collectors.groupingBy(
                        FoodOrder::getPickupTimeSlot,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        List<Canteen> canteens = (canteenIdFilter != null)
                ? canteenRepository.findById(canteenIdFilter).map(List::of).orElse(Collections.emptyList())
                : canteenRepository.findAll();

        List<CanteenWorkloadDto> workloads = canteens.stream()
                .map(this::buildCanteenWorkload)
                .toList();

        List<PopularDishStatDto> topDishes = calculateTopDishes(validOrders, 5);

        return analyticsMapper.toCampusAnalyticsDto(
                totalOrders,
                activeOrders,
                completedOrders,
                totalRevenue,
                averageCheck,
                userRepository.count(),
                dishRepository.count(),
                ordersByStatus,
                ordersByTimeSlot,
                workloads,
                topDishes
        );
    }

    private CanteenWorkloadDto buildCanteenWorkload(Canteen canteen) {
        List<FoodOrder> canteenOrders = foodOrderRepository.findByCanteenIdOrderByCreatedAtDesc(canteen.getId());
        int totalCount = canteenOrders.size();

        int activeQueue = (int) canteenOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.ACCEPTED
                        || o.getStatus() == OrderStatus.COOKING
                        || o.getStatus() == OrderStatus.READY)
                .count();

        int slotCapacity = pickupPointRepository.findByCanteenIdAndIsActiveTrueOrderByIdAsc(canteen.getId())
                .stream()
                .mapToInt(PickupPoint::getMaxOrdersPerSlot)
                .sum();
        if (slotCapacity <= 0) {
            slotCapacity = 15;
        }

        // Для наглядной индикации нагрузки рассчитываем процент от ёмкости слота
        int workloadPercent = Math.min(100, Math.round((activeQueue * 100.0f) / Math.max(5, slotCapacity / 3)));

        String workloadLevel;
        if (workloadPercent >= 75) {
            workloadLevel = "HIGH_PEAK";
        } else if (workloadPercent >= 35) {
            workloadLevel = "MODERATE";
        } else {
            workloadLevel = "LOW";
        }

        BigDecimal revenue = canteenOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .map(FoodOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return analyticsMapper.toCanteenWorkloadDto(
                canteen,
                totalCount,
                activeQueue,
                slotCapacity,
                workloadPercent,
                workloadLevel,
                revenue
        );
    }

    private List<PopularDishStatDto> calculateTopDishes(List<FoodOrder> validOrders, int limit) {
        Map<Long, Dish> dishById = new HashMap<>();
        Map<Long, Integer> portionsByDish = new HashMap<>();
        Map<Long, BigDecimal> revenueByDish = new HashMap<>();

        for (FoodOrder order : validOrders) {
            for (OrderItem item : order.getItems()) {
                Dish dish = item.getDish();
                if (dish == null) continue;
                Long dishId = dish.getId();
                dishById.put(dishId, dish);
                portionsByDish.merge(dishId, item.getQuantity(), Integer::sum);
                BigDecimal itemRevenue = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                revenueByDish.merge(dishId, itemRevenue, BigDecimal::add);
            }
        }

        return portionsByDish.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Long dishId = entry.getKey();
                    return analyticsMapper.toPopularDishStatDto(
                            dishById.get(dishId),
                            entry.getValue(),
                            revenueByDish.getOrDefault(dishId, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)
                    );
                })
                .toList();
    }
}