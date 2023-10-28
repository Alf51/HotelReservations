package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "room_number")
    private int roomNumber;

    @Column(name = "room_size")
    private double roomSize;

    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_hotel", referencedColumnName = "id")
    private Hotel hotel;

    public Room(int roomNumber, double roomSize, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.roomSize = roomSize;
        this.isAvailable = isAvailable;
    }
}
