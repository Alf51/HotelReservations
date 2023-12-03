package org.goldenalf.privatepr.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDto {
    private int id;

    @NotNull(message = "{validation.hotelBook.hotel.hotel-id.not-null}")
    private int hotelId;

    @Min(value = 0, message = "{validation.hotelBook.hotel.hotel-rating.min}")
    @Max(value = 5, message = "{validation.hotelBook.hotel.hotel-rating.max}")
    @NotNull(message = "{validation.hotelBook.review.rating.not-null}")
    private Integer rating;

    private String clientLogin;

    @NotNull(message = "{validation.hotelBook.review.review.not-null}")
    @NotEmpty(message = "{validation.hotelBook.review.review.not-empty}")
    private String review;
}
