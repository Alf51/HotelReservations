package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Review;
import org.goldenalf.privatepr.repositories.ReviewRepository;
import org.goldenalf.privatepr.utils.erorsHandler.reviewError.ReviewErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public void save(Review review) {
        review.setDate(LocalDate.now());
        reviewRepository.save(review);
    }

    @Transactional
    public void delete(int id) {
        if (getReview(id).isPresent()) {
            reviewRepository.deleteById(id);
        } else {
            throw new ReviewErrorException("Ревью не найдено");
        }
    }

    @Transactional
    public void update(int id, Review reviewByUpdate) {
        reviewByUpdate.setId(id);
        reviewByUpdate.setDate(LocalDate.now());
        reviewRepository.save(reviewByUpdate);
    }

    @Transactional(readOnly = true)
    public Optional<Review> getReview(int id) {
        return reviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Review> getAllReview() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByHotelId(int hotelId) {
        return reviewRepository.findAllByHotelId(hotelId);
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByClientId(int clientId) {
        return reviewRepository.findAllByClientId(clientId);
    }
}
