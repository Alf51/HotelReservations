package org.goldenalf.privatepr.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty(message = "имя отеля не может быть пустым")
    @NotNull(message = "введите имя отеля")
    private String name;

    @NotEmpty(message = "адрес отеля не может быть пустым")
    @NotNull(message = "введите адрес отеля")
    private String address;

    @Min(value = 0, message = "оценка не может быть ниже 0")
    @Max(value = 5, message = "Отель ценит вашу оценку, но максимальный бал не может превышать 5")
    private int rating;

    private String description;

    private List<ReviewDto> reviewList = new ArrayList<>();
}

