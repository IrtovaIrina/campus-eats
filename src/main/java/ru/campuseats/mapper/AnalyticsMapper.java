package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.CampusAnalyticsDto;
import ru.campuseats.dto.CanteenWorkloadDto;
import ru.campuseats.dto.PopularDishStatDto;
import ru.campuseats.entity.Canteen;
import ru.campuseats.entity.Dish;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    @Mapping(target = "building", source = "canteen.building")
    @Mapping(target = "rating", source = "canteen.rating")
    @Mapping(target = "totalOrdersCount", source = "totalOrdersCount")
    @Mapping(target = "activeQueueCount", source = "activeQueueCount")
    @Mapping(target = "slotCapacity", source = "slotCapacity")
    @Mapping(target = "workloadPercent", source = "workloadPercent")
    @Mapping(target = "workloadLevel", source = "workloadLevel")
    @Mapping(target = "revenue", source = "revenue")
    CanteenWorkloadDto toCanteenWorkloadDto(
            Canteen canteen,
            Integer totalOrdersCount,
            Integer activeQueueCount,
            Integer slotCapacity,
            Integer workloadPercent,
            String workloadLevel,
            BigDecimal revenue
    );

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "canteenName", source = "dish.canteen.name")
    @Mapping(target = "categoryName", source = "dish.category.name")
    @Mapping(target = "rating", source = "dish.rating")
    @Mapping(target = "totalPortionsSold", source = "totalPortionsSold")
    @Mapping(target = "totalRevenue", source = "totalRevenue")
    PopularDishStatDto toPopularDishStatDto(
            Dish dish,
            Integer totalPortionsSold,
            BigDecimal totalRevenue
    );

    @Mapping(target = "totalOrders", source = "totalOrders")
    @Mapping(target = "activeOrders", source = "activeOrders")
    @Mapping(target = "completedOrders", source = "completedOrders")
    @Mapping(target = "totalRevenue", source = "totalRevenue")
    @Mapping(target = "averageOrderCheck", source = "averageOrderCheck")
    @Mapping(target = "totalUsers", source = "totalUsers")
    @Mapping(target = "totalDishes", source = "totalDishes")
    @Mapping(target = "ordersByStatus", source = "ordersByStatus")
    @Mapping(target = "ordersByTimeSlot", source = "ordersByTimeSlot")
    @Mapping(target = "canteenWorkloads", source = "canteenWorkloads")
    @Mapping(target = "topDishes", source = "topDishes")
    CampusAnalyticsDto toCampusAnalyticsDto(
            Integer totalOrders,
            Integer activeOrders,
            Integer completedOrders,
            BigDecimal totalRevenue,
            BigDecimal averageOrderCheck,
            Long totalUsers,
            Long totalDishes,
            Map<String, Long> ordersByStatus,
            Map<String, Long> ordersByTimeSlot,
            List<CanteenWorkloadDto> canteenWorkloads,
            List<PopularDishStatDto> topDishes
    );
}