package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
    @NotNull(message = "нужно указать номер комнаты в виде целого положительно числа")
    @Min(value = 0, message = "номер комнаты должен быть положительным целым числом")
    private Integer roomNumber;

    @Column(name = "room_size")
    @NotNull(message = "нужно указать размер комнаты")
    @Min(value = 0, message = "размер комнаты должен быть больше 0")
    private Double roomSize;

    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_hotel", referencedColumnName = "id")
    private Hotel hotel;

    public Room(Integer roomNumber, Double roomSize, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.roomSize = roomSize;
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id && roomNumber == room.roomNumber && Objects.equals(hotel, room.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, hotel);
    }
}
