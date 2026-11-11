package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Отзывы и оценки", description = "Отзывы на блюда, пересчёт рейтинга и ответы точек питания")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/dish/{dishId}")
    @Operation(summary = "Получить отзывы по блюду")
    public List<ReviewDto> getReviewsForDish(@PathVariable Long dishId) {
        return reviewService.getReviewsForDish(dishId);
    }

    @GetMapping("/canteen/{canteenId}")
    @Operation(summary = "Получить отзывы по точке питания")
    public List<ReviewDto> getReviewsForCanteen(@PathVariable Long canteenId) {
        return reviewService.getReviewsForCanteen(canteenId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить отзыв и оценку блюду")
    public ReviewDto createReview(@Valid @RequestBody CreateReviewRequestDto request) {
        return reviewService.createReview(request);
    }

    @PatchMapping("/{reviewId}/reply")
    @Operation(summary = "Ответить на отзыв от имени точки питания")
    public ReviewDto replyToReview(
            @PathVariable Long reviewId,
            @RequestParam String replyText
    ) {
        return reviewService.addVendorReply(reviewId, replyText);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить отзыв")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}