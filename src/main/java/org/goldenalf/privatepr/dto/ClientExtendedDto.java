package org.goldenalf.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientExtendedDto {
    private int id;

    @NotEmpty(message = "имя не может быть пустым")
    @NotNull(message = "введите имя")
    @Size(min = 3, max = 50, message = "Имя должно быть от 3х до 50 символов")
    private String name;

    @NotEmpty(message = "login не может быть пустым")
    @NotNull(message = "введите login")
    @Size(min = 3, max = 50, message = "Логин должен содержать 3х до 50 символов")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "пароль не может быть пустым")
    @NotNull(message = "введите пароль")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "введите дату рождения")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
}
