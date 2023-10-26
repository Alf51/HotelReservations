package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity(name = "hotels")
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
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "rating")
    private int rating;

    @Column(name = "description")
    private String description;

    public Hotel(String name, String address, int rating, String description) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.description = description;
    }
}
