package org.goldenalf.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    @Column(name = "check_in")
    @NotNull(message = "введите дату въезда (check_in)")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate checkIn;

    @Column(name = "check_out")
    @NotNull(message = "введите дату въезда (check_out)")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate checkOut;

    @NotNull(message = "Введите логин клиента")
    @NotEmpty(message = "Логин клиента не может быть пустым")
    private String clientLogin;

    @NotNull(message = "введите id комнаты")
    private long roomId;

    @AssertTrue(message = "Дата въезда(check_in) должна быть раньше даты выезда(check_out)")
    boolean isValidCheckIn() {
        if (checkIn != null && checkOut != null) {
            return checkIn.isBefore(checkOut);
        }
        return false;
    }

    @AssertTrue(message = "Новая дата въезда(check_in) должна быть сегодня или позже")
    boolean isValidDate() {
        if (checkIn != null) {
            return checkIn.isAfter(LocalDate.now()) || checkIn.isEqual(LocalDate.now());
        }
        return false;
    }
}
