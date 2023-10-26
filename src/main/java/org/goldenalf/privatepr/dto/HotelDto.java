package org.goldenalf.privatepr.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class HotelDto {
    @Column(name = "name")
    @NotEmpty(message = "имя отеля не может быть пустым")
    @NotNull(message = "введите имя отеля")
    private String name;

    @Column(name = "address")
    @NotEmpty(message = "адрес отеля не может быть пустым")
    @NotNull(message = "введите адрес отеля")
    private String address;

    @Column(name = "rating")
    @Min(value = 0, message = "оценка не может быть ниже 0")
    @Max(value = 5, message = "Отель ценит вашу оценку, но максимальный бал не может превышать 5")
    private int rating;

    private String description;

    public HotelDto() {
    }

    public HotelDto(String name, String address, int rating, String description) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

