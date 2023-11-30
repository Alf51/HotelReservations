package org.goldenalf.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
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
public class BookDto {
    private int id;

    @NotNull(message = "{validation.hotelBook.book.check-in.not-null}")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkIn;

    @NotNull(message = "{validation.hotelBook.book.check-out.not-null}")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkOut;

    private String clientLogin;

    @NotNull(message = "{validation.hotelBook.room.id.not-null}")
    private long roomId;

    @AssertTrue(message = "{validation.hotelBook.book.check-out.is-valid-check-in}")
    boolean isValidCheckIn() {
        if (checkIn != null && checkOut != null) {
            return checkIn.isBefore(checkOut);
        }
        return false;
    }

    @AssertTrue(message = "{validation.hotelBook.book.check-out.is-date-now}")
    boolean isValidDate() {
        if (checkIn != null) {
            return checkIn.isAfter(LocalDate.now()) || checkIn.isEqual(LocalDate.now());
        }
        return false;
    }
}
