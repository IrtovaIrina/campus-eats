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

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    @Mapping(target = "building", source = "canteen.building")
    @Mapping(target = "rating", source = "canteen.rating")
    @Mapping(target = "activeQueueCount", source = "activeQueueCount")
    @Mapping(target = "workloadPercent", source = "workloadPercent")
    @Mapping(target = "workloadLevel", source = "workloadLevel")
    @Mapping(target = "revenue", source = "revenue")
    CanteenWorkloadDto toCanteenWorkloadDto(
            Canteen canteen,
            Integer activeQueueCount,
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
    @Mapping(target = "totalRevenue", source = "totalRevenue")
    @Mapping(target = "averageOrderCheck", source = "averageOrderCheck")
    @Mapping(target = "totalDishes", source = "totalDishes")
    @Mapping(target = "canteenWorkloads", source = "canteenWorkloads")
    @Mapping(target = "topDishes", source = "topDishes")
    CampusAnalyticsDto toCampusAnalyticsDto(
            Integer totalOrders,
            Integer activeOrders,
            BigDecimal totalRevenue,
            BigDecimal averageOrderCheck,
            Long totalDishes,
            List<CanteenWorkloadDto> canteenWorkloads,
            List<PopularDishStatDto> topDishes
    );
}