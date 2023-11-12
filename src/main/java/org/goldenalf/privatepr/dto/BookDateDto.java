package org.goldenalf.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDateDto {
    @NotNull(message = "введите дату въезда (check_in)")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkIn;

    @NotNull(message = "введите дату въезда (check_out)")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkOut;

    @NotNull(message = "введите id отеля")
    private int hotelId;

    @AssertTrue(message = "Дата въезда(check_in) должна быть раньше даты выезда(check_out)")
    boolean isValidCheckIn() {
        if (checkIn != null && checkOut != null) {
            return checkIn.isBefore(checkOut);
        }
        return false;
    }
}
