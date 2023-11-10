package org.goldenalf.privatepr.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private LocalDate birthdate;

    @OneToMany(mappedBy = "client", cascade = jakarta.persistence.CascadeType.PERSIST)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = jakarta.persistence.CascadeType.PERSIST)
    private List<Book> bookList = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_role", joinColumns = @JoinColumn(name = "id_client"))
    @Enumerated(EnumType.STRING)
    @Cascade(value = org.hibernate.annotations.CascadeType.PERSIST)
    private Set<Role> roles = new HashSet<>();

    public Client(String name, String login, String password, LocalDate  birthdate) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.birthdate = birthdate;
    }
}
