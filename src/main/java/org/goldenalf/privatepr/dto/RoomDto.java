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
    @NotNull
    @Min(value = 0, message = "номер комнаты должен быть положительным целым числом")
    private int roomNumber;

    @NotNull
    @Min(value = 0, message = "размер комнаты должен быть больше 0")
    private double roomSize;

    private boolean isAvailable;
}
