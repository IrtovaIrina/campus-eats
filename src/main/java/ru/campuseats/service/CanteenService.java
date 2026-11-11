package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.CanteenDto;
import ru.campuseats.dto.CreateCanteenRequestDto;
import ru.campuseats.entity.*;
import ru.campuseats.mapper.CanteenMapper;
import ru.campuseats.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanteenService {

    private final CanteenRepository canteenRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final NotificationRepository notificationRepository;
    private final DishCatalogService dishCatalogService;
    private final CanteenMapper canteenMapper;

    @Transactional(readOnly = true)
    public List<CanteenDto> getAllCanteens(boolean onlyOpen) {
        List<Canteen> canteens = onlyOpen
                ? canteenRepository.findByIsOpenTrueAndIsVerifiedTrueOrderByRatingDesc()
                : canteenRepository.findAll();
        return canteenMapper.toDtoList(canteens);
    }

    @Transactional(readOnly = true)
    public List<CanteenDto> getVerifiedCanteens() {
        return canteenMapper.toDtoList(canteenRepository.findByIsVerifiedTrueOrderByRatingDesc());
    }

    @Transactional(readOnly = true)
    public CanteenDto getCanteenById(Long id) {
        return canteenMapper.toDto(findCanteenOrThrow(id));
    }

    @Transactional(readOnly = true)
    public CanteenDto getCanteenByManagerId(Long managerId) {
        Canteen canteen = canteenRepository.findFirstByManagerId(managerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "За данным аккаунтом менеджера не закреплена точка питания"
                ));
        return canteenMapper.toDto(canteen);
    }

    @Transactional
    public CanteenDto createCanteen(CreateCanteenRequestDto request) {
        Canteen canteen = canteenMapper.toEntity(request);
        canteen.setIsVerified(true);
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Менеджер с ID " + request.getManagerId() + " не найден"
                    ));
            canteen.setManager(manager);
        }
        return canteenMapper.toDto(canteenRepository.save(canteen));
    }

    @Transactional
    public CanteenDto updateCanteen(Long canteenId, CreateCanteenRequestDto request, Boolean isOpen) {
        Canteen canteen = findCanteenOrThrow(canteenId);
        if (request.getName() != null && !request.getName().isBlank()) {
            canteen.setName(request.getName().trim());
        }
        if (request.getBuilding() != null && !request.getBuilding().isBlank()) {
            canteen.setBuilding(request.getBuilding().trim());
        }
        if (request.getFloor() != null && request.getFloor() >= 1) {
            canteen.setFloor(request.getFloor());
        }
        if (request.getOpeningHours() != null && !request.getOpeningHours().isBlank()) {
            canteen.setOpeningHours(request.getOpeningHours().trim());
        }
        if (request.getDescription() != null) {
            canteen.setDescription(request.getDescription().trim());
        }
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            canteen.setImageUrl(request.getImageUrl().trim());
        }
        if (isOpen != null) {
            canteen.setIsOpen(isOpen);
        }
        return canteenMapper.toDto(canteenRepository.save(canteen));
    }

    @Transactional
    public CanteenDto verifyCanteen(Long canteenId, boolean verified) {
        Canteen canteen = findCanteenOrThrow(canteenId);
        canteen.setIsVerified(verified);
        Canteen saved = canteenRepository.save(canteen);

        if (saved.getManager() != null) {
            String title = verified
                    ? "Точка питания подтверждена ✓"
                    : "Статус точки питания изменён";
            String msg = verified
                    ? "Администратор кампуса подтвердил официальный статус точки «" + saved.getName() + "». Теперь ваше меню доступно студентам!"
                    : "Официальный статус точки «" + saved.getName() + "» приостановлен администратором.";
            notificationRepository.save(Notification.builder()
                    .user(saved.getManager())
                    .title(title)
                    .message(msg)
                    .isRead(false)
                    .build());
        }

        return canteenMapper.toDto(saved);
    }

    @Transactional
    public void deleteCanteen(Long canteenId) {
        Canteen canteen = findCanteenOrThrow(canteenId);

        List<Dish> dishes = dishRepository.findByCanteenIdOrderByCategoryDisplayOrderAscNameAsc(canteenId);
        for (Dish dish : dishes) {
            dishCatalogService.deleteDish(dish.getId());
        }

        List<FoodOrder> orders = foodOrderRepository.findByCanteenIdOrderByCreatedAtDesc(canteenId);
        for (FoodOrder order : orders) {
            paymentTransactionRepository.findByOrderId(order.getId())
                    .ifPresent(paymentTransactionRepository::delete);
            notificationRepository.findAll().stream()
                    .filter(n -> n.getOrder() != null && n.getOrder().getId().equals(order.getId()))
                    .forEach(notificationRepository::delete);
            foodOrderRepository.delete(order);
        }

        canteenRepository.delete(canteen);
    }

    public Canteen findCanteenOrThrow(Long id) {
        return canteenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Точка питания с ID " + id + " не найдена"
                ));
    }
}