package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "{validation.hotelBook.hotel.hotel-name.not-empty}")
    @NotNull(message = "{validation.hotelBook.hotel.hotel-name.not-null}")
    private String name;

    @Column(name = "address")
    @NotEmpty(message = "{validation.hotelBook.hotel.hotel-address.not-empty}")
    @NotNull(message = "{validation.hotelBook.hotel.hotel-address.not-null}")
    private String address;

    @Column(name = "rating")
    @Min(value = 0, message = "{validation.hotelBook.hotel.hotel-rating.min}")
    @Max(value = 5, message = "{validation.hotelBook.hotel.hotel-rating.max}")
    private int rating;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "hotel", cascade = jakarta.persistence.CascadeType.PERSIST)
    private List<Room> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = jakarta.persistence.CascadeType.PERSIST)
    private List<Review> reviewList = new ArrayList<>();

    public Hotel(String name, String address, int rating, String description) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return id == hotel.id && Objects.equals(name, hotel.name) && Objects.equals(address, hotel.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }
}
