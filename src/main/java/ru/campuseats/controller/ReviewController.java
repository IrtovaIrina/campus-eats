package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.CreateReviewRequestDto;
import ru.campuseats.dto.ReviewDto;
import ru.campuseats.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "10. Отзывы и Рейтинги блюд", description = "Оценка блюд студентами, пересчёт рейтинга столовой и ответы менеджеров")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/dish/{dishId}")
    @Operation(summary = "Получить все отзывы по конкретному блюду")
    public List<ReviewDto> getReviewsForDish(@PathVariable Long dishId) {
        return reviewService.getReviewsForDish(dishId);
    }

    @GetMapping("/canteen/{canteenId}")
    @Operation(summary = "Получить все отзывы о блюдах выбранной точки питания")
    public List<ReviewDto> getReviewsForCanteen(@PathVariable Long canteenId) {
        return reviewService.getReviewsForCanteen(canteenId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Оставить отзыв и оценку блюду (1..5 звёзд)",
            description = "Автоматически пересчитывает средний рейтинг блюда и общий рейтинг точки питания кампуса"
    )
    public ReviewDto createReview(@Valid @RequestBody CreateReviewRequestDto request) {
        return reviewService.createReview(request);
    }

    @PatchMapping("/{reviewId}/reply")
    @Operation(summary = "Добавить ответ менеджера точки питания на отзыв студента (для роли VENDOR)")
    public ReviewDto replyToReview(
            @PathVariable Long reviewId,
            @Parameter(description = "Текст ответа от лица столовой", required = true)
            @RequestParam String replyText
    ) {
        return reviewService.addVendorReply(reviewId, replyText);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить отзыв (для роли ADMIN — модерация контента)")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}