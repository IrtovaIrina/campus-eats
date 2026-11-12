package ru.campuseats.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.campuseats.dto.*;
import ru.campuseats.enums.Role;
import ru.campuseats.service.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebPageController {

    private static final String SESSION_USER_ID = "loggedInUserId";

    private final UserService userService;
    private final CanteenService canteenService;
    private final DishCatalogService dishCatalogService;
    private final FavoriteService favoriteService;
    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CanteenOrderService canteenOrderService;
    private final NotificationService notificationService;
    private final ReviewService reviewService;
    private final AnalyticsService analyticsService;

    @GetMapping({"", "/", "/ui"})
    public String showMainPage(
            @RequestParam(defaultValue = "default") String tab,
            @RequestParam(defaultValue = "login") String authMode,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long canteenId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String dietaryTagCode,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer maxCalories,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(required = false) Long selectedDishId,
            @RequestParam(required = false) Long editingDishId,
            @RequestParam(required = false) Long editingCanteenId,
            @RequestParam(required = false) Long trackingOrderId,
            HttpSession session,
            Model model
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);

        if (userId == null) {
            model.addAttribute("currentUser", null);
            model.addAttribute("authMode", authMode);
            return "index";
        }

        UserDto currentUser;
        try {
            currentUser = userService.getUserById(userId);
        } catch (Exception e) {
            session.invalidate();
            return "redirect:/";
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("categories", dishCatalogService.getAllCategories());
        model.addAttribute("dietaryTags", dishCatalogService.getAllDietaryTags());

        if (currentUser.getRole() != Role.ADMIN) {
            model.addAttribute("notifications", notificationService.getUserNotifications(userId));
            model.addAttribute("unreadCount", notificationService.getUnreadCount(userId));
        }

        if (currentUser.getRole() == Role.STUDENT) {
            model.addAttribute("canteens", canteenService.getVerifiedCanteens());
            String studentTab = "default".equals(tab) ? "catalog" : tab;
            model.addAttribute("activeTab", studentTab);
            model.addAttribute("cart", cartService.getUserCart(userId));

            if ("catalog".equals(studentTab)) {
                model.addAttribute("search", search);
                model.addAttribute("canteenId", canteenId);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("dietaryTagCode", dietaryTagCode);
                model.addAttribute("maxPrice", maxPrice);
                model.addAttribute("maxCalories", maxCalories);
                model.addAttribute("sortBy", sortBy);

                model.addAttribute("dishes", dishCatalogService.searchAndFilterDishes(
                        search, canteenId, categoryId, dietaryTagCode, maxPrice, maxCalories, true, sortBy
                ));
            }

            if ("dish".equals(studentTab) && selectedDishId != null) {
                DishDetailDto detail = dishCatalogService.getDishDetails(selectedDishId);
                if (!Boolean.TRUE.equals(detail.getIsAvailable())) {
                    return "redirect:/?tab=catalog";
                }
                model.addAttribute("selectedDish", detail);
                model.addAttribute("dishReviews", reviewService.getReviewsForDish(selectedDishId));
            }

            if ("favorites".equals(studentTab)) {
                model.addAttribute("favorites", favoriteService.getUserFavorites(userId));
            }

            if ("orders".equals(studentTab)) {
                List<OrderResponseDto> orders = orderService.getUserOrderHistory(userId, false);
                model.addAttribute("studentOrders", orders);

                Long orderToTrack = (trackingOrderId != null)
                        ? trackingOrderId
                        : (orders.isEmpty() ? null : orders.get(0).getId());

                if (orderToTrack != null) {
                    model.addAttribute("tracking", orderService.trackOrder(orderToTrack));
                }
            }
        }

        if (currentUser.getRole() == Role.CANTEEN) {
            String canteenTab = "default".equals(tab) ? "canteen_orders" : tab;
            model.addAttribute("activeTab", canteenTab);

            CanteenDto myCanteen = canteenService.getCanteenByManagerId(userId);
            Long myCanteenId = myCanteen.getId();
            model.addAttribute("managedCanteen", myCanteen);
            model.addAttribute("managedCanteenId", myCanteenId);

            if ("canteen_orders".equals(canteenTab)) {
                model.addAttribute("canteenOrders", canteenOrderService.getCanteenQueue(myCanteenId, null));
            }
            if ("canteen_menu".equals(canteenTab)) {
                model.addAttribute("canteenDishes", dishCatalogService.searchAndFilterDishes(
                        null, myCanteenId, null, null, null, null, false, "rating"
                ));
                if (editingDishId != null) {
                    model.addAttribute("editingDish", dishCatalogService.getDishDetails(editingDishId));
                }
            }
            if ("canteen_stats".equals(canteenTab)) {
                model.addAttribute("canteenAnalytics", analyticsService.getCampusDashboard(myCanteenId));
            }
            if ("canteen_reviews".equals(canteenTab)) {
                model.addAttribute("canteenReviews", reviewService.getReviewsForCanteen(myCanteenId));
            }
        }

        if (currentUser.getRole() == Role.ADMIN) {
            String adminTab = "default".equals(tab) ? "admin_dashboard" : tab;
            model.addAttribute("activeTab", adminTab);

            if ("admin_dashboard".equals(adminTab)) {
                model.addAttribute("canteens", canteenService.getAllCanteens(false));
                model.addAttribute("analytics", analyticsService.getCampusDashboard(null));
                if (editingCanteenId != null) {
                    model.addAttribute("editingCanteen", canteenService.getCanteenById(editingCanteenId));
                }
            }
        }

        return "index";
    }

    @PostMapping("/ui/auth/login")
    public String login(
            @ModelAttribute LoginRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UserDto user = userService.authenticate(request);
            session.setAttribute(SESSION_USER_ID, user.getId());
            redirectAttributes.addFlashAttribute("message", "Добро пожаловать, " + user.getFullName() + "!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка входа: " + e.getMessage());
            return "redirect:/?authMode=login";
        }
    }

    @PostMapping("/ui/auth/register")
    public String register(
            @ModelAttribute RegisterUserRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UserDto created = userService.register(request);
            session.setAttribute(SESSION_USER_ID, created.getId());
            if (created.getRole() == Role.CANTEEN) {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "Аккаунт точки питания создан и отправлен на подтверждение администратору."
                );
            } else {
                redirectAttributes.addFlashAttribute("message", "Регистрация успешно завершена!");
            }
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка регистрации: " + e.getMessage());
            return "redirect:/?authMode=register";
        }
    }

    @PostMapping("/ui/profile/update")
    public String updateProfile(
            @ModelAttribute UpdateProfileRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        userService.updateProfile(userId, request);
        redirectAttributes.addFlashAttribute("message", "Данные профиля сохранены.");
        return "redirect:/?tab=profile";
    }

    @PostMapping("/ui/auth/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Вы вышли из аккаунта.");
        return "redirect:/";
    }

    @PostMapping("/ui/favorites/toggle")
    public String toggleFavorite(
            @RequestParam Long dishId,
            @RequestParam(defaultValue = "catalog") String returnTab,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        FavoriteToggleResponseDto result = favoriteService.toggleFavorite(userId, dishId);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        return "redirect:/?tab=" + returnTab;
    }

    @PostMapping("/ui/cart/add")
    public String addToCart(
            @ModelAttribute AddToCartRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        try {
            request.setUserId(userId);
            if (request.getQuantity() == null) {
                request.setQuantity(1);
            }
            request.setClearIfDifferentCanteen(true);
            cartService.addItemToCart(request);
            redirectAttributes.addFlashAttribute("message", "Блюдо добавлено в корзину.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/?tab=cart";
    }

    @PostMapping("/ui/cart/update")
    public String updateCartQuantity(
            @RequestParam Long cartItemId,
            @RequestParam Integer quantity,
            HttpSession session
    ) {
        if (session.getAttribute(SESSION_USER_ID) == null) return "redirect:/";
        cartService.updateItemQuantity(cartItemId, quantity);
        return "redirect:/?tab=cart";
    }

    @PostMapping("/ui/cart/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        cartService.clearCart(userId);
        redirectAttributes.addFlashAttribute("message", "Корзина очищена.");
        return "redirect:/?tab=cart";
    }

    @PostMapping("/ui/orders/checkout")
    public String checkoutOrder(
            @ModelAttribute CheckoutRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        try {
            request.setUserId(userId);
            OrderResponseDto created = orderService.checkoutFromCart(request);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Заказ #" + created.getOrderNumber() + " оформлен. Выберите способ оплаты ниже."
            );
            return "redirect:/?tab=orders&trackingOrderId=" + created.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка оформления: " + e.getMessage());
            return "redirect:/?tab=cart";
        }
    }

    @PostMapping("/ui/payments/pay")
    public String payForOrder(
            @ModelAttribute PaymentRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute(SESSION_USER_ID) == null) return "redirect:/";

        try {
            PaymentReceiptDto receipt = paymentService.processDemoPayment(request);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Оплата прошла успешно: заказ #" + receipt.getOrderNumber()
                            + " (" + receipt.getPaymentMethodDisplayName()
                            + "), транзакция " + receipt.getTransactionReference()
                            + ", сумма " + receipt.getAmountPaid() + " ₽."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка оплаты: " + e.getMessage());
        }
        return "redirect:/?tab=orders&trackingOrderId=" + request.getOrderId();
    }

    @PostMapping("/ui/orders/repeat")
    public String repeatOrder(
            @RequestParam Long orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute(SESSION_USER_ID) == null) return "redirect:/";
        try {
            orderService.repeatOrderToCart(orderId);
            redirectAttributes.addFlashAttribute("message", "Доступные блюда из заказа скопированы в корзину.");
            return "redirect:/?tab=cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка повтора: " + e.getMessage());
            return "redirect:/?tab=orders";
        }
    }

    @PostMapping("/ui/orders/cancel")
    public String cancelOrder(
            @RequestParam Long orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        try {
            orderService.cancelOrderByStudent(orderId, userId);
            redirectAttributes.addFlashAttribute("message", "Заказ отменён.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/?tab=orders";
    }

    @PostMapping("/ui/reviews/add")
    public String addReview(
            @ModelAttribute CreateReviewRequestDto request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        try {
            request.setUserId(userId);
            reviewService.createReview(request);
            redirectAttributes.addFlashAttribute("message", "Отзыв добавлен.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/?tab=dish&selectedDishId=" + request.getDishId();
    }

    @PostMapping("/ui/notifications/read")
    public String markNotificationRead(@RequestParam Long notificationId, HttpSession session) {
        if (session.getAttribute(SESSION_USER_ID) == null) return "redirect:/";
        notificationService.markAsRead(notificationId);
        return "redirect:/?tab=notifications";
    }

    @PostMapping("/ui/notifications/delete")
    public String deleteNotification(
            @RequestParam Long notificationId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute(SESSION_USER_ID) == null) return "redirect:/";
        notificationService.deleteNotification(notificationId);
        redirectAttributes.addFlashAttribute("message", "Уведомление удалено.");
        return "redirect:/?tab=notifications";
    }

    @PostMapping("/ui/canteen/canteen-update")
    public String canteenUpdateProfile(
            @ModelAttribute CreateCanteenRequestDto request,
            @RequestParam(defaultValue = "false") boolean isOpen,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        CanteenDto myCanteen = canteenService.getCanteenByManagerId(userId);
        canteenService.updateCanteen(myCanteen.getId(), request, isOpen);
        redirectAttributes.addFlashAttribute("message", "Данные точки питания обновлены.");
        return "redirect:/?tab=profile";
    }

    @PostMapping("/ui/canteen/status")
    public String canteenUpdateOrderStatus(
            @RequestParam Long orderId,
            @ModelAttribute UpdateOrderStatusRequestDto request,
            RedirectAttributes redirectAttributes
    ) {
        canteenOrderService.updateOrderStatus(orderId, request);
        redirectAttributes.addFlashAttribute(
                "message",
                "Статус заказа изменён на «" + request.getStatus().getDisplayName() + "»."
        );
        return "redirect:/?tab=canteen_orders";
    }

    @PostMapping("/ui/canteen/dish-availability")
    public String canteenToggleAvailability(
            @RequestParam Long dishId,
            @RequestParam boolean available,
            RedirectAttributes redirectAttributes
    ) {
        dishCatalogService.updateDishAvailability(dishId, available);
        redirectAttributes.addFlashAttribute("message", "Статус блюда обновлён.");
        return "redirect:/?tab=canteen_menu";
    }

    @PostMapping("/ui/canteen/dish-create")
    public String canteenCreateDish(
            @ModelAttribute CreateDishRequestDto request,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        try {
            CanteenDto myCanteen = canteenService.getCanteenByManagerId(userId);
            request.setCanteenId(myCanteen.getId());
            dishCatalogService.createDish(request, imageFile);
            redirectAttributes.addFlashAttribute("message", "Блюдо «" + request.getName() + "» добавлено в меню.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/?tab=canteen_menu";
    }

    @PostMapping("/ui/canteen/dish-update")
    public String canteenUpdateDish(
            @RequestParam Long dishId,
            @ModelAttribute CreateDishRequestDto request,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) return "redirect:/";

        try {
            CanteenDto myCanteen = canteenService.getCanteenByManagerId(userId);
            request.setCanteenId(myCanteen.getId());
            dishCatalogService.updateDish(dishId, request, imageFile);
            redirectAttributes.addFlashAttribute("message", "Блюдо «" + request.getName() + "» обновлено.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/?tab=canteen_menu";
    }

    @PostMapping("/ui/canteen/dish-delete")
    public String canteenDeleteDish(
            @RequestParam Long dishId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            dishCatalogService.deleteDish(dishId);
            redirectAttributes.addFlashAttribute("message", "Блюдо удалено.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }
        return "redirect:/?tab=canteen_menu";
    }

    @PostMapping("/ui/canteen/review-reply")
    public String canteenReplyReview(
            @RequestParam Long reviewId,
            @RequestParam String replyText,
            RedirectAttributes redirectAttributes
    ) {
        reviewService.addCanteenReply(reviewId, replyText);
        redirectAttributes.addFlashAttribute("message", "Ответ сохранён.");
        return "redirect:/?tab=canteen_reviews";
    }

    @PostMapping("/ui/canteen/review-delete")
    public String canteenDeleteReview(
            @RequestParam Long reviewId,
            RedirectAttributes redirectAttributes
    ) {
        reviewService.deleteReview(reviewId);
        redirectAttributes.addFlashAttribute("message", "Отзыв удалён.");
        return "redirect:/?tab=canteen_reviews";
    }

    @PostMapping("/ui/admin/canteen/update")
    public String adminUpdateCanteen(
            @RequestParam Long canteenId,
            @ModelAttribute CreateCanteenRequestDto request,
            @RequestParam(defaultValue = "false") boolean isOpen,
            RedirectAttributes redirectAttributes
    ) {
        canteenService.updateCanteen(canteenId, request, isOpen);
        redirectAttributes.addFlashAttribute("message", "Данные точки питания обновлены.");
        return "redirect:/?tab=admin_dashboard";
    }

    @PostMapping("/ui/admin/canteen/verify")
    public String adminVerifyCanteen(
            @RequestParam Long canteenId,
            @RequestParam boolean verified,
            RedirectAttributes redirectAttributes
    ) {
        CanteenDto dto = canteenService.verifyCanteen(canteenId, verified);
        redirectAttributes.addFlashAttribute(
                "message",
                verified
                        ? "Точка питания «" + dto.getName() + "» подтверждена."
                        : "Подтверждение точки «" + dto.getName() + "» отозвано."
        );
        return "redirect:/?tab=admin_dashboard";
    }

    @PostMapping("/ui/admin/canteen/delete")
    public String adminDeleteCanteen(
            @RequestParam Long canteenId,
            RedirectAttributes redirectAttributes
    ) {
        canteenService.deleteCanteen(canteenId);
        redirectAttributes.addFlashAttribute("message", "Точка питания удалена.");
        return "redirect:/?tab=admin_dashboard";
    }
}