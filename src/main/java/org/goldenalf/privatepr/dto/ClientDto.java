package org.goldenalf.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDto {
    @NotEmpty(message = "имя не может быть пустым")
    @NotNull(message = "введите имя")
    private String name;

    @NotEmpty(message = "login не может быть пустым")
    @NotNull(message = "введите login")
    private String login;

    @NotEmpty(message = "пароль не может быть пустым")
    @NotNull(message = "введите пароль")
    private String password;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "введите дату рождения")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
}
