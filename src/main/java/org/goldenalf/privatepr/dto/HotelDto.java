package org.goldenalf.privatepr.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HotelDto {
    private int id;

    @NotEmpty(message = "{validation.hotelBook.hotel.hotel-name.not-empty}")
    @NotNull(message = "{validation.hotelBook.hotel.hotel-name.not-null}")
    @Size(min = 3, max = 50, message = "{validation.hotelBook.hotel.hotel-name.size}")
    private String name;

    @NotEmpty(message = "{validation.hotelBook.hotel.hotel-address.not-empty}")
    @NotNull(message = "{validation.hotelBook.hotel.hotel-address.not-null}")
    private String address;

    @Min(value = 0, message = "{validation.hotelBook.hotel.hotel-rating.min}")
    @Max(value = 5, message = "{validation.hotelBook.hotel.hotel-rating.max}")
    @NotNull(message = "{validation.hotelBook.review.rating.not-null}")
    private Integer rating;

    private String description;

    private List<ReviewDto> reviewList = new ArrayList<>();
}

