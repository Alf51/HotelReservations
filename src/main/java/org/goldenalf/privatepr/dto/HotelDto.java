package org.goldenalf.privatepr.dto;


import jakarta.persistence.Column;

public class HotelDto {
    private String name;

    private String address;

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

