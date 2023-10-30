package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Review;
import org.goldenalf.privatepr.services.ClientService;
import org.goldenalf.privatepr.services.ReviewService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;


@Component
@AllArgsConstructor
public class ReviewValidator implements Validator {
    private final ReviewService reviewService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Review review = (Review) target;

    }
}
