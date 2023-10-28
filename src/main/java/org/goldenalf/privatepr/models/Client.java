package org.goldenalf.privatepr.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "имя не может быть пустым")
    @NotNull(message = "введите имя")
    private String name;

    @Column(name = "login")
    @NotEmpty(message = "login не может быть пустым")
    @NotNull(message = "введите login")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "пароль не может быть пустым")
    @NotNull(message = "введите пароль")
    private String password;

    @Column(name = "birthdate")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "введите дату рождения")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthdate;

    public Client(String name, String login, String password, Date  birthdate) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.birthdate = birthdate;
    }
}
