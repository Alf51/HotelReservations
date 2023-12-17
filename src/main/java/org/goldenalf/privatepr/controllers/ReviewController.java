package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ReviewDto;
import org.goldenalf.privatepr.models.Review;
import org.goldenalf.privatepr.services.impl.ReviewServiceImpl;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.ReviewErrorException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;

    @GetMapping("/{id_review}")
    public String getReview(@PathVariable("id_review") int id, Model model) {
        ReviewDto reviewDto = convertToReviewDto(reviewService.getReview(id).orElseThrow(() -> new ReviewErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.review.exception.review-not-found"))));
        model.addAttribute("review", reviewDto);
        return "review/review";
    }

    @GetMapping("/{id_hotel}/allHotelReviews")
    public String getAllHotelReviews(@PathVariable("id_hotel") int hotelId, Model model) {
        List<ReviewDto> reviewDtoList = convertToReviewDtoList(reviewService.findAllByHotelId(hotelId));
        model.addAttribute("reviewList", reviewDtoList);
        return "review/review-by-hotel";
    }

    @GetMapping("/{id_client}/allClientReviews")
    public String getAllReviewByClient(@PathVariable("id_client") int clientId, Model model) {
        List<ReviewDto> reviewDtoList = convertToReviewDtoList(reviewService.findAllByClientId(clientId));
        model.addAttribute("reviewList", reviewDtoList);
        return "review/review-by-client";
    }

    @GetMapping("{hotelId}/new")
    public String getNewReviewPage(@PathVariable("hotelId") int hotelId, Model model) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setHotelId(hotelId);
        model.addAttribute("review", reviewDto);
        return "review/new";
    }

    @GetMapping("{id}/edit")
    public String getEditReviewPage(@PathVariable("id") int id, Model model) {
        ReviewDto reviewDto = convertToReviewDto(reviewService.getReview(id).orElseThrow(() -> new ReviewErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.review.exception.review-not-found"))));
        model.addAttribute("review", reviewDto);
        return "review/edit";
    }

    @PostMapping("/new")
    public String saveReview(@ModelAttribute("review") @Valid ReviewDto reviewDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ReviewErrorException(errorHandler.getErrorMessage(bindingResult));
        }
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewDto.setClientLogin(login);
        Review review = reviewService.getValidReview(reviewDto);
        reviewService.save(review);

        return "redirect:/hotel/all";
    }

    @PatchMapping("/{id_review}")
    public String updateReview(@PathVariable("id_review") int id,
                                                   @ModelAttribute("review") @Valid ReviewDto reviewDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ReviewErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        Review updatedReview = reviewService.getValidReview(reviewDto);
        reviewService.update(id, updatedReview);
        return "redirect:/hotel/all";
    }

    @DeleteMapping("/{id_review}")
    public String deleteReview(@PathVariable("id_review") int id) {
        reviewService.delete(id);
        return "redirect:/hotel/all";
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
