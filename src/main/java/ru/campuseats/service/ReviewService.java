package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.CreateReviewRequestDto;
import ru.campuseats.dto.ReviewDto;
import ru.campuseats.entity.*;
import ru.campuseats.mapper.ReviewMapper;
import ru.campuseats.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final CanteenRepository canteenRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsForDish(Long dishId) {
        return reviewMapper.toDtoList(reviewRepository.findByDishIdOrderByCreatedAtDesc(dishId));
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsForCanteen(Long canteenId) {
        return reviewMapper.toDtoList(reviewRepository.findByDishCanteenIdOrderByCreatedAtDesc(canteenId));
    }

    @Transactional
    public ReviewDto createReview(CreateReviewRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь с ID " + request.getUserId() + " не найден"
                ));

        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Блюдо с ID " + request.getDishId() + " не найдено"
                ));

        FoodOrder order = null;
        if (request.getOrderId() != null) {
            order = foodOrderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Заказ с ID " + request.getOrderId() + " не найден"
                    ));
            if (reviewRepository.existsByUserIdAndDishIdAndOrderId(user.getId(), dish.getId(), order.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Вы уже оставили отзыв на это блюдо в рамках данного заказа"
                );
            }
        }

        Review review = reviewMapper.toEntity(request, user, dish, order);
        Review saved = reviewRepository.save(review);

        recalculateDishAndCanteenRatings(dish);

        return reviewMapper.toDto(saved);
    }

    @Transactional
    public ReviewDto addCanteenReply(Long reviewId, String replyText) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Отзыв с ID " + reviewId + " не найден"
                ));

        if (replyText == null || replyText.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Текст ответа не может быть пустым");
        }

        review.setCanteenReply(replyText.trim());
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Отзыв с ID " + reviewId + " не найден"
                ));
        Dish dish = review.getDish();
        reviewRepository.delete(review);
        reviewRepository.flush();
        recalculateDishAndCanteenRatings(dish);
    }

    private void recalculateDishAndCanteenRatings(Dish dish) {
        List<Review> dishReviews = reviewRepository.findByDishIdOrderByCreatedAtDesc(dish.getId());
        if (dishReviews.isEmpty()) {
            dish.setRating(5.0);
        } else {
            double avgDish = dishReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(5.0);
            dish.setRating(roundRating(avgDish));
        }
        dishRepository.save(dish);

        Canteen canteen = dish.getCanteen();
        List<Review> canteenReviews = reviewRepository.findByDishCanteenIdOrderByCreatedAtDesc(canteen.getId());
        if (canteenReviews.isEmpty()) {
            canteen.setRating(5.0);
            canteen.setReviewsCount(0);
        } else {
            double avgCanteen = canteenReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(5.0);
            canteen.setRating(roundRating(avgCanteen));
            canteen.setReviewsCount(canteenReviews.size());
        }
        canteenRepository.save(canteen);
    }

    private double roundRating(double val) {
        return BigDecimal.valueOf(val).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}