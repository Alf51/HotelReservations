package org.goldenalf.privatepr.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomDto {
    private Long id;

    @NotNull(message = "{validation.hotelBook.room.room-number.not-null}")
    @Min(value = 0, message = "{validation.hotelBook.room.room-number.min}")
    private Integer roomNumber;

    @NotNull(message = "{validation.hotelBook.room.size.not-null}")
    @Min(value = 0, message = "{validation.hotelBook.room.size.min}")
    private Double roomSize;

    private int hotelId;

    private boolean isAvailable;
}
