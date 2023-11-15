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
    @NotNull(message = "{validation.hotelBook.book.check-in.not-null}")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkIn;

    @NotNull(message = "{validation.hotelBook.book.check-out.not-null}")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkOut;

    @NotNull(message = "{validation.hotelBook.hotel.hotel-id.not-null}")
    private int hotelId;

    @AssertTrue(message = "{validation.hotelBook.book.check-out.is-valid-check-in}")
    boolean isValidCheckIn() {
        if (checkIn != null && checkOut != null) {
            return checkIn.isBefore(checkOut);
        }
        return false;
    }
}
