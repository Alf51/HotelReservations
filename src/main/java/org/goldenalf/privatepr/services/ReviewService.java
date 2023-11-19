package org.goldenalf.privatepr.services;

import org.goldenalf.privatepr.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    void save(Review review);

    void delete(int id);

    void update(int id, Review reviewByUpdate);

    Optional<Review> getReview(int id);

    List<Review> findAllByHotelId(int hotelId);

    List<Review> findAllByClientId(int clientId);
}
