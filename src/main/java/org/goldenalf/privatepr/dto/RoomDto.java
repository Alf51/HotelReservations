package org.goldenalf.privatepr.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomDto {
    private int roomNumber;

    private double roomSize;

    private boolean isAvailable;
}
