package org.goldenalf.privatepr.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ReviewDto;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.models.Review;
import org.goldenalf.privatepr.services.HotelService;
import org.goldenalf.privatepr.services.ReviewService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorResponse;
import org.goldenalf.privatepr.utils.erorsHandler.hotelError.HotelErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.roomError.RoomErrorException;
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
    private final HotelService hotelService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    //TODO обработать
    public ReviewDto getReview(@PathVariable("id") int id) {
        return convertToReviewDto(reviewService.getReview(id).get());
    }

    @GetMapping("/{hotelId}/allReview")
    //TODO реализовать
    public List<ReviewDto> getAllReviewInHotel(@PathVariable("hotelId") int hotelId) {
        return null;
    }

    @PostMapping("/{hotelId}/new")
    //TODO обработать
    public ResponseEntity<HttpStatus> saveReview(@RequestBody @Valid ReviewDto reviewDto,
                                                 @PathVariable("hotelId") int hotelId,
                                                 BindingResult bindingResult) {
        Hotel hotel = hotelService.getHotel(hotelId).orElseThrow(() -> new HotelErrorException("Отель не найден"));
        Review review = convertToReview(reviewDto);

        if (bindingResult.hasErrors()) {
            System.exit(1408);
        }
        reviewService.save(review);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    //TODO обработать
    public ResponseEntity<HttpStatus> updateReview(@PathVariable("id") int id,
                                                   @RequestBody @Valid ReviewDto reviewDto,
                                                   BindingResult bindingResult) {
        Review updatedReview = convertToReview(reviewDto);

        reviewService.update(id, updatedReview);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //TODO можно добавить, что если ревью(комната, отель) не найдены, то статус BAD
    public ResponseEntity<HttpStatus> deleteRoom(@PathVariable("id") int id) {
        reviewService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(HotelErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ReviewDto convertToReviewDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    private List<ReviewDto> convertToReviewDtoList(List<Review> reviews) {
        Type listType = new TypeToken<List<ReviewDto>>() {
        }.getType();
        return modelMapper.map(reviews, listType);
    }

    private Review convertToReview(ReviewDto reviewDto) {
        return modelMapper.map(reviewDto, Review.class);
    }
}
