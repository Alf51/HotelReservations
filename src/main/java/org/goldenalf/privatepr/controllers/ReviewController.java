package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ReviewDto;
import org.goldenalf.privatepr.models.Review;
import org.goldenalf.privatepr.services.ReviewService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.ReviewErrorException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;

    @GetMapping("/{id_review}")
    public ReviewDto getReview(@PathVariable("id_review") int id) {
        return convertToReviewDto(reviewService.getReview(id).orElseThrow(() -> new ReviewErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.review.exception.review-not-found"))));
    }

    @GetMapping("/{id_hotel}/allHotelReviews")
    public List<ReviewDto> getAllHotelReviews(@PathVariable("id_hotel") int hotelId) {
        return convertToReviewDtoList(reviewService.findAllByHotelId(hotelId));
    }

    @GetMapping("/{id_client}/allClientReviews")
    public List<ReviewDto> getAllReviewByClient(@PathVariable("id_client") int clientId) {
        return convertToReviewDtoList(reviewService.findAllByClientId(clientId));
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveReview(@RequestBody @Valid ReviewDto reviewDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ReviewErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        Review review = reviewService.getValidReview(reviewDto);
        reviewService.save(review);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id_review}")
    public ResponseEntity<HttpStatus> updateReview(@PathVariable("id_review") int id,
                                                   @RequestBody @Valid ReviewDto reviewDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ReviewErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        Review updatedReview = reviewService.getValidReview(reviewDto);
        reviewService.update(id, updatedReview);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id_review}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable("id_review") int id) {
        reviewService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private ReviewDto convertToReviewDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    private List<ReviewDto> convertToReviewDtoList(List<Review> reviews) {
        Type listType = new TypeToken<List<ReviewDto>>() {
        }.getType();
        return modelMapper.map(reviews, listType);
    }
}
