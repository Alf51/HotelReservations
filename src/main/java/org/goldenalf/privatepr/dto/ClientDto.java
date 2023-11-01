package org.goldenalf.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


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


    @Temporal(TemporalType.DATE)
    @NotNull(message = "введите дату рождения")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
}
